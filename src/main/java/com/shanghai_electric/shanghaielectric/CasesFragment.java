package com.shanghai_electric.shanghaielectric;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Akalin on 2017/2/28.
 */

public class CasesFragment extends Fragment {
    private Spinner queryDevice,queryComponent;
    private Button queryQuery;
    private CheckBox isFuzzyQuery;
    private EditText fuzzyQuery;
    private TextView moreChoices;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cases_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        queryDevice = (Spinner)getActivity().findViewById(R.id.cases_device);
        queryComponent = (Spinner) getActivity().findViewById(R.id.cases_component);
        queryQuery = (Button)getActivity().findViewById(R.id.cases_query);
        isFuzzyQuery = (CheckBox)getActivity().findViewById(R.id.is_fuzzy_cases);
        fuzzyQuery = (EditText)getActivity().findViewById(R.id.fuzzy_cases_text);
        moreChoices = (TextView) getActivity().findViewById(R.id.cases_more_choices);
        final KeyListener key = fuzzyQuery.getKeyListener();

        moreChoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CasesActivity.class);
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




        queryQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] data =  new String[]{"案例一","案例二","案例三","案例四","案例五","Fragment"};
//                String deviceText = (String)queryDevice.getSelectedItem();
                ResultListActivity.actionStart(getActivity(),data);
//                Toast.makeText(getContext(),deviceText, Toast.LENGTH_SHORT).show();
            }
        });

        String arr[] = getResources().getStringArray(R.array.region);;
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,arr);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        queryDevice.setAdapter(arrayAdapter);

        String arr1[] = getResources().getStringArray(R.array.component);;
        ArrayAdapter<String> arrayAdapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,arr1);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        queryComponent.setAdapter(arrayAdapter1);

    }
}