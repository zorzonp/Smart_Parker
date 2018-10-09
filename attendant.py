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
TIME_DILATION = 200 # Dilate time by a factor of 200 for testting

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
            request = {'image':contents[0],
                        'direction':direction,
                        'ID':gateID,
                        'timestamp':t}
            alprQ.put(request) # Enqueue the request
#### TEMPORARY ####
#           Delete the image
            os.remove(camera + '/' + contents[0])
            time.sleep(5)
###################
            try:
                # Wait for a response from the main thread.
                response = messageQ.get(timeout = REQ_TIMEOUT)
            except queue.Empty:
                print('Thread:' + str(gateID) + ' - ' + 'Timeout on ' + str(gateID) + ' request.')
#####################################################################

def get_entry(plateString):
    t = -1
    with open(TOP_LEVEL_PATH + 'occupancy.txt','r') as f:
        for line in f:
            p = line.split(',')
            if(len(p) == 2):
                if(p[0].strip().capitalize() == plateString.capitalize()):
                    t = p[1]
    try:
        t = float(t)
    except ValueError:
        print('Error: Bad entry time.')
        t = -1
    return t

# log_exit()
# Purpose:  This function queries the local occupancy database for
#           the time that an exiting vehicle entered the structure
#           and appends the exit time.
def log_exit(plateString,exit):

#### TEMPORARY ####
    t = -1
    fOut = open(TOP_LEVEL_PATH + 'temp.txt','w+')
    with open(TOP_LEVEL_PATH + 'occupancy.txt','r') as f:
        for line in f:
            p = line.split(',')
            if(len(p) > 2):
                fOut.write(line)
            else:
                if(p[0].strip().capitalize() == plateString.capitalize()):
                    t = 0
                    fOut.write(line.strip() + ',' + str(exit) + '\n')
                else:
                    fOut.write(line)
    fOut.close()
    os.remove(TOP_LEVEL_PATH + 'occupancy.txt')
    os.rename(TOP_LEVEL_PATH + 'temp.txt',TOP_LEVEL_PATH + 'occupancy.txt')
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
    check = get_entry(plateString) # Check if the plate was already logged.
    if(check < 0): # If plate not found, log its entry time.
        with open(TOP_LEVEL_PATH + 'occupancy.txt','a') as f:
            f.write(plateString + ',' + str(timestamp) + '\n')
    else:
        print('Plate already logged.')
        result = -1
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
            if(p[0].strip().capitalize() == plateString.capitalize()):
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
        t = -1
        p = -1
        for line in f: 
            price = line.split(',')
            try:
                t = float(price[0])*3600/TIME_DILATION # Calculate time for this price.
                p = float(price[1])
            except ValueError:
                print('Warning: invalid line in pricing DB')
                t = -1
                p = -1
            if(duration < t): # Reached time
                break
    return p # Return the price
                
# send_invoice()
# Purpose:  This function queries the remote database for the
#           PayPal account name associated with the plate, and
#           submit an invoice.
def send_invoice(plateString, price):
#### TEMPORARY ####
    acct = ''
    with open(TOP_LEVEL_PATH + 'registered.txt','r') as f:
        for line in f:
            d = line.split(',')
            if(d[0].strip() == plateString):
                acct = d[1].strip()
                break
    if(acct == ''):
        return -1
    print('Sending invoice for $%0.2f to account %s' % (price,acct))
    return 0
#   acct = Query Remote DB(plateString)
#   send invoice to PayPal(acct,price)

# open_gate()
# Purpose:  This function opens the gate.  Probably won't do
#           anything except update the UI (when we have it).
def open_gate(gateID):
#   Open gate(gateID)
#### TEMPORARY ####
    print('Opening Gate %d' % (gateID))
###################

if __name__ == '__main__':
#### TEMPORARY ####
#   if(folder does not exist):
#       create folder
    if(not os.path.exists(TOP_LEVEL_PATH)):
        os.mkdir(TOP_LEVEL_PATH)
###################
    params = [[0,'in','camera0'],[1,'out','camera1']]
    t = []
    mQ = []
    q = queue.Queue()
    for i,param in enumerate(params):
        mQ.append(queue.Queue())
        t.append(threading.Thread(target = camera_manager, args = tuple(param) + (q,mQ[i])))
    for worker in t:
        worker.setDaemon(True)
        worker.start()

    while True:
        try:
            req = q.get()
            #try:
            result = req['image'].split('.')[0]
            time.sleep(5)
            #except TimeoutErr:
            if(req['direction'] == 'in'):
                r = check_plate(result)
                mQ[req['ID']].put({'result':result,'ID':req['ID']})
                if(r == 0):
                    r = log_entry(result, req['timestamp'])
                    if(r == 0):
                        open_gate(req['ID'])
            elif(req['direction'] == 'out'):
                timeIn = get_entry(result)
                mQ[req['ID']].put({'result':result,'ID':req['ID']})
                if(timeIn >= 0): # If valid timestamp,
                    r = log_exit(result,req['timestamp']) # Log the exit time.
                    if(r >= 0):
                        p = calc_price(timeIn,req['timestamp']) # Calculate the price.
                        if(p > 0):
                            send_invoice(result, p)
                        open_gate(req['ID'])
        except KeyboardInterrupt:
            print('Exiting...')
            break

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
