package com.shanghai_electric.shanghaielectric;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CasesShowActivity extends AppCompatActivity {
    LinearLayout more,detailInformation,hangUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases_show);
        more = (LinearLayout)findViewById(R.id.more);
        detailInformation = (LinearLayout)findViewById(R.id.detail_information);
        hangUp = (LinearLayout)findViewById(R.id.hang_up);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                more.setVisibility(View.GONE);
                detailInformation.setVisibility(View.VISIBLE);
            }
        });
        hangUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailInformation.setVisibility(View.GONE);
                more.setVisibility(View.VISIBLE);

            }
        });
//        Intent intent = getIntent();
//        String num = intent.getStringExtra("data");
    }
    public static void actionStart(Context context,String data){
        Intent intent = new Intent (context,CasesShowActivity.class);
        intent.putExtra("data",data);
        context.startActivity(intent);
    }
}
