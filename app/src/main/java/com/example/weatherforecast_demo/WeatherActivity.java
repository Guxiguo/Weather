package com.example.weatherforecast_demo;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.fastjson.JSON;
import com.example.weatherforecast_demo.entity.App;
import com.example.weatherforecast_demo.entity.CityInfo;
import com.example.weatherforecast_demo.entity.Data;
import com.example.weatherforecast_demo.entity.Forecast;
import com.example.weatherforecast_demo.util.HttpUtil;
import com.example.weatherforecast_demo.util.MyDataBaseHelper;

import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView pm10Text;
    private TextView comfortText;
    private ImageView bingPicImg;
    String researchcitycode;

    private String city;
    private Forecast forecast0;
    private String cityId;
    private String parent;
    private String updateTime;
    private String time;
    private String date;
    private String message;
    private String status;
    private String shidu;
    private String pm25;
    private String pm10;
    private String quality;
    private String wendu;
    private String ganmao;
    private List<Forecast> forecasts;
    private String aqi0;

    int databaseid;
    String databasedata;
    int sign = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        titleCity = (TextView)findViewById(R.id.title_city);
        titleUpdateTime = (TextView)findViewById(R.id.title_update_time);
        degreeText = (TextView)findViewById(R.id.degree_text);
        weatherInfoText = (TextView)findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout)findViewById(R.id.forecast_layout);
        aqiText = (TextView)findViewById(R.id.aqi_text);
        pm25Text = (TextView)findViewById(R.id.pm25_text);
        pm10Text = (TextView)findViewById(R.id.pm10_text);
        comfortText = (TextView)findViewById(R.id.comfort_text);
        bingPicImg = (ImageView)findViewById(R.id.bing_pic_img);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String bingPic = prefs.getString("bing_pic",null);

        if(bingPic != null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else{
            loadBingPic();
        }

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        researchcitycode = extras.getString("trancitycode");

        MyDataBaseHelper dbHelper = new MyDataBaseHelper(this,"Weather.db",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();     //同上，获得可写文件
        Cursor cursor  = db.query("Weather",new String[]{"id","data"},"id=?",new String[]{researchcitycode+""},null,null,null);

        if(cursor.moveToNext()) {
            //逐行查找，得到匹配信息
            do {
                databaseid = cursor.getInt(cursor.getColumnIndex("id"));
                databasedata = cursor.getString(cursor.getColumnIndex("data"));
            } while (cursor.moveToNext());
        }
        int tranformat = 0;
        tranformat = Integer.parseInt(researchcitycode);

        if(databaseid ==  tranformat ){
            sign = 1;
            showResponse(databasedata);
        }else {
            sign = 0;
            sendRequestWithOkHttp();
        }

    }

    private void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://t.weather.itboy.net/api/weather/city/"+researchcitycode)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("data is", responseData);
                    showResponse(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }).start();
        loadBingPic();
    }

    /**
     * 加载必应每日一图
     *
     */
    private void loadBingPic(){
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    private void parseJSONWithFastJSON(String jsonData){
        if(jsonData.length()<100){
            Log.d("M","城市ID不存在");
            Toast.makeText(this,"城市ID不存在，请重新输入",Toast.LENGTH_LONG).show();
            WeatherActivity.this.setResult(RESULT_OK,getIntent());
            WeatherActivity.this.finish();
        }
        else {
            App app = JSON.parseObject(jsonData, App.class);
            time = app.getTime();//数据刷新时间

            message = app.getMessage();//当前状态
            status = app.getStatus();//状态号
            date = app.getDate();//当前日期

            CityInfo cityInfo = app.getCityInfo();
            parent = cityInfo.getParent();//所在省份
            city = cityInfo.getCity();//当前城市
            cityId = cityInfo.getCityId();//城市ID
            titleCity.setText(parent+city);//设置城市置顶标题

            updateTime = cityInfo.getUpdateTime();//更新时间
            titleUpdateTime.setText("更新时间:"+updateTime);//显示更新时间

            Data data = app.getData();
            shidu = data.getShidu();
            pm10 = data.getPm10();
            pm10Text.setText(pm10);
            pm25 = data.getPm25();
            pm25Text.setText(pm25);
            quality = data.getQuality();
            ganmao = data.getGanmao();
            comfortText.setText(ganmao + "\n刷新时间:"+time);
            wendu = data.getWendu();
            degreeText.setText(data.getWendu() + "℃");
            weatherInfoText.setText("湿度:"+shidu+" 空气质量:"+quality);

            //未来几天天气预报
            forecasts = data.getForecast();
            //当日AQI
            forecast0 = forecasts.get(0);
            aqi0 = forecast0.getAqi();
            aqiText.setText(aqi0);

            for (int i = 0;i < 7;i++){
                View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
                TextView dataText = (TextView)view.findViewById(R.id.date_text);
                TextView infoText = (TextView)view.findViewById(R.id.info_text);
                TextView maxText = (TextView)view.findViewById(R.id.max_text);
                TextView minText = (TextView)view.findViewById(R.id.min_text);
                dataText.setText(forecasts.get(i).getYmd());
                infoText.setText(forecasts.get(i).getType());
                maxText.setText(forecasts.get(i).getHigh());
                minText.setText(forecasts.get(i).getLow());
                forecastLayout.addView(view);
            }

            if (sign == 0) {
                MyDataBaseHelper dbHelper = new MyDataBaseHelper(this, "Weather.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("id", researchcitycode);
                values.put("data", jsonData);
                db.insert("Weather", null, values);
                Log.d("MainActivity", "数据库写入成功");
            } else if (sign == 1) {
                Log.d("数据库写入失败：", "数据已存在");

            } else {
                MyDataBaseHelper dbHelper = new MyDataBaseHelper(this, "Weather.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("id", researchcitycode);
                values.put("data", jsonData);
                db.update("Weather", values, "id=?", new String[]{researchcitycode + ""});
                Log.d("MainActivity", "数据库更新成功");

            }

        }
    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                parseJSONWithFastJSON(response);
            }
        });
    }

    @Override
    public void onClick(View v){
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MyDataBaseHelper dbHelper = new MyDataBaseHelper(this, "Concern.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (item.getItemId()) {
            case R.id.concern1:
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putString("citycode", researchcitycode);
                editor.apply();
                Toast.makeText(this, "关注成功！", Toast.LENGTH_LONG).show();
                ContentValues values = new ContentValues();
                values.put("city_code", researchcitycode);
                values.put("city_name", city);
                db.insert("Concern", null, values);
                Toast.makeText(this, "关注成功！", Toast.LENGTH_LONG).show();
                break;
            case R.id.refresh:
                sign = 3;
                sendRequestWithOkHttp();
                Log.d("MainActivity", "数据库刷新成功");
                break;
            case R.id.cancel_concern:
                db.delete("Concern","city_code=?",new String[]{researchcitycode+""});
                Toast.makeText(this,"取消关注成功",Toast.LENGTH_LONG).show();
                WeatherActivity.this.setResult(RESULT_OK,getIntent());
                WeatherActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
