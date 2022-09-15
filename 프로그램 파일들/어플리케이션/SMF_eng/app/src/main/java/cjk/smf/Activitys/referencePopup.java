package cjk.smf.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Locale;

import cjk.smf.R;

import static cjk.smf.Adapters.Alarmservice.mqttClient;
import static cjk.smf.MainActivity.maxco2;
import static cjk.smf.MainActivity.maxhumid;
import static cjk.smf.MainActivity.maxtemp;
import static cjk.smf.MainActivity.minco2;
import static cjk.smf.MainActivity.minhumid;
import static cjk.smf.MainActivity.mintemp;


/**
 * Created by user on 2018-08-08.
 */
public class referencePopup extends Activity {
    private String Today;

    private String device_id="";
    private String pubTopic="SMFinTopic";
    private float mintemps;
    private float maxtemps;
    private float minhumids;
    private float maxhumids;
    private float minco2s;
    private float maxco2s;
    private EditText mintempET;
    private EditText maxtempET;
    private EditText minhumidET;
    private EditText maxhumidET;
    private EditText minco2ET;
    private EditText maxco2ET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mintemps=mintemp;
        maxtemps=maxtemp;
        minhumids=minhumid;
        maxhumids=maxhumid;
        minco2s=minco2;
        maxco2s=maxco2;

        Intent intent = getIntent();
        device_id = " "+ intent.getStringExtra("ID");
        if(device_id.length()>1)
        {device_id=device_id.substring(1);}
        Log.d("메세지액티비티에서 가져온 ID",device_id);

        //WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reference_popup);
        mintempET=findViewById(R.id.mintempET);
        maxtempET=findViewById(R.id.maxtempET);
        minhumidET=findViewById(R.id.minhumidET);
        maxhumidET=findViewById(R.id.maxhumidET);
        minco2ET=findViewById(R.id.minco2ET);
        maxco2ET=findViewById(R.id.maxco2ET);
        mintempET.setText(String.format(Locale.KOREA,"%2.0f",mintemps));
        maxtempET.setText(String.format(Locale.KOREA,"%2.0f",maxtemps));
        minhumidET.setText(String.format(Locale.KOREA,"%2.0f",minhumids));
        maxhumidET.setText(String.format(Locale.KOREA,"%2.0f",maxhumids));
        minco2ET.setText(String.format(Locale.KOREA,"%3.0f",minco2s));
        maxco2ET.setText(String.format(Locale.KOREA,"%3.0f",maxco2s));

    }


    //온도설정 버튼 클릭
    public void mOnTemp(View v){
        Log.d("기준치 팝업","온도설정 버튼 클릭 "+device_id);

        MqttMessage mintemp_message = new MqttMessage(("mintemp:"+mintempET.getText().toString()).getBytes());
        try {
            mqttClient.publish(pubTopic,mintemp_message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        MqttMessage maxtemp_message = new MqttMessage(("maxtemp:"+maxtempET.getText().toString()).getBytes());
        try {
            mqttClient.publish(pubTopic,maxtemp_message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        Toast.makeText(referencePopup.this, "온도기준을 설정하였습니다", Toast.LENGTH_SHORT).show();
    }


    //습도설정 버튼 클릭
    public void mOnHumid(View v){
        Log.d("기준치 팝업","습도설정 버튼 클릭 "+device_id);
        MqttMessage minhumid_message = new MqttMessage(("minhumid:"+minhumidET.getText().toString()).getBytes());
        try {
            mqttClient.publish(pubTopic,minhumid_message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        MqttMessage maxhumid_message = new MqttMessage(("maxhumid:"+maxhumidET.getText().toString()).getBytes());
        try {
            mqttClient.publish(pubTopic,maxhumid_message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        Toast.makeText(referencePopup.this, "습도기준을 설정하였습니다", Toast.LENGTH_SHORT).show();
    }

    //Co2설정 버튼 클릭
    public void mOnCo2(View v){
        Log.d("기준치 팝업","Co2설정 버튼 클릭 "+device_id);
        MqttMessage minco2_message = new MqttMessage(("minco2:"+minco2ET.getText().toString()).getBytes());
        try {
            mqttClient.publish(pubTopic,minco2_message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        MqttMessage maxco2_message = new MqttMessage(("maxco2:"+maxco2ET.getText().toString()).getBytes());
        try {
            mqttClient.publish(pubTopic,maxco2_message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        Toast.makeText(referencePopup.this, "Co2기준을 설정하였습니다", Toast.LENGTH_SHORT).show();
    }

    //취소 버튼 클릭
    public void mOnClose(View v){

        Log.d("기준치 팝업","취소버튼 클릭");
       finish();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }


/*
    온습도 전체평균 : 아이디
    http://minsanggyu2.cafe24.com/temphumid_total_graph.php?device_id=9c8deecc68ba40bb89a1635f397c5f5e

    온습도 월별 평균 : 아이디, 연도
    http://minsanggyu2.cafe24.com/temphumid_month_graph.php?device_id=9c8deecc68ba40bb89a1635f397c5f5e&year_time=18

    온습도 특정기간 : 시작년도&시작월&시작일&최종년도&최종월&최종일
    http://minsanggyu2.cafe24.com/temphumid_select_graph.php?year_time_start=18&month_time_start=08&day_time_start=26&year_time_end=18&month_time_end=08&day_time_end=31&device_id=9c8deecc68ba40bb89a1635f397c5f5e
*/

}

