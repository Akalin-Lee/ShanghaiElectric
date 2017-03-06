package com.shanghai_electric.shanghaielectric;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout queryLayout,casesLayout,uploadLayout,userLayout;

    private Fragment queryFragment,casesFragment,uploadFragment,userFragment,currentFragment;

    private ImageView queryImg,casesImg,uploadImg,userImg;

    private TextView queryTv,casesTv,uploadTv,userTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testlayout);

        initUI();
        initTab();
    }

    private void initUI(){
        queryLayout = (RelativeLayout)findViewById(R.id.query);
        casesLayout = (RelativeLayout)findViewById(R.id.cases);
        uploadLayout = (RelativeLayout)findViewById(R.id.upload);
        userLayout = (RelativeLayout)findViewById(R.id.user);

        queryLayout.setOnClickListener(this);
        casesLayout.setOnClickListener(this);
        uploadLayout.setOnClickListener(this);
        userLayout.setOnClickListener(this);

        queryImg = (ImageView)findViewById(R.id.query_img);
        casesImg = (ImageView)findViewById(R.id.cases_img);
        uploadImg = (ImageView)findViewById(R.id.upload_img);
        userImg = (ImageView)findViewById(R.id.user_img);

        queryTv = (TextView) findViewById(R.id.query_text);
        casesTv = (TextView)findViewById(R.id.cases_text);
        uploadTv = (TextView)findViewById(R.id.upload_text);
        userTv = (TextView)findViewById(R.id.user_text);
    }

    private void initTab(){
        if(queryFragment == null){
            queryFragment = new QueryFragment();
        }
        if (!queryFragment.isAdded()) {
            // 提交事务
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_layout, queryFragment).commit();

            // 记录当前Fragment
            currentFragment = queryFragment;
            // 设置图片文本的变化
            queryImg.setImageResource(R.drawable.query_on);
            queryTv.setTextColor(getResources()
                    .getColor(R.color.bottomtab_on));
            casesImg.setImageResource(R.drawable.cases);
            casesTv.setTextColor(getResources().getColor(
                    R.color.bottomtab_off));
            uploadImg.setImageResource(R.drawable.upload);
            uploadTv.setTextColor(getResources().getColor(
                    R.color.bottomtab_off));
            userImg.setImageResource(R.drawable.user);
            userTv.setTextColor(getResources().getColor(
                    R.color.bottomtab_off));

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.query: //查询
                clickTab1Layout();
                break;
            case R.id.cases:
                clickTab2Layout();
                break;
            case R.id.upload:
                clickTab3Layout();
                break;
            case R.id.user:
                clickTab4Layout();
                break;
            default:
                break;
        }
    }
    /**
     * 点击第一个tab
     */
    private void clickTab1Layout() {
        if (queryFragment == null) {
            queryFragment = new QueryFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), queryFragment);

        // 设置底部tab变化
        queryImg.setImageResource(R.drawable.query_on);
        queryTv.setTextColor(getResources()
                .getColor(R.color.bottomtab_on));
        casesImg.setImageResource(R.drawable.cases);
        casesTv.setTextColor(getResources().getColor(
                R.color.bottomtab_off));
        uploadImg.setImageResource(R.drawable.upload);
        uploadTv.setTextColor(getResources().getColor(
                R.color.bottomtab_off));
        userImg.setImageResource(R.drawable.user);
        userTv.setTextColor(getResources().getColor(
                R.color.bottomtab_off));
    }

    private void clickTab2Layout() {
        if (casesFragment == null) {
            casesFragment = new CasesFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), casesFragment);

        // 设置底部tab变化
        queryImg.setImageResource(R.drawable.query);
        queryTv.setTextColor(getResources()
                .getColor(R.color.bottomtab_off));
        casesImg.setImageResource(R.drawable.cases_on);
        casesTv.setTextColor(getResources().getColor(
                R.color.bottomtab_on));
        uploadImg.setImageResource(R.drawable.upload);
        uploadTv.setTextColor(getResources().getColor(
                R.color.bottomtab_off));
        userImg.setImageResource(R.drawable.user);
        userTv.setTextColor(getResources().getColor(
                R.color.bottomtab_off));
    }

    private void clickTab3Layout() {
//        if (uploadFragment == null) {
//            uploadFragment = new UploadFragment();
//        }
//        addOrShowFragment(getSupportFragmentManager().beginTransaction(), uploadFragment);

        // 设置底部tab变化
//        queryImg.setImageResource(R.drawable.query);
//        queryTv.setTextColor(getResources()
//                .getColor(R.color.bottomtab_off));
//        casesImg.setImageResource(R.drawable.cases);
//        casesTv.setTextColor(getResources().getColor(
//                R.color.bottomtab_off));
//        uploadImg.setImageResource(R.drawable.upload_on);
//        uploadTv.setTextColor(getResources().getColor(
//                R.color.bottomtab_on));
//        userImg.setImageResource(R.drawable.user);
//        userTv.setTextColor(getResources().getColor(
//                R.color.bottomtab_off));
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setMessage("确定要上传案例吗？");
        dialog.setCancelable(true);
        dialog.setPositiveButton("是的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MainActivity.this,UploadActivity.class);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();

    }

    private void clickTab4Layout() {
        if (userFragment == null) {
            userFragment = new UserFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), userFragment);

        // 设置底部tab变化
        queryImg.setImageResource(R.drawable.query);
        queryTv.setTextColor(getResources()
                .getColor(R.color.bottomtab_off));
        casesImg.setImageResource(R.drawable.cases);
        casesTv.setTextColor(getResources().getColor(
                R.color.bottomtab_off));
        uploadImg.setImageResource(R.drawable.upload);
        uploadTv.setTextColor(getResources().getColor(
                R.color.bottomtab_off));
        userImg.setImageResource(R.drawable.user_on);
        userTv.setTextColor(getResources().getColor(
                R.color.bottomtab_on));
    }

    /**
     * 添加或者显示碎片
     *
     * @param transaction
     * @param fragment
     */
    private void addOrShowFragment(FragmentTransaction transaction,
                                   Fragment fragment) {
        if (currentFragment == fragment)
            return;

        if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(currentFragment)
                    .add(R.id.content_layout, fragment).commit();
        } else {
            transaction.hide(currentFragment).show(fragment).commit();
        }

        currentFragment = fragment;
    }
}