package com.capstone.bookkeepingproto2.LoginTest;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.bookkeepingproto2.GroupHouseKeeping.GroupArticleActivity;
import com.capstone.bookkeepingproto2.HttpClient.HttpClient;
import com.capstone.bookkeepingproto2.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by New on 2015-05-22.
 */
public class FixInfoActivity extends Activity implements View.OnClickListener{
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fixinfo);

        final TextView email_edt = (TextView)findViewById(R.id.fixinfo_email_et);
        final TextView name_edt = (TextView)findViewById(R.id.fixinfo_name_et);
        final EditText phone_edt = (EditText)findViewById(R.id.fixinfo_phone_et);
        final ImageView img = (ImageView)findViewById(R.id.profile_iv);

        Button fix = (Button)findViewById(R.id.fix_btn);
        fix.setOnClickListener(this);
        Button chg_img = (Button)findViewById(R.id.chg_img_btn);
        chg_img.setOnClickListener(this);
        RequestParams param = new RequestParams();

        SharedPreferences mPreference = getSharedPreferences("myInfo", MODE_PRIVATE);
        final String email = mPreference.getString("email", "");
        param.put("email", email);
        HttpClient.post("getMember/", param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject obj = new JSONObject(new String(responseBody));
                    password = obj.get("pw").toString();
                    email_edt.setText(obj.get("email").toString());
                    name_edt.setText(obj.get("name").toString());
                    phone_edt.setText(obj.get("phone").toString());
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(error);
            }
        });


        getMemberProfileImg(email, img);
    }

    ///////////////////////////////////////////////////////////
    public void getMemberProfileImg(final String email, final ImageView img){
        // Get Member Profile Img
        // If Internal Storage has user's Img, get this one
        // else get from server.
        final String filename = email+"_profile.jpg";

        try {
            FileInputStream fis = openFileInput(filename);
            Bitmap scaled = BitmapFactory.decodeStream(fis);
            fis.close();

            //string temp contains all the data of the file.
            System.out.println("Error here?");
            img.setImageBitmap(scaled);
        }catch( FileNotFoundException e){
            RequestParams param = new RequestParams();
            param.put("email", email);
            System.out.println("Error? : " + e);
            HttpClient.post("getMemberImg/", param, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (new String(responseBody).equals("No Image")) {
                        img.setImageResource(R.drawable.default_person);
                    } else {
                        Bitmap d = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);

                        int nh = (int) (d.getHeight() * (512.0 / d.getWidth()));
                        Bitmap scaled = Bitmap.createScaledBitmap(d, 512, nh, true);
                        img.setImageBitmap(scaled);

                        FileOutputStream outputStream;
                        try {
                            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                            scaled.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                            outputStream.close();
                        } catch (Exception err) {
                            err.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    System.out.println("error message 1 : " + error);
                }
            });
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            System.out.println("Test here");
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.profile_iv);
                // Set the Image in ImageView after decoding the String

                Bitmap d = BitmapFactory.decodeFile(imgDecodableString);

                int nh = (int) ( d.getHeight() * (512.0 / d.getWidth()) );
                Bitmap scaled = Bitmap.createScaledBitmap(d, 512, nh, true);
                imgView.setImageBitmap(scaled);

                //imgView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                System.out.println(":::::" + imgDecodableString);

                File file = new File(imgDecodableString);
                System.out.println("file : " + file);
                SharedPreferences mPreference = getSharedPreferences("myInfo", MODE_PRIVATE);
                String email = mPreference.getString("email", "");

                FileOutputStream outputStream;
                try {
                    String filename = email+"_profile.jpg";
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    scaled.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                RequestParams param = new RequestParams();
                param.put("email", email);
                param.put("image", file, "image/jpg");
                System.out.println("Param : " + param);
                HttpClient.post("uploadImg_Profile/", param, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        System.out.println("img Save Success");
                        System.out.println("output : " + new String(responseBody));
                        switch ( new String(responseBody) ) {
                            case "1":
                                System.out.println("img save error");
                                break;

                            case "Saved":
                                System.out.println("img save Success");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        System.out.println("error message : " + error);
                    }
                });

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onClick(View v) {
        switch( v.getId() ){
            case R.id.chg_img_btn:
                loadImagefromGallery(v);
                break;
            case R.id.fix_btn:
                TextView email_edt = (TextView)findViewById(R.id.fixinfo_email_et);
                EditText pw0_edt = (EditText)findViewById(R.id.fixinfo_pw0_et);
                EditText pw1_edt = (EditText)findViewById(R.id.fixinfo_pw1_et);
                EditText pw2_edt = (EditText)findViewById(R.id.fixinfo_pw2_et);
                EditText phone_edt = (EditText)findViewById(R.id.fixinfo_phone_et);
                if ( !password.equals(pw0_edt.getText().toString())){
                    Toast toastView = Toast.makeText(getApplicationContext(),
                            "Original password wrong", Toast.LENGTH_LONG);
                    toastView.setGravity(Gravity.CENTER, 40, 25);
                    toastView.show();
                }
                else if( !pw2_edt.getText().toString().equals(pw1_edt.getText().toString()) ){
                    Toast toastView = Toast.makeText(getApplicationContext(),
                            "Passwords are different.", Toast.LENGTH_LONG);
                    toastView.setGravity(Gravity.CENTER, 40, 25);
                    toastView.show();
                }
                else{
                    RequestParams param = new RequestParams();
                    param.put("email", email_edt.getText().toString());
                    param.put("pw", pw1_edt.getText().toString());
                    param.put("phone", phone_edt.getText().toString());
                    HttpClient.post("fixInfo/", param, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            System.out.println("Test :::: " + responseBody);
                            if (new String(responseBody).equals("fix info complete")) {
                                startActivity(new Intent(getApplicationContext(), SelectActivity.class));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            System.out.println("error tt : " + error);
                        }
                    });
                }

                break;
        }
    }
}