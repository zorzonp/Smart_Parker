# sp_cred.py
# By: Charles McPherson, Jr.

# Purpose:
# This file is a library that exposes methods to handle credential
# management in the smart parker system.

import hashlib
import os
import base64


PATH = './'
CREDENTIAL = 'cred.bin'

def generate_salt():
    saltBytes = os.urandom(10) # Generate 10 byte token. Better compatibility.
#    saltBytes = secrets.token_bytes(10) # Gernerate 10 byte token.
    salt = base64.b64encode(saltBytes).decode('utf-8') # Generate base64-encoded string.
    return salt

def generate_hash(salt, password):
    h = hashlib.new('sha256') # Generate SHA-256 hasher.
    saltBytes = base64.b64decode(salt.encode('utf-8'))
    h.update(saltBytes)
    h.update(password.encode())
    hashBytes = h.digest()
    hashStr = base64.b64encode(hashBytes).decode('utf-8')
    return hashStr

def save_cred(userInfo):
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
            f.write(userInfo['salt'].encode() + '\n'.encode())
    except OSError:
        print('ERROR - Could not create new credential file.')
        return -1
    return 0

def read_cred(data):
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
                    data['username'] = lines[0].strip()
                    data['salt'] = lines[1].strip()
                    return data
        except OSError:
            print('ERROR - Could not open credential file.')
            return -1
 
