package cjk.smf.Activitys;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cjk.smf.MainActivity;
import cjk.smf.R;

public class PopupActivity extends Activity {

    private  String data;
    private  String TAG="카페24";
    private boolean MainRunning=false;
    private static ArrayList<String> mArrayList2;
    public static Activity popupActivity;

    TextView txtText1;
    TextView txtText2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG+": 팝업데이터","받기실행");
        // 화면이 꺼졌을때도 팝업 띄우기
        getWindow().addFlags(

                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |

                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        popupActivity = PopupActivity.this;

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_activity);

        //UI 객체생성
        txtText1 = (TextView)findViewById(R.id.txtText1);

        txtText2 = (TextView)findViewById(R.id.txtText2);

        //데이터 가져오기
        Intent intent = getIntent();
        data = intent.getStringExtra("alarm_data");
        Log.d("받은 팝업데이터",data);
        txtText1.setText(data);

    }

    //확인 버튼 클릭
    public void mOnMove(View v){
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);
        finish();


        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info;
        info = activityManager.getRunningTasks(7);
        for (Iterator iterator = info.iterator(); iterator.hasNext();)  {
            ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) iterator.next();
            if(runningTaskInfo.topActivity.getClassName().equals("cjk.smf.MainActivity")) {
                Log.d(TAG+": 메인 액티비티 : ", "실행중");

                MainRunning=true;}
        }
        MainActivity BBActivity = (MainActivity)MainActivity.AActivity;
        if(MainRunning)
        {BBActivity.finish();}

        mainstart();

    }


    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);
        finish();
    }

    private void mainstart() {
        Intent mainstart = new Intent(this, MainActivity.class);
        mainstart.putExtra("from", 1);

        startActivity(mainstart);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }


}

