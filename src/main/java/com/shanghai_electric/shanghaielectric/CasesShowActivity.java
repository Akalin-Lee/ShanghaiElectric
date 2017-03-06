package com.shanghai_electric.shanghaielectric;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CasesShowActivity extends AppCompatActivity {
    private TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases_show);
        Intent intent = getIntent();
        String num = intent.getStringExtra("data");
        show = (TextView)findViewById(R.id.show);
        show.setText("这是第"+num+"个案例");
    }
    public static void actionStart(Context context,String data){
        Intent intent = new Intent (context,CasesShowActivity.class);
        intent.putExtra("data",data);
        context.startActivity(intent);
    }
}
