<?php
/*******************************************************************
 * Author:      Peter Zorzonello                                   *
 * Class:       EC601-A1                                           *
 * File:        add_owner.php                                      *
 * Description: Adds an owner/operator to the owners table         *
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

                if(!empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['salt'])){

                        $uname = $_POST['username'];
                        $pword = utf8_decode($_POST['password']);
                        $salt = utf8_decode($_POST['salt']);

                        $query = "INSERT INTO owners (username, password, salt) VALUES(:username, :password, :salt)";

                        $statment = $connection->prepare($query);

                        $value = $statment->execute(array(':username' => $uname, ':password' => $pword, ':salt' => $salt));

                        if ($value == true){
                                $response["status"] = 1;
                                $response["message"] = "Success";
                        }else{
                                $response["status"] = 0;
                                $response["message"] = "Falure to insert";
                        }

                }else{
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