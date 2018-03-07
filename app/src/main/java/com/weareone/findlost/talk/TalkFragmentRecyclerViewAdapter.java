package com.weareone.findlost.talk;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weareone.findlost.R;

import java.util.List;

import cn.jpush.im.android.api.model.Conversation;

public class TalkFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "TalkFragmentRecyclerVie";
    private List<Conversation> mConversations;
    private Context mContext;

    public TalkFragmentRecyclerViewAdapter(List<Conversation> conversations, Context context) {
        this.mConversations = conversations;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(mContext).inflate(R.layout.talk_item_outside, parent, false);
        Log.d(TAG, "onCreateViewHolder: ");
        return new TalkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((TalkViewHolder) holder).mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TalkActivity.class);
                intent.putExtra("username", mConversations.get(position).getTitle());
                mContext.startActivity(intent);
            }
        });
        ((TalkViewHolder) holder).mTextView.setText(mConversations.get(position).getLatestText());
        Log.d(TAG, "onBindViewHolder: " + mConversations.get(position).getLatestText());
        ((TalkViewHolder) holder).username.setText(mConversations.get(position).getTitle());
        Log.d(TAG, "onBindViewHolder: " + mConversations.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return mConversations.size();
    }

    static class TalkViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLinearLayout;
        TextView username;
        TextView mTextView;

        public TalkViewHolder(View itemView) {
            super(itemView);
            mLinearLayout = itemView.findViewById(R.id.linear_talk);
            username = itemView.findViewById(R.id.tv_username);
            mTextView = itemView.findViewById(R.id.tv_text);
        }
    }
}
