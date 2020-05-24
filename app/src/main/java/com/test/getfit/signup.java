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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

public class signup extends AppCompatActivity {
    EditText emailid,passwrd,passwrd2;
    Button btn2;
    FirebaseAuth fAuth;
    FirebaseFirestore store;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailid=findViewById(R.id.emailid);
        passwrd=findViewById(R.id.passwrd);
        passwrd2=findViewById(R.id.passwrd2);
        btn2=findViewById(R.id.btn2);
        bar=findViewById(R.id.bar);

        fAuth=FirebaseAuth.getInstance();
        store=FirebaseFirestore.getInstance();


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em=emailid.getText().toString().trim();
                String pass1=passwrd.getText().toString().trim();
                String pass2=passwrd2.getText().toString().trim();

                if(TextUtils.isEmpty(em)){
                    emailid.setError("This field cannot be empty !");
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(em).matches())
                {
                    emailid.setError("Please enter valid mail !");
                    return;
                }

                if(TextUtils.isEmpty(pass1)){
                    passwrd.setError("This field cannot be empty !");
                    return;
                }
                if(TextUtils.isEmpty(pass2)){
                    passwrd2.setError("This field cannot be empty !");
                    return;
                }
                if(pass1.equals(pass2)){
                    if(pass1.length()<8){
                        Toast.makeText(signup.this,"Password must be atleast 8 characters long",Toast.LENGTH_SHORT).show();
                    }
                    bar.setVisibility(View.VISIBLE);
                    fAuth.createUserWithEmailAndPassword(em, pass1)
                            .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful())
                                    {
                                        bar.setVisibility(View.GONE);

                                        Toast.makeText(signup.this,"Registration success !",Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(signup.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                    else {
                                        bar.setVisibility(View.GONE);

                                        if(task.getException() instanceof FirebaseAuthUserCollisionException)
                                        {
                                            Toast.makeText(getApplicationContext(),"You are already registered",Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(signup.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    }

                                }
                            });
                }
                else
                {
                    Toast.makeText(signup.this,"Passwords do not match !",Toast.LENGTH_SHORT).show();
                    passwrd.setError("Password mismatch !");
                    passwrd2.setError("Password mismatch !");
                }
            }
        });

    }
}
