package com.example.weatherforecast_demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private List<Integer> idList = new ArrayList<>();
    private List<Integer> pidList = new ArrayList<>();
    private List<String> city_nameList = new ArrayList<>();
    private List<String> city_codeList = new ArrayList<>();
    ArrayAdapter simpleAdapter;
    Button OK,MyConcern;
    EditText Research;
    ListView ProvinceList;

    private void parseJSONWithJSONObject(String jsonData){

        try{
            JSONArray jsonArray = new JSONArray(jsonData);
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Integer id = jsonObject.getInt("id");
                Integer pid = jsonObject.getInt("pid");
                String city_code = jsonObject.getString("city_code");
                String city_name = jsonObject.getString("city_name");
                if(pid == 0 ) {
                    idList.add(id);
                    pidList.add(pid);
                    city_codeList.add(city_code);
                    city_nameList.add(city_name);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getJson(String fileName, Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = context.getAssets().open(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OK = findViewById(R.id.ok);
        Research = findViewById(R.id.research);
        ProvinceList = findViewById(R.id.provincelist);
        OK.setOnClickListener(this);
        MyConcern = findViewById(R.id.my_concern);
        MyConcern.setOnClickListener(this);

        String responseData = getJson("city.json",this);
        parseJSONWithJSONObject(responseData);

        simpleAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,city_nameList);

        ProvinceList.setAdapter(simpleAdapter);
        ProvinceList = findViewById(R.id.provincelist);
        ProvinceList.setOnItemClickListener(new AdapterView.OnItemClickListener(){      //配置ArrayList点击按钮
            @Override
            public void  onItemClick(AdapterView<?> parent, View view , int position , long id){
                int tran = idList.get(position);
                Intent intent = new Intent(MainActivity.this, CityActivity.class);
                intent.putExtra("tran",tran);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ok:
                String researchcitycode = String.valueOf(Research.getText());
                if (researchcitycode.length() == 0){
                    Toast.makeText(this,"城市编码不能为空！",Toast.LENGTH_LONG).show();
                }
                else if(researchcitycode.length()>9){
                    Toast.makeText(this,"数字长度不能大于九位！",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                    intent.putExtra("trancitycode", researchcitycode);
                    startActivity(intent);
                }
                break;
            case R.id.my_concern:
                Intent intent = new Intent(MainActivity.this, ConcernList.class);
                startActivity(intent);
                break;
        }
    }
}