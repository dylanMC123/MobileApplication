package com.example.project.LoginFunctionality;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.project.MainActivity;
import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerificationPage extends AppCompatActivity
{
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        //get firebase instance and declare a handler object
        mAuth = FirebaseAuth.getInstance();
        Handler handler = new Handler();

        //get current user
        //this will be the account that has just been created, if it is null then user account creation was not successful.
        final FirebaseUser user = mAuth.getCurrentUser();


        //send verification email to email provided
        //this will wait until the code is sent and refresh every 10 seconds
        //todo - Check if user can sit on the verification page if email is not sent
        //todo - Resend verification link
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(EmailVerificationPage.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();

                    handler.postDelayed((Runnable) user.reload(),10000);

                    //if it is verified then move to the main page
                    if (user.isEmailVerified())
                    {
                        Intent intent = new Intent(EmailVerificationPage.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }//if
                }//if
                else
                {
                    Toast.makeText(EmailVerificationPage.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                }//else
            }//onComplete
        });//OnCompleteListener
    }//onCreate
}//EmailVerificationPage