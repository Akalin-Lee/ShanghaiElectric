package com.shanghai_electric.shanghaielectric;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shanghai_electric.shanghaielectric.adapter.FactorAdapter;
import com.shanghai_electric.shanghaielectric.json.CaseShow;
import com.shanghai_electric.shanghaielectric.json.InflunceFactor;
import com.shanghai_electric.shanghaielectric.json.RuleShow;
import com.shanghai_electric.shanghaielectric.util.LogUtil;

import org.w3c.dom.Text;

import java.util.List;

public class CasesShowActivity extends AppCompatActivity {
    LinearLayout more,detailInformation,hangUp;
    private String result = null;
    private CaseShow caseShow;
    private List<InflunceFactor> influnceFactors;
    private String attachment_url;

    TextView case_name;
    TextView trouble;
    TextView device;
    TextView component;
    TextView case_time;
    TextView handle_people;
    TextView information;
    TextView supplier;
    TextView arrange_people;
    TextView contact;
    TextView department;
    TextView review_people;
    TextView attachment_name;
    ListView influnce_factor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases_show);


        case_name = (TextView)findViewById(R.id.case_name);
        trouble = (TextView)findViewById(R.id.trouble);
        device = (TextView)findViewById(R.id.device);
        component = (TextView)findViewById(R.id.component);
        case_time = (TextView)findViewById(R.id.case_time);
        handle_people = (TextView)findViewById(R.id.handle_people);
        information = (TextView)findViewById(R.id.information);
        supplier = (TextView)findViewById(R.id.supplier);
        arrange_people = (TextView) findViewById(R.id.arrange_people);
        contact = (TextView)findViewById(R.id.contact);
        department = (TextView)findViewById(R.id.department);
        review_people = (TextView)findViewById(R.id.review_people);
        attachment_name = (TextView)findViewById(R.id.attachment_name);
        influnce_factor = (ListView) findViewById(R.id.influnce_factor);
        more = (LinearLayout)findViewById(R.id.more);
        detailInformation = (LinearLayout)findViewById(R.id.detail_information);
        hangUp = (LinearLayout)findViewById(R.id.hang_up);




        Intent intent = getIntent();
        result = intent.getStringExtra("data");

        parseJsonWithGson(result);




        case_name.setText(caseShow.getCase_name());
        trouble.setText(caseShow.getTrouble());
        device.setText(caseShow.getDevice());
        component.setText(caseShow.getComponent());
        case_time.setText(caseShow.getCase_time());
        handle_people.setText(caseShow.getHandle_people());
        information.setText(caseShow.getInformation());
        supplier.setText(caseShow.getSupplier());
        arrange_people.setText(caseShow.getArrange_people());
        contact.setText(caseShow.getContact());
        department.setText(caseShow.getDepartment());
        review_people.setText(caseShow.getReview_people());
        attachment_name.setText(caseShow.getAttachment_name());


        influnceFactors = caseShow.getInflunce_factors();
        FactorAdapter adapter = new FactorAdapter(CasesShowActivity.this,R.layout.factor_item,influnceFactors);
        influnce_factor.setAdapter(adapter);
        MyListView.setListViewHeightBasedOnChildren(influnce_factor);


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
    }
    public static void actionStart(Context context,String data){
        Intent intent = new Intent (context,CasesShowActivity.class);
        intent.putExtra("data",data);
        context.startActivity(intent);
    }
    private void parseJsonWithGson(String string){
        Gson gson = new Gson();
        caseShow = gson.fromJson(string,CaseShow.class);
    }
}
