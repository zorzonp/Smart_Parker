package com.example.xcliang.singin;

import android.os.Parcel;
import android.os.Parcelable;


public class User implements Parcelable {
    //fields in a user
    String firstName;
    String lastName;
    String username;
    String license_plate;
    String license_state;
    String make;
    String model;
    String year;
    String color;
    String email;
    String good_standing;
    String password_hash;


    //This is a constructor for the class. It creates an instance of the class with provided data
    User(String firstName, String lastName, String username, String license_plate,
         String license_state, String make, String model, Integer year, String color, String email,
         Boolean good_standing, String password_hash){


        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.license_plate = license_plate;
        this.license_state = license_state;
        this.make = make;
        this.model = model;
        this.year = year.toString();
        this.color = color;
        this.email = email;
        this.good_standing = good_standing.toString();
        this.password_hash = password_hash;

    }

    //required by Parcel library, auto generated
    protected User(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        username = in.readString();
        license_plate = in.readString();
        license_state = in.readString();
        make = in.readString();
        model = in.readString();
        year = in.readString();
        color = in.readString();
        email = in.readString();
        good_standing = in.readString();
        password_hash = in.readString();
    }

    //required by Parcel library, auto generated
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    //Useful for debugging. Prints all the fields.
    public void print(){
        System.out.println("First Name: "+firstName);
        System.out.println("Last Name: "+lastName);
        System.out.println("Username: "+username);
        System.out.println("Plate: "+license_plate);
        System.out.println("State: "+license_state);
        System.out.println("Make: "+make);
        System.out.println("Model: "+model);
        System.out.println("Year: "+year);
        System.out.println("Color: "+color);
        System.out.println("Email: "+email);
        System.out.println("Good Standing: "+good_standing);
        System.out.println("Password: "+password_hash);
    }

    //required by Parcel library
    @Override
    public int describeContents(){
        return 0;
    }

    //required by Parcel library, sends the data
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(username);
        dest.writeString(license_plate);
        dest.writeString(license_state);
        dest.writeString(make);
        dest.writeString(model);
        dest.writeString(year);
        dest.writeString(color);
        dest.writeString(email);
        dest.writeString(good_standing.toString());
        dest.writeString(password_hash);
    }





}
