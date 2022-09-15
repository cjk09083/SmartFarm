package cjk.smf.Activitys;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import cjk.smf.R;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.EntityUtils;


/**
 * Created by user on 2018-08-08.
 */
public class UploadActivity extends Activity {

    private String humid ="";
    private String temp ="";
    private String id="";
    private String co2="";
    private String responseString="";
    private String address="";
    private EditText humidET;
    private EditText tempET;
    private EditText idET;
    private EditText co2ET;

    private TextView TV1;
    static public String[] data_buf = new String[100];
    phpDown task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_upload);
        idET = (EditText)findViewById(R.id.txtText1);
        humidET = (EditText)findViewById(R.id.txtText3);
        tempET = (EditText)findViewById(R.id.txtText2);
        co2ET = (EditText)findViewById(R.id.txtText4);


    }




    //업로드 버튼 클릭
    public void mOnMove(View v){
        Log.d("카페24","업로드 버튼 클릭");
        humid = humidET.getText().toString();
        temp = tempET.getText().toString();
        id=idET.getText().toString();
        co2=co2ET.getText().toString();

        if(temp.isEmpty()||id.isEmpty()||humid.isEmpty())
        {        Toast.makeText(this, "정보중에 공백이 존재합니다.", Toast.LENGTH_LONG).show();
        }

        else {
            new SendPost().execute();
            Toast.makeText(this, "업로드 성공 with : "+id, Toast.LENGTH_LONG).show();
        }

    }

    //자료확인 버튼 클릭
    public void mOnMiddle(View v){
        id=idET.getText().toString();
        if(id.isEmpty()){id="1";}
        Log.d("카페24","자료확인버튼 클릭 + id : " +id);
        Intent backintent = new Intent(
                getApplicationContext(),//현재제어권자
                RecyclerActivity.class); // 이동할 컴포넌트
        backintent.putExtra("ID",id);
        startActivity(backintent); // 백그라운드 서비스
        //task = new phpDown();
        //task.execute("http://cjk09083.cafe24.com/get_user_data.php?userID="+id);
    }


    //전체차트확인 버튼 클릭
    public void mOnChart(View v){
        id=idET.getText().toString();
        if(id.isEmpty()){id="1";}
        Log.d("카페24","전체차트버튼 클릭 + id : " +id);
        Intent temphumid_intent = new Intent(
                getApplicationContext(),//현재제어권자
                temphumid_web_total.class); // 이동할 컴포넌트
        temphumid_intent.putExtra("ID",id);
        temphumid_intent.putExtra("mode", 0);
        startActivity(temphumid_intent); // 백그라운드 서비스

        //task = new phpDown();
        //task.execute("http://cjk09083.cafe24.com/get_user_data.php?userID="+id);
    }

    //월별차트확인 버튼 클릭
    public void monthChart(View v){
        Calendar calendar = new GregorianCalendar(Locale.KOREA);
        String nYear = String.valueOf(calendar.get(Calendar.YEAR));
        nYear= nYear.substring(2);
        id=idET.getText().toString();
        if(id.isEmpty()){id="1";}
        Log.d("카페24","월별차트버튼 클릭 + id : " +id+", year : "+nYear);
        Intent temphumid_intent = new Intent(
                getApplicationContext(),//현재제어권자
                temphumid_web_total.class); // 이동할 컴포넌트
        temphumid_intent.putExtra("ID",id);
        temphumid_intent.putExtra("year",nYear);
        temphumid_intent.putExtra("mode", 1);
        startActivity(temphumid_intent); // 백그라운드 서비스

        //task = new phpDown();
        //task.execute("http://cjk09083.cafe24.com/get_user_data.php?userID="+id);
    }

    public void dayChart(View v){
        id=idET.getText().toString();
        if(id.isEmpty()){id="1";}
        Log.d("카페24","기간차트버튼 클릭 + id : " +id);
        Intent temphumidpopup_intent = new Intent(
                getApplicationContext(),//현재제어권자
                temphumidPopup.class); // 이동할 컴포넌트
        temphumidpopup_intent.putExtra("ID",id);
        startActivity(temphumidpopup_intent); // 백그라운드 서비스

        //task = new phpDown();
        //task.execute("http://cjk09083.cafe24.com/get_user_data.php?userID="+id);
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    public class SendPost extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... unused) {
            String content = executeClient();

            return content;
        }}

    protected void onPostExecute(String result) {
        // 모두 작업을 마치고 실행할 일 (메소드 등등)

    }

    // 실제 전송하는 부분
    public String executeClient() {
        ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
        //post.add(new BasicNameValuePair("temphumid_id", id));
        //post.add(new BasicNameValuePair("temp", temp));
        post.add(new BasicNameValuePair("temp", temp));
        post.add(new BasicNameValuePair("humid", humid));
        post.add(new BasicNameValuePair("id", id));
        post.add(new BasicNameValuePair("co2", co2));
        post.add(new BasicNameValuePair("co2gen", "0"));

        // 연결 HttpClient 객체 생성
        HttpClient client = new DefaultHttpClient();

        // 객체 연결 설정 부분, 연결 최대시간 등등
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 5000);

        // Post객체 생성
        //HttpPost httpPost = new HttpPost("http://cjk09083.cafe24.com/insert.php");
        HttpPost httpPost = new HttpPost("http://cjk09083.cafe24.com/temphumidinsert.php");

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
            httpPost.setEntity(entity);
            HttpResponse response=client.execute(httpPost);
            responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            Log.d("카페24 Post 결과", ""+responseString);
            /*
            Intent i = new Intent(Intent.ACTION_VIEW);
            Uri u = Uri.parse("http://minsanggyu2.cafe24.com/user_data.php");
            i.setData(u);
            startActivity(i);
            */
            return EntityUtils.getContentCharSet(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private class phpDown extends AsyncTask<String, Integer,String> {
        @Override
        protected String doInBackground(String... urls) {
            int i =0;

            StringBuilder jsonHtml = new StringBuilder();
            try{
                // 연결 url 설정
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                // 연결되었으면.
                if(conn != null){
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    data_buf = new String[100];
                    // 연결되었음 코드가 리턴되면.
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for(;;){
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            data_buf[i] = br.readLine();
                            if(data_buf[i] == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            Log.d("카페24 DB아이템 : "+i, data_buf[i]);
                            i= i+1;
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }


            return jsonHtml.toString();

        }

        protected void onPostExecute(String str){

            //Intent mainActivityIntent = new Intent(MainActivity.this, MainActivity.class);
            //startActivity(mainActivityIntent);

        }

    }
}

