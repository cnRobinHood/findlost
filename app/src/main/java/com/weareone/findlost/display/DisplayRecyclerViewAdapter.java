package com.weareone.findlost.display;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weareone.findlost.R;
import com.weareone.findlost.entities.Item;
import com.weareone.findlost.talk.TalkActivity;
import com.weareone.findlost.utils.HttpUtil;

import java.util.List;

/**
 * Created by Rott on 2018/2/27.
 */

public class DisplayRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "DisplayRecyclerViewAdap";
    private List<Item> itemList;
    private Context mContext;


    public DisplayRecyclerViewAdapter(Context mContext, List<Item> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recy_item, parent, false);
        ItemHolder holder = new ItemHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ((ItemHolder) holder).username.setText(itemList.get(position).getUsername());
        ((ItemHolder) holder).remark.setText(itemList.get(position).getRemark());
        Log.d(TAG, "onBindViewHolder: " + itemList.get(position).getIamgeId());
        if (itemList.get(position).getIamgeId() != null && !"".equals(itemList.get(position).getIamgeId())) {
            String image2 = itemList.get(position).getIamgeId().substring(1);
            final String image = image2.split(",")[0];
            String image1 = image.replace(".png", "");
            String image3 = image1.replace(".jpg", "");
            Glide.with(mContext).load(HttpUtil.BASEUEL + "/api/image/download/" + image3).
                    into(((ItemHolder) holder).itemImg);
            ((ItemHolder) holder).userLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("item", itemList.get(position));
                    Intent intent = new Intent(mContext, DetialActivity.class);
                    intent.putExtra("item", bundle);
                    mContext.startActivity(intent);
                }
            });
            Log.d(TAG, "onBindViewHolder111: " + HttpUtil.BASEUEL + "/api/image/download/" + image1);
        }
        ((ItemHolder) holder).mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TalkActivity.class);
                intent.putExtra("username", itemList.get(position).getUsername());
                mContext.startActivity(intent);
            }
        });
        ((ItemHolder) holder).date.setText("发布于：" + itemList.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        LinearLayout recyclerLinear;
        TextView remark;
        TextView username;
        TextView date;
        ImageView itemImg;
        LinearLayout mLinearLayout;
        LinearLayout userLinearLayout;
        int count;

        public ItemHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.tv_date);
            userLinearLayout = itemView.findViewById(R.id.linear_detial);
            itemImg = itemView.findViewById(R.id.iv_lost);
            remark = itemView.findViewById(R.id.tv_remark);
            username = itemView.findViewById(R.id.tv_username);
            mLinearLayout = itemView.findViewById(R.id.linear_user);
            recyclerLinear = itemView.findViewById(R.id.linear_recycler);


        }
    }
}
