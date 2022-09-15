package cjk.smf.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import cjk.smf.Activitys.RecyclerActivity;
import cjk.smf.R;

public class DataOneFragment extends Fragment {

    static public String[] data_buf = new String[100];
    phpDown task;

    private String id;
    private String name;
    private String php_data=" ";
    private String light_value;
    private String day_value;
    private String week_value;
    private String month_value;
    private String reft_value;
    private String humid_value;

    private String recent_time_value;
    private float recent_temp_value;
    private float recent_humid_value;
    private float recent_co2_value;

    private String now_weekDay = "";
    private String now_year = "";
    private String now_month = "";
    private String now_day = "";
    private String day_address = "";

    private Button databtn;
    private TextView farm_name;
    private TextView farm_date;
    private TextView recent_time_text;
    private TextView recent_temp_text;
    private TextView recent_humid_text;
    private TextView recent_co2_text;
    private TextView light_text;
    private TextView day_text;
    private TextView week_text;
    private TextView month_text;
    private TextView reft_text;

    public DataOneFragment() {
        // required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            name = getArguments().getString("name");
            id = getArguments().getString("id");
            recent_time_value = getArguments().getString("time");
            recent_temp_value = getArguments().getFloat("temp");
            recent_humid_value = getArguments().getFloat("humid");
            recent_co2_value = getArguments().getFloat("co2");
            light_value = String.valueOf(getArguments().getFloat("lux"));

            recent_time_value = recent_time_value.substring(2, 16);

//                      Log.d("frag 받은 고객 데이터", " tag : "+tag+", id : "+id+", name : "+name+", memo : "+memo
//                    +", date : "+date+", pic1 : "+pic1+", pic2 : "+pic2+", pic3 : "+pic3);
        }
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat weekdayFormat = new SimpleDateFormat("EE", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yy", Locale.getDefault());
        now_weekDay = weekdayFormat.format(currentTime);
        now_year = yearFormat.format(currentTime);
        now_month = monthFormat.format(currentTime);
        now_day = dayFormat.format(currentTime);  //현재 날짜 구하기

        now_year = "19";
        now_month = "11";
        now_day = "28";

        day_address = "http://cjk09083.cafe24.com/smf/get_lux_data.php?device_id=" + id + "&year_time=" + now_year + "&month_time=" + now_month + "&day_time=" + now_day;

        task = new phpDown();
        task.execute(day_address);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_one, container, false);

        light_text = myView.findViewById(R.id.now_text);
        day_text = (TextView) myView.findViewById(R.id.day_text);
        week_text = (TextView) myView.findViewById(R.id.week_text);
        month_text = (TextView) myView.findViewById(R.id.month_text);
        recent_time_text = (TextView) myView.findViewById(R.id.recent_time);
        recent_temp_text = (TextView) myView.findViewById(R.id.recent_now);
        recent_humid_text = (TextView) myView.findViewById(R.id.recent_acc);
        recent_co2_text = (TextView) myView.findViewById(R.id.recent_reft);
        databtn = myView.findViewById(R.id.databtn);

        farm_name = myView.findViewById(R.id.farm_name);
        farm_date = myView.findViewById(R.id.farm_date);  //각종 뷰 선언
        light_text.setText(light_value);
        farm_name.setText(name);
//        farm_date.setText(date);

        recent_time_text.setText(recent_time_value);
        recent_temp_text.setText(recent_temp_value + "℃");
        recent_humid_text.setText(recent_humid_value + "%");
        recent_co2_text.setText(recent_co2_value + "ppm");

        databtn.setOnClickListener(new View.OnClickListener() {  //마지막 회원삭제
            public void onClick(View v) {
                Log.d("농장", "자료확인버튼 클릭 + id : " + id);
                Intent backintent = new Intent(
                        getActivity(),//현재제어권자
                        RecyclerActivity.class); // 이동할 컴포넌트
                backintent.putExtra("id", id);
                startActivity(backintent); // 백그라운드 서비스
            }
        });

        return myView;
    }


    private class phpDown extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... urls) {
            int i = 0;

            StringBuilder jsonHtml = new StringBuilder();
            try {
                // 연결 url 설정
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 연결되었으면.
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);

                    data_buf = new String[200];
                    // 연결되었음 코드가 리턴되면.
//                    Log.d("Login 농장 json 연결코드 : ", ""+conn.getResponseCode());

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        String read_data = "";
                        for (; ; ) {
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            data_buf[i] = br.readLine();
                            if (data_buf[i] == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            //Log.d("카페24 DB아이템 : "+i, data_buf[i]);
                            //entries2.add(new Entry(i, Float.valueOf(value2));
                            read_data += data_buf[i];
                            i = i + 1;
                        }
//                        Log.d("Login 농장 json : ", read_data);
                        php_data = read_data;

                        //showResult();


                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            return jsonHtml.toString();

        }

        protected void onPostExecute(String str) {
            if (php_data.length() > 3) {
                showResult();
            }
        }
    }

    private void showResult() {

        StringTokenizer stok = new StringTokenizer(php_data, "/", false);
        day_value=  stok.nextToken();
        week_value=  stok.nextToken();
        month_value=  stok.nextToken();

        day_text.setText(day_value);
        week_text.setText(week_value);
        month_text.setText(month_value);
    }

}



