<?php
/*******************************************************************
 * Author:      Peter Zorzonello                                   *
 * Class:       EC601-A1                                           *
 * File:        add_history.php                                    *
 * Description: Adds a drivers recent vist to the history table.   *
 *              This table keepts track of all people who use the  *
 *              Smart Parker Service                               *
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

                if(!empty($_POST['username']) && !empty($_POST['in_datetime']) && !empty($_POST['out_datetime'])){

                        
                        $uname = $_POST['username'];
                        $in_time = $_POST['in_datetime'];
                        $out_time = $_POST['out_datetime'];
                        

                        $query = "INSERT INTO history (username, in_time, out_time) VALUES(:username, )";



                        $statment = $connection->prepare($query);


                        $value = $statment->execute(array(':username' => $uname);

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