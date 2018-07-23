package com.ashutosh.ashuinsta;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ashutosh.ashuinsta.HomeActivity;
import com.ashutosh.ashuinsta.R;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ParseInstallation;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    Button login;
    EditText user,pass;
    String username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        user = (EditText)findViewById(R.id.userET);
        pass = (EditText)findViewById(R.id.passET);
        login = (Button)findViewById(R.id.loginBTN);

        username = user.getText().toString();
        password = pass.getText().toString();

        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser!=null)
        {
            //Alreday signed in,,, SHOW USER LISTS...
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "please Login.", Toast.LENGTH_SHORT).show();
        }

    }

    public void click(View view)
    {
        ParseUser.logInInBackground(user.getText().toString().trim(), pass.getText().toString().trim(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null || ParseUser.getCurrentUser() != null) {
                    alertDisplayer1("Sucessful Login", "Welcome back " + user.getText().toString().toUpperCase() + " !");

                } else {
                    alertSignup();
                }
            }
        });
    }

    private void alertDisplayer1(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setIcon(R.drawable.loginwelcome)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }


    private void alertSignup()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

        alertDialogBuilder.setTitle("SIGN UP");
        alertDialogBuilder.setMessage("User Doesn't Exist! Are You Sure To Signup With this Credential ? ");
        alertDialogBuilder.setIcon(R.drawable.signup1);

        alertDialogBuilder.setPositiveButton("YES,Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ParseUser userNew = new ParseUser();  // Set the user's username and password, which can be obtained by a forms
                userNew.setUsername(username);
                userNew.setPassword(password);
                userNew.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null)
                        {
                            Toast.makeText(MainActivity.this, "OTP VERIFIED SUCCESSFULLY...", Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Successful Sign Up!")
                                    .setIcon(R.drawable.signupwelcome)
                                    .setMessage("Welcome  " + username.toUpperCase() + "!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            // don't forget to change the line below with the names of your Activities
                                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    });
                            AlertDialog okay = builder.create();
                            okay.show();

                        } else{
                            ParseUser.logOut();
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                // saving data to Users class...
                ParseObject obj = new ParseObject("Users");
                obj.put("username",username);
                obj.put("following","");
                ParseACL acl = new ParseACL();
                acl.setPublicReadAccess(true);
                acl.setPublicWriteAccess(true);
                obj.setACL(acl);
                obj.saveInBackground(); // No exception Handled.
            }
        });

        alertDialogBuilder.setNegativeButton("NO, Don't SignUp", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(MainActivity.this, "Staying On this Page.... ", Toast.LENGTH_SHORT).show();
                dialog.cancel();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
