package com.test.getfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {
    EditText email,pass;
    Button signin;
    TextView newusr;
    ProgressBar bar;
    FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        signin=findViewById(R.id.btn);
        bar=findViewById(R.id.bar2);
        newusr=findViewById(R.id.newusr);

        mAuth= FirebaseAuth.getInstance();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email1=email.getText().toString().trim();
                String pass1=pass.getText().toString().trim();


                if(TextUtils.isEmpty(email1)){
                    email.setError("Email is required !");
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email1).matches())
                {
                    email.setError("Please enter valid mail !");
                    return;
                }

                if(TextUtils.isEmpty(pass1)){
                    pass.setError("Password is required !");
                    return;
                }
                if(pass1.length()<8){
                    pass.setError("Password must be atleast 8 characters long !");
                    return;
                }

                bar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email1,pass1)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    finish();
                                    bar.setVisibility(View.GONE);
                                    startActivity(new Intent(MainActivity.this,home.class));
                                } else {
                                    Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    bar.setVisibility(View.GONE);
                                }

                            }
                        });




            }
        });
        newusr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,signup.class));
            }
        });
    }


}
