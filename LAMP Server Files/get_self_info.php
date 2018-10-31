<?php
/*******************************************************************
 * Author:      Peter Zorzonello                                   *
 * Class:       EC601-A1                                           *
 * File:        get_self_info.php                                  *
 * Description: Looks up a driver user and retunes information.    *
 *              The info returned is that of the person making     * 
 *              the request.                                       *
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
                if(!empty($_POST['username']) && !empty($_POST['password'])){

                    $uname = $_POST['username'];
                    $password = $_POST['password'];

                    /*Create the query for the DB*/
                    $user_query = "SELECT * FROM users WHERE username = :username";
                    $user_statment = $connection->prepare($user_query);
                    
                    /*execute the query*/
                    $user_statment->execute(array(':username'=>$uname));  
                    

                    /*Check that we got a result*/
                    if($user_statment->rowCount()>0){
                        $user_rows = $user_statment->fetchAll(PDO::FETCH_ASSOC);
                        $user_pass = $user_rows[0]['password'];
                        

                        //make sure the password given matches the password in the DB
                        if ($user_pass == $password){

                            $response_to_send = array();
                            /*get all the properties the user can change*/
                            $response_to_send["first_name"] = $user_rows[0]['first_name'];
                            $response_to_send["last_name"] = $user_rows[0]["last_name"];
                            $response_to_send["username"] = $user_rows[0]["username"];
                            $response_to_send["license_plate"] = $user_rows[0]["license_num"];
                            $response_to_send["state"] = $user_rows[0]["license_state"];
                            $response_to_send["make"] = $user_rows[0]["make"];
                            $response_to_send["model"] = $user_rows[0]["model"];
                            $response_to_send["year"] = $user_rows[0]["year"];
                            $response_to_send["color"] = $user_rows[0]["color"];
                            $response_to_send["email"] = $user_rows[0]["email"];

                            /*get the property the user can view*/
                            $response_to_send["good_standing"] = $user_rows[0]["good_standing"];

                            /*return the results*/
                            $response["status"] = 1;
                            $response["message"] = "Success";
                            $response["result"] = $response_to_send;

                        }else{
                            $response['status'] = 0;
                            $response['message'] = "Falure, password and username do not match";
                        }


                    }else{
                        $response['status'] = 0;
                        $response['message'] = "Falure, could not find user";
                    }


                }else{/*one of the required fileds was not set*/
                        $response["status"] = 0;
                        $response["message"] = "One of the fields was not set";
                }
                /*send result as JSON*/
                echo json_encode($response);

        }catch(PDOException $e){ /*Any exceptions are caught.*/
            $message = $e->getMessage();
            $response["status"] = 0;
            $response["message"] = "Falure: . $message";
            echo json_encode($response);
        }

?>