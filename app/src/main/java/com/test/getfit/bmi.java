package com.test.getfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class bmi extends AppCompatActivity {

    public static final String TAG = "TAG";
    Button cc;
    EditText bmi,ml;
    String userID;
    LinearLayout linearLayout;

    FirebaseAuth fAuth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        cc=findViewById(R.id.cc);
        bmi=findViewById(R.id.bmi);
        ml=findViewById(R.id.ml);
        linearLayout=findViewById(R.id.lay);
        fAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        userID=fAuth.getCurrentUser().getUid();


        DocumentReference df=fstore.collection("users").document(userID);
        df.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                float h=Integer.parseInt(documentSnapshot.getString("Height"));
                float w=Integer.parseInt(documentSnapshot.getString("Weight"));

                float c=h/100;
                float b=(w/(c*c));

                bmi.setText(String.valueOf(b));
                bmi.setFocusable(false);
                bmi.setCursorVisible(false);
                bmi.setBackgroundColor(Color.TRANSPARENT);

                ml.setFocusable(false);
                ml.setCursorVisible(false);
                ml.setBackgroundColor(Color.TRANSPARENT);

                if(b>=18.5 && b<=25)
                {
                    linearLayout.setBackgroundResource(R.color.normal);
                    ml.setText("Normal weight\nNo need to worry !");
                }
                else {
                    linearLayout.setBackgroundResource(R.color.danger);
                    if(b<=15)
                        ml.setText("Very severely underweight !\nYou need to see a doctor");
                    else if(b<=16 && b>15)
                        ml.setText("Severely underweight !\nYou need to see a doctor");
                    else if(b<18.5 && b>16)
                        ml.setText("Underweight !\nCheck out our diet plans");
                    else if(b<=30 && b>25)
                        ml.setText("Overweight !");
                    else if(b<=35 && b>30)
                        ml.setText("Obese class 1\nModerately obese !");
                    else if(b<=40 && b>35)
                        ml.setText("Obese class 2\nSeverely obese !");
                    else
                        ml.setText("Obese class 3\nVery severely obese !");
                }

                userID=fAuth.getCurrentUser().getUid();

                DocumentReference documentReference=fstore.collection("bmi").document(userID);
                Map<String,Object> user=new HashMap<>();
                user.put("Bmi",String.valueOf(b));

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: "+userID);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+e.toString());
                    }
                });
            }
        });


        cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float b1=Float.parseFloat(String.valueOf(bmi.getText()));
                if(b1<18.5) {
                    startActivity(new Intent(bmi.this, bmr2.class));
                }
                else if(b1>25){
                    startActivity(new Intent(bmi.this,bmr1.class));
                }
                else {
                    startActivity(new Intent(bmi.this, bmr.class));
                }
            }
        });
    }
}
