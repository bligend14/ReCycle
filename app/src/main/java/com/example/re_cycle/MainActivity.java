package com.example.re_cycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends AppCompatActivity
{

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
            for (UserInfo profile : user.getProviderData())
            {
                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                Log.e("이름","이름:"+ name);
                if (name != null)
                {
                    if (name.length() == 0)
                    {
                        GotoActivity(MemberinitActivity.class);
                    }
                }
                else if (name == null)
                {
                    GotoActivity(MemberinitActivity.class);
                }
            }
        }

        findViewById(R.id.logoutButton).setOnClickListener(onClickListener);
        ;
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
            }
        }
    };


    private void  GotoActivity(Class I)
    {
        Intent intent = new Intent(this,I);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}