<?php
/*******************************************************************
 * Author:      Peter Zorzonello                                   *
 * Class:       EC601-A1                                           *
 * File:        get_salt.php                                       *
 * Description: Returns the salt for a user in the owners table    *
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

                if(!empty($_POST['username']) && !empty($_POST['type'])){

                        $uname = $_POST['username'];
                        $type = $_POST['type'];

                        if ($type == 'user' || $type == 'owner'){
                                
                                if ($type == 'user'){
                                        $query = "SELECT salt FROM users WHERE username = :username";
                                }elseif ($type == 'owner') {
                                        $query = "SELECT salt FROM owners WHERE username = :username";
                                }
                                
                                $statment = $connection->prepare($query);

                                $statment->execute(array(':username' => $uname));

                                if ($statment->rowCount()>0){
                                        /*get all the requested info*/
                                        $rows = $statment->fetchAll(PDO::FETCH_ASSOC);

                                        /*return the results*/
                                        $response["status"] = 1;
                                        $response["message"] = "Success";
                                        $response["result"] = $rows[0];

                                }else{ /*if the query returned no results, the user could not be found*/
                                        $response["status"] = 0;
                                        $response["message"] = "Falure, could not find user";
                                }

                        }else{ /*the request did not provide a valid type*/
                                $response["status"] = 0;
                                $response["message"] = "Falure, type $type is not valid."; 
                        }


                }else{
                        $response["status"] = 0;
                        $response["message"] = "Falure, required field not set";
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