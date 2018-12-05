# Smart Parker Attendant
# Author: Charles McPherson, Jr.

# Purpose:  
# This file is the main worker for the smart parker operator. 
# It is responsible for passing image data for ALPR,
# controlling access, calculating prices, and passing data
# between local and remote databases.

# Changes:
# 10-02-2018 - cm - Created file.
# 10-03-2018 - cm - Added actual code.
# 10-08-2018 - cm - Finished test code, querying text files and polling for image files.

import os
import queue
import threading
import invoicing
import time
import sqlite3
import base64
import json
import sp_cred
import requests
from requests.exceptions import RequestException

# DATABASE COLUMNS
# occupancy - TABLE times (number, state, timeIn, timeOut)
# pricing - TABLE standard (hours, price)
# registered - TABLE users (number, state, account)

REQ_TIMEOUT = 10 # Timeout for requests.
TOP_LEVEL_PATH = './'
TIME_DILATION = 200 # Dilate time by a factor of 200 for testting
ALPR_KEY = '../../ALPR_Key.txt' # Location of ALPR key
MIN_CONF = 70 # Minimum confidence for ALPR result.
PARAMETER_FILE = './params.ini' # Parameter file.

# camera_manager()
# Purpose:  This function manages one gate at the structure and
#           its associated camera.  When an event occurs, it
#           snaps an image, and passes it into the ALPR queue
#           for processing in the main thread.
# Inptus:   gateID - an integer representing the gate number.
def camera_manager(gateID, direction, cameraAddr, alprQ, messageQ):
    
#### TEMPORARY ####
    if(not os.path.exists(TOP_LEVEL_PATH + cameraAddr)):
        os.mkdir(TOP_LEVEL_PATH + cameraAddr)
    camera = TOP_LEVEL_PATH + cameraAddr
###################
    while True: # main loop
#### TEMPORARY ####
        contents = os.listdir(camera)
        if(len(contents) > 0):
            print('Thread:' + str(gateID) + ' - ' + str(contents))
            with open(camera + '/' + contents[0],'rb') as f:
                im = f.read # Get image bytes.
###################
            t = time.time() # Obtain a timestamp.
            # Build the ALPR request
            request = {'image':camera + '/' + contents[0],
                        'direction':direction,
                        'ID':gateID,
                        'timestamp':t}
            alprQ.put(request) # Enqueue the request
            try:
                # Wait for a response from the main thread.
                response = messageQ.get(timeout = REQ_TIMEOUT)
            except queue.Empty:
                print('Thread:' + str(gateID) + ' - ' + 'Timeout on ' + str(gateID) + ' request.')
            # Delete the image, now that the main process is done.
            os.remove(camera + '/' + contents[0])
#####################################################################

# load_alpr_key()
# Purpose:  This function reads in the OpenALRP API key from an external
#           text file.  This prevents accidental inclusion on GitHub.
# Outputs:  key - The API key.  If the file is not opened, an empty string
#           is returned.
def load_alpr_key():
    key = '' # Initialize to empty in case the file cannot be opened.
    try:
        # Attempt to open the file.
        with open(ALPR_KEY,'r') as f:
            key = f.readline().strip() # Read in the key, removing whitespace.
    except OSError:
        print("Error: Unable to obtain key from file '%s'" % ALPR_KEY)
    return key # Return the key.

# get_entry()
# Purpose:  This function queries the database to determine the time of
#           entry for a vehicle with the provided license plate number
#           and state.
def get_entry(plateString, plateState):
    t = -1

    conn = sqlite3.connect('occupancy.db') # Establish database connection.
    c = conn.cursor()
    # Search for current person in database.
    c.execute("SELECT * FROM times WHERE number=? AND state=? AND timeOut IS NULL",(plateString, plateState))
    results = c.fetchall()
    conn.close() # Close the connection.
    if(len(results) > 0):
        t = results[0][2]
    else:
        print('Error: No match')

    return t
# log_exit()
# Purpose:  This function queries the local occupancy database for
#           the time that an exiting vehicle entered the structure
#           and appends the exit time.
def log_exit(plateString, plateState, exit):
    t = -1

    conn = sqlite3.connect('occupancy.db') # Establish database connection.
    c = conn.cursor()
    # Search for current person in datatbase.
    c.execute("SELECT * FROM times WHERE number=? AND state=? AND timeOut IS NULL",(plateString, plateState))
    entries = c.fetchall()
    if(len(entries) == 0):
        print('Error: No match')
    elif(len(entries) > 1):
        print('Error: Duplicate entries')
    else:
        # Update the exit time.
        c.execute("UPDATE times SET timeOut=? WHERE number=? AND state=? AND timeIn=? AND timeOut IS NULL",(exit,entries[0][0],entries[0][1],entries[0][2]))
        t = 0
    
    conn.commit()
    conn.close()
    return t


