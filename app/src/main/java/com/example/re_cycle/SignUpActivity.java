package com.example.re_cycle;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUpActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);//버튼ui id 찾아오기
    }


    @Override
    public void onStart()
    {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


    View.OnClickListener onClickListener = new View.OnClickListener()//버튼ui 리스너 함수
    {
        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.signUpButton:
                    signUp();
                    break;

            }
        }
    };

    private void  signUp()
    {
        String email = ((EditText) findViewById(R.id.EmailEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
        String Checkpassword = ((EditText) findViewById(R.id.CheckPasswordEditText)).getText().toString();

        if (email.length() > 0 && password.length() > 0 && Checkpassword.length() > 0)
        {
            if (password.equals(Checkpassword))
            {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful())//성공 했을 떄의 ui로직
                                {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    toast("회원가입에 성공했습니다");
                                } else//실패 했을 때의 ui로직
                                {
                                    if (task.getException() != null)
                                    {
                                        toast(task.getException().toString());
                                    }
                                }
                            }
                        });
            }
            else
            {
                toast("비밀번호가 일치하지 않습니다");
            }
        }
        else
        {
            toast("이메일 혹은 비밀번호를 입력해 주세요");
        }
    }

    private void toast(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
