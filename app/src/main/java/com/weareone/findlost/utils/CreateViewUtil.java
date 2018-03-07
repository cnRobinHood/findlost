package com.weareone.findlost.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weareone.findlost.R;
import com.weareone.findlost.entities.RowItem;

import java.util.List;

public class CreateViewUtil {
    public static void createContentView(Context context, LinearLayout contentLayout, List<RowItem> rowItemList) {
        View childView;
        TextView keyTv;
        TextView valueTv;
        LayoutInflater layoutInflater;
        layoutInflater = LayoutInflater.from(context);
        for (int i = 0; i < rowItemList.size(); i++) {
            childView = layoutInflater.inflate(R.layout.userinfo_item, null);
            keyTv = childView.findViewById(R.id.tv_key);
            valueTv = childView.findViewById(R.id.tv_value);
            keyTv.setText(rowItemList.get(i).getKey());
            valueTv.setText(rowItemList.get(i).getValue());
            contentLayout.addView(childView);
        }
    }
}
