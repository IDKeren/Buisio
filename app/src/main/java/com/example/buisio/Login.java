package com.example.buisio;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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


public class Login extends AppCompatActivity {
    TextInputEditText textEmail, textPassword;
    Button login,registerNow;
    private CheckBox loginCB;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mAuth = FirebaseAuth.getInstance();
        findViews();
        rememberMe();

    registerNow.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(),Register.class);
            startActivity(intent);
            finish();
        }
    });

    login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            progressBar.setVisibility(View.VISIBLE);
            String email,password;
            email = String.valueOf(textEmail.getText());
            password = String.valueOf(textPassword.getText());

            if(TextUtils.isEmpty(email)){
                Toast.makeText(Login.this,"Enter eMail",Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(Login.this,"Enter Password",Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(Login.this,"Login Successful",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(Login.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }
    });

    }

    private void rememberMe() {
        loginCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && currentUser != null && currentUser.getEmail() != null) {
                    textEmail.setText(currentUser.getEmail());
                } else {
                    textEmail.setText(""); // Clear the email field if not checked
                }
            }
        });
    }

    private void findViews(){
        login = findViewById(R.id.buttonLogin);
        registerNow = findViewById(R.id.registerNow);
        textEmail = findViewById(R.id.LoginUsernameOrEmail);
        textPassword = findViewById(R.id.loginPassword);
        progressBar = findViewById(R.id.progressBar);
        loginCB = findViewById(R.id.loginCheckBox);
    }



}
