package com.shanghai_electric.shanghaielectric;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.shanghai_electric.shanghaielectric.util.DialogUtil;
import com.shanghai_electric.shanghaielectric.util.NetWorkUtil;
import com.shanghai_electric.shanghaielectric.util.OnItemSelectedListener;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadActivity extends AppCompatActivity {
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int UPLOAD_RESULT = 3;
    public static final int NOT_RESPONSE=-1;

    private Button uploadPhoto,casesUpload;
    private ImageView picture;
    private Uri imageUri;
    private TextView casesTimeView;
    private EditText casesNameView,troubleNameView,handlePeopleView,informationView,supplierView,arrangePeopleView,contactView,departmentView,reviewPeopleView;
    private String casesName,troubleName,casesTime,handlePeople,information,supplier,arrangePeople,contact,department,reviewPeople;
    private CustomDialog waitDialog;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case UPLOAD_RESULT:
                    String result = (String)msg.obj;
                    waitDialog.cancel();
                    Toast.makeText(UploadActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
//                    finish();
                    break;
                case NOT_RESPONSE:
                   waitDialog.cancel();
                    Toast.makeText(UploadActivity.this, "上传失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private void showDialogPick(final TextView timeText) {
        final StringBuffer time = new StringBuffer();
        //获取Calendar对象，用于获取当前时间
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        //实例化TimePickerDialog对象
        final TimePickerDialog timePickerDialog = new TimePickerDialog(UploadActivity.this, new TimePickerDialog.OnTimeSetListener() {
            //选择完时间后会调用该回调函数
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time.append(" " + hourOfDay + ":" + minute);
                //设置TextView显示最终选择的时间
                timeText.setText(time);
            }
        }, hour, minute, true);
        //实例化DatePickerDialog对象
        DatePickerDialog datePickerDialog = new DatePickerDialog(UploadActivity.this, new DatePickerDialog.OnDateSetListener() {
            //选择完日期后会调用该回调函数
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //因为monthOfYear会比实际月份少一月所以这边要加1
                time.append(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                timeText.setText(time);
                //选择完日期后弹出选择时间对话框
//                        timePickerDialog.show();
            }
        }, year, month, day);
        //弹出选择日期对话框
        datePickerDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        casesNameView=(EditText) findViewById(R.id.upload_cases_name);
        troubleNameView=(EditText)findViewById(R.id.upload_trouble_name);
        casesTimeView=(TextView)findViewById(R.id.upload_cases_time);
        handlePeopleView=(EditText)findViewById(R.id.upload_handle_people);
        informationView=(EditText)findViewById(R.id.upload_information);
        supplierView=(EditText)findViewById(R.id.upload_supplier);
        arrangePeopleView=(EditText)findViewById(R.id.upload_arrange_people);
        contactView=(EditText)findViewById(R.id.upload_contact);
        departmentView=(EditText)findViewById(R.id.upload_department);
        reviewPeopleView=(EditText)findViewById(R.id.upload_review_people);
        waitDialog = new CustomDialog(UploadActivity.this,R.style.CustomDialog);

        uploadPhoto = (Button)findViewById(R.id.uploadPhoto);
        picture = (ImageView)findViewById(R.id.picture) ;
        casesUpload = (Button)findViewById(R.id.cases_upload);

//        contactView.addTextChangedListener(new TextWatcher() {
//                    @Override
//                            public void afterTextChanged(Editable s) {
//                            // TODO Auto-generated method stub
//                        }
//
//                            @Override
//                            public void beforeTextChanged(CharSequence s, int start, int count,
//                                    int after) {
//                            // TODO Auto-generated method stub
//                        }
//
//                            @Override
//                            public void onTextChanged(CharSequence s, int start, int before,
//                                    int count) {
//                            String temp = s.toString();
//                            String addChar = temp.substring(start);
//                            String str = contactView.getText().toString();
//                            if (temp.length() == 3 || temp.length() == 8) {
//                                    if (TextUtils.isEmpty(addChar)) {
//                                            temp += "-";
//                                            contactView.setText(temp);
//                                            contactView.setSelection(temp.length());
//                                        }
//                                }
//                        }
//        });

        casesTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogPick(casesTimeView);
            }
        });

        casesUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(UploadActivity.this);
                dialog.setMessage("确定上传？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!NetWorkUtil.isNetworkConnected(UploadActivity.this)){
                            Toast.makeText(UploadActivity.this, "网络不可用，请检查您的网络设置", Toast.LENGTH_SHORT).show();
                        }else {
                            waitDialog.show();
                            casesName = casesNameView.getText().toString();
                            troubleName = troubleNameView.getText().toString();
                            casesTime = casesTimeView.getText().toString();
                            handlePeople = handlePeopleView.getText().toString();
                            information = informationView.getText().toString();
                            supplier = supplierView.getText().toString();
                            arrangePeople = arrangePeopleView.getText().toString();
                            contact = contactView.getText().toString();
                            department = departmentView.getText().toString();
                            reviewPeople = reviewPeopleView.getText().toString();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        final String url = getString(R.string.url)
                                                + getString(R.string.primary_path) + "/upload";
                                        OkHttpClient client = new OkHttpClient();
                                        RequestBody requestBody = new FormBody.Builder()
                                                .add("casesName", casesName)
                                                .add("troubleName", troubleName)
                                                .add("casesTime", casesTime)
                                                .add("handlePeople", handlePeople)
                                                .add("information", information)
                                                .add("supplier", supplier)
                                                .add("arrangePeople", arrangePeople)
                                                .add("contact", contact)
                                                .add("department", department)
                                                .add("reviewPeople", reviewPeople)
                                                .build();
                                        Request request = new Request.Builder()
                                                .url(url)
                                                .post(requestBody)
                                                .build();
                                        final Response response = client.newCall(request).execute();
                                        if (response.isSuccessful()) {
                                            String responseData = response.body().string();
                                            Message message = new Message();
                                            message.what = UPLOAD_RESULT;
                                            message.obj = responseData;
                                            handler.sendMessage(message);
                                        } else {
                                            Message message = new Message();
                                            message.what = NOT_RESPONSE;
                                            handler.sendMessage(message);

                                        }
                                    } catch (Exception e) {
                                        Message message = new Message();
                                        message.what = NOT_RESPONSE;
                                        handler.sendMessage(message);
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });





        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        WindowManager wm = getWindowManager();
        int mWidth = wm.getDefaultDisplay().getWidth();
        DialogUtil.showItemSelectDialog(UploadActivity.this,mWidth,onIllegalListener,"相机拍摄","从相册中选择");//可填添加任意多个Item呦  
    }

    private OnItemSelectedListener onIllegalListener=new OnItemSelectedListener(){
        @Override
        public void getSelectedItem(String content){
            switch (content){
                case "相机拍摄":
                    takePhoto();
                    break;
                case "从相册中选择":
                    chooseFromAlbum();
                    break;
                default:
                    break;
            }
        }
    };

    private void takePhoto(){
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = Uri.fromFile(outputImage);
        } else {
            imageUri = FileProvider.getUriForFile(UploadActivity.this, "com.example.cameraalbumtest.fileprovider", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private void chooseFromAlbum(){
        if (ContextCompat.checkSelfPermission(UploadActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UploadActivity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
        } else {
            openAlbum();
        }

    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(UploadActivity.this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(UploadActivity.this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(UploadActivity.this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
}
