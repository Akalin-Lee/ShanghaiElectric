package com.shanghai_electric.shanghaielectric;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.shanghai_electric.shanghaielectric.util.LogUtil;
import com.shanghai_electric.shanghaielectric.util.NetWorkUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CasesActivity extends AppCompatActivity {

    public static final int UPDAT_TEXT4=4;
    public static final int UPDAT_TEXT5=5;
    public static final int UPDAT_TEXT7=7;
    public static final int SHOW_ANSWER2=6;
    public static final int NOT_RESPONSE=-1;
    public static final int LIST_NOT_RESPONSE=-2;


//    private CheckBox isFuzzyQuery;
    private EditText fuzzyQuery;
    private Button casesQuery;
    private Spinner plantNameView;
    private Spinner queryDevice,queryComponent;
    private EditText caseNameView;
    private EditText queryInformationView;
    private EditText querySupplierView;
    private EditText queryTroubleView;
    private CustomDialog dialog;

    String[] defaultString1 = new String[]{"请选择"};
    String[] defaultString2 = new String[]{"请先选择设备"};
    String[] arr = defaultString1;
    String[] arr1 = defaultString2;
    String[] arr2 = defaultString1;
    String device = "";
    String component = "";
    String fuzzyWord = null;

    String plantName = "";
    String casesName = null;
    String information = null;
    String supplier = null;
    String troubleName;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case UPDAT_TEXT4:
                    arr=(String[])msg.obj;
                    initSpinner1();
                    break;
                case UPDAT_TEXT5:
                    arr1=(String[])msg.obj;
                    initSpinner2();
                    break;
                case SHOW_ANSWER2:
                    String ans = (String)msg.obj;
                    dialog.cancel();
                    ResultListActivity.actionStart(CasesActivity.this,ans);
                    break;
                case UPDAT_TEXT7:
                    arr2=(String[])msg.obj;
                    initSpinner3();
                    break;
                case NOT_RESPONSE:
                    dialog.cancel();
                    Toast.makeText(CasesActivity.this, "获取查询结果失败", Toast.LENGTH_SHORT).show();
                    break;
                case LIST_NOT_RESPONSE:
                    dialog.cancel();
                    Toast.makeText(CasesActivity.this, "获取查询列表失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases);

        dialog = new CustomDialog(CasesActivity.this,R.style.CustomDialog);
        dialog.show();

        queryDevice = (Spinner)findViewById(R.id.query_device);
        queryComponent = (Spinner)findViewById(R.id.query_component);
//        isFuzzyQuery = (CheckBox)findViewById(R.id.is_fuzzy_query);
        fuzzyQuery = (EditText)findViewById(R.id.fuzzy_query_text);
        casesQuery = (Button)findViewById(R.id.cases_query);
        plantNameView = (Spinner)findViewById(R.id.plant_name);
        caseNameView = (EditText)findViewById(R.id.query_name);
        queryInformationView = (EditText)findViewById(R.id.query_information);
        querySupplierView = (EditText)findViewById(R.id.query_supplier);
        queryTroubleView = (EditText)findViewById(R.id.query_trouble);
        final KeyListener key = fuzzyQuery.getKeyListener();




        casesQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String[] data =  new String[]{"案例一","案例二","案例三","案例四","案例五","Fragment"};
//                ResultListActivity.actionStart(getActivity(),data);
                if(!NetWorkUtil.isNetworkConnected(CasesActivity.this)){
                    Toast.makeText(CasesActivity.this, "网络不可用，请检查您的网络设置", Toast.LENGTH_SHORT).show();
                }else {
                    fuzzyWord = fuzzyQuery.getText().toString();
                    casesName = caseNameView.getText().toString();
                    information = queryInformationView.getText().toString();
                    supplier = querySupplierView.getText().toString();
                    troubleName = queryTroubleView.getText().toString();

                    if((device.isEmpty() || component.isEmpty())
                            &&(plantName.isEmpty())
                            &&(fuzzyWord.trim().isEmpty())&&(troubleName.trim().isEmpty())
                            &&(casesName.trim().isEmpty())&&(troubleName.trim().isEmpty())){
                        Toast.makeText(CasesActivity.this, "请填写完整查询信息", Toast.LENGTH_SHORT).show();
                    }else {
                        dialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    final String url = getString(R.string.url)
                                            +getString(R.string.primary_path)+"/cases";
                                    OkHttpClient client = new OkHttpClient();
                                    RequestBody requestBody = new FormBody.Builder()
                                            .add("device",device)
                                            .add("component",component)
                                            .add("fuzzyWord",fuzzyWord)
                                            .add("caseName",casesName)
                                            .add("plantName",plantName)
                                            .add("information",information)
                                            .add("supplier",supplier)
                                            .add("troubleName",troubleName)
                                            .build();
                                    Request request = new Request.Builder()
                                            .url(url)
                                            .post(requestBody)
                                            .build();
                                    final Response response = client.newCall(request).execute();
                                    if(response.isSuccessful()){
                                        String responseData = response.body().string();
                                        Message message = new Message();
                                        message.what = SHOW_ANSWER2;
                                        message.obj = responseData;
                                        handler.sendMessage(message);
                                    }else{
                                        Message message = new Message();
                                        message.what = NOT_RESPONSE;
                                        handler.sendMessage(message);

                                    }
                                } catch (Exception e) {
                                    Message message = new Message();
                                    message.what = NOT_RESPONSE;
                                    handler.sendMessage(message);
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                }
            }
        });


