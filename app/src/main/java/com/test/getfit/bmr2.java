package com.test.getfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

public class bmr2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText txt,txt2;
    float r,cal;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    Spinner s,s2;
    Button calculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmr2);

        txt=findViewById(R.id.txt);
        txt2=findViewById(R.id.txt2);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        userID=fAuth.getCurrentUser().getUid();

        txt.setFocusable(false);
        txt.setCursorVisible(false);
        txt.setBackgroundColor(Color.TRANSPARENT);

        s=findViewById(R.id.s);
        s.setOnItemSelectedListener(this);
        s2=findViewById(R.id.s2);
        s2.setOnItemSelectedListener(this);

        calculate=findViewById(R.id.calculate);

        DocumentReference documentReference=fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(bmr2.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String sex=documentSnapshot.getString("Gender");
                int w=Integer.parseInt(documentSnapshot.getString("Weight"));
                int h=Integer.parseInt(documentSnapshot.getString("Height"));
                int a=Integer.parseInt(documentSnapshot.getString("Age"));
                if(sex.matches("Male"))
                {
                    r = (float)  ((10*w) + (6.25*h) - (5*a) + 5);
                    txt2.setText(String.valueOf(r));
                }
                else {
                    r = (float)  ((10*w) + (6.25*h) - (5*a) - 161);
                    txt2.setText(String.valueOf(r));
                }
                txt2.setFocusable(false);
                txt2.setCursorVisible(false);
                txt2.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference df=fStore.collection("bmr").document(userID);
                Map<String,Object> user=new HashMap<>();
                user.put("Bmr",txt2.getText().toString());

                df.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "onSuccess: "+userID);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "onFailure: "+e.toString());
                    }
                });

                int position=s.getSelectedItemPosition();
                float t=Float.parseFloat(txt2.getText().toString());

                if(position==0) {
                    cal= (float) (t*1.2);
                }
                else if(position==1) {
                    cal= (float) (t*1.375);
                }
                else if(position==3) {
                    cal= (float) (t*1.55);
                }
                else
                {
                    cal= (float) (t*1.725);
                }

                int pos=s2.getSelectedItemPosition();

                if(pos==0){
                    cal=cal+500;
                }
                else {
                    cal=cal+1000;
                }

                DocumentReference documentReference=fStore.collection("calculated").document(userID);
                Map<String,Object> usr=new HashMap<>();
                usr.put("Calories",String.valueOf(cal));

                documentReference.set(usr).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "onFailure: "+e.toString());
                    }
                });

                startActivity(new Intent(getApplicationContext(),calorie.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
