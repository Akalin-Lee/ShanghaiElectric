package com.shanghai_electric.shanghaielectric;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class CasesActivity extends AppCompatActivity {
    private CheckBox isFuzzyQuery;
    private EditText fuzzyQuery;
    private Button casesQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases);

        isFuzzyQuery = (CheckBox)findViewById(R.id.is_fuzzy_query);
        fuzzyQuery = (EditText)findViewById(R.id.fuzzy_query_text);
        casesQuery = (Button)findViewById(R.id.cases_query);
        final KeyListener key = fuzzyQuery.getKeyListener();




        casesQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String[] data =  new String[]{"案例一","案例二","案例三","案例四","案例五","Activity"};
//                ResultListActivity.actionStart(CasesActivity.this,data);
            }
        });


        isFuzzyQuery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(fuzzyQuery.getKeyListener()!=null){
                    KeyListener key =fuzzyQuery.getKeyListener(); }
                if(b){
                    fuzzyQuery.setKeyListener(key);
                    fuzzyQuery.setFocusable(true);
                    fuzzyQuery.setFocusableInTouchMode(true);
                    fuzzyQuery.requestFocus();
                }else{
                    fuzzyQuery.setText("");
                    fuzzyQuery.setFocusable(false);
                    fuzzyQuery.setKeyListener(null);
//                    fuzzyQuery.setFocusableInTouchMode(false);
                }
            }
        });
    }
}
