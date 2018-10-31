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
                                <!-- <input type="text" name="state" placeholder="State"></input><br/> -->
                                <p>State: 
                                <select name="state">
                                        <option value="AL">AL</option>
                                        <option value="AK">AK</option>
                                        <option value="AZ">AZ</option>
                                        <option value="AR">AR</option>
                                        <option value="CA">CA</option>
                                        <option value="CO">CO</option>
                                        <option value="CT">CT</option>
                                        <option value="DE">DE</option>
                                        <option value="FL">FL</option>
                                        <option value="GA">GA</option>
                                        <option value="HI">HI</option>
                                        <option value="ID">ID</option>
                                        <option value="IL">IL</option>
                                        <option value="IN">IN</option>
                                        <option value="IA">IA</option>
                                        <option value="KS">KS</option>
                                        <option value="KY">KY</option>
                                        <option value="LA">LA</option>
                                        <option value="ME">ME</option>
                                        <option value="MD">MD</option>
                                        <option value="MA">MA</option>
                                        <option value="MI">MI</option>
                                        <option value="MN">MN</option>
                                        <option value="MS">MS</option>
                                        <option value="MO">MO</option>
                                        <option value="MT">MT</option>
                                        <option value="NE">NE</option>
                                        <option value="NV">NV</option>
                                        <option value="NH">NH</option>
                                        <option value="NJ">NJ</option>
                                        <option value="NM">NM</option>
                                        <option value="NY">NY</option>
                                        <option value="NC">NC</option>
                                        <option value="ND">ND</option>
                                        <option value="OH">OH</option>
                                        <option value="OK">OK</option>
                                        <option value="OR">OR</option>
                                        <option value="PA">PA</option>
                                        <option value="RI">RI</option>
                                        <option value="SC">SC</option>
                                        <option value="SD">SD</option>
                                        <option value="TN">TN</option>
                                        <option value="TX">TX</option>
                                        <option value="UT">UT</option>
                                        <option value="VT">VT</option>
                                        <option value="VA">VA</option>
                                        <option value="WA">WA</option>
                                        <option value="WV">WV</option>
                                        <option value="WI">WI</option>
                                        <option value="WY">WY</option>
                                        <option value="AS">AS</option>
                                        <option value="DC">DC</option>
                                        <option value="FM">FM</option>
                                        <option value="GU">GU</option>
                                        <option value="MH">MH</option>
                                        <option value="MP">MP</option>
                                        <option value="PW">PW</option>
                                        <option value="PR">PR</option>
                                        <option value="VI">VI</option>
                                </select></p><br/>
                                <input type="text" name="make" placeholder="Make"></input><br/>
                                <input type="text" name="model" placeholder="Model"></input><br/>
                                <input type="text" name="year" placeholder="Year"></input><br/>
                                <input type="text" name="color" placeholder="Color"></input><br/>

                                <br/>
                                <br/>

                                <input type="text" name="username" placeholder="Username"></input><br/>
                                <input type="text" name="password" placeholder="Password"></input><br/>
                                <input type="text" name="email" placeholder="Email"></input><br/> 
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
                        <h3>Test Update User</h3>
                        <form action="update_driver_info.php" method="post" id="form">
                                <input type="text" name="first_name" id="first_name" placeholder="First Name"></input><br/>
                                <input type="text" name="last_name" id="last_name" placeholder="Last Name"></input><br/>
                                <input type="text" name="license_plate" placeholder="License Plate Number"></input><br/>
                                <!-- <input type="text" name="state" placeholder="State"></input><br/> -->
                                <p>State: 
                                <select name="state">
                                        <option value="AL">AL</option>
                                        <option value="AK">AK</option>
                                        <option value="AZ">AZ</option>
                                        <option value="AR">AR</option>
                                        <option value="CA">CA</option>
                                        <option value="CO">CO</option>
                                        <option value="CT">CT</option>
                                        <option value="DE">DE</option>
                                        <option value="FL">FL</option>
                                        <option value="GA">GA</option>
                                        <option value="HI">HI</option>
                                        <option value="ID">ID</option>
                                        <option value="IL">IL</option>
                                        <option value="IN">IN</option>
                                        <option value="IA">IA</option>
                                        <option value="KS">KS</option>
                                        <option value="KY">KY</option>
                                        <option value="LA">LA</option>
                                        <option value="ME">ME</option>
                                        <option value="MD">MD</option>
                                        <option value="MA">MA</option>
                                        <option value="MI">MI</option>
                                        <option value="MN">MN</option>
                                        <option value="MS">MS</option>
                                        <option value="MO">MO</option>
                                        <option value="MT">MT</option>
                                        <option value="NE">NE</option>
                                        <option value="NV">NV</option>
                                        <option value="NH">NH</option>
                                        <option value="NJ">NJ</option>
                                        <option value="NM">NM</option>
                                        <option value="NY">NY</option>
                                        <option value="NC">NC</option>
                                        <option value="ND">ND</option>
                                        <option value="OH">OH</option>
                                        <option value="OK">OK</option>
                                        <option value="OR">OR</option>
                                        <option value="PA">PA</option>
                                        <option value="RI">RI</option>
                                        <option value="SC">SC</option>
                                        <option value="SD">SD</option>
                                        <option value="TN">TN</option>
                                        <option value="TX">TX</option>
                                        <option value="UT">UT</option>
                                        <option value="VT">VT</option>
                                        <option value="VA">VA</option>
                                        <option value="WA">WA</option>
                                        <option value="WV">WV</option>
                                        <option value="WI">WI</option>
                                        <option value="WY">WY</option>
                                        <option value="AS">AS</option>
                                        <option value="DC">DC</option>
                                        <option value="FM">FM</option>
                                        <option value="GU">GU</option>
                                        <option value="MH">MH</option>
                                        <option value="MP">MP</option>
                                        <option value="PW">PW</option>
                                        <option value="PR">PR</option>
                                        <option value="VI">VI</option>
                                </select></p><br/>
                                <input type="text" name="make" placeholder="Make"></input><br/>
                                <input type="text" name="model" placeholder="Model"></input><br/>
                                <input type="text" name="year" placeholder="Year"></input><br/>
                                <input type="text" name="color" placeholder="Color"></input><br/>

                                <br/>
                                <br/>

                                <input type="text" name="username" placeholder="Username"></input><br/>
                                <input type="text" name="new_uname" placeholder="New Username"></input><br/>
                                <input type="text" name="password" placeholder="Password"></input><br/>
                                <input type="text" name="new_password" placeholder="New Password"></input><br/>
                                <input type="text" name="email" placeholder="Email"></input><br/> 
                                <input type="text" name="salt" placeholder="1234567890"></input><br/>
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
                                <input type="text" name="type" placeholder="Type"></input><br/>
                                <input type="submit" name="submit" id="submit" value="Submit">
                        </form>
                        <h3>Test Get Self</h3>
                        <form action="get_self.php" method="post" id="form">
                                <input type="text" name="username" placeholder="Username"></input><br/>
                                <input type="text" name="password" placeholder="Password"></input><br/>
                                <input type="submit" name="submit" id="submit" value="Submit">
                        </form>
                </div>
        </body>
</html>