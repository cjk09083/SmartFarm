package cjk.smf.Fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cjk.smf.Adapters.MyMarkerView;
import cjk.smf.R;


public class DataTwoFragment extends Fragment {


    static public String[] data_buf = new String[100];
    phpDown task;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> timeList;

    private LineChart nh3Chart;
    private LineChart h2sChart;
    private LineChart coChart;
    private LineChart vocChart;
    private String id;
    private String name;
    private String type;
    private String date;
    private List<Entry> light_entries;
    private List<Entry> temp_entries;
    private List<Entry> humid_entries;
    private List<Entry> co2_entries;
    private TextView farm_name;
    private TextView farm_date;
    private Button daybtn;
    private Button weeksbtn;
    private Button monthbtn;
    private Button yearbtn;
    private Button selectbtn;
    private String Year;
    private String Month;
    private String Day;
    private String EndYear;
    private String EndMonth;
    private String EndDay;
    private String start_time;
    private String end_time;
    private String mJsonString = " ";
    private String lightlabel = "LUX";
    private String templabel = "Temp";
    private String humidlabel = "Humid";
    private String co2label = "Co2";
    private String descriptions = "";
    private Spinner spinner1;
    private TextView timetv;
    private int mode = 1;
    private int unit = 1;
    private int null_val = 1;

    private String now_weekDay = "";
    private String now_year = "";
    private String now_month = "";
    private String now_day = "";


    private String address1 = "http://cjk09083.cafe24.com/login/temphumid_year2_graph.php?device_id=1&year_time=19";
    private String day_address = "http://cjk09083.cafe24.com/login/temphumid_day_graph.php?device_id=1&year_time=19&month_time=6&day_time=2";
    private String weeks_address = "http://cjk09083.cafe24.com/login/temphumid_weeks_graph.php?device_id=1&year_time=19&month_time=6&day_time=2";
    private String month_address = "http://cjk09083.cafe24.com/login/temphumid_month_graph.php?device_id=1&year_time=19&month_time=6&day_time=2";
    private String month6_address = "http://cjk09083.cafe24.com/login/temphumid_6month_graph.php?device_id=1&year_time=19&month_time=6&day_time=2";
    private String year_address = "http://cjk09083.cafe24.com/login/temphumid_year_graph.php?device_id=1&year_time=19&month_time=6&day_time=2";
    private String select_address = "http://cjk09083.cafe24.com/login/temphumid_select_graph.php?year_time_start=19&month_time_start=04&day_time_start=27&year_time_end=19&month_time_end=07&day_time_end=10";

    ///기준 19년 04월 27일 ~ 19년 07월 10일
    public DataTwoFragment() {
        // required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            id = getArguments().getString("id");
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

        day_address = "http://cjk09083.cafe24.com/smf/temphumid_day_graph_eng.php?device_id="+id+"&year_time=" + now_year + "&month_time=" + now_month + "&day_time=" + now_day;
        weeks_address = "http://cjk09083.cafe24.com/smf/temphumid_weeks_graph.php?device_id="+id+"&year_time=" + now_year + "&month_time=" + now_month + "&day_time=" + now_day;
        month_address = "http://cjk09083.cafe24.com/smf/temphumid_month_graph2.php?device_id="+id+"&year_time=" + now_year + "&month_time=" + now_month + "&day_time=" + now_day;
        month6_address = "http://cjk09083.cafe24.com/smf/temphumid_6month_graph2.php?device_id="+id+"&year_time=" + now_year + "&month_time=" + now_month + "&day_time=" + now_day;


        Log.d("농장 현재 날짜", now_year + "년 " + now_month + "월 " + now_day + "일 " + now_weekDay + "요일");
        Log.d("농장 오늘 그래프", day_address );

        task = new phpDown();
        task.execute(day_address);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,

                             @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_two, container, false);
        nh3Chart = (LineChart) myView.findViewById(R.id.chart);
        h2sChart = (LineChart) myView.findViewById(R.id.chart2);
        coChart = (LineChart) myView.findViewById(R.id.chart3);
        vocChart = (LineChart) myView.findViewById(R.id.chart4);

        daybtn = (Button) myView.findViewById(R.id.dayBtn);
        weeksbtn = (Button) myView.findViewById(R.id.weeksBtn);
        monthbtn = (Button) myView.findViewById(R.id.monthBtn);
        yearbtn = (Button) myView.findViewById(R.id.yearBtn);
        timetv = (TextView) myView.findViewById(R.id.timeTV);

        farm_name = myView.findViewById(R.id.farm_name);
        farm_date = myView.findViewById(R.id.farm_date);  //각종 뷰 선언
        farm_name.setText(name);
        farm_date.setText(date);

        arrayList = new ArrayList<>();
        arrayList.add("광량");
        arrayList.add("온도");
        arrayList.add("습도");
        arrayList.add("이산화탄소");


        arrayAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.custom_spinner_item2,
                arrayList);
        spinner1 = (Spinner) myView.findViewById(R.id.spinner1);
        spinner1.setAdapter(arrayAdapter);

//        spinner1.setOnItemSelectedListener(new AdapterView.O>


        daybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mode = 1;
                Log.d("농장 오늘 그래프", day_address );
                task = new phpDown();
                task.execute(day_address);
            }
        });

        weeksbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mode = 2;
                Log.d("농장 이번주 그래프", weeks_address );
                task = new phpDown();
                task.execute(weeks_address);
            }
        });

        monthbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mode = 2;
                Log.d("농장 이번달 그래프", month_address );
                task = new phpDown();
                task.execute(month_address);
            }
        });
        yearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mode = 4;
                Log.d("농장 6개월 그래프", month6_address );
                task = new phpDown();
                //task.execute(year_address);
                task.execute(month6_address);
            }
        });

        return myView;
    }

    private void setnh3Data() {
        LineDataSet lineDataSet1 = new LineDataSet(light_entries, lightlabel);
        lineDataSet1.setLineWidth(2);
        lineDataSet1.setCircleRadius(6);

        lineDataSet1.setCircleColor(Color.parseColor("#F3BB71"));
        lineDataSet1.setCircleColorHole(Color.parseColor("#F3BB71"));
        lineDataSet1.setColor(Color.parseColor("#F3BB71"));

        lineDataSet1.setDrawCircleHole(true);
        lineDataSet1.setDrawCircles(true);
        lineDataSet1.setDrawHorizontalHighlightIndicator(false);
        lineDataSet1.setDrawHighlightIndicators(false);
        lineDataSet1.setDrawValues(false);

        lineDataSet1.setFillAlpha(255);
        lineDataSet1.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_set1);
        lineDataSet1.setFillDrawable(drawable);
        lineDataSet1.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                // change the return value here to better understand the effect
                // return 0;
                return nh3Chart.getAxisLeft().getAxisMinimum();
            }
        });


        MarkerView marker = new MyMarkerView(this.getActivity(), R.layout.content_marker_view);
        marker.setChartView(nh3Chart);
        nh3Chart.setMarker(marker);
        lineDataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER); //부드러운 차트
        LineData lineData = new LineData(lineDataSet1);
        nh3Chart.setData(lineData);

        XAxis xAxis = nh3Chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //Log.d("Login 농장 그래프 ", "x축 시간데이터 : "+(int) value % timeList.size()+"번");
                return timeList.get((int) value % timeList.size());
            }
        });

        YAxis yLAxis = nh3Chart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = nh3Chart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText(descriptions);

        nh3Chart.setDoubleTapToZoomEnabled(true);
        nh3Chart.setDrawGridBackground(false);
        nh3Chart.setDescription(description);
        nh3Chart.animateY(200, Easing.EasingOption.EaseInCubic);
        nh3Chart.invalidate();


    }

    private void seth2sData() {
        LineDataSet lineDataSet1 = new LineDataSet(temp_entries, templabel);
        lineDataSet1.setLineWidth(2);
        lineDataSet1.setCircleRadius(6);

        lineDataSet1.setCircleColor(Color.parseColor("#FE85A6"));
        lineDataSet1.setCircleColorHole(Color.parseColor("#FE85A6"));
        lineDataSet1.setColor(Color.parseColor("#FE85A6"));

        lineDataSet1.setDrawCircleHole(true);
        lineDataSet1.setDrawCircles(true);
        lineDataSet1.setDrawHorizontalHighlightIndicator(false);
        lineDataSet1.setDrawHighlightIndicators(false);
        lineDataSet1.setDrawValues(false);

        lineDataSet1.setFillAlpha(255);
        lineDataSet1.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_set2);
        lineDataSet1.setFillDrawable(drawable);
        lineDataSet1.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                // change the return value here to better understand the effect
                // return 0;
                return nh3Chart.getAxisLeft().getAxisMinimum();
            }
        });

        MarkerView marker = new MyMarkerView(this.getActivity(), R.layout.content_marker_view);
        marker.setChartView(h2sChart);
        h2sChart.setMarker(marker);
        lineDataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER); //부드러운 차트
        LineData lineData = new LineData(lineDataSet1);
        h2sChart.setData(lineData);

        XAxis xAxis = h2sChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return timeList.get((int) value % timeList.size());
            }
        });

        YAxis yLAxis = h2sChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = h2sChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText(descriptions);

        h2sChart.setDoubleTapToZoomEnabled(true);
        h2sChart.setDrawGridBackground(false);
        h2sChart.setDescription(description);
        h2sChart.animateY(200, Easing.EasingOption.EaseInCubic);
        h2sChart.invalidate();


    }

    private void setcoData() {
        LineDataSet lineDataSet1 = new LineDataSet(humid_entries, humidlabel);
        lineDataSet1.setLineWidth(2);
        lineDataSet1.setCircleRadius(6);

        lineDataSet1.setCircleColor(Color.parseColor("#7AECED"));
        lineDataSet1.setCircleColorHole(Color.parseColor("#7AECED"));
        lineDataSet1.setColor(Color.parseColor("#7AECED"));

        lineDataSet1.setDrawCircleHole(true);
        lineDataSet1.setDrawCircles(true);
        lineDataSet1.setDrawHorizontalHighlightIndicator(false);
        lineDataSet1.setDrawHighlightIndicators(false);
        lineDataSet1.setDrawValues(false);

        lineDataSet1.setFillAlpha(255);
        lineDataSet1.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_set3);
        lineDataSet1.setFillDrawable(drawable);
        lineDataSet1.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                // change the return value here to better understand the effect
                // return 0;
                return nh3Chart.getAxisLeft().getAxisMinimum();
            }
        });

        MarkerView marker = new MyMarkerView(this.getActivity(), R.layout.content_marker_view);
        marker.setChartView(coChart);
        coChart.setMarker(marker);
        lineDataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER); //부드러운 차트
        LineData lineData = new LineData(lineDataSet1);
        coChart.setData(lineData);

        XAxis xAxis = coChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return timeList.get((int) value % timeList.size());
            }
        });

        YAxis yLAxis = coChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = coChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText(descriptions);

        coChart.setDoubleTapToZoomEnabled(true);
        coChart.setDrawGridBackground(false);
        coChart.setDescription(description);
        coChart.animateY(200, Easing.EasingOption.EaseInCubic);
        coChart.invalidate();


    }

    private void setvocData() {
        LineDataSet lineDataSet1 = new LineDataSet(co2_entries, co2label);
        lineDataSet1.setLineWidth(2);
        lineDataSet1.setCircleRadius(6);

        lineDataSet1.setCircleColor(Color.parseColor("#B1DC71"));
        lineDataSet1.setCircleColorHole(Color.parseColor("#B1DC71"));
        lineDataSet1.setColor(Color.parseColor("#B1DC71"));

        lineDataSet1.setDrawCircleHole(true);
        lineDataSet1.setDrawCircles(true);
        lineDataSet1.setDrawHorizontalHighlightIndicator(false);
        lineDataSet1.setDrawHighlightIndicators(false);
        lineDataSet1.setDrawValues(false);

        lineDataSet1.setFillAlpha(255);
        lineDataSet1.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_set4);
        lineDataSet1.setFillDrawable(drawable);
        lineDataSet1.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                // change the return value here to better understand the effect
                // return 0;
                return nh3Chart.getAxisLeft().getAxisMinimum();
            }
        });

        MarkerView marker = new MyMarkerView(this.getActivity(), R.layout.content_marker_view);
        marker.setChartView(vocChart);
        vocChart.setMarker(marker);
        lineDataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER); //부드러운 차트
        LineData lineData = new LineData(lineDataSet1);
        vocChart.setData(lineData);

        XAxis xAxis = vocChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return timeList.get((int) value % timeList.size());
            }
        });

        YAxis yLAxis = vocChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = vocChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText(descriptions);

        vocChart.setDoubleTapToZoomEnabled(true);
        vocChart.setDrawGridBackground(false);
        vocChart.setDescription(description);
        vocChart.animateY(200, Easing.EasingOption.EaseInCubic);
        vocChart.invalidate();


    }

    private void showResult() {
        light_entries = new ArrayList<>();
        temp_entries = new ArrayList<>();
        humid_entries = new ArrayList<>();
        co2_entries = new ArrayList<>();
        timeList = new ArrayList<>();

        String TAG_JSON = "data";
        String TAG_ID = "temp";
        String TAG_ID2 = "humid";
        String TAG_ID3 = "co2";
        String TAG_ID4 = "lux";
        String TAG_TIME = "created_at";

        int daytime=0;
        int month=0;
        int day=0;
        int start_month=0;
        int start_day=0;
        int end_month=0;
        int end_day=0;
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            Log.d("Login 농장 그래프", "데이터 개수 jsonArry : " +jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String value = item.getString(TAG_ID4 + i);
                String value2 = item.getString(TAG_ID + i);
                String value3 = item.getString(TAG_ID2 + i);
                String value4 = item.getString(TAG_ID3 + i);
                String time = item.getString(TAG_TIME + i);


                if (value.equals("null")) { value = "0"; }
                float fvalue = Float.parseFloat(value);
                fvalue = Math.round(fvalue * 100);
                fvalue = fvalue / 100;

                if (value2.equals("null")) { value2 = "0";   }
                float fvalue2 = Float.parseFloat(value2);
                fvalue2 = Math.round(fvalue2 * 100);
                fvalue2 = fvalue2 / 100;

                if (value3.equals("null")) { value3 = "0";   }
                float fvalue3 = Float.parseFloat(value3);
                fvalue3 = Math.round(fvalue3 * 100);
                fvalue3 = fvalue3 / 100;

                if (value4.equals("null")) { value4 = "0";   }
                float fvalue4 = Float.parseFloat(value4);
                fvalue4 = Math.round(fvalue4 * 100);
                fvalue4 = fvalue4 / 100;


                //Log.d("Login 고객", "json 저장 결과 Float "+TAG_ID+i+" : "+fvalue);

                if(!time.startsWith("null")) {
                    if (i == 0) {
                        start_time = time.substring(2, 16);
                        null_val = 0;
                        Log.d("농장 그래프 시간 " + i, " start_time : " + start_time);
                    }


                    if (null_val == 1) {
                        start_time = time.substring(2, 16);
                        null_val = 0;
                    }
                    if(mode==4){
                        end_time = time.substring(23, 37);
                    }else {
                        end_time = time.substring(2, 16);
                    }
//                    Log.d("농장 그래프 시간 " + i, " end_time : " + time);

                        //end_time = time;

                    light_entries.add(new Entry(i, fvalue));
                    temp_entries.add(new Entry(i, fvalue2));
                    humid_entries.add(new Entry(i, fvalue3));
                    co2_entries.add(new Entry(i, fvalue4));
                    if (mode == 1) {
                        daytime= Integer.valueOf(time.substring(11, 13));
                        timeList.add(i, ""+daytime);
                    } else if(mode == 2){
                        month = Integer.valueOf(time.substring(5, 7));
                        day = Integer.valueOf(time.substring(8, 10));
                        timeList.add(i, month + "/" + day);
                    } else {
                        start_month = Integer.valueOf(time.substring(5, 7));
                        start_day = Integer.valueOf(time.substring(8, 10));
                        end_month = Integer.valueOf(time.substring(26, 28));
                        end_day = Integer.valueOf(time.substring(29, 31));
//                        timeList.add(i, start_month + "/" + start_day +"~"+end_month+"/"+end_day);
//                        timeList.add(i, start_month + "/" + start_day);
                        timeList.add(i, end_month + "/" + end_day );

                    }
                }else{
                    light_entries.add(new Entry(i, 0));
                    temp_entries.add(new Entry(i, 0));
                    humid_entries.add(new Entry(i, 0));
                    co2_entries.add(new Entry(i, 0));
                    if (mode == 1) {
                        daytime++;
                        timeList.add(i, ""+daytime);
                    } else if(mode == 2) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Integer.valueOf("20"+now_year), month-1, 1);
                        day++;
                        if(day>cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                            month++;
                            if(month==13){
                                month=1;
                            }
                            day=1;
                        }
                        timeList.add(i, month + "/" + day);
                    } else{
                        Calendar cal2 = Calendar.getInstance();
                        cal2.set(Integer.valueOf("20"+now_year), start_month-1, 1);
                        start_day++;
                        if(start_day>cal2.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                            start_month++;
                            if(start_month==13){
                                start_month=1;
                            }
                            start_day=1;
                        }

                        Calendar cal3 = Calendar.getInstance();
                        cal3.set(Integer.valueOf("20"+now_year), end_month-1, 1);
                        end_day++;
                        if(end_day>cal3.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                            end_month++;
                            if(end_month==13){
                                end_month=1;
                            }
                            end_day=1;
                        }
//                        timeList.add(i, start_month + "/" + start_day +"~"+end_month+"/"+end_day);
//                        timeList.add(i, start_month + "/" + start_day );
                        timeList.add(i, end_month + "/" + end_day );

                    }

                }
            }
//            Log.d("Login 농장 그래프", "데이터 개수 hn3: " + light_entries.size() + ", h2s : " + temp_entries.size() + ", co : " + humid_entries.size() + ", voc : " + co2_entries.size() + ", time : " + timeList.size());
            Log.d("농장 그래프 시간 " + timeList.size(), " end_time : " + end_time);

            timetv.setText("From "+start_time + "\n to " + end_time );
            setnh3Data();
            seth2sData();
            setcoData();
            setvocData();

        } catch (JSONException e) {

            Log.d("Login 농장", "showResult : ", e);
        }

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

                    data_buf = new String[300];
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
                        mJsonString = read_data;

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
            if (mJsonString.length() > 3) {
                showResult();
            }
            //Intent mainActivityIntent = new Intent(MainActivity.this, MainActivity.class);
            //startActivity(mainActivityIntent);

        }

    }

}