# log_entry()
# Purpose:  This function logs the entry time of the vehicle with
#           the given plate number to the occupancy database.
def log_entry(plateString, plateState, timestamp):
    result = 0
    conn = sqlite3.connect('occupancy.db')
    c = conn.cursor()
    # Search database for duplicate entry.
    c.execute("SELECT * FROM times WHERE number=? AND state=? AND timeOut IS NULL",(plateString,plateState))
    entries = c.fetchall()
    if(len(entries) > 0):
        print('Error: Plate already logged.')
        result = -1
    else:
        c.execute("INSERT INTO times VALUES (?,?,?,NULL)",(plateString,plateState,timestamp))
    
    conn.commit()
    conn.close()

    return result
###################
#   result = Post to database(plateString, timestamp)
#   if(result == Already in database):
#       return -1
#   return 0

# check_plate()
# Purpose:  This function queries the remote database for the
#           given plate to ensure that the person is a registered
#           user.
def check_plate(userData,plateString,plateState):
#### TEMPORARY ####
    print("Checking plate: State: %s Number :%s" % (plateState, plateString)) 
    result = -1
    params = {'username':userData['username'],
        'password':userData['password'],
        'license_plate':plateString,
        'state':plateState}    
    ### NEW ###
    url = 'https://smartparker.cf/lookup_license.php'
    try:
        r = requests.post(url,data=params)
    except RequestError as e:
        print(e)
        return result
    obj = r.json()
    ### END ###
    if(obj['status'] == 0):
        print('Error from remote DB: %s' % obj['message'])
    else:
        result = 0
    return result

# calc_price()
# Purpose:  Calculate the price to charge the customer based
#           on the pricing table in the local database.
def calc_price(inTime, outTime):
    duration = (outTime - inTime)*TIME_DILATION/3600 # Calculate time in hours
    conn = sqlite3.connect('pricing.db')
    c = conn.cursor()

    # Find all pricing entries greater than the duration.
    c.execute("SELECT * FROM standard WHERE hours > ? ORDER BY price ASC",(duration,))
    # Grab only the first entry.
    entries = c.fetchone()
    if(len(entries) == 0):
        # Get the entry for extended duration.
        c.execute("SELECT * FROM standard WHERE hours=-1")
        entries = c.fetchone()
        if(len(entries) == 0):
            print("Error: No extended duration price.")
            return -1
    conn.close() # Close SQL connection.
    p = entries[1] # Get the price.
    return p # Return the price.
                
# send_invoice()
# Purpose:  This function queries the remote database for the
#           PayPal account name associated with the plate, and
#           submit an invoice.
def send_invoice(userData,plateString,plateState,price,inv):
    acct = ''
    params = {'username':userData['username'],
        'password':userData['password'],
        'license_plate':plateString,
        'state':plateState}
    ### NEW ###
    url = 'https://smartparker.cf/lookup_license.php'
    try:
        resp = requests.post(url,data=params)
    except RequestException as e:
        print(e)
        return -1
    obj = resp.json()
    ### END NEW ###
    if(obj['status'] == 0):
        print('Error in remote DB: %s' % obj['message'])
        return -1
    else:
        acct = obj['result']['email']
        invID = inv.Draft(price,acct,'Thanks for parking or whatever')
        if(invID == ''):
            print('Error drafting invoice to %s for %0.2f' % (acct,price))
            return -1
        else:
            r = inv.Send()
            if(r == -1):
                print('Error sending invoice')
                return -1
        print('Sending invoice %s for %0.2f to account %s' % (invID,price,acct))
        return 0

# open_gate()
# Purpose:  This function opens the gate.  Probably won't do
#           anything except update the UI (when we have it).
def open_gate(gateID):
#   Open gate(gateID)
#### TEMPORARY ####
    print('Opening Gate %d' % (gateID))
###################

# read_plate()
# Purpose:  This function calls the open ALPR API to determine the
#           license plate state and number.
# Inputs:   fileName - The path to the file.
#           key - The API key.
# Outputs:  plate - A dictionary containing the number and state of the plate.
def read_plate(fileName, key):
    plate = {'number':'','state':''}
    try:
        with open(fileName, 'rb') as img:
            # Convert the image to a 64-bit representation to meet the 
            # OpenALPR requirements.
            img_64 = base64.b64encode(img.read())
    except OSError:
        print('Error: Image file could not be opened.')
        return plate
    # Issue request to OpenALPR API
    url = 'https://api.openalpr.com/v2/recognize_bytes?country=us&secret_key=%s&topn=1' % key
    try:
        r = requests.post(url,data=img_64)
    except RequestError as e:
        print(e)
        return plate
    obj = r.json() # Decode JSON object
    if(obj['error'] == False): # Ensure there is no error before proceeding.
        if(len(obj['results']) > 0): # If result was obtained,
            conf = obj['results'][0]['confidence'] # Ensure confidence is higher than minimum.
            if(conf >= MIN_CONF):
                # Pull out plate number and state information.
                plate['number'] = obj['results'][0]['plate'].upper()
                plate['state'] = obj['results'][0]['region'].upper()
            else:
                print('Error: ALPR result has low confidence. conf = %d%%' % conf)
        else:
            print('Error: No ALPR result returned.')
    else:
        # Print OpenALPR error.
        print('Error: Open ALPR error = %s' % obj['error'])
    return plate # Return plate information.

