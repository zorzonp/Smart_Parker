package com.example.xcliang.singin;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static android.app.PendingIntent.getActivity;
import static android.content.Intent.getIntent;

public class DisplayUser extends AppCompatActivity {

    User driver;

    TextView fNameView;
    TextView uNameView;
    TextView lNameView;
    TextView emailView;
    TextView plateView;
    TextView stateView;
    TextView makeView;
    TextView modelView;
    TextView yearView;
    TextView colorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user);

        //get the info from the previous activity
        Bundle bundle = getIntent().getExtras();
        driver = bundle.getParcelable("user");

        System.out.println("IN second activity");
        driver.print();

        fNameView = (TextView)findViewById(R.id.fNameView);
        lNameView = (TextView)findViewById(R.id.lNameView);
        uNameView = (TextView)findViewById(R.id.unameView);
        emailView = (TextView)findViewById(R.id.emailView);
        plateView = (TextView)findViewById(R.id.plateView);
        stateView = (TextView)findViewById(R.id.stateView);
        makeView = (TextView)findViewById(R.id.makeView);
        modelView = (TextView)findViewById(R.id.modelView);
        yearView = (TextView)findViewById(R.id.yearView);
        colorView = (TextView)findViewById(R.id.colorView);

        fNameView.setText(driver.firstName);
        lNameView.setText(driver.lastName);
        uNameView.setText(driver.username);
        emailView.setText(driver.email);
        plateView.setText(driver.license_plate);
        stateView.setText(driver.license_state);
        makeView.setText(driver.make);
        modelView.setText(driver.model);
        yearView.setText(driver.year.toString());
        colorView.setText(driver.color);





    }
}
