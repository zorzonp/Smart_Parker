<?php
/*******************************************************************
 * Author:      Peter Zorzonello                                   *
 * Class:       EC601-A1                                           *
 * File:        add_customer.php                                   *
 * Description: Adds a driver to the users database. Used for      *
 *              signing up for the service.                        *
 *******************************************************************/

        /*Includes the creds we need to connect to the DB instance*/
        include "../inc/dbinfo.inc";

        try {
                $server = DB_SERVER;
                $username = DB_USERNAME;
                $password = DB_PASSWORD;
                $db = DB_DATABASE;


                $connection = new PDO("mysql:host=$server;dbname=$db", "$username", $password);

                $connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

                $response = array();

                if(!empty($_POST['first_name']) && !empty($_POST['last_name']) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['salt']) && !empty($_POST['license_plate']) && !empty($_POST['state']) && !empty($_POST['make']) && !empty($_POST['model']) && !empty($_POST['year']) && !empty($_POST['color']) && !empty($_POST['email'])){

                        $fname = $_POST['first_name'];
                        $lname = $_POST['last_name'];
                        $uname = $_POST['username'];
                        $pword = $_POST['password'];
                        $salt = $_POST['salt'];
                        $lnum = $_POST['license_plate'];
                        $lstate = $_POST['state'];
                        $make = $_POST['make'];
                        $model = $_POST['model'];
                        $year = $_POST['year'];
                        $color = $_POST['color'];
                        $email = $_POST['email'];
                        $good_standing = True;

                        $query = "INSERT INTO users (username, first_name, last_name, password, salt, license_num, license_state, make, model, year, color, email, good_standing) VALUES(:username, :first_name, :last_name, :password, :salt, :license_num, :license_state, :make, :model, :year, :color, :email, :good_standing)";



                        $statment = $connection->prepare($query);


                        $value = $statment->execute(array(':username' => $uname, ':first_name' => $fname, ':last_name' => $lname, ':password' => $pword, ':salt' => $salt , ':license_num' => $lnum, ':license_state' => $lstate, ':make' => $make, ':model' => $model, ':year' => $year, ':color' => $color, ':email' => $email, ':good_standing' => $good_standing));

                        if ($value == true){
                                $response["status"] = 1;
                                $response["message"] = "Success";
                                
                        }else{
                                $response["status"] = 0;
                                $response["message"] = "Falure to insert";
                                
                        }
                }else{
                        $response["status"] = 0;
                        $response["message"] = "One of the fields was not set";
                }

                echo json_encode($response);


        }catch(PDOException $e) {
                /*Any exceptions are caught.*/
                $message = $e->getMessage();
                $response["status"] = 0;
                $response["message"] = "Falure: . $message";
                echo json_encode($response);
        }
?>