package com.example.xcliang.singin;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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

    //Generates a salted and hashed password
    public String salt_and_hash( String salt, String password){
        String hash = BCrypt.hashpw(password, salt);
        return hash;
    }

    //This function will generate a random salt that can be concatinated with a password.
    public String genSalt(){
        String salt = BCrypt.gensalt();
        return salt;
    }


}
