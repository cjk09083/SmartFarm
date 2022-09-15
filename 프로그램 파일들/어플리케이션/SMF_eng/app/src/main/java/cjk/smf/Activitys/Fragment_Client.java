package cjk.smf.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import cjk.smf.Fragments.DataOneFragment;
import cjk.smf.Fragments.DataThreeFragment;
import cjk.smf.Fragments.DataTwoFragment;
import cjk.smf.R;

import static cjk.smf.MainActivity.copyItems;

public class Fragment_Client extends CommonActivity

{

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Fragment_Client.ViewPagerAdapter fragment_adapter;

    private DataOneFragment oneFragment;
    private DataTwoFragment twoFragment;
    private DataThreeFragment threeFragment;
    private String Userdata;
    private String id;
    private String gen;
    private String time;
    private String host;
    private String to_farmNM;
    private String to_type;
    private String to_date;
    private Float tempvalue;
    private Float humidvalue;
    private Float co2value;
    private Float mintemp;
    private Float maxtemp;
    private Float minhumid;
    private Float maxhumid;
    private Float minco2;
    private Float maxco2;
    private Float lux;

    private int mode = 1;

    private Toolbar myToolbar;
    private Spinner mySpinner;
    private ArrayList<String> customAritcle;
    private ArrayList<String> dataAritcle = new ArrayList<String>();
    private ArrayList<String> dateAritcle = new ArrayList<String>();

    private ArrayAdapter<String> myAdapter;

    private boolean userIsInteracting = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragement_client);


        Intent intent = getIntent();
        Userdata  = intent.getExtras().getString("data");
        StringTokenizer stok = new StringTokenizer(Userdata, "/", false);
        id = stok.nextToken();
        tempvalue = Float.valueOf(stok.nextToken());
        humidvalue = Float.valueOf(stok.nextToken());
        co2value = Float.valueOf(stok.nextToken());
        gen = stok.nextToken();
        time = stok.nextToken();
        mintemp= Float.valueOf(stok.nextToken());
        maxtemp = Float.valueOf(stok.nextToken());
        minhumid = Float.valueOf(stok.nextToken());
        maxhumid = Float.valueOf(stok.nextToken());
        minco2 = Float.valueOf(stok.nextToken());
        maxco2 = Float.valueOf(stok.nextToken());
        lux = Float.valueOf(stok.nextToken());

        Log.d("선택한 농장 Fragement ", "id : " + id + ", temp : " + tempvalue + ", humid : " + humidvalue + ", co2 : " + co2value + ", time : " + time);


        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        mySpinner = (Spinner) findViewById(R.id.spinner_article);
        myToolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //홈버튼 왼쪽으로 옮김
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher); //홈버튼 이미지 변경

        customAritcle = new ArrayList<String>();
        name="Farm "+ id ;
        customAritcle = setspinner(Userdata);
        myAdapter = new ArrayAdapter<String>(Fragment_Client.this,
                R.layout.custom_spinner_item,
                customAritcle);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        setupViewPager(viewPager);

        viewPager.setOffscreenPageLimit(2); //탭전환시 데이터 사라지는 문제 해결 미리 로드


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();
                View myView = null;

                Log.v("result--", "tab :" + position);


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("클릭한 농장 ", "pos : " + position + ", Name : " + mySpinner.getSelectedItem().toString());


                if (userIsInteracting) {

                    Toast.makeText(Fragment_Client.this,
                            mySpinner.getSelectedItem().toString(),
                            Toast.LENGTH_SHORT)
                            .show();

                    String article = mySpinner.getSelectedItem().toString();

                    String data = dataAritcle.get(position);


                    Log.d("이동할 농장 정보  ", "pos : " + position + ", Name : " + article + ", Data : " + data);

                    Intent intent = new Intent(Fragment_Client.this, Fragment_Client.class);
                    intent.putExtra("data", data);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }


    private void setupViewPager(ViewPager viewPager) {

        Log.v("result--", "뷰페이저 호출");

        fragment_adapter = new ViewPagerAdapter(getSupportFragmentManager());

         oneFragment = new DataOneFragment();
         Bundle bundle = new Bundle();
         bundle.putString("id", id);
         bundle.putFloat("temp", tempvalue);
         bundle.putFloat("humid", humidvalue);
         bundle.putFloat("co2", co2value);
         bundle.putString("time", time);
         bundle.putString("name", name);
         bundle.putString("gen", gen);
         bundle.putFloat("mintemp", mintemp);
         bundle.putFloat("maxtemp", maxtemp);
         bundle.putFloat("minhumid", minhumid);
         bundle.putFloat("maxhumid", maxhumid);
         bundle.putFloat("minco2", minco2);
         bundle.putFloat("maxco2", maxco2);
         bundle.putFloat("lux", lux);

        oneFragment.setArguments(bundle);
         fragment_adapter.addFragment(oneFragment, getString(R.string.data_make_subject));

        twoFragment = new DataTwoFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("id", id);
        twoFragment.setArguments(bundle2);
        fragment_adapter.addFragment(twoFragment, getString(R.string.data_make_front));

        threeFragment = new DataThreeFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString("id", id);
        bundle3.putFloat("mintemp", mintemp);
        bundle3.putFloat("maxtemp", maxtemp);
        bundle3.putFloat("minhumid", minhumid);
        bundle3.putFloat("maxhumid", maxhumid);
        bundle3.putFloat("minco2", minco2);
        bundle3.putFloat("maxco2", maxco2);
        threeFragment.setArguments(bundle3);
        fragment_adapter.addFragment(threeFragment, getString(R.string.data_make_end));

        viewPager.setAdapter(fragment_adapter);

    }

    private ArrayList<String> setspinner(String now_article) {
        ArrayList<String> customAritcle = new ArrayList<String>();
        customAritcle.clear();

        for (int i = 0; i < copyItems.size(); i++) {
            Log.d("농장 Name 스피너1", "get : " + copyItems.get(i).getName() + " vs now : " + now_article);

            if (now_article.equals(copyItems.get(i).getName())) {
                String userData = copyItems.get(i).getName();
                StringTokenizer stok = new StringTokenizer(userData, "/", false);
                String userName = "Farm "+stok.nextToken();
                customAritcle.add(0, userName);
                dataAritcle.add(0, userData);
            } else {
                String userData = copyItems.get(i).getName();
                StringTokenizer stok = new StringTokenizer(userData, "/", false);
                String userName = "Farm "+stok.nextToken();
                customAritcle.add(userName);
                dataAritcle.add(userData);
            }
        }
//        for (int i=0;i<copyItems.size();i++) {
//            Log.d("농장 Name 스피너 목록 "+i, "name : " + customAritcle.get(i) + ", type : " + dataAritcle.get(i) + ", date : " + dateAritcle.get(i));
//        }
        return customAritcle;

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}