//        isFuzzyQuery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(fuzzyQuery.getKeyListener()!=null){
//                    KeyListener key =fuzzyQuery.getKeyListener(); }
//                if(b){
//                    fuzzyQuery.setKeyListener(key);
//                    fuzzyQuery.setFocusable(true);
//                    fuzzyQuery.setFocusableInTouchMode(true);
//                    fuzzyQuery.requestFocus();
//                }else{
//                    fuzzyQuery.setText("");
//                    fuzzyQuery.setFocusable(false);
//                    fuzzyQuery.setKeyListener(null);
//                }
//            }
//        });
        //选择设备时及进行部件列表更新
        queryDevice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!NetWorkUtil.isNetworkConnected(CasesActivity.this)){
                    Toast.makeText(CasesActivity.this, "网络不可用，无法获取查询选项", Toast.LENGTH_SHORT).show();
                }else {
                    if (i != 0) {
                        initComponent(i);
                        device = (String)queryDevice.getSelectedItem();
                    }else{
                        device="";
                        arr1 = defaultString2;
                        initSpinner2();
                        queryComponent.setEnabled(false);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //选择部件时对数据进行更新
        queryComponent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    component = (String) queryComponent.getSelectedItem();
                } else {
                    component = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        plantNameView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    plantName = (String)plantNameView.getSelectedItem();
                }else{
                    plantName="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        initString();
        initPlant();
    }

    private void initSpinner1(){
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(CasesActivity.this,R.layout.spinner_item,R.id.text_spinner,arr);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        queryDevice.setAdapter(arrayAdapter);


    }
    private void initSpinner2(){
        ArrayAdapter<String> arrayAdapter1=new ArrayAdapter<String>(CasesActivity.this,R.layout.spinner_item,R.id.text_spinner,arr1);
        arrayAdapter1.setDropDownViewResource(R.layout.spinner_drop_down_item);
        queryComponent.setAdapter(arrayAdapter1);
        queryComponent.setEnabled(true);

    }
    private void initSpinner3(){
        ArrayAdapter<String> arrayAdapter2=new ArrayAdapter<String>(CasesActivity.this,R.layout.spinner_item,R.id.text_spinner,arr2);
        arrayAdapter2.setDropDownViewResource(R.layout.spinner_drop_down_item);
        plantNameView.setAdapter(arrayAdapter2);
        dialog.cancel();

    }

    private void initString(){
        final String url = getString(R.string.url)
                +getString(R.string.primary_path)+"/device";
        if(!NetWorkUtil.isNetworkConnected(CasesActivity.this)){
            initSpinner1();
            dialog.cancel();
            Toast.makeText(CasesActivity.this, "网络不可用，无法获取查询选项", Toast.LENGTH_SHORT).show();
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(url)
                                .build();
                        final Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            final String resoponseData = response.body().string();
                            JSONArray jsonArray = new JSONArray(resoponseData);
                            String[] tmp = new String[jsonArray.length() + 1];
                            tmp[0] = "请选择";
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                tmp[i + 1] = name;
                            }
                            Message message = new Message();
                            message.what = UPDAT_TEXT4;
                            message.obj = tmp;
                            handler.sendMessage(message);

                        } else {
                            Message message = new Message();
                            message.what = LIST_NOT_RESPONSE;
                            handler.sendMessage(message);
                            LogUtil.e("aaaaaaa", "sibai");
                        }
                    } catch (Exception e) {
                        Message message = new Message();
                        message.what = LIST_NOT_RESPONSE;
                        handler.sendMessage(message);
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void initPlant(){
        final String url = getString(R.string.url)
                +getString(R.string.primary_path)+"/plant";
        if(!NetWorkUtil.isNetworkConnected(CasesActivity.this)){
            initSpinner3();
//            dialog.cancel();
            Toast.makeText(CasesActivity.this, "网络不可用，无法获取查询选项", Toast.LENGTH_SHORT).show();
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(url)
                                .build();
                        final Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            final String resoponseData = response.body().string();
                            JSONArray jsonArray = new JSONArray(resoponseData);
                            String[] tmp = new String[jsonArray.length() + 1];
                            tmp[0] = "请选择";
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                tmp[i + 1] = name;
                            }
                            Message message = new Message();
                            message.what = UPDAT_TEXT7;
                            message.obj = tmp;
                            handler.sendMessage(message);

                        } else {
                            Message message = new Message();
                            message.what = LIST_NOT_RESPONSE;
                            handler.sendMessage(message);
                            LogUtil.e("aaaaaaa", "sibai");
                        }
                    } catch (Exception e) {
                        Message message = new Message();
                        message.what = LIST_NOT_RESPONSE;
                        handler.sendMessage(message);
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void initComponent(int component){
        String url1 = null;
        switch(component){
            case 1:
                url1 = getString(R.string.url)
                        +getString(R.string.primary_path)+"/device/turbine";
                break;
            case 2:
                url1 =getString(R.string.url)
                        +getString(R.string.primary_path)+"/device/generator";
                break;
            case 3:
                url1 =getString(R.string.url)
                        +getString(R.string.primary_path)+"/device/condenser";
                break;
            default:
                break;
        }
        final String url2 = url1;
//        progressbar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url2)
                            .build();
                    final Response response = client.newCall(request).execute();
                    if(response.isSuccessful()){
                        final String resoponseData = response.body().string();
                        JSONArray jsonArray = new JSONArray(resoponseData);
                        String[] tmp = new String[jsonArray.length()+1];
                        tmp[0]="请选择";
                        for(int i = 0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            tmp[i+1] = name;
                        }
                        Message message = new Message();
                        message.what = UPDAT_TEXT5;
                        message.obj = tmp;
                        handler.sendMessage(message);

                    }
                    else{
                        //此处应有网络异常处理逻辑
                        Message message = new Message();
                        message.what = LIST_NOT_RESPONSE;
                        handler.sendMessage(message);
                        LogUtil.e("aaaaaaa","sibai");
                    }
                } catch (Exception e) {
                    Message message = new Message();
                    message.what = LIST_NOT_RESPONSE;
                    handler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
