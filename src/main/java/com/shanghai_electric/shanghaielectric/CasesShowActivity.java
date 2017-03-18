package com.shanghai_electric.shanghaielectric;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shanghai_electric.shanghaielectric.Service.DownloadService;
import com.shanghai_electric.shanghaielectric.adapter.FactorAdapter;
import com.shanghai_electric.shanghaielectric.json.CaseShow;
import com.shanghai_electric.shanghaielectric.json.InflunceFactor;
import com.shanghai_electric.shanghaielectric.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CasesShowActivity extends AppCompatActivity {
    public static final int SHOW_PDF = 1;

    LinearLayout more,detailInformation,hangUp;
    private String result = null;
    private CaseShow caseShow;
    private List<InflunceFactor> influnceFactors;
    private String attachment_url;
    private File file;
    String downloadUrl;

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

    private DownloadService.DownloadBinder downloadBinder;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case SHOW_PDF:
                    boolean show = (boolean)msg.obj;
                    if(show){
                    Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory("android.intent.category.DEFAULT");
//                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                    startActivity(intent);}
                    else{
                        Toast.makeText(CasesShowActivity.this, "文件下载中，请稍候", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

    };

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

        Intent intent2 = new Intent(this, DownloadService.class);
        startService(intent2); // 启动服务
        bindService(intent2, connection, BIND_AUTO_CREATE); // 绑定服务
//        if (ContextCompat.checkSelfPermission(CasesShowActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(CasesShowActivity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
//        }




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

        attachment_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadUrl = getString(R.string.url)+caseShow.getAttachment_url();
                long downloadedLength = 0;
                String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                file = new File(directory + fileName);
                if (file.exists()) {


                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url(downloadUrl)
                                        .build();
                                Response response = client.newCall(request).execute();
                                if (response != null && response.isSuccessful()) {
                                    long contentLength = response.body().contentLength();
                                    response.close();
                                    long fileLength = file.length();
                                    boolean isShow = contentLength==fileLength;
                                    Message message = new Message();
                                    message.what = SHOW_PDF;
                                    message.obj = isShow;
                                    handler.sendMessage(message);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    ).start();


                }else{
                    if (ContextCompat.checkSelfPermission(CasesShowActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CasesShowActivity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 2);
                    }else {
                        downloadBinder.startDownload(downloadUrl);
                    }
                }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拒绝权限将无法下载附件", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拒绝权限将无法下载附件", Toast.LENGTH_SHORT).show();
                }else{
                    downloadBinder.startDownload(downloadUrl);
                }
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
