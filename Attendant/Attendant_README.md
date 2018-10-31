Attendant README

This document is for "attendant.py"

1. System Requirements

	a. Software only tested on Mac OS (10.10 - 10.14)
	b. Python 3.6 and 3.7 have been tested, though other
	   versions of Python 3 are likely to work.
	c. SQLite3 - Run "pip install sqlite3"

2. Setup

	a. Without an API key from OpenALPR http://www.openalpr.com/,
	   license plates will not be recognized.
	
	b. API key must be located in a plain text file called
	   "ALPR_Key.txt" in the folder TWO LEVELS ABOVE
	   the folder containing "atttendant.py"
	
	Ex. if attendant.py is located in ~/Documents/Smart_Parker/Attendant/,
	the file "ALRP_Key.txt" must be located in ~/Documents/

	c. Copy a couple of the image files from "Demo_Plates/" into
	   the folder containing "attendant.py" just to prepare.

3. Running

	a. Execute "python3 attendant.py"  This will create two folders,
	   "camera0/" and "camera1/" but should just sit doing nothing.

	b. Move, DON'T COPY, one of the image files into the "camera0" folder.
	   This simulates a vehicle attempting to enter the structure.
	   You should see a few messages pop up on the screen.

	c. Assuming the gate was opened, i.e., you saw the "Opening gate 0"
	   message, copy the same file back out of the "Demo_Plates/" folder
	   and then move it into the "camera1/" folder.  You should see some
	   messages pop up on the screen and it should mention sending an invoice.