def read_params(fileName):
    params = []
    foundIn = False
    foundOut = False
    try:
        with open(fileName,'r') as f:
            for rawLine in f:
                line = rawLine.strip() # Ignore trailing and leading whitespace.
                if(len(line) > 0 and line[0] != '#'):
                    parsed = line.split(',') # Parse the line.
                    if(len(parsed) < 3): # If not enough parameters,
                        print('Error parsing file %s at line: %s' % (fileName,rawLine))
                        print('Not enough fields.')
                        return {'status':-1,'params':[]}
                    elif(len(parsed) > 3): # If too many parameters,
                        print('Error parsing file %s at line %s' % (fileName,rawLine))
                        print('Expected 3 fields, found %d.' % len(parsed))
                        return {'status':-1,'params':[]}
                    else: # If correct number of parameters, parse into dictionary.
                        try:
                            parameterList = {'gateID':int(parsed[0].strip()),
                                'direction':parsed[1].strip(),
                                'label':parsed[2].strip()}
                        except ValueError: # Trap error where non-integer given for gateID.
                            print('Error parsing file %s at line: %s' % (fileName,rawLine))
                            print('Field 1 (Gate ID) must be an integer.')
                            return {'status':-1,'params':[]}
                        # Ensure that direction is either 'in' or 'out'
                        if(parameterList['direction'] != 'in' and 
                            parameterList['direction'] != 'out'):
                                print('Error parsing paramter file at line %s' % line)
                                print("Field 2 (Direction) must be 'in' or 'out'")
                                return {'status':-1,'params':[]}
                        else:
                            # Mark entrance or exit as found.
                            if(parameterList['direction'] == 'in'):
                                foundIn = True
                            else:
                                foundOut = True
                            # Append parameters to list.
                            params.append(parameterList) 
    except OSError:
        print('ERROR - Could not open parameter file %s' % fileName)
        return {'status':-1,'params':[]}
    if(len(params) < 2):
        print('ERROR - Not enough gates identified.  Must be at least 2')
        return {'status':-1,'params':[]}
    elif(not foundIn or not foundOut):
        print('ERROR - Must have at least one entrance AND at least one exit')
        return {'status':-1,'params':[]}
    else:
        return {'status':0,'params':params}


if __name__ == '__main__':
#### TEMPORARY ####
#   if(folder does not exist):
#       create folder
    if(not os.path.exists(TOP_LEVEL_PATH)):
        os.mkdir(TOP_LEVEL_PATH)
###################
    r = read_params(PARAMETER_FILE)
    if(r['status'] < 0):
        quit() # Error reading parameter file.
    params = r['params']
    openalpr_key = load_alpr_key()
    user_cred = sp_cred.load_cred()
    inv = invoicing.Invoicer('../../Paypal_Key.txt')
    if(len(openalpr_key) > 0 and len(user_cred['username']) > 0):
        t = []
        mQ = []
        q = queue.Queue()
        for i,param in enumerate(params):
            mQ.append(queue.Queue())
            t.append(threading.Thread(target = camera_manager, args = tuple(param.values()) + (q,mQ[i])))
        for worker in t:
            worker.setDaemon(True)
            worker.start()

        while True:
            try:
                req = q.get()
                #try:
                result = read_plate(req['image'],openalpr_key)
                if(result['number'] != ''): # If the result is good, extract the state and number.
                    state = result['state']
                    number = result['number']
                #except TimeoutErr:
                    if(req['direction'] == 'in'):
                        r = check_plate(user_cred,number,state)
                        mQ[req['ID']].put({'result':result,'ID':req['ID']})
                        if(r == 0):
                            r = log_entry(number,state,req['timestamp'])
                            if(r == 0):
                                open_gate(req['ID'])
                    elif(req['direction'] == 'out'):
                        timeIn = get_entry(number, state)
                        mQ[req['ID']].put({'result':result,'ID':req['ID']})
                        if(timeIn >= 0): # If valid timestamp,
                            r = log_exit(number,state,req['timestamp']) # Log the exit time.
                            if(r >= 0):
                                p = calc_price(timeIn,req['timestamp']) # Calculate the price.
                                if(p > 0):
                                    # Send invoice.
                                    send_invoice(user_cred,number,state,p,inv)
                                open_gate(req['ID'])
                else:
                    mQ[req['ID']].put({'result':result,'ID':req['ID']})
            except KeyboardInterrupt:
                print('Exiting...')
                break
