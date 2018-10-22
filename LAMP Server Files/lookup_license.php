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

                                                /*return the results*/
                                                $response["status"] = 1;
                                                $response["message"] = "Success";
                                                $response["result"] = $rows[0];

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