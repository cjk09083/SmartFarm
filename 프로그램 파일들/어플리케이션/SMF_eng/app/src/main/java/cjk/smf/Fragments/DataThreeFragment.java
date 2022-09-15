package cjk.smf.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Locale;

import cjk.smf.Activitys.referencePopup;
import cjk.smf.R;

import static cjk.smf.Adapters.Alarmservice.mqttClient;


public class DataThreeFragment extends Fragment {

    private String id;
    private String pubTopic0="SMFinTopic";
    private String pubTopic="SMFinTopic";

    private float mintemp;
    private float maxtemp;
    private float minhumid;
    private float maxhumid;
    private float minco2;
    private float maxco2;
    private EditText mintempET;
    private EditText maxtempET;
    private EditText minhumidET;
    private EditText maxhumidET;
    private EditText minco2ET;
    private EditText maxco2ET;
    private Button tempbtn;
    private Button humidbtn;
    private Button co2btn;

    ///기준 19년 04월 27일 ~ 19년 07월 10일
    public DataThreeFragment() {
        // required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id");
            mintemp = getArguments().getFloat("mintemp");
            maxtemp = getArguments().getFloat("maxtemp");
            minhumid = getArguments().getFloat("minhumid");
            maxhumid = getArguments().getFloat("maxhumid");
            minco2 = getArguments().getFloat("minco2");
            maxco2 = getArguments().getFloat("maxco2");
            pubTopic=pubTopic0+"/"+id;
            Log.d("현재 기준치"+id,"mintemp : "+ mintemp+ ", maxtemp : "+ maxtemp+ ", minhumid : "+ minhumid+ ", maxhumid : "+ maxhumid+ ", minco2 : "+ minco2+ ", maxco2 : "+ maxco2 );

        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,

                             @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_reference, container, false);

        mintempET=myView.findViewById(R.id.mintempET);
        maxtempET=myView.findViewById(R.id.maxtempET);
        minhumidET=myView.findViewById(R.id.minhumidET);
        maxhumidET=myView.findViewById(R.id.maxhumidET);
        minco2ET=myView.findViewById(R.id.minco2ET);
        maxco2ET=myView.findViewById(R.id.maxco2ET);
        tempbtn=myView.findViewById(R.id.tempBtn);
        humidbtn=myView.findViewById(R.id.humidBtn);
        co2btn=myView.findViewById(R.id.co2Btn);


        mintempET.setText(String.format(Locale.KOREA,"%2.0f",mintemp));
        maxtempET.setText(String.format(Locale.KOREA,"%2.0f",maxtemp));
        minhumidET.setText(String.format(Locale.KOREA,"%2.0f",minhumid));
        maxhumidET.setText(String.format(Locale.KOREA,"%2.0f",maxhumid));
        minco2ET.setText(String.format(Locale.KOREA,"%3.0f",minco2));
        maxco2ET.setText(String.format(Locale.KOREA,"%3.0f",maxco2));


        tempbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("기준치 팝업","온도설정 버튼 클릭 "+id);

                MqttMessage mintemp_message = new MqttMessage(("mintemp:"+mintempET.getText().toString()).getBytes());
                try {
                    mqttClient.publish(pubTopic0,mintemp_message);
                    mqttClient.publish(pubTopic,mintemp_message);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                MqttMessage maxtemp_message = new MqttMessage(("maxtemp:"+maxtempET.getText().toString()).getBytes());
                try {
                    mqttClient.publish(pubTopic0,maxtemp_message);
                    mqttClient.publish(pubTopic,maxtemp_message);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), "온도기준을 설정하였습니다", Toast.LENGTH_SHORT).show();
            }});

        humidbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("기준치 팝업","습도설정 버튼 클릭 "+id);
                MqttMessage minhumid_message = new MqttMessage(("minhumid:"+minhumidET.getText().toString()).getBytes());
                try {
                    mqttClient.publish(pubTopic0,minhumid_message);
                    mqttClient.publish(pubTopic,minhumid_message);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                MqttMessage maxhumid_message = new MqttMessage(("maxhumid:"+maxhumidET.getText().toString()).getBytes());
                try {
                    mqttClient.publish(pubTopic0,maxhumid_message);
                    mqttClient.publish(pubTopic,maxhumid_message);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), "습도기준을 설정하였습니다", Toast.LENGTH_SHORT).show();
            }});

        co2btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("기준치 팝업","Co2설정 버튼 클릭 "+id);
                MqttMessage minco2_message = new MqttMessage(("minco2:"+minco2ET.getText().toString()).getBytes());
                try {
                    mqttClient.publish(pubTopic0,minco2_message);

                    mqttClient.publish(pubTopic,minco2_message);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                MqttMessage maxco2_message = new MqttMessage(("maxco2:"+maxco2ET.getText().toString()).getBytes());
                try {
                    mqttClient.publish(pubTopic0,maxco2_message);
                    mqttClient.publish(pubTopic,maxco2_message);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), "Co2기준을 설정하였습니다", Toast.LENGTH_SHORT).show();
            }});



        return myView;
    }



}



