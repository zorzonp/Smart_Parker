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

    ServerHelper helper;

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



    //Each time we want to communicate with the server it need to be with an asynchronous task.
    //Create a class for each request type to the server
    private class updateTask extends AsyncTask<String, Void, Void> {

        AlertDialog alertDialog;
        JSONObject objReturn;


        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(UpdateUserActivity.this).create();
        }

        @Override
        protected Void doInBackground(String... params){

            String oldUsername = params[0];
            String oldPassword = params[1];
            String newUsername = params[2];
            String newPassword = params[3];
            String fname = params[4];
            String lname = params[5];
            String license_num = params[6];
            String state = params[7];
            String make = params[8];
            String model = params[9];
            String year = params[10];
            String color = params[11];
            String email = params[12];
            String url = params[13];
            String salt = params[14];



            try{

                objReturn = updateUser(oldUsername, oldPassword, newUsername, newPassword, fname,
                        lname, license_num, state, make,model, year, color, email, url, salt);


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

        helper = new ServerHelper();

        //get the info from the previous activity
        Bundle bundle = getIntent().getExtras();
        origonalDriver = bundle.getParcelable("user");

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
        passwordText.setText(origonalDriver.password_hash);
    }


    public void cancelBtnPressed(View view){
        //navigate to the change info page
        Intent displayUserIntent = new Intent(this, DisplayUser.class );
        displayUserIntent.putExtra("user", origonalDriver);
        startActivity(displayUserIntent);

    }

    public void submitBtnPressed(View view){
        //add a progress dialog before the program get user info
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(UpdateUserActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("ProgressDialog");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(800);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();


        //check each field to see if it changed if it did then update the driver user
        User newDriverInfo = origonalDriver;
        if (usernameText.getText().toString() != origonalDriver.username){
            newDriverInfo.username = usernameText.getText().toString();
        }

        if (firstNameText.getText().toString() != origonalDriver.firstName){
            newDriverInfo.firstName = firstNameText.getText().toString();
        }
        if (lastNameTest.getText().toString() != origonalDriver.lastName){
            newDriverInfo.lastName = lastNameTest.getText().toString();
        }
        if (emailText.getText().toString() != origonalDriver.email){
            newDriverInfo.email = emailText.getText().toString();
        }
        if (plateText.getText().toString() != origonalDriver.license_plate){
            newDriverInfo.license_plate = plateText.getText().toString();
        }
        if (stateText.getText().toString() != origonalDriver.license_state){
            newDriverInfo.license_state = stateText.getText().toString();
        }
        if (makeText.getText().toString() != origonalDriver.make){
            newDriverInfo.make = makeText.getText().toString();
        }
        if (modelText.getText().toString() != origonalDriver.model){
            newDriverInfo.model = modelText.getText().toString();
        }
        if (yearText.getText().toString() != origonalDriver.year){
            newDriverInfo.year = yearText.getText().toString();
        }
        if (colorText.getText().toString() != origonalDriver.color){
            newDriverInfo.color = colorText.getText().toString();
        }
        

        String salt = "";
        if (passwordText.getText().toString() != origonalDriver.password_hash){

            ServerHelper helper = new ServerHelper();
            //generate a new salt if the password was updated
            salt = helper.genSalt();

            //rehash the password
            String password = helper.salt_and_hash(salt,passwordText.getText().toString());


            newDriverInfo.password_hash = password;


        }


        String updateUrl = "https://smartparker.cf/update_driver_info.php";

        //create an instance of the async task that communicates with server and
        // signs up a user
        updateTask task = new updateTask();
        try{
            //call the task and hold all execution until it finishes
            task.execute(origonalDriver.username, origonalDriver.password_hash,
                    newDriverInfo.username, newDriverInfo.password_hash,
                    newDriverInfo.firstName, newDriverInfo.lastName,
                    newDriverInfo.license_plate, newDriverInfo.license_state,
                    newDriverInfo.make, newDriverInfo.model,
                    newDriverInfo.year, newDriverInfo.color, newDriverInfo.email,updateUrl, salt).get();



        }catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }

    public JSONObject updateUser(String oldUsername, String oldPassword, String newUsername,
                                 String newPassword, String fname, String lname, String license_num,
                                 String state, String make, String model, String year, String color,
                                 String email, String url, String salt) throws UnsupportedEncodingException {


        //create parameter to be sent to the server
        String data = URLEncoder.encode("username", "UTF-8") + "=" +
                URLEncoder.encode(oldUsername, "UTF-8");
        data += "&" + URLEncoder.encode("new_password", "UTF-8") + "=" +
                URLEncoder.encode(newPassword, "UTF-8");
        data += "&" + URLEncoder.encode("salt", "UTF-8") + "=" +
                URLEncoder.encode(salt, "UTF-8");
        data += "&" + URLEncoder.encode("first_name", "UTF-8") + "=" +
                URLEncoder.encode(fname, "UTF-8");
        data += "&" + URLEncoder.encode("last_name", "UTF-8") + "=" +
                URLEncoder.encode(lname, "UTF-8");
        data += "&" + URLEncoder.encode("license_plate", "UTF-8") + "=" +
                URLEncoder.encode(license_num, "UTF-8");
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
                URLEncoder.encode(newUsername, "UTF-8");
        data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                URLEncoder.encode(oldPassword, "UTF-8");

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
}
