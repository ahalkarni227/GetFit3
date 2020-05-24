package com.test.getfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class home extends AppCompatActivity {
    private static final String TAG = "home";
    EditText fname,mobno,age,weight,height,em,pass;
    RadioGroup grp;
    RadioButton male,female,bt;
    TextView cm,kg,gender;
    Button submit;
    int a1,w1,h1;
    FirebaseFirestore fstore;
    FirebaseAuth mAuth;
    String userID;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fname=findViewById(R.id.fullname);
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
        grp=findViewById(R.id.grp);
        mobno=findViewById(R.id.mobno);
        age=findViewById(R.id.age);
        weight=findViewById(R.id.weight);
        height=findViewById(R.id.height);

        submit=findViewById(R.id.submit);
        cm=findViewById(R.id.cm);
        kg=findViewById(R.id.kg);

        pb=findViewById(R.id.pb);

        mAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name= fname.getText().toString();
                String mob=mobno.getText().toString();
                String a=age.getText().toString().trim();
                String h=height.getText().toString().trim();
                String w=weight.getText().toString().trim();

                if(TextUtils.isEmpty(name))
                {
                    fname.setError("Enter this field !");
                    return;
                }

                int selectedId = grp.getCheckedRadioButtonId();
                bt = (RadioButton) findViewById(selectedId);
                if(selectedId==-1)
                {
                    Toast.makeText(home.this,"Please select gender !",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(mob))
                {
                    mobno.setError("Enter this field !");
                    return;
                }
                if(!mob.matches("[5-9]{1}[0-9]{9}"))
                {
                    mobno.setError("Please enter a valid 10 digit mobile number !");
                    return;
                }

                if(TextUtils.isEmpty(a))
                {
                    age.setError("Enter this field !");
                    return;
                }
                else {
                    a1 = Integer.parseInt(a);
                }
                if(a1<13)
                {
                    age.setError("Age must be aleast 13 !");
                    return;
                }
                else if(a1>99)
                {
                    age.setError("Invalid age !");
                    return;
                }

                if(TextUtils.isEmpty(h))
                {
                    height.setError("Enter this field !");
                    return;
                }
                else
                {
                    h1=Integer.parseInt(h);
                }
                if(h1<=50 || h1>=270)
                {
                    height.setError("Please provide correct height !");
                    return;
                }

                if(TextUtils.isEmpty(w))
                {
                    weight.setError("Enter this field !");
                    return;
                }
                else
                {
                    w1=Integer.parseInt(w);
                }
                if(w1<30 || w1>300)
                {
                    weight.setError("Please provide correct weight !");
                    return;
                }

                pb.setVisibility(View.VISIBLE);

                userID=mAuth.getCurrentUser().getUid();

                DocumentReference documentReference=fstore.collection("users").document(userID);
                Map<String,Object> user=new HashMap<>();
                user.put("Full name",name);
                user.put("Gender",bt.getText());
                user.put("Mobile",mob);
                user.put("Age",a);
                user.put("Height",h);
                user.put("Weight",w);

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pb.setVisibility(View.GONE);
                        startActivity(new Intent(home.this,bmi.class));
                        //finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+e.toString());
                    }
                });

            }
        });


    }

}

