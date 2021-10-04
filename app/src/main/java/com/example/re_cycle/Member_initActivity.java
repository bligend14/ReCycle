package com.example.re_cycle;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


public class Member_initActivity extends AppCompatActivity
{
    private static final String TAG = "MemberinitActivity";
    private ImageView porfile_imageview;
    private String profilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_init);

        porfile_imageview = findViewById(R.id.cameraImageView);
        porfile_imageview.setOnClickListener(onClickListener);//사진 이미지뷰 id 찾아오기

        findViewById(R.id.confirmedButton).setOnClickListener(onClickListener);//확인 버튼 id 찾아오기
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }


    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode ,Intent data)
    {
        super.onActivityResult(requestCode,resultCode, data);
        switch (requestCode)
        {
            case 0 :
                {
                    if(resultCode == Activity.RESULT_OK)
                    {
                        profilePath = data.getStringExtra("profilePath");
                        Bitmap bmp = BitmapFactory.decodeFile(profilePath);

                        //이미지 회전 현상 방지
                        ExifInterface exif = null;
                        try {
                            exif = new ExifInterface(profilePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED);
                        Bitmap bmRotated = rotateBitmap(bmp, orientation);//이미지 회전

                        porfile_imageview.setImageBitmap(bmRotated);

                        Log.e("profilePath","profilePath:"+ profilePath);

                    }
                break;

            }

        }
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
                case R.id.cameraImageView:
                    GotoActivity(CameraActivity.class);
                    break;
            }
        }
    };

    private void  profileUpdate()
    {
        final String name = ((EditText) findViewById(R.id.NameEditText)).getText().toString();
        final String birthday = ((EditText) findViewById(R.id.BirthdayEditText)).getText().toString();
        final String phonenumber = ((EditText) findViewById(R.id.Phonnumber_EditTest)).getText().toString();

        if (name.length() > 0)
        {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();// Create a storage reference from our app

            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final StorageReference mountainImagesRef = storageRef.child("Users/"+user.getUid()+"/profileImage.jpg"); // Create a reference to 'images/mountains.jpg'

            try
            {
                InputStream stream = new FileInputStream(new File(profilePath));
                UploadTask uploadTask = mountainImagesRef.putStream(stream);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
                {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            Log.e("실패1","실패");
                            throw task.getException();
                        }
                        // Continue with the task to get the download URL
                        return mountainImagesRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            Uri downloadUri = task.getResult();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Memberinfo info = new Memberinfo(name, birthday, phonenumber,downloadUri.toString());
                            db.collection("users").document(user.getUid()).set(info)
                                    .addOnSuccessListener(new OnSuccessListener<Void>()
                                    {
                                        @Override
                                        public void onSuccess(Void aVoid)
                                        {
                                            toast("회원정보 등록을 성공하였습니다.");
                                            // GotoActivity(MainActivity.class);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener()
                                    {
                                        @Override
                                        public void onFailure(@NonNull Exception e)
                                        {
                                            toast("회원정보 등록에 실패하였습니다.");
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });
                        }
                        else
                        {
                            Log.e("실패2","실패");
                            // Handle failures
                            // ...
                        }
                    }
                });
            }
            catch (FileNotFoundException e)
            {
                Log.e("로그","에러:"+e.toString());
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

    private void  GotoActivity(Class I)
    {
        Intent intent = new Intent(this,I);
        startActivityForResult(intent,0);
    }
}
