package com.shanghai_electric.shanghaielectric.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shanghai_electric.shanghaielectric.R;
import com.shanghai_electric.shanghaielectric.json.InflunceFactor;

import java.util.List;

/**
 * Created by Akalin on 2017/3/13.
 */

public class FactorAdapter extends ArrayAdapter<InflunceFactor> {
    private int resourceId;

    public FactorAdapter(Context context, int textViewResourceId, List<InflunceFactor> object){
        super(context,textViewResourceId,object);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InflunceFactor influnceFactor = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView number = (TextView)view.findViewById(R.id.number);
        TextView description = (TextView)view.findViewById(R.id.description);
        TextView factor = (TextView)view.findViewById(R.id.factor);
        number.setText(influnceFactor.getNumber());
        description.setText(influnceFactor.getDescription());
        factor.setText(String.valueOf(influnceFactor.getFactor()));
        return view;
    }
}
