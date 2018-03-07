package com.weareone.findlost.talk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weareone.findlost.R;
import com.weareone.findlost.entities.Message;

import java.util.List;

/**
 * Created by asus on 2018/2/28.
 */

public class TalkRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> messages;
    private Context mContext;

    public TalkRecyclerViewAdapter(List<Message> messages, Context mContext) {
        this.messages = messages;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_dialog_left_item, parent, false);
            return new MessageLeftHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_dialog_right_item, parent, false);
            return new MessageRightHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getFlag();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MessageLeftHolder) {
            ((MessageLeftHolder) holder).remark.setText(messages.get(position).getMessage());
        } else {
            ((MessageRightHolder) holder).remark.setText(messages.get(position).getMessage());

        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageLeftHolder extends RecyclerView.ViewHolder {
        TextView remark;

        public MessageLeftHolder(View itemView) {
            super(itemView);
            remark = itemView.findViewById(R.id.tv_name);
        }
    }

    static class MessageRightHolder extends RecyclerView.ViewHolder {
        TextView remark;

        public MessageRightHolder(View itemView) {
            super(itemView);
            remark = itemView.findViewById(R.id.tv_chat_me_message);

        }
    }
}
