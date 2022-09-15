package cjk.smf;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cjk.smf.Activitys.CommonActivity;
import cjk.smf.Activitys.Fragment_Client;
import cjk.smf.Activitys.LoadingActivity;
import cjk.smf.Adapters.Alarmservice;
import cjk.smf.Recycler.RecyclerAdapter_main;
import cjk.smf.Recycler.RecyclerItem;
import cjk.smf.Recycler.RecyclerItemClickListener;


/**
 * Created by user on 2018-08-08.
 */
public class MainActivity extends CommonActivity {
    private String resentData ="";
    private String temp ="";
    private String id="1";
    private Switch alarm_switch;
    public static float mintemp;
    public static float maxtemp;
    public static float minhumid;
    public static float maxhumid;
    public static float minco2;
    public static float maxco2;
    private Toolbar myToolbar;
    public static ArrayList<RecyclerItem> copyItems = new ArrayList<>();
    private ArrayList<RecyclerItem> mItems = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public static Activity AActivity;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private boolean alarm_permit;
    phpDown task;
    static public String[] data_buf = new String[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        <item name="windowNoTitle">true</item> //타이틀바 없애기

        AActivity=MainActivity.this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        alarm_switch= (Switch)findViewById(R.id.switch1);
        SharedPreferences pref= getSharedPreferences("pref", MODE_PRIVATE); //SharedPreferences 선언
        alarm_permit=pref.getBoolean("alarm_per",true);  //최근 설정한 ID 읽어오기 (기본값 1)
        alarm_switch.setChecked(alarm_permit);

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(myToolbar); //api가 높으면 없어야함
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //홈버튼 왼쪽으로 옮김
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher); //홈버튼 이미지 변경
//        getSupportActionBar().setTitle("");  //툴바 타이틀 변경

        task = new phpDown();
        task.execute("http://cjk09083.cafe24.com/smf/get_users_data_eng.php");
        Intent intent = getIntent();

        Log.d("카페24","setup 완료 ");

        Intent backintent = new Intent(
                getApplicationContext(),//현재제어권자
                Alarmservice.class); // 이동할 컴포넌트
        startService(backintent); // 백그라운드 서비스

        setRecyclerView();

        alarm_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Toast.makeText(MessageActivity.this, "체크상태 = " + isChecked, Toast.LENGTH_SHORT).show();
                SharedPreferences pref= getSharedPreferences("pref", MODE_PRIVATE); // 선언
                SharedPreferences.Editor editor = pref.edit();// editor에 put 하기
                if (true == isChecked) {
                    editor.putBoolean("alarm_per",true);
                    Log.d("카페24","팝업 알람 허용 저장");

                    //////////////////////////// SharedPreferences 데이터 저장 완료
                } else{
                    editor.putBoolean("alarm_per",false);
                    Log.d("카페24","팝업 알람 금지 저장");

                }
                editor.commit(); //완료한다.


            }
        });

    }









    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();

            finish();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(this, "뒤로가기 버튼을 한번더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();

        }
    }

    private void setRecyclerView() {
        // 각 Item 들이 RecyclerView 의 전체 크기를 변경하지 않는 다면
        // setHasFixedSize() 함수를 사용해서 성능을 개선할 수 있습니다.
        // 변경될 가능성이 있다면 false 로 , 없다면 true를 설정해주세요.
        recyclerView.setHasFixedSize(true);

        // RecyclerView 에 Adapter 를 설정해줍니다.
        adapter = new RecyclerAdapter_main(mItems);


        recyclerView.setAdapter(adapter);
        // 다양한 LayoutManager 가 있습니다. 원하시는 방법을 선택해주세요.
        // 지그재그형의 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        // 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        // 가로 또는 세로 스크롤 목록 형식

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//        mLayoutManager.setReverseLayout(true);//순서 역순1
//        mLayoutManager.setStackFromEnd(true);// 순서 역순2
        recyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getApplicationContext(), mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        //mainBinding.recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(48));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Toast.makeText(getApplicationContext(),position+"번 째 아이템 클릭",Toast.LENGTH_SHORT).show();
                        String Userdata = String.valueOf(mItems.get(position).getName());

                        Log.d("선택한 고객 정보  ", "pos : "+position+ ", 정보 : "+ Userdata);

                        Intent intent = new Intent(MainActivity.this, Fragment_Client.class);
                        intent.putExtra("data", Userdata);
                        startActivity(intent);


                        Intent loadintent = new Intent(MainActivity.this, LoadingActivity.class);
                        startActivity(loadintent);  //로딩화면 호출
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();


                    }
                }));
        //setData();
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
                    data_buf = new String[200];
                    // 연결되었음 코드가 리턴되면.
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for(;;){
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            data_buf[i] = br.readLine();
                            if(data_buf[i] == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            Log.d("카페24 DB아이템 : "+i, data_buf[i]);
                            resentData=data_buf[i];
                            mItems.add(new RecyclerItem(resentData));
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

            copyItems.clear();
            copyItems.addAll(mItems);
            adapter.notifyDataSetChanged();
            //Intent mainActivityIntent = new Intent(MainActivity.this, MainActivity.class);
            //startActivity(mainActivityIntent);

        }

    }



}

