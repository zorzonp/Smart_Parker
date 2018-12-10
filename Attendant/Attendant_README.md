Attendant README

This document is for "attendant.py"

1. System Requirements

        a. Software only tested on Mac OS (10.10 - 10.14)

        b. Python 3.6 and 3.7 have been tested, though other versions of Python 3 are likely to work.

        c. SQLite3 must be installed on the system. (https://www.sqlite.org/download.html)

        d. SQLite3 for Python - Run "pip3 install sqlite3"

        e. Requests for Python - Run "pip3 install requests"

2. Setup

        a. Without an API key from OpenALPR http://www.openalpr.com/, license plates will not be recognized.

        b. Open ALPR API key must be located in a plain text file called "ALPR_Key.txt" in the folder TWO LEVELS ABOVE the folder containing "atttendant.py"

        Ex. if attendant.py is located in ~/Documents/Smart_Parker/Attendant/, the file "ALRP_Key.txt" must be located in ~/Documents/
        
        c. Client ID and secret key must be obtained from PayPal.  This requires a PayPal developer account (developer.paypal.com)

        d. Put these in a text file with the client ID on the first line and the secret key on the second.  Name this file "Paypal_key.txt" and put it in the same location as the "ALPR_key.txt" file.

        e. Copy a couple of the image files from "Demo_Plates/" into the folder containing "attendant.py" just to prepare.

        f. There's a default ini file in the folder.  It sets up one entrance gate and two exit gates.  

3. Running

        a. Execute "python3 attendant.py"  This will create folders for the images. ut should just sit doing nothing.

        b. Move, DON'T COPY, one of the image files into the folder for the entrance.  This simulates a vehicle attempting to enter the structure.  You should see a few messages pop up on the screen.

        c. Assuming the gate was opened, i.e., you saw the "Opening gate 0" message, copy the same file back out of the "Demo_Plates/" folder and then move it into the folder for one of the exits.  You should see some messages pop up on the screen and it should mention sending an invoice.
