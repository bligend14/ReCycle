package com.example.re_cycle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SubActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        findViewById(R.id.Sub_btn2).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SubActivity.this,MainActivity.class);
                intent.putExtra("키","위");
                startActivity(intent);
            }
        });
    }

    public void BtnClick1(View v)
    {
        Toast.makeText(this, "ㅋㅋ", Toast.LENGTH_LONG).show();
    }
}
