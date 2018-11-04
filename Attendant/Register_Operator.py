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
from urllib import request
from urllib import parse
from urllib import error

PATH = './'
CREDENTIAL = 'cred.txt'

def generate_salt():
    salt = secrets.token_bytes(10) # Gernerate 10 byte token.
    return salt

def generate_hash(salt, password):
    h = hashlib.new('sha256') # Generate SHA-256 hasher.
    h.update(salt.encode())
    h.update(password.encode())
    hashBytes = h.digest()
    return hashBytes

def register_operator(userInfo):
    # Prepare data for POST.
    dataDict = {'username':userInfo['username'],
        'password':userInfo['hash'],
        'salt':userInfo['salt']}
    payload = parse.urlencode(dataDict).encode()

    # Post to API
    url = 'https://smartparker.cf/add_owner.php'
    req = request.Request(url,data=payload)
    try:
        r = request.urlopen(req)
    except error.URLError as e:
        print(e.Message)
        return -1

    # Check response
    resp = json.loads(r.read())
    if(resp['status'] == 0):
        print('ERROR - Registration error: %s' % resp['message'])
        return -1
    else:
        print('Registration successful')
        return 0

def save_data(userInfo,saveAll):
    # Check for credentials file.
    files = os.listdir(PATH)
    if(CREDENTIAL in files):
        # Overwrite previous credential file.
        print('WARNING - Overwriting previous credentials')
        try:
            os.remove(os.path.join(PATH,CREDENTIAL)
        except OSError:
            print('ERROR - Could not delete previous credential file.')
            return -1
    try:
        with open(os.path.join(PATH,CREDENTIAL),'w+') as f:
            f.write(userInfo['username'] + '\n')
            if(saveAll):
                f.write(userInfo['hash'] + '\n')
    except OSError:
        print('ERROR - Could not create new credential file.')
        return -1
    return 0

if __name__ == '__main__':
    # Check for existing credential file.
    files = os.listdir(PATH)
    url = 'https://smartparker.cf/'
    data = {}
    if(CREDENTIAL in files):
        print('Credentials found.')
        while True:
            yn = raw_input('Update credentials (Y/N): ').upper()
            if(yn == 'Y')
                url = url + 'update_owner.php'
                
