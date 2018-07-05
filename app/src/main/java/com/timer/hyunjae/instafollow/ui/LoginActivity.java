package com.timer.hyunjae.instafollow.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.steelkiwi.instagramhelper.InstagramHelper;
import com.steelkiwi.instagramhelper.InstagramHelperConstants;
import com.steelkiwi.instagramhelper.model.Data;
import com.steelkiwi.instagramhelper.model.InstagramUser;
import com.steelkiwi.instagramhelper.utils.SharedPrefUtils;
import com.timer.hyunjae.instafollow.BaseActivity;
import com.timer.hyunjae.instafollow.R;
import com.timer.hyunjae.instafollow.utils.SharedValue;

public class LoginActivity extends BaseActivity {
    String scope = "basic+public_content+follower_list+comments+relationships+likes";
    final String CLIENT_ID = "12a43299835f4217b20248245999508f";
    final String redirect = "http://sampleurl.com";
    InstagramHelper instagramHelper = new InstagramHelper.Builder().withClientId(CLIENT_ID).withRedirectUrl(redirect).withScope(scope).build();
    InstagramUser instagramUser = new InstagramUser();
    private Button LoginBtn;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_login);
        init_autoscreen(1);

        LoginBtn = (Button)findViewById(R.id.btnConnect);
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instagramHelper.loginFromActivity(LoginActivity.this);
                Toast.makeText(mContext, "인스타로그인", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        /*if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }else */
        if(requestCode == InstagramHelperConstants.INSTA_LOGIN && resultCode == RESULT_OK){
            InstagramUser user = instagramHelper.getInstagramUser(this);
            Data user2 = user.getData();

            Log.v("mylog","INS/userName: "+ user.getData().getUsername());
            Log.v("mylog","INS/getFullName: "+ user.getData().getFullName());
            Log.v("mylog","INS/getWebsite: "+ user.getData().getWebsite());
            Log.v("mylog","INS/getBio: "+ user.getData().getBio());
            Log.v("mylog","INS/getId: "+ user.getData().getId());
            Log.v("mylog","INS/getProfilePicture: "+ user.getData().getProfilePicture());
            Log.v("mylog","INS/getCounts: "+ user.getData().getCounts());
            Log.v("mylog","INS/getFollowedBy: "+ user2.getCounts().getFollowedBy());
            Log.v("mylog","INS/getFollows: "+ user2.getCounts().getFollows());
            Log.v("mylog","INS/getMedia: "+ user2.getCounts().getMedia());
            SharedValue.userName = user.getData().getUsername();
            SharedValue.getFullName = user.getData().getFullName();
            SharedValue.getWebsite = user.getData().getWebsite();
            SharedValue.getBio = user.getData().getBio();
            SharedValue.getId = user.getData().getId();
            SharedValue.getProfilePicture = user.getData().getProfilePicture();
            SharedValue.getCounts = String.valueOf(user.getData().getCounts());
            SharedValue.getFollowedBy = String.valueOf(user2.getCounts().getFollowedBy());
            SharedValue.getFollows = String.valueOf(user2.getCounts().getFollows());
            SharedValue.getMedia = String.valueOf(user2.getCounts().getMedia());

            MoveToActivity(new Intent(mContext,MainActivity.class));
            finish();


        }
        super.onActivityResult(requestCode,resultCode,data);
    }

}
