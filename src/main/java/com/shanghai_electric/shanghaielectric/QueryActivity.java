package com.shanghai_electric.shanghaielectric;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class QueryActivity extends AppCompatActivity {
    private CheckBox isFuzzyQuery;
    private EditText fuzzyQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        isFuzzyQuery = (CheckBox)findViewById(R.id.is_fuzzy_query);
        fuzzyQuery = (EditText)findViewById(R.id.fuzzy_query_text);
        final KeyListener key = fuzzyQuery.getKeyListener();


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
