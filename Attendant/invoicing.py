# Invoicing library for Smart Parker

import requests
import time
import json
from requests.exceptions import RequestException

class Invoicer:
    def __init__(self,keyfile):
        self.token = ''
        self.token_timestamp = 0
        self.token_lifetime = 0
        self.invoice_ids = []
        self.client = ''
        self.secret = ''
        try:
            with open(keyfile,'r') as f:
                self.client = f.readline().strip()
                self.secret = f.readline().strip()
        except OSError:
            print('ERROR - Could not find credential file')
       
    
    def __obtain_token__(self):
        headers = {'Accept':'application/json',
            'Accept-Language':'en_US'}
        data = {'grant_type':'client_credentials'}
        url = 'https://api.sandbox.paypal.com/v1/oauth2/token'
        if(self.client == '' or self.secret == ''):
            print('ERROR: No API keys loaded.')
        else:
            try:
                r = requests.post(url,headers=headers,data=data,
                    auth=(self.client,self.secret))
                if(r.status_code > 400):
                    print("ERROR: API Error " + r.json()['message'])
                    return ''
                elif(r.status_code == 400):
                    print('ERROR: API Error ' + r.json()['error'])
                    return ''
                else:
                    self.token = r.json()['access_token']
                    self.token_timestamp = time.time()
                    self.token_lifetime = r.json()['expires_in']
                    return self.token
            except RequestException as e:
                print(e)
                return ''
    
    def Draft(self, price, email, message=''):
        if(self.token == ''):
            self.__obtain_token__()
        elif(time.time() - self.token_timestamp > self.token_lifetime - 30):
            self.__obtain_token__()
        url = 'https://api.sandbox.paypal.com/v1/invoicing/invoices/'
        headers = {'Content-Type':'application/json',
            'Authorization':'Bearer ' + self.token}
        merchant_info = {'email':'cmcphers-facilitator@bu.edu',
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
        if(message == ''):
            data = {'merchant_info':merchant_info,
                'billing_info':billing_info,
                'items':items}
        else:
            data = {'merchant_info':merchant_info,
                'billing_info':billing_info,
                'items':items,
                'note':message}
        try:
            r = requests.post(url,headers=headers,data=json.dumps(data))
            if(r.status_code > 400):
                print('ERROR: API Error ' + r.json()['message'])
                return ''
            elif(r.status_code == 400):
                print('ERROR: API Error ' + r.json()['error'])
                return ''
            else:
                self.invoice_ids.append(r.json()['id'])
                return r.json()['id']
        except RequestException as e:
            print(e)
            return ''

    def Send(self,invoice = ''):
        if(self.token == ''):
            self.__obtain_token__()
        elif(time.time() - self.token_timestamp > self.token_lifetime - 30):
            self.__obtain_token__()
        url_base = 'https://api.sandbox.paypal.com/v1/invoicing/invoices/'
        url_method = '/send?notify_merchant=true'
        headers = {'Content-Type':'application/json',
            'Authorization':'Bearer ' + self.token}
        if(invoice != '' and (not invoice in self.invoice_ids)):
            print('ERROR - Invoice not found')
            return -1
        elif(invoice != ''):
            loc = self.invoice_ids.index(invoice)
            url = url_base + invoice + url_method
            try:
                r = requests.post(url,headers=headers)
                if(r.status_code > 400):
                    print('ERROR: API Error ' + r.json()['message'])
                    return -1
                elif(r.status_code == 400):
                    print('ERROR: API Error ' + r.json()['error'])
                    return -1
                else:
                    self.invoice_ids.pop(loc)
                    return 0
            except RequestException as e:
                print(e)
                return -1
        else:
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
