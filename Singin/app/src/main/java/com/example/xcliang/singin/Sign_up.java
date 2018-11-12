package com.example.xcliang.singin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import java.util.Properties;
import java.io.PrintWriter;

import org.json.JSONObject;




public class Sign_up extends AppCompatActivity {

    EditText username_text;
    EditText firstname_text;
    EditText lastname_text;
    EditText password_text;
    EditText licencenum_text;
    EditText licencestate_text;
    EditText make_text;
    EditText model_text;
    EditText year_text;
    EditText color_text;
    EditText venmo_uname;

    User driver;

    String url = "https://smartparker.cf/add_customer.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        username_text = (EditText)findViewById(R.id.username_signup_field);
        firstname_text = (EditText)findViewById(R.id.firstname_signup_field);
        lastname_text = (EditText)findViewById(R.id.username_signup_field);
        password_text = (EditText)findViewById(R.id.password_signup_field);
        licencenum_text = (EditText)findViewById(R.id.licencenum_signup_field);
        licencestate_text = (EditText)findViewById(R.id.licencestate_signup_field);
        make_text = (EditText)findViewById(R.id.make_signup_field);
        model_text = (EditText)findViewById(R.id.model_signup_field);
        year_text = (EditText)findViewById(R.id.year_signup_field);
        color_text = (EditText)findViewById(R.id.color_signup_field);
        venmo_uname = (EditText)findViewById(R.id.venmoname_signup_field);



        Button btn1=(Button)findViewById(R.id.btn_button1);
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                gotoLayout2();
            }
        });

//        driver = getinfo_signup(username_text.getText(), password_text, firstname_text, lastname_text,
//                licencenum_text, licencestate_text, make_text, model_text, year_text, color_text,
//                venmo_uname, url);

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
        //when the user hits the very first button on the very first sign up page
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
        //when the user hits the back button on the last sign up page
        setContentView(R.layout.activity_signin3);
        Button btn4=(Button)findViewById(R.id.btn_button3_1);

        Button btnSubmit=(Button)findViewById(R.id.btn_button3_2);


        btn4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                gotoLayout2();
            }
        });

        //when the submit button is pressed this is called
        btnSubmit.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               String username = username_text.getText().toString();
               String password = password_text.getText().toString();
               String fname = firstname_text.getText().toString();
               String lname = lastname_text.getText().toString();
               String license_num = licencenum_text.getText().toString();
               String state = licencestate_text.getText().toString();
               String make = make_text.getText().toString();
               String model = model_text.getText().toString();
               String year = year_text.getText().toString();
               String color = color_text.getText().toString();
               String venmo = venmo_uname.getText().toString();


               try {
                   driver = getinfo_signup(username, password, fname, lname, license_num, state, make,
                           model, year, color, venmo, url);
               } catch (UnsupportedEncodingException e) {
                   e.printStackTrace();
               }

               //TODO: transition to the next page. send them the driver

           }
        });
    }




    public User getinfo_signup(String username, String password, String firstname, String lastname,
                               String licencenum, String licencestate, String make, String model,
                               String year, String color, String venmo_uname, String url_name)
            throws UnsupportedEncodingException {

        //create parameter sended to the server
        String data = URLEncoder.encode("username", "UTF-8") + "=" +
                URLEncoder.encode(username, "UTF-8");
        data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                URLEncoder.encode(password, "UTF-8");
        data += "&" + URLEncoder.encode("firstname", "UTF-8") + "=" +
                URLEncoder.encode(firstname, "UTF-8");
        data += "&" + URLEncoder.encode("lastname", "UTF-8") + "=" +
                URLEncoder.encode(lastname, "UTF-8");
        data += "&" + URLEncoder.encode("licencenum", "UTF-8") + "=" +
                URLEncoder.encode(licencenum, "UTF-8");
        data += "&" + URLEncoder.encode("licencestate", "UTF-8") + "=" +
                URLEncoder.encode(licencestate, "UTF-8");
        data += "&" + URLEncoder.encode("make", "UTF-8") + "=" +
                URLEncoder.encode(make, "UTF-8");
        data += "&" + URLEncoder.encode("model", "UTF-8") + "=" +
                URLEncoder.encode(model, "UTF-8");
        data += "&" + URLEncoder.encode("year", "UTF-8") + "=" +
                URLEncoder.encode(year, "UTF-8");
        data += "&" + URLEncoder.encode("color", "UTF-8") + "=" +
                URLEncoder.encode(color, "UTF-8");
        data += "&" + URLEncoder.encode("venmo_uname", "UTF-8") + "=" +
                URLEncoder.encode(venmo_uname, "UTF-8");

        String json_str = "";
        json_str = sendpost(url_name, data);

        if (json_str != "") {
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
                    String make1 = result_obj.getString("make");
                    String model1 = result_obj.getString("model");
                    Integer year1 = result_obj.getInt("year");
                    String color1 = result_obj.getString("color");
                    String email = result_obj.getString("email");
                    Integer standing_int = result_obj.getInt("good_standing");
                    Boolean standing = false;

                    if (standing_int == 1) {
                        standing = true;
                    }

                    //create the user
                    User user = new User(fName, lName, uName, plate, state, make1, model1, year1, color1, email, standing, password);
                    user.print();
                    return user;
                } else {//if the request for a salt was not successful
                    String message = obj.getString("message");
                    System.out.println(message);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }




    /**
     * Request to send a POST method to the specified URL we define
     * @param url Post Request URL
     * @param param Request parameter, request parameter should be name1=value1&name2=value2
     */

    public String sendpost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);

            // Open the connection to the URL
            URLConnection conn = realUrl.openConnection();

            // Set general request properties
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // To send a POST request, must set the following two lines.
            conn.setDoOutput(true);
            conn.setDoInput(true);

            //OutputStreamWriter dos = new OutputStreamWriter(conn.getOutputStream());
            //write data to the server
            //dos.write(param);
            //dos.flush();

            out = new PrintWriter(conn.getOutputStream());

            //send post parameter
            out.print(param);
            out.flush();

            // Define the BufferedReader input stream to read the response of the URL
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("An exception occurred while sending a POST request"+e);
            e.printStackTrace();
        }

        //using finally to Close output stream, input stream
        finally{
            try{
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        //System.out.println("post result："+result);
        return result;
    }


}

