package com.weareone.findlost.talk;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weareone.findlost.R;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;


public class TalkFragment extends Fragment {
    private static final String TAG = "TalkFragment";
    private RecyclerView mRecyclerView;
    private List<Conversation> mConversations;

    public TalkFragment() {
    }

    public static TalkFragment newInstance() {
        TalkFragment fragment = new TalkFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_talk, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();

        mRecyclerView = view.findViewById(R.id.recycler_talk);
        if (mConversations == null) {
            mConversations = new ArrayList<>();
        }
        TalkFragmentRecyclerViewAdapter adapter = new TalkFragmentRecyclerViewAdapter(mConversations, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);

    }

    private void init() {
        mConversations = JMessageClient.getConversationList();
    }
}
