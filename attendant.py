# Smart Parker Attendant
# Author: Charles McPherson, Jr.

# Purpose:  
# This file is the main worker for the smart parker operator. 
# It is responsible for passing image data for ALPR,
# controlling access, calculating prices, and passing data
# between local and remote databases.

# Changes:
# 10-02-2018 - cm - Created file.

import os
import queue
import threading
import time


REQ_TIMEOUT = 10 # Timeout for requests.
TOP_LEVEL_PATH = './'

# camera_manager()
# Purpose:  This function manages one gate at the structure and
#           its associated camera.  When an event occurs, it
#           snaps an image, and passes it into the ALPR queue
#           for processing in the main thread.
# Inptus:   gateID - an integer representing the gate number.
def camera_manager(gateID, direction, cameraAddr, alprQ, messageQ):
    
#### TEMPORARY ####
    if(not os.path.exists(TOP_LEVEL_PATH + cameraAddr)):
        os.mkrdir(TOP_LEVEL_PATH + cameraAddr)
    camera = TOP_LEVEL_PATH + cameraAddr
###################
     while True: # main loop
#### TEMPORARY ####
        contents = os.listdir(camera)
        if(len(contents) > 0):
        with open(camera + contents[0],'rb') as f:
                im = f.read # Get image bytes.
###################
            t = time.time() # Obtain a timestamp.
            # Build the ALPR request
            request = {'image':im,
                        'direction':direction,
                        'ID',gateID,
                        'timestamp',t}
            alprQ.put(request) # Enqueue the request
#### TEMPORARY ####
#           Delete the image
            os.delete(camera + contents[0])
###################
            try:
                # Wait for a response from the main thread.
                response = messageQ.get(timeout = REQ_TIMEOUT)
            except queue.Empty:
                print('Timeout on ' + str(gateID) + ' request.')
#####################################################################

# get_entry()
# Purpose:  This function queries the local occupancy database for
#           the time that an exiting vehicle entered the structure
#           and returns it to the caller.
def get_entry(plateString):
#### TEMPORARY ####
    t = -1
    with open(TOP_LEVEL_PATH + 'occupancy.txt','r') as f:
        for line in f:
            p = line.split(',').strip()
            if(p[0].ToUpper() == plateString.ToUpper()):
                t = p[1]
    try:
        t = float(t)
    except: ValueError
        print('Error: Bad entry time.')
        t = -1
    return t
###################
#   result = Query occupancy database(plateString)
#   if(result == Not found):
#       return -1
#   return result

# log_entry()
# Purpose:  This function logs the entry time of the vehicle with
#           the given plate number to the occupancy database.
def log_entry(plateString, timestamp):
#### TEMPORARY ####
    result = 0
    with open(TOP_LEVEL_PATH + 'occupancy.txt','w+') as f:
        for line in f:
            p = line.split(',')
            if(p[0].strip().ToUpper() == plateString.ToUpper()):
                result = -1
        if(result == 0):
            f.seek(0,2) # Go to end of file
            f.write(plateString + ',' + str(timestamp) + '\n') # Log entry time to file.
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
def check_plate(plateString):
#### TEMPORARY ####
    result = -1
    with open(TOP_LEVEL_PATH + 'registered.txt','r') as f:
        for line in f:
            p = line.split(',')
            if(p[0].strip().ToUpper() == plateString.ToUpper()):
                result = 0
    return result
#   result = Query from remote database (plateString)
#   if(result == Not Found):
#       return -1
#   return 0

# calc_price()
# Purpose:  Calculate the price to charge the customer based
#           on the pricing table in the local database.
def calc_price(inTime, outTime):
#   duration = outTime - inTime
#   result = Get Price from Database(duration)
#   return result
    duration = outTime - inTime
    with open(TOP_LEVEL_PATH + 'pricing.txt','r') as f:
        tLast = -1
        pLast = -1
        for line in f:
            price = line.split(',')
            try:
                t = float(price[0])*3600 # Calculate time for this price.
                p = float(price[1])
            except: ValueError
                print('Warning: invalid line in pricing DB')
                t = -1
                p = -1
            if(duration < t): # Reached time
                break
            if(t > 0 and p > 0):
                tLast = t
                pLast = p
    return pLast # Return the price
                
# send_invoice()
# Purpose:  This function queries the remote database for the
#           PayPal account name associated with the plate, and
#           submit an invoice.
def send_invoice(plateString, price):
#   acct = Query Remote DB(plateString)
#   send invoice to PayPal(acct,price)

# open_gate()
# Purpose:  This function opens the gate.  Probably won't do
#           anything except update the UI (when we have it).
def open_gate(gateID):
#   Open gate(gateID)
#### TEMPORARY ####
#   print('Opening Gate.')
###################

if __name__ = '__main__':
#### TEMPORARY ####
#   if(folder does not exist):
#       create folder
###################
#   params = read in parameters from ini file.
#   t = []
#   mQ = []
#   q = create queue for ALPR requests
#   for i,param in enumerate(params):
#       mQ.append(new queue for passing messages to thread)
#       t.append(new thread(args = (param,q,mQ[i])))
#   for thread in t:
#       t.start()
#
#   while True:
#       req = q.get()
#       try:
#           result = get identification info(req['image'],timeout)
#       except: TimeoutErr
#       if(req['direction'] = 'in'):
#           r = check_plate(result)
#           if(not r):
#               log_entry(result,req['timestamp'])
#               open_gate(req['ID'])
#       elif(req['direction'] = 'out'):
#           r = get_entry(result)
#           if(not r):
#               p = calc_price(r,req['timestamp'])
#               send_invoice(result,p)
#               open_gate(req['ID'])
