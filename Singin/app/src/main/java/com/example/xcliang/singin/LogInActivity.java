package com.example.xcliang.singin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import android.support.v7.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class LogInActivity extends AppCompatActivity {

    EditText uname_text;
    EditText pword_text;

    String url = "https://smartparker.cf/get_salt.php";
    String getUserURL = "https://smartparker.cf/get_self.php";
    String username;
    String plain_password;
    String salt = "";
    User driver;

    ServerHelper helper = new ServerHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        uname_text = (EditText)findViewById(R.id.username_login_field);
        pword_text = (EditText)findViewById(R.id.passwd_login_field);

        uname_text.setText("");
        pword_text.setText("");


    }

    //Each time we want to communicate with the server it need to be with an asynchronous task.
    //Create a class for each request type to the server
    private class MyTask extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... params){


            try{
                getSalt(username, url);


            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
        }
    }

    private  class MyGetUserTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected  Void doInBackground(Void... params){
            try{
                driver = getUserInfo(username, plain_password, getUserURL);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
        }
    }



    //This procedure is triggered by the Login button.
    //It will call the required tasks to communicate with the server
    public void login(View view){
        //get the values from the text boxes on the page
        username = uname_text.getText().toString();
        plain_password = pword_text.getText().toString();

        //call the task that gets the user's salt
        MyTask task = new MyTask();
        try {
            //call the task and hold all execution until it finishes
            task.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //only proceed to get info if the salt was set
        if (salt != ""){
            //call the task to get the users info
            MyGetUserTask task1 = new MyGetUserTask();
            try {
                //call the task and hold all execution until it finishes
                task1.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //navigate to the display page
            Intent intent = new Intent(this, DisplayUser.class);
            intent.putExtra("user", driver);
            startActivity(intent);
        }


    }


    //This function will take a username, and password from the user
    //and send it as a POST request to url_name. It will read the response and inform the user.
    public User getUserInfo(String username, String password, String url_name)
            throws UnsupportedEncodingException{


        //salt and hash the password
        //password = salt_and_hash(salt, password);

        //encode the data we want to send to the server for this request
        String data = URLEncoder.encode("username", "UTF-8") + "=" +
                URLEncoder.encode(username, "UTF-8");
        data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                URLEncoder.encode(password, "UTF-8");

        String json_str = "";
        json_str = helper.communicateWithServer(url_name, data);

        //make sure something was returned
        if(json_str != ""){
            try {
                //Create the JSON object from the returned text from the server.
                JSONObject obj = new JSONObject(json_str);

                //get the Status from the returned JSON object
                String status_str = obj.getString("status");
                System.out.println(obj.toString());

                //convert the Status from a string to an int
                Integer status = Integer.parseInt(status_str);

                if (status == 1) { //the salt was returned
                    //get the result object inside the main object
                    JSONObject results = obj.getJSONObject("result");
                    //for some reason Java will not get the JSON inside the JSON unless it goes
                    //to a string first abd then back to JSON
                    String results_str = results.toString();
                    JSONObject result_obj = new JSONObject(results_str);

                    //get each field
                    String fName = result_obj.getString("first_name");
                    String lName = result_obj.getString("last_name");
                    String uName = result_obj.getString("username");
                    String plate = result_obj.getString("license_plate");
                    String state = result_obj.getString("state");
                    String make = result_obj.getString("make");
                    String model = result_obj.getString("model");
                    Integer year = result_obj.getInt("year");
                    String color = result_obj.getString("color");
                    String email = result_obj.getString("email");
                    Integer standing_int = result_obj.getInt("good_standing");
                    Boolean standing = false;

                    if(standing_int == 1) {
                        standing = true;
                    }

                    //create the user
                    User user = new User(fName, lName, uName, plate, state, make, model, year, color, email, standing, password);
                    user.print();
                    return user;
                } else {//if the request for a salt was not successful
                    String message = obj.getString("message");
                    System.out.println(message);

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //return the user to the task
        return null;
    }

    //this procedure will query the server for the user's salt. You must provide the username
    //for the salt you are requesting.
    public void getSalt(String username, String url_name) throws UnsupportedEncodingException {

        //encode the data we want to send to the server for this request
        String data = URLEncoder.encode("username", "UTF-8") + "=" +
                URLEncoder.encode(username, "UTF-8");
        data += "&" + URLEncoder.encode("type", "UTF-8") + "=" +
                URLEncoder.encode("user", "UTF-8");


        //communicate with the server
        String text = "";
        text = helper.communicateWithServer(url_name, data);


        //process the returned data
        try {
            //Create the JSON object from the returned text from the server.
            JSONObject obj = new JSONObject(text);

            //get the Status from the returned JSON object
            String status_str = obj.getString("status");
            System.out.println("Object " + obj.toString());

            //convert the Status from a string to an int
            Integer status = Integer.parseInt(status_str);

            if (status == 1) { //the salt was returned

                //get the result object inside the main object
                JSONObject results = obj.getJSONObject("result");

                //for some reason Java will not get the JSON inside the JSON unless it goes
                //to a string first abd then back to JSON
                String results_str = results.toString();
                JSONObject result_obj = new JSONObject(results_str);

                salt = result_obj.getString("salt");


            } else {//if the request for a salt was not successful

                String message = obj.getString("message");
                System.out.println(message);

            }


        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
