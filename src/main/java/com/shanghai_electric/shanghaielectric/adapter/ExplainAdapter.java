package com.shanghai_electric.shanghaielectric.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shanghai_electric.shanghaielectric.R;
import com.shanghai_electric.shanghaielectric.json.ExplainItem;

import java.util.List;

/**
 * Created by Akalin on 2017/3/13.
 */

public class ExplainAdapter extends ArrayAdapter<ExplainItem> {
    private int resourceId;

    public ExplainAdapter(Context context, int textViewResourceId, List<ExplainItem> object){
        super(context,textViewResourceId,object);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExplainItem explainItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView number = (TextView)view.findViewById(R.id.number);
        TextView trouble_reason = (TextView)view.findViewById(R.id.trouble_reason);
        TextView method = (TextView)view.findViewById(R.id.method);
        number.setText(String.valueOf(explainItem.getNumber()));
        trouble_reason.setText(explainItem.getTrouble_reason());
        method.setText(explainItem.getMethod());
        return view;
    }
}
