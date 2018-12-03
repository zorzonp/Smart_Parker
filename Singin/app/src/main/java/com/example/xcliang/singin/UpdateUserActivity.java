package com.example.xcliang.singin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

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
}
