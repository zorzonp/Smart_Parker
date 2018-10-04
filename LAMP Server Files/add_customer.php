<?php
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

                if(!empty($_POST['first_name']) && !empty($_POST['last_name']) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['salt']) && !empty($_POST['license_plate']) && !empty($_POST['state']) && !empty($_POST['make']) && !empty($_POST['model']) && !empty($_POST['year']) && !empty($_POST['color']) && !empty($_POST['venmo_username'])){

                        $fname = $_POST['first_name'];
                        $lname = $_POST['last_name'];
                        $uname = $_POST['username'];
                        $pword = $_POST['password'];
                        $salt = $_POST['salt'];
                        $lnum = $_POST['license_plate'];
                        $lstate = $_POST['state'];
                        $make = $_POST['make'];
                        $modle = $_POST['model'];
                        $year = $_POST['year'];
                        $color = $_POST['color'];
                        $venmo = $_POST['venmo_username'];

                        $query = "INSERT INTO users (username, first_name, last_name, password, salt, license_num, license_state, make, modle, year, color, venmo_uname) VALUES(:username, :first_name, :last_name, :password, :salt, :license_num, :license_state, :make, :modle, :year, :color, :venmo_uname)";



                        $statment = $connection->prepare($query);


                        $value = $statment->execute(array(':username' => $uname, ':first_name' => $fname, ':last_name' => $lname, ':password' => $pword, ':salt' => $salt , ':license_num' => $lnum, ':license_state' => $lstate, ':make' => $make, ':modle' => $modle, ':year' => $year, ':color' => $color, ':venmo_uname' => $venmo));


                        if ($value == true){
                                $response["status"] = 1;
                                $response["message"] = "Success";
                                echo "Success, again";
                        }else{
                                $response["status"] = 0;
                                $response["message"] = "Falure to insert";
                                echo "Opps!";
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
