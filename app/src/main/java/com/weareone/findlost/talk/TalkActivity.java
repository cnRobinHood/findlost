package com.weareone.findlost.talk;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.weareone.findlost.R;
import com.weareone.findlost.entities.Message;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.api.BasicCallback;

public class TalkActivity extends AppCompatActivity {
    private static final String TAG = "TalkActivity";
    private RecyclerView recyclerView;
    private Button button;
    private EditText editText;
    private List<Message> messages = new ArrayList<>();
    private TalkRecyclerViewAdapter adapter;
    private Intent mIntent;
    private String username;
    private Toolbar mToolbar;
    private LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutParams.flags);
        }
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mIntent = getIntent();
        username = mIntent.getStringExtra("username");
        JMessageClient.registerEventReceiver(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        actionBar.setTitle(username);
        Conversation conversation = JMessageClient.getSingleConversation(username);
        if (conversation == null) {
            Conversation.createSingleConversation(username);
            conversation = JMessageClient.getSingleConversation(username);
        }
        for (cn.jpush.im.android.api.model.Message m : conversation.getAllMessage()
                ) {
            Message message = new Message();
            if (m.getFromName().equals(username)) {
                message.setFlag(0);
            } else {
                message.setFlag(1);
            }
            message.setMessage(((TextContent) m.getContent()).getText());
            messages.add(message);
        }
        recyclerView = findViewById(R.id.lv_chat_dialog);
        button = findViewById(R.id.bt_send);
        editText = findViewById(R.id.et_message);
        manager = new LinearLayoutManager(this);

        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new TalkRecyclerViewAdapter(messages, this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        moveToPosition(manager, messages.size());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.setMessage(editText.getText().toString());
                message.setFlag(1);
                messages.add(message);
                adapter.notifyDataSetChanged();
                if (JMessageClient.getSingleConversation(username) != null) {
                    cn.jpush.im.android.api.model.Message textMessage = JMessageClient.getSingleConversation(username).createSendMessage(new TextContent(editText.getText().toString()));
                    JMessageClient.sendMessage(textMessage);
                    textMessage.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            Log.d(TAG, "gotResult send: " + i);
                        }
                    });
                }
                moveToPosition(manager, messages.size());
                Log.d(TAG, "onClick: " + messages.size());


                editText.setText("");
            }
        });


    }

    //    public void onEvent(MessageEvent event) {
//        cn.jpush.im.android.api.model.Message newMessage = event.getMessage();
//        Message message = new Message();
//        message.setMessage(((TextContent)newMessage.getContent()).getText());
//        Log.d(TAG, "onEvent: "+((TextContent)newMessage.getContent()).getText());
//        message.setFlag(2);
//         messages.add(message);
//        adapter.notifyDataSetChanged();
//    }
    public void onEventMainThread(MessageEvent event) {
        //do your own business
        cn.jpush.im.android.api.model.Message newMessage = event.getMessage();
        Message message = new Message();
        message.setMessage(((TextContent) newMessage.getContent()).getText());
        Log.d(TAG, "onEvent: " + ((TextContent) newMessage.getContent()).getText());
        message.setFlag(0);
        messages.add(message);
        adapter.notifyDataSetChanged();
        moveToPosition(manager, messages.size());
    }


    @Override
    protected void onResume() {
        super.onResume();

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

    public void moveToPosition(LinearLayoutManager manager, int n) {
        manager.scrollToPositionWithOffset(n, 0);
        manager.setStackFromEnd(true);
    }

}
