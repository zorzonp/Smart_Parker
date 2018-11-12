package com.example.xcliang.singin;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class ServerHelper {


    //refactoring, this code is called each time we connect to the server.
    //we can reuse it by making it a function
    public String communicateWithServer(String url_name, String data){
        String text = "";

        BufferedReader reader=null;

        //try and make a request to the server
        try{
            //create the URL
            URL url = new URL(url_name);

            //open the connection
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            //write the data to the server
            wr.write(data);
            wr.flush();

            //read the response from the server
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();

            String line = null;

            //keep reading from the response until we get the whole thing
            while((line = reader.readLine()) != null){
                sb.append(line + "\n");

            }

            //convert the string builder into a String object we can use
            text = sb.toString();



        }catch (Exception ex){
            System.out.println("Error: " + ex.toString());

        }
        finally {
            try{
                //close the reader if still open.
                reader.close();
            }catch (Exception ex){
                System.out.println("Error: " + ex.toString());
            }
        }
        return text;
    }

    //TODO: Salt the password and then hash it
    public String salt_and_hash(String salt, String password){
        String hash = "";
        return hash;
    }

    //This function will generate a random salt that can be concatinated with a password.
    //The salt returned will always be 10 characters long.
    public String genSalt(){
        String salt = "";

        //create an instance of a random number generator
        Random rand = new Random();

        //create a 10 character long salt
        for(Integer i = 0; i <= 10; i++){
            int n = rand.nextInt(127);
            char c = (char) n;
            salt = salt + c;
        }
        //System.out.println("New SALT: " + salt);

        //return the salt to the user
        return salt;
    }


}
