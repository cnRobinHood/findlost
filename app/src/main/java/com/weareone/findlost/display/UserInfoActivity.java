package com.weareone.findlost.display;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.weareone.findlost.R;
import com.weareone.findlost.entities.RowItem;
import com.weareone.findlost.entities.Userinfo;
import com.weareone.findlost.utils.CreateViewUtil;

import java.util.ArrayList;
import java.util.List;


public class UserInfoActivity extends AppCompatActivity {
    private LinearLayout contentLl;
    private List<RowItem> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("个人信息");
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        Userinfo userInfo = intent.getBundleExtra("userinfo").getParcelable("userinfo");


        contentLl = findViewById(R.id.ll_content);
        mList = new ArrayList<RowItem>();
        mList.add(new RowItem("用户名", userInfo.getUsername()));
        if (userInfo.getSex() == 0) {
            mList.add(new RowItem("性别", "男"));
        } else {
            mList.add(new RowItem("性别", "女"));
        }

        mList.add(new RowItem("真实姓名", userInfo.getUserrealname()));
        mList.add(new RowItem("手机号", userInfo.getPhonenum()));
        mList.add(new RowItem("qq号", userInfo.getQq()));
        CreateViewUtil.createContentView(this, contentLl, mList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
