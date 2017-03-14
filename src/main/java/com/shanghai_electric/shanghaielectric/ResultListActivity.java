package com.shanghai_electric.shanghaielectric;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.shanghai_electric.shanghaielectric.util.LogUtil;
import com.shanghai_electric.shanghaielectric.util.NetWorkUtil;

import org.json.JSONArray;
import org.json.JSONObject;

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
    private ListView listView;
    private String result;
    String[] data = null;
    int[] id = null;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case UPDAT_TEXT6:
                    String string = (String)msg.obj;

                    CasesShowActivity.actionStart(ResultListActivity.this, string);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);
        listView = (ListView) findViewById(R.id.result_list);
        Intent intent = getIntent();
        result = intent.getStringExtra("data");

        parseJson(result);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ResultListActivity.this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String case_id = String.valueOf(id[i]);
                if(!NetWorkUtil.isNetworkConnected(ResultListActivity.this)){
                    Toast.makeText(ResultListActivity.this, "网络不可用，请检查您的网络设置", Toast.LENGTH_SHORT).show();
                }else{

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
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
//                String num = String.valueOf(i);

            }
        });
    }

    public static void actionStart(Context context, String data) {
        Intent intent = new Intent(context, ResultListActivity.class);
        intent.putExtra("data", data);
        context.startActivity(intent);
    }

    private void parseJson(String str) {
        try {
            JSONArray jsonArray = new JSONArray(str);
            data = new String[jsonArray.length()];
            id = new int[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                data[i] = jsonObject.getString("name");
                id[i] = jsonObject.getInt("id");
            }
        } catch (Exception e) {
            Toast.makeText(ResultListActivity.this, "failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
