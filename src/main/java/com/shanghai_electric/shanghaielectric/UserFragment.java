package com.shanghai_electric.shanghaielectric;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.shanghai_electric.shanghaielectric.util.NetWorkUtil;

/**
 * Created by Akalin on 2017/2/28.
 */

public class UserFragment extends Fragment implements View.OnClickListener{

    private Button isWifi,isMobile,type;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isWifi = (Button)getActivity().findViewById(R.id.wifi);
        isMobile = (Button)getActivity().findViewById(R.id.mobile);
        type = (Button)getActivity().findViewById(R.id.type);

        isWifi.setOnClickListener(this);
        isMobile.setOnClickListener(this);
        type.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.wifi:
                boolean a = NetWorkUtil.isWifiConnected(getContext());
                String aa = String.valueOf(a);
                Toast.makeText(getContext(), "wifi is "+aa, Toast.LENGTH_SHORT).show();
                break;
            case R.id.mobile:
                boolean b = NetWorkUtil.isMobileConnected(getContext());
                String bb = String.valueOf(b);
                Toast.makeText(getContext(), "mobile is "+bb, Toast.LENGTH_SHORT).show();
                break;
            case R.id.type:
                boolean c = NetWorkUtil.isNetworkConnected(getContext());
                String cc = String.valueOf(c);
                Toast.makeText(getContext(), "type is "+cc, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
