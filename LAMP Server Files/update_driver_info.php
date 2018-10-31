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

            if (!empty($_POST['username']) && !empty($_POST['password'])){

                $uname = $_POST['username'];
                $password = $_POST['password'];

                /*Create the query for the DB*/
                $user_query = "SELECT password FROM users WHERE username = :username";
                $user_statment = $connection->prepare($user_query);
                    
                /*execute the query*/
                $user_statment->execute(array(':username'=>$uname));  


                /*Check that we got a result*/
                    if($user_statment->rowCount()>0){
                        $user_rows = $user_statment->fetchAll(PDO::FETCH_ASSOC);
                        $user_pass = $user_rows[0]['password'];
                        

                        //make sure the password given matches the password in the DB
                        if ($user_pass == $password){

                            #make sure all the user info is being returned. Even if it did not change.
                            if(!empty($_POST['first_name']) && !empty($_POST['last_name']) && !empty($_POST['salt']) && !empty($_POST['license_plate']) && !empty($_POST['state']) && !empty($_POST['make']) && !empty($_POST['model']) && !empty($_POST['year']) && !empty($_POST['color']) && !empty($_POST['email']) && !empty($_POST['new_uname']) && !empty($_POST['new_password'])){

                                $fname = $_POST['first_name'];
                                $lname = $_POST['last_name'];
                                $uname = $_POST['username'];
                                $pword = $_POST['new_password'];
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


                        }else{ //pasword is not the same as in the db
                            $response["status"] = 0;
                            $response["message"] = "Password does not match";
                        }

                    }else{ //could not find user
                            $response["status"] = 0;
                            $response["message"] = "User not found";
                    }

            }else {
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