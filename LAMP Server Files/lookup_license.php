<?php
/*******************************************************************
 * Author:      Peter Zorzonello                                   *
 * Class:       EC601-A1                                           *
 * File:        lookup_license.php                                 *
 * Description: Looks up a driver user and retunes information.    *
 *              The person making the request must be in the owner *
 *              table.                                             *
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

                /*make sure the required fields are set*/
                if(!empty($_POST['license_plate']) && !empty($_POST['state']) && !empty($_POST['username']) && !empty($_POST['password'])){

                        $lnum = $_POST['license_plate'];
                        $lstate = $_POST['state'];
                        $uname = $_POST['username'];
                        $password = $_POST['password'];

                        $operator_query = "SELECT password FROM owners WHERE username = :username";
                        $operator_statment = $connection->prepare($operator_query);
                        $operator_statment->execute(array(':username'=>$uname));

                        if($operator_statment->rowCount()>0){
                                $operator_rows = $operator_statment->fetchAll(PDO::FETCH_ASSOC);
                                $operator_pass = $operator_rows[0]['password'];

                                //make sure the password given matches the password in the DB
                                if ($operator_pass == $password){


                                        $query = "SELECT * FROM users WHERE license_num = :license_num and license_state = :state";

                                        $statment = $connection->prepare($query);

                                        $statment->execute(array(':license_num' => $lnum, ':state' => $lstate));

                                        if ($statment->rowCount()>0){
                                                /*get all the requested info*/
                                                $rows = $statment->fetchAll(PDO::FETCH_ASSOC);

                                                $response_to_send = array();
                                                $response_to_send["first_name"] = $rows[0]["first_name"];
                                                $response_to_send["last_name"] = $rows[0]["last_name"];
                                                $response_to_send["username"] = $rows[0]["username"];
                                                $response_to_send["license_plate"] = $rows[0]["license_num"];
                                                $response_to_send["state"] = $rows[0]["license_state"];
                                                $response_to_send["make"] = $rows[0]["make"];
                                                $response_to_send["model"] = $rows[0]["model"];
                                                $response_to_send["year"] = $rows[0]["year"];
                                                $response_to_send["color"] = $rows[0]["color"];
                                                $response_to_send["email"] = $rows[0]["email"];
                                                $response_to_send["good_standing"] = $rows[0]["good_standing"];

                                                /*return the results*/
                                                $response["status"] = 1;
                                                $response["message"] = "Success";
                                                $response["result"] = $response_to_send;

                                        }else{ /*if the query returned no results, the user could not be found*/
                                                $response["status"] = 0;
                                                $response["message"] = "Falure, could not find user";
                                        }
                                }else{
                                        $response['status'] = 0;
                                        $response['message'] = "Falure, password and username do not match";
                                }
                        }else{
                                $response['status'] = 0;
                                $response['message'] = "Falure, could not find operator";
                        }
                }else{ /*one of the required fileds was not set*/
                        $response["status"] = 0;
                        $response["message"] = "One of the fields was not set";
                }

                echo json_encode($response);

        }catch(PDOException $e){ /*Any exceptions are caught.*/
            $message = $e->getMessage();
            $response["status"] = 0;
            $response["message"] = "Falure: . $message";
            echo json_encode($response);
        }

?>