package com.example.project.LoginFunctionality;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordPage extends AppCompatActivity
{
    EditText user_email;
    Button send_link;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_page);

        user_email = (EditText) findViewById(R.id.Email);


        send_link = (Button) findViewById(R.id.send_link_button);
        send_link.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendPasswordReset(user_email.getText().toString());
            }//onClick
        });//setOnClickListener
    }//onCreate
    public void sendPasswordReset(String email)
    {
        //Get firebase instance
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //send reset link to the email provided
        //todo - check the email against the database and only send if it exists

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(ForgotPasswordPage.this, "Email has been sent", Toast.LENGTH_SHORT).show();

                    //direct user back to the login page
                    Intent i = new Intent(ForgotPasswordPage.this, LogInPage.class);
                    startActivity(i);
                    finish();
                }//if
            }//onComplete
        });//addOnCompleteListener
    }//sendPasswordReset
}