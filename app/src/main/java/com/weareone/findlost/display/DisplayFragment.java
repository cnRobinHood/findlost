package com.weareone.findlost.display;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.weareone.findlost.R;
import com.weareone.findlost.entities.Item;
import com.weareone.findlost.utils.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DisplayFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final int SEARCHBYTYPE = 0;
    public static final int SEARCHBYWORD = 1;
    private static final String TAG = "DisplayFragment";
    private static String keyword;
    private static int flag;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private List<Item> itemList = new ArrayList<>();
    private boolean isLoading = false;
    private DisplayRecyclerViewAdapter adapter;
    private int current = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    adapter.notifyDataSetChanged();
                    isLoading = false;
                    break;
                case 2:
                    adapter.notifyDataSetChanged();
                    isLoading = false;
                    break;
            }
        }
    };


    public DisplayFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DisplayFragment newInstance(@Nullable String keyword1, @Nullable int flag1) {
        keyword = null;
        if (keyword1 != null) {
            keyword = keyword1;
            flag = flag1;
        }
        DisplayFragment fragment = new DisplayFragment();

        return fragment;
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //结束后停止刷新
                refreshLayout.setRefreshing(false);
            }
        }, 3000);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (keyword == null) {
                    loadData();
                    refreshLayout.setRefreshing(false);
                }

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display, container, false);
        refreshLayout = view.findViewById(R.id.swipe_ly);
        refreshLayout.setOnRefreshListener(this);
        recyclerView = view.findViewById(R.id.recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new DisplayRecyclerViewAdapter(getActivity(), itemList);

        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!isLoading && !recyclerView.canScrollVertically(1)) {
                    if (keyword == null) {
                        loadMore();
                    } else {
                        loadSearchData();
                    }

                    isLoading = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        if (keyword == null) {
            loadData();
        } else {
            loadSearchData();
        }

    }

    private void loadData() {
        itemList.clear();
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request
                .Builder()
                .post(new FormBody.Builder().build())//Post请求的参数传递
                .url(HttpUtil.BASEUEL + "/api/item/queryLasted")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null) {
//                    .runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getContext(), "加载成功", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    String data = response.body().string();
                    itemList.addAll(JSON.parseArray(data, Item.class));
                    Log.d(TAG, "onResponse: " + data);
                    Log.d(TAG, "onResponse: ");
                    mHandler.sendEmptyMessage(1);
                }

            }
        });
    }

    private void loadMore() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request
                .Builder()
                .post(new FormBody.Builder().add("id", itemList.get(itemList.size() - 1).getItemid().toString()).build())//Post请求的参数传递
                .url(HttpUtil.BASEUEL + "/api/item/queryNext")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: ");
                try {
                    if (response != null && response.code() == 200) {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(getContext(), "加载成功", Toast.LENGTH_SHORT).show();
//                            }
//                        });
                        itemList.addAll(JSON.parseArray(response.body().string(), Item.class));
                        mHandler.sendEmptyMessage(2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void loadSearchData() {
        String url = "";
        Request request = null;
        if (flag == SEARCHBYTYPE) {
            url = HttpUtil.BASEUEL + "/api/item/searchByType";
            request = new Request
                    .Builder()
                    .post(new FormBody.Builder().add("type", keyword).add("current", current + "").build())//Post请求的参数传递
                    .url(url)
                    .build();
        } else if (flag == SEARCHBYWORD) {
            url = HttpUtil.BASEUEL + "/api/item/searchByKey";
            request = new Request
                    .Builder()
                    .post(new FormBody.Builder().add("key", keyword).add("current", current + "").build())//Post请求的参数传递
                    .url(url)
                    .build();
        }
        current++;
        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null) {
//                    .runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getContext(), "加载成功", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    String data = response.body().string();
                    itemList.addAll(JSON.parseArray(data, Item.class));
                    Log.d(TAG, "onResponse: " + data);
                    Log.d(TAG, "onResponse: ");
                    mHandler.sendEmptyMessage(1);
                }

            }
        });

    }
}
