<?php
/*******************************************************************
 * Author:      Peter Zorzonello                                   *
 * Class:       EC601-A1                                           *
 * File:        update_owner_info.php                              *
 * Description: Updates the owners password                        *
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

                //check for required fields
                if(!empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['newSalt']) && !empty($_POST['newPassword'])){
                
                        $uname = $_POST['username'];
                        $password = utf8_decode($_POST['password']);

                        /*Create the query for the DB*/
                        // $owner_query = "SELECT password FROM owners WHERE username = :username";
                        $owner_query = "SELECT * FROM owners WHERE username = :username";
                        $owner_statment = $connection->prepare($owner_query);
        
                        /*execute the query*/
                        // $owner_statment->execute(array(':username'=>$uname));  
                        $owner_statment->execute(array(':username'=>$uname));

                        //echo($owner_statment->rowCount());

                        /*Check that we got a result*/
                        if($owner_statment->rowCount() > 0){
                                //we have at least one result so now get the password from that entry
                                $owner_rows = $owner_statment->fetchAll(PDO::FETCH_ASSOC);
                                $owner_pass = $owner_rows[0]['password'];
                                

                                //make sure the password given matches the password in the DB
                                if ($owner_pass == $password){
                                        //get the salt and password from the Python and place in a format SQL can understand
                                        $newPassword = utf8_decode($_POST['newPassword']);
                                        $newSalt = utf8_decode($_POST['newSalt']);

                                        //make the update
                                        $query = "UPDATE owners SET password = :newPassword, salt = :newSalt WHERE username = :username";
                                        $statment = $connection->prepare($query);
                                        $value = $statment->execute(array(':username' => $uname, ':newSalt' => $newSalt, ':newPassword' => $newPassword));


                                        //if the update was successful
                                        if ($value == true){
                                                $response["status"] = 1;
                                                $response["message"] = "Success";
                                        }else{
                                                //the update was not successful
                                                $response["status"] = 0;
                                                $response["message"] = "Falure to update";
                                        }

                                }else{
                                        //pasword is not the same as in the db
                                        $response["status"] = 0;
                                        $response["message"] = "Password does not match";
                                }
                        }else{//the number of results was 0, no user was found
                                $response["status"] = 0;
                                $response["message"] = "That user could not be found";  
                        }

                }else{ 
                        // one of the required fields was not set. Tell the caller this.
                        $response["status"] = 0;
                        $response["message"] = "Falure, one or more fields are missing.";
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