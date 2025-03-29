package com.example.project.LoginFunctionality;

import static com.google.firebase.database.DatabaseKt.database;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Register_Page extends AppCompatActivity
{
    EditText txt_email, txt_passwd, txt_passwd_confirm;
    Button btn_Register;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        FirebaseDatabase database = FirebaseDatabase.getInstance("");
        mAuth = FirebaseAuth.getInstance();

        txt_email = (EditText) findViewById(R.id.register_email);
        txt_passwd = (EditText) findViewById(R.id.register_password);
        txt_passwd_confirm = (EditText) findViewById(R.id.register_confirm_password) ;

        btn_Register = (Button) findViewById(R.id.btn_confirm_register);
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = txt_email.getText().toString();
                String password = String.valueOf(txt_passwd.getText().toString());
                String password_confirm = String.valueOf(txt_passwd_confirm.getText().toString());

                if (!password.equals(password_confirm))
                {
                   Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                }//if
                else
                {
                    createAccount(email,password);
                }//else
            }//onClick
        });//onClickListener
    }//onCreate

    private void createAccount(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            sendEmailVerification();
                            Toast.makeText (Register_Page.this, "success please sign in", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Register_Page.this, LogInPage.class);
                            startActivity(i);
                            finish();
                            // Sign in success, update UI with the signed-in user's information
                        } //if
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register_Page.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }//else
                    }//onComplete
                });//onCompleteListener
    }//createAccount
    private void sendEmailVerification()
    {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(Register_Page.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Register_Page.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                }
            }//onComplete
        });//onCompleteListener
    }//sendEmailVerification
}//class
