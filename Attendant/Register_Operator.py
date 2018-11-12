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
import secrets
import hashlib
import os
import json
import base64
from urllib import request
from urllib import parse
from urllib import error

PATH = './'
CREDENTIAL = 'cred.bin'

def generate_salt():
    saltBytes = secrets.token_bytes(10) # Gernerate 10 byte token.
    salt = base64.b64encode(salt).encode('utf-8') # Generate base64-encoded string.
    return salt

def generate_hash(salt, password):
    h = hashlib.new('sha256') # Generate SHA-256 hasher.
    saltBytes = base64.b64decode(salt.decode('utf-8'))
    h.update(saltBytes)
    h.update(password.encode())
    hashBytes = h.digest()
    hashStr = base64.b64encode(hashBytes).encode('utf-8')
    return hashBytes

    # Check response
    resp = json.loads(r.read())
    if(resp['status'] == 0):
        print('ERROR - Registration error: %s' % resp['message'])
        return -1
    else:
        print('Registration successful')
        return 0

def save_data(userInfo):
    # Check for credentials file.
    files = os.listdir(PATH)
    if(CREDENTIAL in files):
        # Overwrite previous credential file.
        print('WARNING - Overwriting previous credentials')
        try:
            os.remove(os.path.join(PATH,CREDENTIAL))
        except OSError:
            print('ERROR - Could not delete previous credential file.')
            return -1
    try:
        with open(os.path.join(PATH,CREDENTIAL),'wb+') as f:
            f.write(userInfo['username'].encode() + '\n'.encode())
            f.write(userInfo['salt'] + '\n'.encode())
    except OSError:
        print('ERROR - Could not create new credential file.')
        return -1
    return 0

def read_data(data):
    # Check for credential file.
    files = os.listdir(PATH)
    if(CREDENTIAL in files):
        # Open file.
        try:
            with open(os.path.join(PATH,CREDENTIAL),'r') as f:
                lines = []
                for line in f:
                    lines.append(line)
                if(len(lines) < 2):
                    return -1
                else:
                    data['username'] = lines[0]
                    data['salt'] = lines[1]
                    return data
        except OSError:
            print('ERROR - Could not open credential file.')
            return -1
                    
def CreateCredentials():
    # Create payload data structure.
    data = {'username':'','salt':'','hash':''}
    # Generate salt.
    data['salt'] = generate_salt()
    # Prompt for a username and password.
    while True:
        data['username'] = input('Enter username: ')
        data['hash'] = getpass.getpass('Enter password: ')
        data['hash'] = generate_hash(data['salt'],data['hash'])
        
        # Attempt to register online.
        url = 'https://smartparker.cf/test.php'
        payload = parse.urlencode(data).encode()
        req = request.Request(url,data=payload)
        print('URL = %s, payload = %s' % (url,payload))
        try:
            # Send POST
            resp = request.urlopen(req)
            obj = json.loads(resp.read())
            #if(obj['status'] == 0):
            #    print('SERVER ERROR - %s' % obj['message'])
            #else:
            print(obj)
            save_data(data) # Save credential file.
            break # Leave the loop.
        except error.URLError as e:
            print(e.message)
    return 0

def UpdateCredentials():
    # Set up data structure.
    data = {'username':'','salt':'','hash':'','newSalt':'','newHash':''}
    newData = read_data(data) # Get the data from the existing credential file.
    while(newData == -1):
        print('Attempting to recover credentials.')
        newData = RecoverCredentials(data)
    data = newData
    # Generate new salt.
    data['newSalt'] = generate_salt()
    # Prompt for new password.
    while True:
        data['newHash'] = getpass.getpass('Enter password: ')
        data['newHash'] = generate_hash(data['newSalt'],data['newHash'])

        # Attept to update credentials.
        url = 'https://smartparker.cf/update_owner.php'
        payload = parse.urlencode(data).encode()
        req = request.Request(url,data=payload)
        print('URL = %s, payload = %s' % (url,payload))
        try:
        #    resp = request.urlopen(req)
        #    obj = json.loads(resp.read())
        #    if(obj['status'] == 0):
        #        print('SERVER ERROR - %s' % obj['message'])
        #    else:
                save = {'username':data['username'],'salt':data['newSalt'],
                    'hash':data['newHash']}
                save_data(save) # Update credentials locally.
                break # Leave the loop.
        except error.URLError as e:
            print(e.message)
    return 0

def RecoverCredentials(data):
    while True:
        # Prompt for username.
        data['username'] = input('Enter username: ')
        # Create payload structure.
        rData = {'username':data['username'],'type':'owner'}
    
        # Attempt to obtain salt from server.
        url = 'https://smartparker.cf/get_salt.php'
        payload = parse.urlencode(data).encode()
        req = request.Request(url,data=payload)
        try:
            resp = request.urlopen(req)
            obj = json.loads(resp.read())
            if(obj['status'] == 0):
                print('SERVER ERROR - %s' % obj['message'])
            else:
                data['salt'] = obj['result']
                print('Recovery successful')
                break
        except error.URLError as e:
            print(e.message)
    return 0
if __name__ == '__main__':
    # Check for existing credential file.
    files = os.listdir(PATH)
    if(CREDENTIAL in files):
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
