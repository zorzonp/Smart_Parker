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



    //Each time we want to communicate with the server it need to be with an asynchronous task.
    //Create a class for each request type to the server
    private class getSaltTask extends AsyncTask<String, Void, Void> {

        AlertDialog alertDialog;
        Integer status = 0;

        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(UpdateUserActivity.this).create();
        }

        @Override
        protected Void doInBackground(String... params){

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        helper = new ServerHelper();

        //get the info from the previous activity
        Bundle bundle = getIntent().getExtras();
        origonalDriver = bundle.getParcelable("user");

        usernameText = (EditText)findViewById(R.id.updateUsernameText);
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
        //check each field to see if it changed if it did then update the driver user
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
        String oldSalt = salt;
        System.out.println("Salt: " + oldSalt + " :end");


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
        if (passwordText.getText().toString() != origonalDriver.password_hash){

            ServerHelper helper = new ServerHelper();
            //generate a new salt if the password was updated
            String salt = helper.genSalt();

            //rehash the password
            String password = helper.salt_and_hash(salt,passwordText.getText().toString());


            newDriverInfo.password_hash = password;
        }





    }

    private JSONObject updateUser(String oldUserName, String  oldPassword, newUserName, newPassword, first_name,
                                  last_name, salt, plate, state, make, model, year, color, email,
                                  url){

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
