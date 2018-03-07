package com.weareone.findlost;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weareone.findlost.display.DisplayFragment;
import com.weareone.findlost.display.UserInfoActivity;
import com.weareone.findlost.entities.Userinfo;
import com.weareone.findlost.issue.IssueActivity;
import com.weareone.findlost.search.SearchActivity;
import com.weareone.findlost.talk.TalkFragment;
import com.weareone.findlost.utils.ActivityCollector;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private LinearLayout lostsLinear;
    private LinearLayout issueLinear;
    private LinearLayout talkLinear;
    private long oldTime = 0;
    private Userinfo userinfo;
    private TextView etUsername;
    private Toolbar toolbar;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);
        lostsLinear = findViewById(R.id.linear_losts);
        issueLinear = findViewById(R.id.linear_issue);
        talkLinear = findViewById(R.id.linear_talk);
        Bundle bundle = getIntent().getBundleExtra("userBundle");
        userinfo = bundle.getParcelable("userinfo");
        Log.d(TAG, "onCreate: " + userinfo);
        issueLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this).setItems(
                        new String[]{"发布招领", "发布失物"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Bundle userBundle = new Bundle();
                                userBundle.putParcelable("userinfo", userinfo);
                                Log.d(TAG, "handleMessage: " + userinfo.getUsername());
                                Intent intent = new Intent(MainActivity.this, IssueActivity.class);
                                intent.putExtra("userBundle", userBundle);
                                startActivity(intent);
                            }
                        }).show();


            }
        });
        talkLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbar.setTitle("交流");
                replaceFragment(TalkFragment.newInstance());
            }
        });
        lostsLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbar.setTitle("findlost");
                replaceFragment(DisplayFragment.newInstance(null, 0));
            }
        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        replaceFragment(DisplayFragment.newInstance(null, -1));
        NavigationView navigationView = findViewById(R.id.nav_view);
        View view = navigationView.inflateHeaderView(R.layout.nav_header_main);
        mLinearLayout = view.findViewById(R.id.linear_nav);
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable("userinfo", userinfo);
                intent.putExtra("userinfo", bundle1);
                startActivity(intent);
            }
        });
        etUsername = view.findViewById(R.id.tv_username);
        if (userinfo != null) {
            etUsername.setText(userinfo.getUsername());
        }

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (oldTime == 0) {
                oldTime = System.currentTimeMillis();
                Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();

            } else if (System.currentTimeMillis() - oldTime < 2000) {
                ActivityCollector.finishAllActivity();
            } else {
                oldTime = 0;
                Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_book) {
            toolbar.setTitle("书本");
            replaceFragment(DisplayFragment.newInstance("1", 0));
        } else if (id == R.id.nav_bag) {
            toolbar.setTitle("背包");
            replaceFragment(DisplayFragment.newInstance("4", 0));
        } else if (id == R.id.nav_money) {
            toolbar.setTitle("钱物");
            replaceFragment(DisplayFragment.newInstance("2", 0));
        } else if (id == R.id.nav_headgar) {
            toolbar.setTitle("首饰");
            replaceFragment(DisplayFragment.newInstance("3", 0));
        } else if (id == R.id.nav_phone) {
            toolbar.setTitle("电子产品");
            replaceFragment(DisplayFragment.newInstance("6", 0));
        } else if (id == R.id.nav_card) {
            toolbar.setTitle("证件");
            replaceFragment(DisplayFragment.newInstance("5", 0));
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            MainActivity.this.startActivityForResult(intent, 1);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 4) {
            String s = data.getStringExtra("keyword");
            toolbar.setTitle("搜索");
            replaceFragment(DisplayFragment.newInstance(s, 1));
        }
    }
}
