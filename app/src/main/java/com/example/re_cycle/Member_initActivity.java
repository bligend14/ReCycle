package com.example.re_cycle;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;


public class Member_initActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private static final String TAG = "MemberinitActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_init);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.confirmedButton).setOnClickListener(onClickListener);//확인 버튼 id 찾아오기
    }


    View.OnClickListener onClickListener = new View.OnClickListener()//버튼ui 리스너 함수
    {
        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.confirmedButton:
                    profileUpdate();
                    break;
            }
        }
    };

    private void  profileUpdate()
    {
        String name = ((EditText) findViewById(R.id.NameEditText)).getText().toString();
        String birthday = ((EditText) findViewById(R.id.BirthdayEditText)).getText().toString();
        String phonenumber = ((EditText) findViewById(R.id.Phonnumber_EditTest)).getText().toString();

        if (name.length() > 0)
        {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Memberinfo info = new Memberinfo(name, birthday, phonenumber);

            if (user != null)
            {
                db.collection("Users").document(user.getUid()).set(info)
                        .addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void aVoid) //회원정보 등록 성공
                            {
                                toast("회원정보 등록 성공");
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() //회원 정보 등록 실패
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                toast("회원정보 등록 실패");
                            }
                        });
            }
        }
        else
        {
            toast("필수정보를 입력해 주세요");
        }
    }

    private void toast(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed()
    {
    }
}
