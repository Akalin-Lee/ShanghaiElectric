package com.shanghai_electric.shanghaielectric;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ResultListActivity extends AppCompatActivity {
    private ListView listView;
    private String result;
    String[] data = null;
    int[] id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);
        listView = (ListView) findViewById(R.id.result_list);
        Intent intent = getIntent();
        result = intent.getStringExtra("data");

        parseJson(result);


//        = intent.getStringArrayExtra("data");
//        String[] data = getData();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ResultListActivity.this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String num = String.valueOf(i);
                CasesShowActivity.actionStart(ResultListActivity.this, num);
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
