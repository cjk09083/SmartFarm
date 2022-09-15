package cjk.smf.Activitys;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cjk.smf.R;
import cjk.smf.Recycler.RecyclerAdapter;
import cjk.smf.Recycler.RecyclerItem;
import cjk.smf.Recycler.RecyclerItemClickListener;
import cjk.smf.databinding.ActivityRecycleBinding;

public class RecyclerActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static ArrayList<String> mArrayList;


    private String[] names = {"회원1","회원2","회원3","회원4",};
    private static final int LAYOUT = R.layout.activity_recycle;
    private ActivityRecycleBinding mainBinding;
    private RecyclerView.Adapter adapter;
    private EditText nameET;
    private EditText ageET;
    private ArrayList<RecyclerItem> mItems = new ArrayList<>();
    static public String[] data_buf = new String[100];
    private String id =" " ;
    phpDown task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("농장 자료확인", "실행");
        task = new phpDown();

        // ArrayList 생성
            mArrayList = new ArrayList<String>();

        // ArrayList 값 추가
        mainBinding = DataBindingUtil.setContentView(this,LAYOUT);
        Button addBtn = (Button) findViewById(R.id.add_item_btn);
        Button delBtn = (Button) findViewById(R.id.del_item_btn);
        TextView idtv = (TextView) findViewById(R.id.idtv);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
//        Log.d("농장 id : ", id);
        idtv.setText("Farm ID : "+id);
        setRecyclerView();
        setRefresh();
        task.execute("http://cjk09083.cafe24.com/smf/get_user_data_eng.php?userID="+id);

///////////////////////////////////////////////////////여기까지 리스트 관련///////////////////////////////////////////////////
///////////////////////////////////////////////////////여기까지 리스트 관련///////////////////////////////////////////////////
///////////////////////////////////////////////////////여기까지 리스트 관련///////////////////////////////////////////////////
///////////////////////////////////////////////////////여기까지 리스트 관련///////////////////////////////////////////////////
///////////////////////////////////////////////////////여기까지 리스트 관련///////////////////////////////////////////////////




        addBtn.setOnClickListener(new View.OnClickListener() {  //회원추가
            public void onClick(View v) {
                Log.d("카페24 id2 : ", id);

            }
        });



        delBtn.setOnClickListener(new View.OnClickListener() {  //마지막 회원삭제
        public void onClick(View v) {
        }
    });
}


    private void setRecyclerView(){
        // 각 Item 들이 RecyclerView 의 전체 크기를 변경하지 않는 다면
        // setHasFixedSize() 함수를 사용해서 성능을 개선할 수 있습니다.
        // 변경될 가능성이 있다면 false 로 , 없다면 true를 설정해주세요.
        mainBinding.recyclerView.setHasFixedSize(true);

        // RecyclerView 에 Adapter 를 설정해줍니다.
        adapter = new RecyclerAdapter(mItems);

        mainBinding.recyclerView.setAdapter(adapter);

        // 다양한 LayoutManager 가 있습니다. 원하시는 방법을 선택해주세요.
        // 지그재그형의 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        // 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        // 가로 또는 세로 스크롤 목록 형식
        mainBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getApplicationContext(),new LinearLayoutManager(this).getOrientation());
        mainBinding.recyclerView.addItemDecoration(dividerItemDecoration);
        //mainBinding.recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(48));

        mainBinding.recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), mainBinding.recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
//                        Toast.makeText(getApplicationContext(),position+"번 째 아이템 클릭",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
//                        Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
                    }
                }));
        setData();
    }

    private void setRefresh(){
        mainBinding.swipeRefreshLo.setOnRefreshListener(this);
        mainBinding.swipeRefreshLo.setColorSchemeColors(getResources().getIntArray(R.array.google_colors));
    }


    @Override
    public void onRefresh() {
        mainBinding.recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(mainBinding.recyclerView,"새로고침 성공",Snackbar.LENGTH_SHORT).show();
                mainBinding.swipeRefreshLo.setRefreshing(false);
            }
        },500);
        mArrayList.clear();
        task = new phpDown();
        task.execute("http://cjk09083.cafe24.com/smf/get_user_data_eng.php?userID="+id);
       // setData();
    }

    private void setData(){
        mItems.clear();
        // RecyclerView 에 들어갈 데이터를 추가합니다.
        for(int i = 0; i < mArrayList.size(); i++){
            Log.d("리스트크기",""+mArrayList.size());
            mItems.add(new RecyclerItem(mArrayList.get(i)));
        }
        // 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행합니다.
        adapter.notifyDataSetChanged();
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
                            Log.d("카페24 DB아이템 "+i, data_buf[i]);
                            mArrayList.add(data_buf[i]);
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
            setData();

            //Intent mainActivityIntent = new Intent(MainActivity.this, MainActivity.class);
            //startActivity(mainActivityIntent);

        }

    }
}
