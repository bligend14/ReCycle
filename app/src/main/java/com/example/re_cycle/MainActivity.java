package com.example.re_cycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null)
        {
           GotoActivity(loginActivity.class);
        }
        else
        {
            GotoActivity(Member_initActivity.class);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("Users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task)
                {
                    if (task.isSuccessful())
                    {
                        DocumentSnapshot document = task.getResult();
                        if (document != null)
                        {
                            if (document.exists())
                            {
                                Log.e(TAG, "DocumentSnapshot data: " + document.getData());
                            }
                            else
                            {
                                Log.e(TAG, "No such document");
                                GotoActivity(Member_initActivity.class);
                            }
                        }
                    }
                    else
                    {
                        Log.e(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }

        findViewById(R.id.logoutButton).setOnClickListener(onClickListener);
        findViewById(R.id.PhotoButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.logoutButton:
                    FirebaseAuth.getInstance().signOut();
                    GotoActivity(loginActivity.class);
                    break;
                case  R.id.PhotoButton:
                    GotoActivity(CameraActivity.class);
            }
        }
    };


    private void  GotoActivity(Class I)
    {
        Intent intent = new Intent(this,I);
        startActivity(intent);
    }
}