package com.timer.hyunjae.instafollow.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.timer.hyunjae.instafollow.BaseActivity;
import com.timer.hyunjae.instafollow.R;

public class IntroActivity extends BaseActivity {

    private Thread thread;

    Handler handler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            // 4초뒤에 다음화면(MainActivity)으로 넘어가기 Handler 사용
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            MoveToActivity(intent); // 다음화면으로 넘어가기
            finish(); // Activity 화면 제거
        }
    };

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_intro);
        init_autoscreen(1);

//        thread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2000);
//
//                } catch (InterruptedException ex) {
//
//                }
//
//            }
//        };
//        thread.start();
//        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
//        MoveToActivity(intent);
//        finish();
    }
    @Override public void onBackPressed() {

    }
    @Override
    public void onResume() {
        super.onResume();
        // 다시 화면에 들어어왔을 때 예약 걸어주기
        handler.postDelayed(r, 2000); // 4초 뒤에 Runnable 객체 수행
    }

    @Override
    public void onPause() {
        super.onPause();
        // 화면을 벗어나면, handler 에 예약해놓은 작업을 취소하자
        handler.removeCallbacks(r); // 예약 취소
    }
}
