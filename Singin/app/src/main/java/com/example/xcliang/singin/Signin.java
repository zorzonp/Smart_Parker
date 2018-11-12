package com.example.xcliang.singin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.view.View;


public class Signin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Button btn1=(Button)findViewById(R.id.btn_button1);
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                gotoLayout2();
            }
        });

    }

    private void gotoLayout2(){
        setContentView(R.layout.activity_signin2);
        Button btn2=(Button)findViewById(R.id.btn_button2_1);
        Button btn3=(Button)findViewById(R.id.btn_button2_2);

        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                gotoLayout1();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                gotoLayout3();
            }
        });
    }

    private void gotoLayout1(){
        setContentView(R.layout.activity_signin);
        Button btn1=(Button)findViewById(R.id.btn_button1);
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                gotoLayout2();
            }
        });
    }

    private void gotoLayout3(){
        setContentView(R.layout.activity_signin3);
        Button btn4=(Button)findViewById(R.id.btn_button3_1);
        btn4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                gotoLayout2();
            }
        });
    }


}
