package com.timer.hyunjae.instafollow.ui;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.timer.hyunjae.instafollow.BaseActivity;
import com.timer.hyunjae.instafollow.R;
import com.timer.hyunjae.instafollow.common.log;
import com.timer.hyunjae.instafollow.ui.InstagramApp;
import com.timer.hyunjae.instafollow.utils.ApplicationData;
import com.timer.hyunjae.instafollow.utils.SharedValue;

public class MainActivity extends BaseActivity {

    private ImageView Profill_IV;
    private TextView Follow_Tv,Follower_Tv,ID_Tv,Media_Tv;
    private Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_autoscreen(1);

        setWidgetReference();
    }

    private void bindEventHandlers() {
//        btnViewInfo.setOnClickListener(this);
//        btnGetAllImages.setOnClickListener(this);
//        btnFollwing.setOnClickListener(this);
//        btnFollowers.setOnClickListener(this);
    }

    private void setWidgetReference() {
        Profill_IV = (ImageView)findViewById(R.id.instagram_profill);
        Follow_Tv = (TextView)findViewById(R.id.instagram_follow);
        Follower_Tv = (TextView)findViewById(R.id.instagram_follower);
        ID_Tv = (TextView)findViewById(R.id.instagram_id);
        Media_Tv = (TextView)findViewById(R.id.instagram_file);

        Thread mThread = new Thread(){
            @Override
            public void run(){
                try
                {
                    URL url = new URL(SharedValue.getProfilePicture);

                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                } catch (Exception e) {
                    log.vlog(2,"프로필 에러 " + e);
                }
            }
        };

        mThread.start();

        try{
            mThread.join();
            Profill_IV.setImageBitmap(bitmap);
        }catch (InterruptedException e){
            log.vlog(2,"프로필에러" + e);
        }
        Follow_Tv.setText(SharedValue.getFollowedBy + "\n팔로우");
        Follower_Tv.setText(SharedValue.getFollows + "\n팔로워");
        ID_Tv.setText(SharedValue.userName);
        Media_Tv.setText(SharedValue.getMedia + "\n게시물");

    }

    private void displayInfoDialogView() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);
        alertDialog.setTitle("Profile Info");

//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.profile_view, null);
//        alertDialog.setView(view);
//        ImageView ivProfile = (ImageView) view
//                .findViewById(R.id.ivProfileImage);
//        TextView tvName = (TextView) view.findViewById(R.id.tvUserName);
//        TextView tvNoOfFollwers = (TextView) view
//                .findViewById(R.id.tvNoOfFollowers);
//        TextView tvNoOfFollowing = (TextView) view
//                .findViewById(R.id.tvNoOfFollowing);
//        new ImageLoader(MainActivity.this).DisplayImage(
//                userInfoHashmap.get(InstagramApp.TAG_PROFILE_PICTURE),
//                ivProfile);
//        tvName.setText(userInfoHashmap.get(InstagramApp.TAG_USERNAME));
//        tvNoOfFollowing.setText(userInfoHashmap.get(InstagramApp.TAG_FOLLOWS));
//        tvNoOfFollwers.setText(userInfoHashmap
//                .get(InstagramApp.TAG_FOLLOWED_BY));
        alertDialog.create().show();
    }
}