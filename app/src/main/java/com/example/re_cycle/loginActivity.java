package com.example.re_cycle;


import android.content.Intent;
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


public class loginActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.loginButton).setOnClickListener(onClickListener);//로그인 버튼 id 찾아오기
        findViewById(R.id.GotoSignUpText).setOnClickListener(onClickListener);//회원가입으로 가는 텍스트 id 찾아오기
        findViewById(R.id.PasswordResetText).setOnClickListener(onClickListener);//비밀번호 찾기 텍스트 id 찾아오기
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    View.OnClickListener onClickListener = new View.OnClickListener()//버튼ui 리스너 함수
    {
        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.loginButton:
                    login();
                    break;
                case R.id.GotoSignUpText:
                    GotoActivity(SignUpActivity.class);
                    break;
                case R.id.PasswordResetText:
                    GotoActivity(Password_ResetActivity.class);

            }
        }
    };

    private void  login()
    {
        String email = ((EditText) findViewById(R.id.EmailEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();

        if (email.length() > 0 && password.length() > 0)
        {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) //성공 했을 떄의 ui로직
                            {
                                toast("로그인에 성공했습니다");
                                FirebaseUser user = mAuth.getCurrentUser();
                                GotoActivity(MainActivity.class);

                            }
                            else//실패 했을 때의 ui로직
                            {
                                if (task.getException() != null)
                                {
                                    toast(task.getException().toString());
                                }
                                else
                                {
                                    toast("비밀번호가 일치하지 않습니다");
                                }
                            }
                        }
                    });
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

    private void  GotoActivity(Class I)
    {
        Intent intent = new Intent(this,I);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
