# Invoicing library for Smart Parker

import requests
import time
import json
from requests.exceptions import RequestException

# Invoicer class manages all pending invoices and allows them
# to be sent and received.
class Invoicer:
    # Constructor
    def __init__(self,keyfile):
        self.token = '' # The current bearer token for the Paypal API.
        self.token_timestamp = 0 # Timestamp when token was received.
        self.token_lifetime = 0 # Lifetime of token
        self.invoice_ids = [] # Pending inoivces to send
        self.client = '' # Client ID for API
        self.secret = '' # Secret key for API
        self.facilitator = '' # Facilitator email account
        try:
            # Read API credentials from key file.
            with open(keyfile,'r') as f:
                lines = []
                for line in f:
                    lines.append(line)
                if(len(lines) < 1 or len(lines) > 3):
                    print('ERROR - Invalid Paypal credential file %s.' % keyfile)
                else:
                    self.facilitator = lines[0].strip()
                    self.client = lines[1].strip()
                    self.secret = lines[2].strip()
        except OSError:
            print('ERROR - Could not find credential file')
       
    # __obtain_token__()
    # Purpose: Obtains a bearer token from the Paypal API
    # Inputs: None
    # Outputs: Returns the bearer token
    def __obtain_token__(self):
        # Setup header and data structures for HTTP request
        headers = {'Accept':'application/json',
            'Accept-Language':'en_US'}
        data = {'grant_type':'client_credentials'}
        url = 'https://api.sandbox.paypal.com/v1/oauth2/token'
        # Check that API keys have been loaded.
        if(self.client == '' or self.secret == ''):
            print('ERROR: No API keys loaded.')
        else:
            # Make the request
            try:
                r = requests.post(url,headers=headers,data=data,
                    auth=(self.client,self.secret))
                # If the request fails, return the message.
                if(r.status_code > 400):
                    print("ERROR: API Error " + r.json()['message'])
                    return ''
                elif(r.status_code == 400):
                    print('ERROR: API Error ' + r.json()['message'])
                    return ''
                # Otherwise, save the token info.
                else:
                    self.token = r.json()['access_token']
                    self.token_timestamp = time.time()
                    self.token_lifetime = r.json()['expires_in']
                    return self.token
            except RequestException as e:
                print(e)
                return ''
    # Draft()
    # Putpose:  This function creates a draft invoice and stores it
    #           in the invoice list.
    # Inputs:   price - The price of the service.
    #           email - The email address to which to send the invoice
    #           message - An optional message to include with the invoice
    # Outputs:  Returns the invoice ID returned.
    def Draft(self, price, email, message=''):
        # If a token has not been obtained or the token has expired,
        # obtain a new one.
        if(self.token == ''):
            self.__obtain_token__()
        elif(time.time() - self.token_timestamp > self.token_lifetime - 30):
            self.__obtain_token__()
        # Set up the headers and info for the invoice request
        url = 'https://api.sandbox.paypal.com/v1/invoicing/invoices/'
        headers = {'Content-Type':'application/json',
            'Authorization':'Bearer ' + self.token}
        merchant_info = {'email':self.facilitator,
            'business_name':'Smart Parker',
            'address':{'line1':'8 St. Marys St.',
                    'city':'Boston',
                    'state':'MA',
                    'postal_code':'02215',
                    'country_code':'US'}}
        billing_info = [{'email':email}]
        items = [{'name':'Parking',
            'quantity':1.0,
            'unit_price':{'currency':'USD',
                'value':str(price)}}]
        # Include a message if provided
        if(message == ''):
            data = {'merchant_info':merchant_info,
                'billing_info':billing_info,
                'items':items}
        else:
            data = {'merchant_info':merchant_info,
                'billing_info':billing_info,
                'items':items,
                'note':message}
        # Attempt to make the request.
        try:
            r = requests.post(url,headers=headers,data=json.dumps(data))
            # If the request fails, print the mssage.
            if(r.status_code > 400):
                print('ERROR: API Error ' + r.json()['message'])
                return ''
            elif(r.status_code == 400):
                print('ERROR: API Error ' + r.json()['message'])
                return ''
            else:
                # If successful, add the invoice ID to the list
                # and return it.
                self.invoice_ids.append(r.json()['id'])
                return r.json()['id']
        except RequestException as e:
            print(e)
            return ''

    # Send()
    # Purpose: This function sends the invoice.
    # Inputs:   invoice - The invoice ID (optional) If not provided, it
    #           sends all of them in the list.
    def Send(self,invoice = ''):
        # Obtain a token if necessary.
        if(self.token == ''):
            self.__obtain_token__()
        elif(time.time() - self.token_timestamp > self.token_lifetime - 30):
            self.__obtain_token__()
        # Set up the request.
        url_base = 'https://api.sandbox.paypal.com/v1/invoicing/invoices/'
        url_method = '/send?notify_merchant=true'
        headers = {'Content-Type':'application/json',
            'Authorization':'Bearer ' + self.token}
        if(invoice != '' and (not invoice in self.invoice_ids)):
            print('ERROR - Invoice not found')
            return -1
        elif(invoice != ''):
            # Locate the invoice in the list.
            loc = self.invoice_ids.index(invoice)
            url = url_base + invoice + url_method
            # Attempt to make the request
            try:
                r = requests.post(url,headers=headers)
                # If the request fails, add the reason.
                if(r.status_code > 400):
                    print('ERROR: API Error ' + r.json()['message'])
                    return -1
                elif(r.status_code == 400):
                    print('ERROR: API Error ' + r.json()['error'])
                    return -1
                else:
                    # Otherwise, remove that invoice from the list.
                    self.invoice_ids.pop(loc)
                    return 0
            except RequestException as e:
                print(e)
                return -1
        else:
            # Send all invoices in sequence.
            invList = self.invoice_ids
            for inv in invList:
                loc = self.invoice_ids.index(inv)
                url = url_base + inv + url_method
                try:
                    r = requests.post(url,headers=headers)
                    if(r.status_code > 400):
                        print('ERROR: API Error ' + r.json()['message'])
                    elif(r.status_code == 400):
                        print('ERROR: API Error ' + r.json()['error'])
                    else:
                        self.invoice_ids.pop(loc)
                except RequestException as e:
                    print(e)
            if(len(self.invoice_ids) == 0):
                return 0
            else:
                return -1
