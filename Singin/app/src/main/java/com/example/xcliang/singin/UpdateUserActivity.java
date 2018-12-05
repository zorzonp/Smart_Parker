package com.example.xcliang.singin;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class UpdateUserActivity extends AppCompatActivity {

    User origonalDriver;

    EditText usernameText;
    EditText firstNameText;
    EditText lastNameTest;
    EditText emailText;
    EditText plateText;
    EditText stateText;
    EditText makeText;
    EditText modelText;
    EditText yearText;
    EditText colorText;
    EditText passwordText;

    Button cancelBtn;
    Button submitBtn;

    ServerHelper helper;
    String salt;

    String updateUrl = "https://smartparker.cf/update_driver_info.php";

    JSONObject objReturn;

    private String pass;



    //Each time we want to communicate with the server it need to be with an asynchronous task.
    //Create a class for each request type to the server

    //This class is to communicate with the server and get the salt
    private class getSaltTask extends AsyncTask<String, Void, Void> {

        AlertDialog alertDialog;
        Integer status = 0;

        //set up an alert box in case there is an error
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(UpdateUserActivity.this).create();
        }

        @Override
        protected Void doInBackground(String... params){

            //get the parameters needed by the getSalt function.
            String username = params[0];
            String url = params[1];

            try{
                status = getSalt(username, url);


            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){


            super.onPostExecute(result);

            //if the status returned from getSalt was 1 then there was an error
            //show the error.
            if (status == 1){
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Could not find user");
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setButton(Dialog.BUTTON_NEGATIVE,"Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        }
    }

    //this class is to communicate with the server to update the user info in the database
    private class updateUserTask extends AsyncTask<String, Void, Void> {

        AlertDialog alertDialog;


        //set up an alert box for if there is an error.
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(UpdateUserActivity.this).create();
        }

        @Override
        protected Void doInBackground(String... params){

            //get all of the required parameters needed for the function
            String oldUsername = params[0];
            String oldPassword = params[1];
            System.out.println("Old Pass: " + oldPassword);
            System.out.println("Pass: " + pass);
            String newUsername = params[2];
            String newPassword = params[3];
            System.out.println("New Pass: " + newPassword);
            String firstName = params[4];
            String lastName = params[5];
            String plate = params[6];
            String state = params[7];
            String make = params[8];
            String model = params[9];
            String year = params[10];
            String color = params[11];
            String email = params[12];
            String salt = params[13];
            String url = params[14];


            //try and update the user
            try{
                objReturn = updateUser(oldUsername, oldPassword, newUsername, newPassword, firstName,
                        lastName, salt, plate, state, make, model, year, color, email, url);


            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){


            super.onPostExecute(result);

            //get the Status from the returned JSON object
            String status_str = null;
            try {
                status_str = objReturn.getString("status");


                //convert the Status from a string to an int
                Integer status = Integer.parseInt(status_str);

                if (status == 0){
                    alertDialog.setTitle("Alert");
                    String message = objReturn.getString("message");
                    String errorCheck = "23000";
                    if( message.toLowerCase().contains(errorCheck.toLowerCase())){
                        message = "Username is already taken. Please select a different name.";
                    }
                    alertDialog.setMessage(message);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setButton(Dialog.BUTTON_NEGATIVE,"Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        //set up the helper functions
        helper = new ServerHelper();

        //get the info from the previous activity
        Bundle bundle = getIntent().getExtras();
        origonalDriver = bundle.getParcelable("user");

        //get all of the objects on the display
        usernameText = findViewById(R.id.updateUsernameText);
        firstNameText = findViewById(R.id.updateFirstNameText);
        lastNameTest = findViewById(R.id.updateLastNameText);
        emailText = findViewById(R.id.updateEmailText);
        plateText = findViewById(R.id.updatePlateText);
        stateText = findViewById(R.id.updateStateText);
        makeText = findViewById(R.id.updateMakeText);
        modelText = findViewById(R.id.updateModelText);
        yearText = findViewById(R.id.updateYearText);
        colorText = findViewById(R.id.updateColorText);
        passwordText = findViewById(R.id.updatePasswordText);

        cancelBtn = findViewById(R.id.cancelBtn);
        submitBtn = findViewById(R.id.submitBtn);

        //set the default text for each field
        usernameText.setText(origonalDriver.username);
        firstNameText.setText(origonalDriver.firstName);
        lastNameTest.setText(origonalDriver.lastName);
        emailText.setText(origonalDriver.email);
        plateText.setText(origonalDriver.license_plate);
        stateText.setText(origonalDriver.license_state);
        makeText.setText(origonalDriver.make);
        modelText.setText(origonalDriver.model);
        yearText.setText(origonalDriver.year);
        colorText.setText(origonalDriver.color);
        passwordText.setText("");
        pass = origonalDriver.password_hash;
    }


    //when the user presses cancel go back to the display user activity
    public void cancelBtnPressed(View view){
        //navigate to the change info page
        Intent displayUserIntent = new Intent(this, DisplayUser.class );
        displayUserIntent.putExtra("user", origonalDriver);
        startActivity(displayUserIntent);

    }

    public void submitBtnPressed(View view){
        //make the new driver identical to the old driver
        User newDriverInfo = origonalDriver;

        //Get the old salt, needed later
        //call the task that gets the user's salt
        getSaltTask task = new getSaltTask();
        try {
            //call the task and hold all execution until it finishes
            task.execute(origonalDriver.username, "https://smartparker.cf/get_salt.php").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //if the username field changed then update the username otherwise keep the old one
        if (usernameText.getText().toString() != origonalDriver.username){
            newDriverInfo.username = usernameText.getText().toString();
        }

        //if the first name field changed then update the first name otherwise keep the old one
        if (firstNameText.getText().toString() != origonalDriver.firstName){
            newDriverInfo.firstName = firstNameText.getText().toString();
        }

        //if the last name field changed then update the last name otherwise keep the old one
        if (lastNameTest.getText().toString() != origonalDriver.lastName){
            newDriverInfo.lastName = lastNameTest.getText().toString();
        }

        //if the email field changed then update the email otherwise keep the old one
        if (emailText.getText().toString() != origonalDriver.email){
            newDriverInfo.email = emailText.getText().toString();
        }

        //if the plate field changed then update the plate otherwise keep the old one
        if (plateText.getText().toString() != origonalDriver.license_plate){
            newDriverInfo.license_plate = plateText.getText().toString();
        }

        //if the state field changed then update the state otherwise keep the old one
        if (stateText.getText().toString() != origonalDriver.license_state){
            newDriverInfo.license_state = stateText.getText().toString();
        }

        //if the make field changed then update the make otherwise keep the old one
        if (makeText.getText().toString() != origonalDriver.make){
            newDriverInfo.make = makeText.getText().toString();
        }

        //if the model field changed then update the model otherwise keep the old one
        if (modelText.getText().toString() != origonalDriver.model){
            newDriverInfo.model = modelText.getText().toString();
        }

        //if the year field changed then update the year otherwise keep the old one
        if (yearText.getText().toString() != origonalDriver.year){
            newDriverInfo.year = yearText.getText().toString();
        }

        //if the color field changed then update the color otherwise keep the old one
        if (colorText.getText().toString() != origonalDriver.color){
            newDriverInfo.color = colorText.getText().toString();
        }

        //if the password field changed then update the password otherwise keep the old one
        if (passwordText.getText().toString().length() != 0){

            System.out.println("Gen new pass");
            System.out.println("OldPass: " + origonalDriver.password_hash);
            System.out.println("new pass: " + passwordText.getText().toString());
            //generate a new salt if the password was updated
            salt = helper.genSalt();

            //rehash the password
            String password = helper.salt_and_hash(salt,passwordText.getText().toString());


            newDriverInfo.password_hash = password;
        }

        //update the MySQL DB
        updateUserTask updateTask = new updateUserTask();
        try {

            //call the task and hold all execution until it finishes
            updateTask.execute(origonalDriver.username, origonalDriver.password_hash,
                    newDriverInfo.username, newDriverInfo.password_hash, newDriverInfo.firstName,
                    newDriverInfo.lastName, newDriverInfo.license_plate,
                    newDriverInfo.license_state, newDriverInfo.make, newDriverInfo.model,
                    newDriverInfo.year, newDriverInfo.color, newDriverInfo.email, salt,
                    updateUrl).get();


            //get the Status from the returned JSON object
            String status_str = null;
            try {
                status_str = objReturn.getString("status");


                //convert the Status from a string to an int
                Integer status = Integer.parseInt(status_str);

                if (status != 0){
                    //if the status was no an error transition to the display activity
                    // with the new data

                    Intent displayUserIntent = new Intent(this, DisplayUser.class );
                    displayUserIntent.putExtra("user", newDriverInfo);
                    startActivity(displayUserIntent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }





    }

    //this procedure will communicate with the server. It take the parameters and encodes it to
    //send to the server. It returns the the JSON object that is returned from the server.
    private JSONObject updateUser(String oldUserName, String  oldPassword, String newUserName,
                                  String newPassword, String first_name, String last_name,
                                  String salt, String plate, String state, String make,
                                  String model, String year, String color, String email,
                                  String url) throws UnsupportedEncodingException {

        //create parameter to send to the server
        String data = URLEncoder.encode("username", "UTF-8") + "=" +
                URLEncoder.encode(oldUserName, "UTF-8");
        data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                URLEncoder.encode(pass, "UTF-8");
        data += "&" + URLEncoder.encode("salt", "UTF-8") + "=" +
                URLEncoder.encode(salt, "UTF-8");
        data += "&" + URLEncoder.encode("first_name", "UTF-8") + "=" +
                URLEncoder.encode(first_name, "UTF-8");
        data += "&" + URLEncoder.encode("last_name", "UTF-8") + "=" +
                URLEncoder.encode(last_name, "UTF-8");
        data += "&" + URLEncoder.encode("license_plate", "UTF-8") + "=" +
                URLEncoder.encode(plate, "UTF-8");
        data += "&" + URLEncoder.encode("state", "UTF-8") + "=" +
                URLEncoder.encode(state, "UTF-8");
        data += "&" + URLEncoder.encode("make", "UTF-8") + "=" +
                URLEncoder.encode(make, "UTF-8");
        data += "&" + URLEncoder.encode("model", "UTF-8") + "=" +
                URLEncoder.encode(model, "UTF-8");
        data += "&" + URLEncoder.encode("year", "UTF-8") + "=" +
                URLEncoder.encode(year, "UTF-8");
        data += "&" + URLEncoder.encode("color", "UTF-8") + "=" +
                URLEncoder.encode(color, "UTF-8");
        data += "&" + URLEncoder.encode("email", "UTF-8") + "=" +
                URLEncoder.encode(email, "UTF-8");
        data += "&" + URLEncoder.encode("new_uname", "UTF-8") + "=" +
                URLEncoder.encode(newUserName, "UTF-8");
        data += "&" + URLEncoder.encode("new_password", "UTF-8") + "=" +
                URLEncoder.encode(newPassword, "UTF-8");

        String json_str = "";
        json_str = helper.communicateWithServer(url, data);

        if (json_str != "") {
            try {
                //Create the JSON object from the returned text from the server.
                JSONObject obj = new JSONObject(json_str);

                //get the Status from the returned JSON object
                String status_str = obj.getString("status");
                System.out.println(obj.toString());

                //convert the Status from a string to an int
                Integer status = Integer.parseInt(status_str);

                if (status != 1) { //the salt was returned
                    String message = obj.getString("message");
                    System.out.println(message);
                }
                return obj;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;


    }


    //this procedure will query the server for the user's salt. You must provide the username
    //for the salt you are requesting.
    public Integer getSalt(String username, String url_name) throws UnsupportedEncodingException {

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
                return 0;


            } else {//if the request for a salt was not successful

                String message = obj.getString("message");
                System.out.println(message);
                return 1;
            }


        } catch (Exception e){
            e.printStackTrace();
            return 1;
        }
    }
}
