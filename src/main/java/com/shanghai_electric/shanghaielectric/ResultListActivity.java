package com.shanghai_electric.shanghaielectric;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.shanghai_electric.shanghaielectric.util.CharacterParser;
import com.shanghai_electric.shanghaielectric.util.LogUtil;
import com.shanghai_electric.shanghaielectric.util.NetWorkUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResultListActivity extends AppCompatActivity {

    public static final int UPDAT_TEXT6=6;
    public static final int NOT_RESPONSE=-1;

    private ListView listView;
    private String result;
    private CustomDialog dialog;
    private EditText filterQuery;
    private Map<String,Integer> resultMap,tmpResultMap;

    String[] data = null;
    int[] id = null;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case UPDAT_TEXT6:
                    String string = (String)msg.obj;
                    dialog.cancel();
                    CasesShowActivity.actionStart(ResultListActivity.this, string);
                    break;
                case NOT_RESPONSE:
                    dialog.cancel();
                    Toast.makeText(ResultListActivity.this, "获取案例详情失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);
        listView = (ListView) findViewById(R.id.result_list);
        dialog = new CustomDialog(ResultListActivity.this,R.style.CustomDialog);
        filterQuery = (EditText)findViewById(R.id.filter_query);

        Intent intent = getIntent();
        result = intent.getStringExtra("data");

        parseJson(result);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ResultListActivity.this, R.layout.cases_list_item, R.id.text1,data);
        listView.setAdapter(adapter);

        filterQuery.addTextChangedListener(textWatcher);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String case_id = String.valueOf(id[i]);
                if(!NetWorkUtil.isNetworkConnected(ResultListActivity.this)){
                    Toast.makeText(ResultListActivity.this, "网络不可用，请检查您的网络设置", Toast.LENGTH_SHORT).show();
                }else{
                    dialog.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                final String url = getString(R.string.url)
                                        +getString(R.string.primary_path)+"/cases";
                                OkHttpClient client = new OkHttpClient();
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("case_id",case_id)
                                        .build();
                                Request request = new Request.Builder()
                                        .url(url)
                                        .post(requestBody)
                                        .build();
                                final Response response = client.newCall(request).execute();
                                if(response.isSuccessful()){
                                    String responseData = response.body().string();
                                    Message message = new Message();
                                    message.what = UPDAT_TEXT6;
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
//                String num = String.valueOf(i);

            }
        });
    }
    private TextWatcher textWatcher =  new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            CharacterParser characterParser = CharacterParser.getInstance();
            String filterWord = editable.toString();
            if(!filterWord.isEmpty()) {
                String filterSpell = characterParser.getSelling(filterWord);
                tmpResultMap = new LinkedHashMap<>();
//                for (String string : data) {
//                    String spell = characterParser.getSelling(string);
//                    if (spell.contains(filterSpell)) {
//                        tmpList.add(string);
//                    }
//                }
                for(int i=0;i<data.length;i++){
                    String spell = characterParser.getSelling(data[i]);
                    if (spell.contains(filterSpell)) {
                        tmpResultMap.put(data[i],id[i]);
                    }
                }

                data = new String[tmpResultMap.size()];
                id = new int[tmpResultMap.size()];
                Iterator iterator = tmpResultMap.keySet().iterator();
                int i = 0;
                while(iterator.hasNext()){
                    String key = (String)iterator.next();
                    data[i] = key;
                    id[i++] = tmpResultMap.get(key);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ResultListActivity.this, R.layout.cases_list_item, R.id.text1, data);
                listView.setAdapter(adapter);
            }else{
                tmpResultMap = resultMap;
                data = new String[tmpResultMap.size()];
                id = new int[tmpResultMap.size()];
                Iterator iterator = tmpResultMap.keySet().iterator();
                int i = 0;
                while(iterator.hasNext()){
                    String key = (String)iterator.next();
                    data[i] = key;
                    id[i++] = (int)tmpResultMap.get(key);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ResultListActivity.this, R.layout.cases_list_item, R.id.text1, data);

                listView.setAdapter(adapter);
            }
        }
    };

    public static void actionStart(Context context, String data) {
        Intent intent = new Intent(context, ResultListActivity.class);
        intent.putExtra("data", data);
        context.startActivity(intent);
    }

    private void parseJson(String str) {
        try {
            JSONArray jsonArray = new JSONArray(str);

            resultMap = new LinkedHashMap<>();
            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                data[i] = jsonObject.getString("name");
//                id[i] = jsonObject.getInt("id");
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                resultMap.put(jsonObject.getString("name"),jsonObject.getInt("id"));
            }


            tmpResultMap = resultMap;
            data = new String[tmpResultMap.size()];
            id = new int[tmpResultMap.size()];
            LogUtil.e("aaa","444444444444444444");
            Iterator iterator = tmpResultMap.keySet().iterator();
            int i = 0;
            LogUtil.e("aaa","45555555555555555555555");
            while(iterator.hasNext()){
                LogUtil.e("aaa","11111111111111111111");
                String key = (String)iterator.next();
                LogUtil.e("aaa","22222222222222222222222");
                data[i] = key;
                LogUtil.e("aaa","13333333333333333333333");
                id[i++] = tmpResultMap.get(key);
            }
            LogUtil.e("aaa","222222222222222222222222");
        } catch (Exception e) {
            LogUtil.e("aaa","0000000000000000");
            Toast.makeText(ResultListActivity.this, "failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
