# Register_Operator.py
# By: Charles McPherson, Jr.

# Purpose:
# This application is used to manage operator credentials
# and generate the credentials to access the remote database.
# There are two major functions:
# 
# REGISTER
# Registration is a 5 step process
# 
# 1. Obtain username and password.
# 2. Generate salt - 10 character random string.
# 3. Generate hash using SHA-256.
# 4. Register user with PHP backend using user info and hash.
# 5. Save salt to credentials file.  If "save info" is selected, hash is stored as well.
#

import getpass
import os
import json
import sp_cred
from urllib import request
from urllib import parse
from urllib import error

                   
def CreateCredentials():
    # Create payload data structure.
    data = {'username':'','salt':'','password':''}
    # Generate salt.
    data['salt'] = sp_cred.generate_salt()
    # Prompt for a username and password.
    while True:
        data['username'] = input('Enter username: ')
        while True:
            data['password'] = getpass.getpass('Enter password: ')
            if(getpass.getpass('Re-enter password: ') != data['password']):
                print('ERROR - Passwords do not match.')
            else:
                break

        data['password'] = sp_cred.generate_hash(data['salt'],data['password'])
        
        # Attempt to register online.
        url = 'https://smartparker.cf/add_owner.php'
        payload = parse.urlencode(data).encode()
        req = request.Request(url,data=payload)
        print('URL = %s, payload = %s' % (url,payload))
        try:
            # Send POST
            resp = request.urlopen(req)
            obj = json.loads(resp.read())
            if(obj['status'] == 0):
                print('SERVER ERROR - %s' % obj['message'])
            else:
                print(obj) # TEMPORARY
                sp_cred.save_cred(data) # Save credential file.
            break # Leave the loop.
        except error.URLError as e:
            print(e.reason)
    return 0

def UpdateCredentials():
    # Set up data structure.
    data = {'username':'','salt':'','password':'','newSalt':'','newPassword':''}
    newData = sp_cred.read_cred(data) # Get the data from the existing credential file.
    while(newData == -1):
        print('Attempting to recover credentials.')
        newData = RecoverCredentials(data)
    print(newData)
    data = newData
    # Generate new salt.
    data['newSalt'] = sp_cred.generate_salt()
    # Prompt for current password.
    while True:
        data['password'] = getpass.getpass('Enter current password: ')
        data['password'] = sp_cred.generate_hash(data['salt'],data['password'])

        while True:
            data['newPassword'] = getpass.getpass('Enter new password: ')
            if(getpass.getpass('Re-enter new password: ') != data['newPassword']):
                print('ERROR - Passwords do not match.')
            else:
                break

        data['newPassword'] = sp_cred.generate_hash(data['newSalt'],data['newPassword'])

        # Attept to update credentials.
        url = 'https://smartparker.cf/update_owner_info.php'
        payload = parse.urlencode(data).encode()
        req = request.Request(url,data=payload)
        print('URL = %s, payload = %s' % (url,payload))
        try:
            resp = request.urlopen(req)
            obj = json.loads(resp.read())
            if(obj['status'] == 0):
                print('SERVER ERROR - %s' % obj['message'])
            else:
                save = {'username':data['username'],'salt':data['newSalt'],
                    'password':data['newPassword']}
                sp_cred.save_cred(save) # Update credentials locally.
                break # Leave the loop.
        except error.URLError as e:
            print(e.reason)
    return 0

def RecoverCredentials(data):
    while True:
        # Prompt for username.
        data['username'] = input('Enter username: ')
        # Create payload structure.
        rData = {'username':data['username'],'type':'owner'}
    
        # Attempt to obtain salt from server.
        url = 'https://smartparker.cf/get_salt.php'
        payload = parse.urlencode(rData).encode()
        req = request.Request(url,data=payload)
        try:
            resp = request.urlopen(req)
            obj = json.loads(resp.read())
            if(obj['status'] == 0):
                print('SERVER ERROR - %s' % obj['message'])
                return -1
            else:
                data['salt'] = obj['result']['salt']
                print('Recovery successful')
                break
        except error.URLError as e:
            print(e.reason)
            return -1
    return data

if __name__ == '__main__':
    # Check for existing credential file.
    files = os.listdir(sp_cred.PATH)
    if(sp_cred.CREDENTIAL in files):
        print('Credentials found.')
        while True:
            yn = input('Update credentials? (Y/N): ').upper()
            if(yn == 'Y'):
                UpdateCredentials() # Update credentials.
                break
            elif(yn == 'N'):
                print('Exiting...')
                break
            else:
                print('ERROR - Invalid entry.')
    else:
        print('No credential file found.')
        while True:
            yn = input('Create credentials? (Y/N): ').upper()
            if(yn == 'Y'):
                CreateCredentials() # Create new credentials.
                break
            elif(yn == 'N'):
                print('Exiting...')
                break
            else:
                print('ERROR - Invalid entry.')
