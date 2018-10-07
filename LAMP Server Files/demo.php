<html>
<!--*****************************************************************************************
*                                                                                           *
* Author:      Peter Zorzonello                                                             *
* Class:       EC601-A1                                                                     *
* File:        demo.php                                                                     *
* Description: This file is a demo used to test the PHP functions to add users, add owners, * 
*              and retrieve info.                                                           *
*********************************************************************************************-->
        <head>
        </head>
        <body>
                <div>
                        <h1 style="color:red">DO NOT PUT REAL PERSONAL INFO IN THIS FORM!!!!</h1>
                        <h3>Test Add User</h3>
                        <form action="add_customer.php" method="post" id="form">
                                <input type="text" name="first_name" id="first_name" placeholder="First Name"></input><br/>
                                <input type="text" name="last_name" id="last_name" placeholder="Last Name"></input><br/>
                                <input type="text" name="license_plate" placeholder="License Plate Number"></input><br/>
                                <input type="text" name="state" placeholder="State"></input><br/>
                                <input type="text" name="make" placeholder="Make"></input><br/>
                                <input type="text" name="model" placeholder="Model"></input><br/>
                                <input type="text" name="year" placeholder="Year"></input><br/>
                                <input type="text" name="color" placeholder="Color"></input><br/>

                                <br/>
                                <br/>

                                <input type="text" name="username" placeholder="Username"></input><br/>
                                <input type="text" name="password" placeholder="Password"></input><br/>
                                <input type="text" name="venmo_username" placeholder="Venmo Username"></input><br/> 
                                <input type="text" name="salt" placeholder="1234567890"></input><br/>
                                <input type="submit" name="submit" id="submit" value="Submit">
                        </form>
                        <h3>Test lookup</h3>    
                        <form action="lookup_license.php" method="post" id="form">
                                <input type="text" name="license_plate" placeholder="License Plate Number"></input><br/>
                                <input type="text" name="state" placeholder="State"></input><br/>
                                <input type="text" name="username" placeholder="Username"></input><br/>
                                <input type="text" name="password" placeholder="Password"></input><br/>
                                <input type="submit" name="submit" id="submit" value="Submit">
                        </form>
                </div>
                
                <div>
                        <h3>Test Add Owner</h3>
                        <form action="add_owner.php" method="post" id="form">
                                <input type="text" name="username" placeholder="Username"></input><br/>
                                <input type="text" name="password" placeholder="Password"></input><br/>
                                <input type="text" name="salt" placeholder="1234567890"></input><br/>
                                <input type="submit" name="submit" id="submit" value="Submit">
                        </form>
                                        
                        <h3>Test Get Salt</h3>
                        <form action="get_salt.php" method="post" id="form">
                                <input type="text" name="username" placeholder="Username"></input><br/>
                                <input type="submit" name="submit" id="submit" value="Submit">
                        </form>
                </div>
        </body>
</html>