package cjk.smf.Adapters;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Iterator;
import java.util.List;

import cjk.smf.Activitys.PopupActivity;
import cjk.smf.Activitys.RestartService;
import cjk.smf.MainActivity;

import static cjk.smf.R.drawable.ecology;

public class Alarmservice extends Service {
    private static final String TAG = "카페24";

    public Alarmservice() {
    }

    private static final int REBOOT_DELAY_TIMER = 10 * 1000;
    private String mqttcontent="";
    private String pre_mqttcontent="";

    private String subscribeTopic[]={"SMFoutTopic","TestoutTopic2"};
    public static MqttClient mqttClient;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Log.d("test", "서비스의 onCreate");
        unregisterRestartAlarm();

        try {
            connectMqtt();
            Log.d("카페24", "MQTT 연결");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("카페24", "MqttConnect Error");
        }

        /*
        ///////////////////////////////상태 알림참///////////////////////////////////
        Intent mMainIntent =new Intent(this,MainActivity.class);
        PendingIntent mPendingIntent = PendingIntent.getActivity(this,1,mMainIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder mBuilder=
                new Notification.Builder(this)
                        .setSmallIcon(ecology)
                        .setContentTitle("알람 대기중")
                        .setContentIntent(mPendingIntent)
                        .setContentText("클릭 시 어플로 이동합니다.");
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //mNotifyMgr.notify(001,mBuilder.build());          //Task killer로 죽는 서비스 실행
        startForeground(121, mBuilder.build());         //죽지않는 서비스 실행
        */

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 실행
        //Log.d("test", "서비스의 onDestroy");

        registerRestartAlarm();
        ///////////////////////////////상태 알림참///////////////////////////////////
        Intent mMainIntent =new Intent(this,MainActivity.class);
        PendingIntent mPendingIntent = PendingIntent.getActivity(this,1,mMainIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder=
                new NotificationCompat.Builder(this)
                        .setSmallIcon(ecology)
                        .setContentTitle("푸시 알람 대기중")
                        .setContentText("클릭 시 어플로 이동합니다.");
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(001);
        super.onDestroy();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////// 특정 문자열 개수 세기//////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    int getCharNumber(String str, char c)
    {
        int count = 0;
        for(int i=0;i<str.length();i++)
        {
            if(str.charAt(i) == c)
                count++;
        }
        return count;
    }




    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////MQTT관련 함수//////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////



    private void connectMqtt() throws Exception{
        mqttClient = new MqttClient("tcp://postman.cloudmqtt.com:17181", MqttClient.generateClientId(),null);
        MqttConnectOptions options = new MqttConnectOptions();
        //options.setCleanSession(true);
        options.setUserName("mzxzkerr");
        options.setPassword("6ePJPdo2Nkr6".toCharArray());
        mqttClient.connect(options);
        mqttClient.subscribe(subscribeTopic);
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.d(TAG,"Mqtt ReConnect");
                try{connectMqtt();}catch(Exception e){Log.d(TAG,"MqttReConnect Error");}
            }
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                mqttcontent=new String(message.getPayload());
                Log.d(TAG,"messageArrived topic ["+topic+"], message : "+mqttcontent + " vs "+pre_mqttcontent);
                if(!pre_mqttcontent.equals(mqttcontent)){
                    if(topic.equals("SMFoutTopic")){
                        SharedPreferences pref= getSharedPreferences("pref", MODE_PRIVATE); //SharedPreferences 선언
                        boolean alarm_permit=pref.getBoolean("alarm_per",true);  //최근 설정한 ID 읽어오기 (기본값 1)
                        if(alarm_permit) {
                            Log.d(TAG,"팝업 알람 허용됨 : "+alarm_permit);
                            popupstart();
                        } else{
                            Log.d(TAG,"팝업 알람 금지됨 : "+alarm_permit);

                        }
                        alarmstate();
                    }
                } else{
                    Log.d(TAG,"messageArrived again : "+mqttcontent);
                }

                pre_mqttcontent=mqttcontent;
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }



    private void popupstart() {
        Intent popupintent = new Intent(this, PopupActivity.class);

        Log.d(TAG+": 알람팝업데이터", mqttcontent);
        popupintent.putExtra("alarm_data", mqttcontent);
        popupintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);     //팝업이 켜져있으면 끄고 다시 실행
        List<ActivityManager.RunningTaskInfo> info;
        info = activityManager.getRunningTasks(7);
        for (Iterator iterator = info.iterator(); iterator.hasNext();)  {
            ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) iterator.next();
            // Log.i("RunningTaskInfo",runningTaskInfo.topActivity.getClassName());
            Log.d(TAG+": 최상단 액티비티 : ", runningTaskInfo.topActivity.getClassName());
            if(runningTaskInfo.topActivity.getClassName().equals("cjk.smf.Activitys.PopupActivity")) {
                Log.d(TAG+": 팝업 액티비티 : ", "실행중");
                PopupActivity popupActivity = (PopupActivity) PopupActivity.popupActivity;
                popupActivity.finish();}
        }

        startActivity(popupintent);
        //상단 알람바 실행
    }

    private void alarmstate() {
        ///////////////////////////////상태 알림참///////////////////////////////////
        int idx=mqttcontent.indexOf(",");
        String id = mqttcontent.substring(3,idx);

        Intent alarmIntent = new Intent(this, MainActivity.class);
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(ecology)
                        .setContentTitle("["+id+"번 농장] 비상 알람 발생")
                        .setContentIntent(mPendingIntent)
                        .setContentText("내용 : "+mqttcontent );
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(002, mBuilder.build());
        Log.d(TAG+": 알람 상태창", "작동");
    }
    /**
     * 서비스가 시스템에 의해서 또는 강제적으로 종료되었을 때 호출되어
     * 알람을 등록해서 10초 후에 서비스가 실행되도록 한다.
     */
    private void registerRestartAlarm() {

        Log.d("PersistentService", "registerRestartAlarm()");

        Intent intent = new Intent(Alarmservice.this, RestartService.class);
        intent.setAction(RestartService.ACTION_RESTART_PERSISTENTSERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(Alarmservice.this, 0, intent, 0);

        long firstTime = SystemClock.elapsedRealtime();
        firstTime += REBOOT_DELAY_TIMER; // 10초 후에 알람이벤트 발생

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,REBOOT_DELAY_TIMER, sender);
    }


    /**
     * 기존 등록되어있는 알람을 해제한다.
     */
    private void unregisterRestartAlarm() {

        Log.d("PersistentService", "unregisterRestartAlarm()");
        Intent intent = new Intent(Alarmservice.this, RestartService.class);
        intent.setAction(RestartService.ACTION_RESTART_PERSISTENTSERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(Alarmservice.this, 0, intent, 0);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);
    }

}
