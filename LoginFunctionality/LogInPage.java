package com.example.project.LoginFunctionality;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.MainActivity;
import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LogInPage extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    TextView forgotPassword;
    EditText txt_email, txt_passwd;
    Button btn_login, btn_Register_page;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null)
        {
            Intent i = new Intent(LogInPage.this, MainActivity.class);
            startActivity(i);
            finish();
        }



        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mobileapp-7f8b1-default-rtdb.europe-west1.firebasedatabase.app/");


        txt_email = (EditText) findViewById(R.id.user_email_verification);
        txt_passwd = (EditText) findViewById(R.id.user_password_verification);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);

        btn_Register_page = (Button) findViewById(R.id.btn_register);
        btn_Register_page.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(LogInPage.this, Register_Page.class);
                startActivity(i);
                finish();
            }//onClick
        });//onClickListener

        btn_login =(Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = txt_email.getText().toString();
                String password = txt_passwd.getText().toString();
                if (email.equals("") || password.equals(""))
                {
                    Toast.makeText(LogInPage.this, "Fields Empty", Toast.LENGTH_SHORT).show();
                }else
                {
                    signIn(email, password);
                }
            }//onClick
        });//onClickListener

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(LogInPage.this, ForgotPasswordPage.class);
                startActivity(i);
                finish();
            }//onClick
        });//setOnClickListener
    }//onCreate

    private void signIn(String email, String password)
    {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                final FirebaseUser user = mAuth.getCurrentUser();

                if (task.isSuccessful()) {

                    if (user.isEmailVerified()) {
                        Toast.makeText(LogInPage.this, "Sign in Successful", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LogInPage.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }//if
                    else {
                        Toast.makeText(LogInPage.this, "Please verify email", Toast.LENGTH_SHORT).show();
                    }//else
                }//if
                else
                {
                    Toast.makeText(LogInPage.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }//else
            }//onComplete
        });//onCompleteListener
    }//signIn
}//class