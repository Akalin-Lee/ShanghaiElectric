package com.shanghai_electric.shanghaielectric;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shanghai_electric.shanghaielectric.util.LogUtil;
import com.shanghai_electric.shanghaielectric.util.MyApplication;
import com.shanghai_electric.shanghaielectric.util.NetWorkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Akalin on 2017/2/28.
 */

public class QueryFragment  extends Fragment{

    public static final int UPDAT_TEXT1=1;
    public static final int UPDAT_TEXT2=2;
    public static final int SHOW_ANSWER=3;
    public static final int NOT_RESPONSE=-1;
    public static final int LIST_NOT_RESPONSE=-2;

    private Spinner queryDevice,queryComponent;
    private Button queryQuery;
    private CheckBox isFuzzyQuery;
    private EditText fuzzyQuery;
    private TextView moreChoices;
//    private ProgressBar progressbar;
    private LinearLayout fuzzyQueryLayout;
    private CustomDialog dialog;

    String[] defaultString1 = new String[]{"请选择"};
    String[] defaultString2 = new String[]{"请先选择设备"};
    String[] arr = defaultString1;
    String[] arr1 = defaultString2;
    String device = "";
    String component ="";
    String fuzzyWord = null;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case UPDAT_TEXT1:
                    arr=(String[])msg.obj;
                    initSpinner1();
                    break;
                case UPDAT_TEXT2:
                    arr1=(String[])msg.obj;
                    initSpinner2();
                    break;
                case SHOW_ANSWER:
                    String ans = (String)msg.obj;
                    dialog.cancel();
//                    Toast.makeText(getContext(), ans, Toast.LENGTH_SHORT).show();
                    RuleShowActivity.actionStart(getActivity(),ans);
                    break;
                case NOT_RESPONSE:
                    dialog.cancel();
                    Toast.makeText(getActivity(), "获取查询结果失败", Toast.LENGTH_SHORT).show();
                    break;
                case LIST_NOT_RESPONSE:
                    dialog.cancel();
                    Toast.makeText(getActivity(), "获取查询列表失败", Toast.LENGTH_SHORT).show();
                    break;


            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.query_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dialog = new CustomDialog(getActivity(),R.style.CustomDialog);
        dialog.show();
        queryDevice = (Spinner)getActivity().findViewById(R.id.query_device);
        queryComponent = (Spinner)getActivity().findViewById(R.id.query_component);
        queryQuery = (Button)getActivity().findViewById(R.id.query_query);
        isFuzzyQuery = (CheckBox)getActivity().findViewById(R.id.is_fuzzy_query);
        fuzzyQuery = (EditText)getActivity().findViewById(R.id.fuzzy_query_text);
        moreChoices = (TextView) getActivity().findViewById(R.id.query_more_choices);
//        progressbar = (ProgressBar)getActivity().findViewById(R.id.progress_bar_query);
        fuzzyQueryLayout = (LinearLayout)getActivity().findViewById(R.id.fuzzy_query_layout);
        final KeyListener key = fuzzyQuery.getKeyListener();



        moreChoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),QueryActivity.class);
                startActivity(intent);
            }
        });


        isFuzzyQuery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(fuzzyQuery.getKeyListener()!=null){
                KeyListener key =fuzzyQuery.getKeyListener(); }
                if(b){
                    fuzzyQueryLayout.setVisibility(View.VISIBLE);
                    fuzzyQuery.setKeyListener(key);
                    fuzzyQuery.setFocusable(true);
                    fuzzyQuery.setFocusableInTouchMode(true);
                    fuzzyQuery.requestFocus();
                }else{
                    fuzzyQueryLayout.setVisibility(View.GONE);
                    fuzzyQuery.setText("");
                    fuzzyQuery.setFocusable(false);
                    fuzzyQuery.setKeyListener(null);
                }
            }
        });



        //发起查询的时候事件
        queryQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!NetWorkUtil.isNetworkConnected(getActivity())){
                    Toast.makeText(getContext(), "网络不可用，请检查您的网络设置", Toast.LENGTH_SHORT).show();
                }else {
                    fuzzyWord = fuzzyQuery.getText().toString();
//                    LogUtil.e("aaa","----"+fuzzyWord+"----");
                if((device.isEmpty() || component.isEmpty())&&(fuzzyWord.trim().isEmpty())){
                    Toast.makeText(getContext(), "请填写完整查询信息", Toast.LENGTH_SHORT).show();
                }else {
                    dialog.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                final String url = getString(R.string.url)
                                        +getString(R.string.primary_path)+"/rule";
                                OkHttpClient client = new OkHttpClient();
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("device",device)
                                        .add("component",component)
                                        .add("fuzzyWord",fuzzyWord)
                                        .add("troubleName","")
                                        .build();
                                Request request = new Request.Builder()
                                        .url(url)
                                        .post(requestBody)
                                        .build();
                                final Response response = client.newCall(request).execute();
                                if(response.isSuccessful()){
                                    String responseData = response.body().string();
                                    Message message = new Message();
                                    message.what = SHOW_ANSWER;
                                    message.obj = responseData;
                                    handler.sendMessage(message);
                                }else{
                                    LogUtil.e("aa","11111111111111");
                                    Message message = new Message();
                                    message.what = NOT_RESPONSE;
                                    handler.sendMessage(message);
                                }
                            } catch (Exception e) {
                                LogUtil.e("aa","222222222222221");
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


        //设备被选择时查询相应的部件
        queryDevice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!NetWorkUtil.isNetworkConnected(getActivity())){
                    Toast.makeText(getContext(), "网络不可用，无法获取部件列表", Toast.LENGTH_SHORT).show();
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

        //部件被选择的时候进行数据更新
        queryComponent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    component = (String)queryComponent.getSelectedItem();
                }else{
                    component="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        initString();



//        Toast.makeText(getContext(),"main Thread"
//                +queryDevice.getItemIdAtPosition(queryDevice.getSelectedItemPosition()),Toast.LENGTH_LONG).show();
//        queryDevice.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////                Spinner spinner=(Spinner)adapterView;
////                Toast.makeText(getContext(),"xxxx"+spinner.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> adapterView) {
//            Toast.makeText(getContext(),"没有改变的处理",Toast.LENGTH_LONG).show();
//        }
//    });
    }
    private void initSpinner1(){
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),R.layout.spinner_item,R.id.text_spinner,arr);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        queryDevice.setAdapter(arrayAdapter);
//        progressbar.setVisibility(View.GONE);
        dialog.cancel();
    }
    private void initSpinner2(){
        ArrayAdapter<String> arrayAdapter1=new ArrayAdapter<String>(getActivity(),R.layout.spinner_item,R.id.text_spinner,arr1);
        arrayAdapter1.setDropDownViewResource(R.layout.spinner_drop_down_item);
        queryComponent.setAdapter(arrayAdapter1);
        queryComponent.setEnabled(true);

    }
    private void initComponent(int component){
            String url1 = null;
            switch (component) {
                case 1:
                    url1 = getString(R.string.url)
                            + getString(R.string.primary_path) + "/device/turbine";
                    break;
                case 2:
                    url1 = getString(R.string.url)
                            + getString(R.string.primary_path) + "/device/generator";
                    break;
                case 3:
                    url1 = getString(R.string.url)
                            + getString(R.string.primary_path) + "/device/condenser";
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
                            message.what = UPDAT_TEXT2;
                            message.obj = tmp;
                            handler.sendMessage(message);

                        } else {
                            Message message = new Message();
                            message.what = LIST_NOT_RESPONSE;
                            handler.sendMessage(message);
                            LogUtil.e("aaaaaaa", "sibai");
                        }
                        //此处需要有网络异常的处理
                    } catch (Exception e) {
                        Message message = new Message();
                        message.what = LIST_NOT_RESPONSE;
                        handler.sendMessage(message);
                        LogUtil.e("aaaaaaa", "sibai");
                        e.printStackTrace();
                    }
                }
            }).start();

    }
    private void initString(){
        final String url = getString(R.string.url)
                +getString(R.string.primary_path)+"/device";
        if(!NetWorkUtil.isNetworkConnected(getActivity())){
            initSpinner1();
            dialog.cancel();
            Toast.makeText(getContext(), "网络不可用，无法获取查询选项", Toast.LENGTH_SHORT).show();
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
                            message.what = UPDAT_TEXT1;
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
                        //此处需要有网络异常的处理
                    }
                }
            }).start();
        }
    }


}
