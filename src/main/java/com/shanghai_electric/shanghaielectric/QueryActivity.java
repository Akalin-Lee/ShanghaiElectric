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

public class QueryActivity extends AppCompatActivity {
    private CheckBox isFuzzyQuery;
    private EditText fuzzyQuery;
    private Button query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        isFuzzyQuery = (CheckBox)findViewById(R.id.is_fuzzy_query);
        fuzzyQuery = (EditText)findViewById(R.id.fuzzy_query_text);
        query = (Button)findViewById(R.id.query);
        final KeyListener key = fuzzyQuery.getKeyListener();

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QueryActivity.this,RuleShowActivity.class);
                startActivity(intent);
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
