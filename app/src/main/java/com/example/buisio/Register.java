package com.example.buisio;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    TextInputEditText editTextEmail,editTextPassword,editTextPhone;
    Button buttonReg,buttonLoginNow;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        mAuth = FirebaseAuth.getInstance();
        findViews();
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email,password,phone;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                phone = String.valueOf(editTextPhone.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this,"Enter eMail",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this,"Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(Register.this,"Enter Phone Number",Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this,"Account Created",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });
        buttonLoginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findViews(){
        buttonReg = findViewById(R.id.buttonReg);
        buttonLoginNow = findViewById(R.id.loginNow);
        editTextEmail = findViewById(R.id.registerUsernameOrEmail);
        editTextPassword = findViewById(R.id.registerTextPassword);
        editTextPhone = findViewById(R.id.registerPhoneNumber);
        progressBar = findViewById(R.id.progressBar);
    }



}
