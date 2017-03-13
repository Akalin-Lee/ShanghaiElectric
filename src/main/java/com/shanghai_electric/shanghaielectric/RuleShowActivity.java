package com.shanghai_electric.shanghaielectric;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shanghai_electric.shanghaielectric.json.ExplainItem;
import com.shanghai_electric.shanghaielectric.json.RuleShow;

import java.util.List;

public class RuleShowActivity extends AppCompatActivity {
    String result = null;
    RuleShow ruleShow;
    List<ExplainItem> trouble_explain;

    TextView trouble_name;
    TextView device;
    TextView component;
    TextView explain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_show);
        trouble_name = (TextView)findViewById(R.id.trouble_name);
        device = (TextView)findViewById(R.id.device);
        component = (TextView)findViewById(R.id.component);
        explain = (TextView)findViewById(R.id.explain);


        Intent intent = getIntent();
        result = intent.getStringExtra("result");
        parseJsonWithGson(result);
        trouble_explain = ruleShow.getTrouble_explain();
        String str = "device:"+ruleShow.getDevice()+"\n"
                +"component:"+ruleShow.getComponent()+"\n"
                +"trouble+name"+ruleShow.getTrouble_name()+"\n"
                +"explainLengrth:"+trouble_explain.size();

        trouble_name.setText(ruleShow.getTrouble_name());
        device.setText(ruleShow.getDevice());
        component.setText(ruleShow.getComponent());
        explain.setText(ruleShow.getFuzzy_word());

        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public static void actionStart(Context context,String data){
        Intent intent = new Intent(context,RuleShowActivity.class);
        intent.putExtra("result",data);
        context.startActivity(intent);
    }

    private void parseJsonWithGson(String res){
        Gson gson = new Gson();
        ruleShow = gson.fromJson(res,RuleShow.class);
    }
}
