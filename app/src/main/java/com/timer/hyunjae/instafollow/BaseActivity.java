package com.timer.hyunjae.instafollow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.timer.hyunjae.instafollow.common.log;
import com.timer.hyunjae.instafollow.config.Config;
import com.timer.hyunjae.instafollow.popup.Popup;
import com.timer.hyunjae.instafollow.ui.MainActivity;
import com.timer.hyunjae.instafollow.utils.AutoScreen;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

//import android.graphics.Typeface;

public class BaseActivity extends AppCompatActivity
{
    public Context mContext = null;
    private Timer timer = null;
    private Timer TimeOut_timer = null;
    private final android.os.Handler handler = new android.os.Handler();

    static public long TimeOut_StartTime = 0;
    static public long TimeOut_CurrentTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            setStatusBarBackground();

            Config.DEVICE_SCREEN_HEIGHT = displayMetrics.heightPixels;
            Config.DEVICE_SCREEN_WIDTH = displayMetrics.widthPixels;

            Config.InitConfig();
            super.onCreate(savedInstanceState);
            mContext = this;
            Config.TAG = this.getLocalClassName();
            Config.actList.add(this);
//            AppPosPreferenceManager.getFcmToken(mContext);

            ActionBar ab = getSupportActionBar();
        }
        catch (Exception e)
        {
            log.vlog(2,"BaseActivity onCreate Error : " + e.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onResume()
    {
        try
        {
            // 타이머 다시시작
            super.onResume();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        catch (Exception e)
        {
            log.vlog(2,"onResume onCreate Error : " + e.getMessage());
        }
    }

    public void init_autoscreen(int option)
    {
        try
        {
            AutoScreen.Adjust(mContext, getWindow().getDecorView(), option);
        }
        catch (Exception e)
        {
            log.vlog(2, "BaseActivity init_autoscreen Error : " + e.getMessage());
        }
    }

    public void MovetoMain()
    {
        ClearTop();
        Intent intent = new Intent(mContext, MainActivity.class);
        MoveToActivity(intent);
        finish();
    }

    public void BacktoMain()
    {
        ClearTop();
        Intent intent = new Intent(mContext, MainActivity.class);
        BackToActivity(intent);
        finish();
    }

    public void ClearTop()
    {
        for(int i = 0; i < Config.actList.size(); i++)
        {
            Config.actList.get(i).finish();
        }
    }

    public void MoveToMain(Intent intent)
    {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_center_from_right, R.anim.slide_left_from_center);
    }

    public void MoveToActivity(Intent intent)
    {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_center_from_right, R.anim.slide_left_from_center);
    }

    public void BackToActivity(Intent intent)
    {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_center_from_left, R.anim.slide_right_from_center);
    }

    public void FinishAnim()
    {
        finish();
        overridePendingTransition(R.anim.slide_center_from_left, R.anim.slide_right_from_center);
    }

    public void ShowPopup(String title, String msg)
    {
        if(!BaseActivity.this.isFinishing())
        {
            final Popup popup = new Popup(mContext, title, msg, "", "확인");
            popup.Cancel_Click = new Popup.onClick()
            {
                @Override
                public void onClick()
                {
                    //
                }
            };
            popup.OK_Click = new Popup.onClick()
            {
                @Override
                public void onClick()
                {
                    return;
                }
            };
            popup.show();
        }
    }

    public void ShowPopup_Finish(String msg, String errmsg)
    {
        if(!BaseActivity.this.isFinishing())
        {
            if(Config.RELEASE == false)
            {
                msg = msg + "\n" + errmsg;
            }

            final Popup popup = new Popup(mContext, "", msg, "", "닫기");
            popup.OK_Click = new Popup.onClick()
            {
                @Override
                public void onClick()
                {
                    finish();
                    return;
                }
            };
            popup.show();
        }
    }

    public void ShowPopup_APPEND()
    {
        if(!BaseActivity.this.isFinishing())
        {
            final Popup popup = new Popup(mContext, "종료", "잉ing 종료하시겠습니까?", "취소", "종료");
            popup.Cancel_Click = new Popup.onClick()
            {
                @Override
                public void onClick()
                {
                    //
                }
            };
            popup.OK_Click = new Popup.onClick()
            {
                @Override
                public void onClick()
                {
                    APP_END();
                }
            };
            popup.show();
        }
    }

    public void APP_END()
    {
        for (int i = 0; i < Config.actList.size(); i++)
        {
            Config.actList.get(i).finish();
        }

        Config.actList.clear();
        finish();
    }

    public String JSON_getString(JSONObject resData, String key)
    {
        String result = "";

        try
        {
            result = resData.getString(key);
        }
        catch (Exception e)
        {
            log.vlog(2, "Json_getString Exception : " + e.getMessage());
            result = "";
        }

        return result;
    }

    public int JSON_getInt(JSONObject resData, String key)
    {
        int result = 0;

        try
        {
            result = resData.getInt(key);
        }
        catch (Exception e)
        {
            log.vlog(2, "JSON_getInt Exception : " + e.getMessage());
            result = 0;
        }

        return result;
    }

    public void TimeOutStart()
    {
        TimeOut_StartTime  = System.currentTimeMillis();

        TimerTask mm = new TimerTask()
        {
            @Override
            public void run()
            {
                Runnable runnable = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        TimeOut_CurrentTime  = System.currentTimeMillis();
                        long left_time = Config.POLLING_TIMEOUT - (TimeOut_CurrentTime - TimeOut_StartTime);
                        log.vlog(2, "타임아웃 까지 남은 시간 : " + left_time + "ms");

                        // 폴링 타임아웃 체크
                        if (left_time <= 0)
                        {
                            TimeOutStop();
                            return;
                        }
                    }
                };
                handler.post(runnable);
            }
        };

        TimeOut_timer = new Timer();
        TimeOut_timer.schedule(mm, 0, 1000);
    }

    public void TimeOutStop()
    {
        if(TimeOut_timer != null)
        {
            log.vlog(2, "TimeOut 중지됨");
            TimeOut_timer.cancel();
        }
    }

    private void setStatusBarBackground() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.BLACK);
    }

//    public void setFont(TextView textView){
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/BMJUA_ttf.ttf");//폰트
//        textView.setTypeface(typeface);
//    }
}
