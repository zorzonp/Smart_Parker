# Smart Parker Attendant
# Author: Charles McPherson, Jr.

# Purpose:  
# This file is the main worker for the smart parker operator. 
# It is responsible for passing image data for ALPR,
# controlling access, calculating prices, and passing data
# between local and remote databases.

# Changes:
# 10-02-2018 - cm - Created file.

REQ_TIMEOUT = 10 # Timeout for requests.

# camera_manager()
# Purpose:  This function manages one gate at the structure and
#           its associated camera.  When an event occurs, it
#           snaps an image, and passes it into the ALPR queue
#           for processing in the main thread.
def camera_manager(gateID, direction, cameraAddr, alprQ, messageQ):
    
#### TEMPORARY ####
#   if (folder $cameraAddr does not exist):
#       create folder $cameraAddr
###################
#   while True: # main loop
#### TEMPORARY ####
#       if(image file exists in folder $cameraAddr):
#           im = read image file
###################
#           t = get timestamp
#           request = {'image':im,'direction':direction,'ID':gateID,'timestamp',t}
#           alprQ.put(request) # Enqueue the request
#### TEMPORARY ####
#           delete image file from folder
###################
#           try:
#               response = messageQ.get(timeout = REQ_TIMEOUT) # Wait for response.
#           except TimeoutException:
#               print('Timeout on ' + str(gateID) + ' request.')

# get_entry()
# Purpose:  This function queries the local occupancy database for
#           the time that an exiting vehicle entered the structure
#           and returns it to the caller.
def get_entry(plateString):
#   result = Query occupancy database(plateString)
#   if(result == Not found):
#       return -1
#   return result

# log_entry()
# Purpose:  This function logs the entry time of the vehicle with
#           the given plate number to the occupancy database.
def log_entry(plateString, timestamp):
#   result = Post to database(plateString, timestamp)
#   if(result == Already in database):
#       return -1
#   return 0

# check_plate()
# Purpose:  This function queries the remote database for the
#           given plate to ensure that the person is a registered
#           user.
def check_plate(plateString):
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
