package com.weareone.findlost.display;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.weareone.findlost.R;
import com.weareone.findlost.entities.Item;
import com.weareone.findlost.utils.HttpUtil;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class DetialActivity extends AppCompatActivity {
    private TextView username;
    private TextView remark;
    private Button button;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private Item item;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detial);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutParams.flags);
        }
        username = findViewById(R.id.tv_username);
        remark = findViewById(R.id.tv_remark);
        button = findViewById(R.id.bt_change);
        imageView1 = findViewById(R.id.iv_1);
        imageView2 = findViewById(R.id.iv_2);
        imageView3 = findViewById(R.id.iv_3);
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("详情");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("item");
        if (bundle != null) {
            item = bundle.getParcelable("item");
        }
        if (item != null) {
            username.setText(item.getUsername());
            remark.setText(item.getRemark());
            if (item.getIamgeId() != null && !"".equals(item.getIamgeId())) {
                String image2 = item.getIamgeId().substring(1);
                String image[] = image2.split(",");
                if (image.length == 0) {
                    imageView1.setVisibility(View.GONE);
                    imageView2.setVisibility(View.GONE);
                    imageView3.setVisibility(View.GONE);
                } else if (image.length == 1) {
                    imageView2.setVisibility(View.GONE);
                    imageView3.setVisibility(View.GONE);
                } else if (image.length == 2) {
                    imageView3.setVisibility(View.GONE);
                }
                for (int i = 0; i < image.length; i++) {
                    String image1 = image[i].replace(".png", "");
                    String image3 = image1.replace(".jpg", "");
                    if (i == 0) {
                        Glide.with(getApplicationContext()).load(HttpUtil.BASEUEL + "/api/image/download/" + image3).
                                into(imageView1);
                    } else if (i == 1) {
                        Glide.with(getApplicationContext()).load(HttpUtil.BASEUEL + "/api/image/download/" + image3).
                                into(imageView2);
                    } else {
                        Glide.with(getApplicationContext()).load(HttpUtil.BASEUEL + "/api/image/download/" + image3).
                                into(imageView3);
                    }

                }


            }

        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient okHttpClient = new OkHttpClient();
                final Request request = new Request
                        .Builder()
                        .post(new FormBody.Builder().add("id", item.getItemid().toString()).build())//Post请求的参数传递
                        .url(HttpUtil.BASEUEL + "/api/item/changeStatus")
                        .build();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "认领成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

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
