<?php
/*******************************************************************
 * Author:      Peter Zorzonello                                   *
 * Class:       EC601-A1                                           *
 * File:        update_driver_info.php                             *
 * Description: Allows the driver to update their personal info.   *
 *******************************************************************/

        /*Includes the creds we need to connect to the DB instance*/
        include "../inc/dbinfo.inc";


        try {

        	/*variables needed to access the DB*/
        	$server = DB_SERVER;
            $username = DB_USERNAME;
            $password = DB_PASSWORD;
            $db = DB_DATABASE;

            /*connect to the DB*/
            $connection = new PDO("mysql:host=$server;dbname=$db", "$username", $password);

            $connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

            /*holds the response*/
            $response = array();

            #make sure all the user info is being returned. Even if it did not change.
            if(!empty($_POST['first_name']) && !empty($_POST['last_name']) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['salt']) && !empty($_POST['license_plate']) && !empty($_POST['state']) && !empty($_POST['make']) && !empty($_POST['model']) && !empty($_POST['year']) && !empty($_POST['color']) && !empty($_POST['email']) && !empty($_POST['new_uname'])){

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
                $new_uname = $_POST['new_uname'];

                $query = "UPDATE users SET username = :new_username, first_name = :first_name, last_name = :last_name, password = :password, salt = :salt, license_num = :license_num, license_state = :license_state, make = :make, model = :model, year = :year, color = :color, email = :email WHERE username = :username";

                $statment = $connection->prepare($query);

                $value = $statment->execute(array(':username' => $uname, ':first_name' => $fname, ':last_name' => $lname, ':password' => $pword, ':salt' => $salt , ':license_num' => $lnum, ':license_state' => $lstate, ':make' => $make, ':model' => $model, ':year' => $year, ':color' => $color, ':email' => $email, ':new_username' => $new_uname));

                 if ($value == true){
                    $response["status"] = 1;
                    $response["message"] = "Success";
                }else{
                    $response["status"] = 0;
                    $response["message"] = "Falure to update";
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