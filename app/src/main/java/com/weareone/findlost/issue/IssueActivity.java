package com.weareone.findlost.issue;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.weareone.findlost.R;
import com.weareone.findlost.entities.Item;
import com.weareone.findlost.entities.Userinfo;
import com.weareone.findlost.utils.HttpUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class IssueActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "IssueActivity";
    int count = 0;
    private ImageView mImageView;
    private Uri imageUri;
    private String imagePath;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private TextView sureImage;
    private TextView backImage;//返回按钮
    private EditText itemRemark;//对失物的描述
    private Button btBook;
    private Button btMoney;
    private Button btHeadgar;
    private Button btCard;
    private Button btPhone;
    private Button btBag;
    private File imageFile;
    private List<String> paths = new ArrayList<>();
    private Map<String, Object> map = new HashMap();
    private int type;
    private Userinfo userinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        //mImageView = findViewById(R.id.camera);
        Bundle bundle = getIntent().getBundleExtra("userBundle");
        userinfo = bundle.getParcelable("userinfo");
        Log.d(TAG, "onCreate: " + userinfo);
        sureImage = findViewById(R.id.sure);//确认发布按钮
        backImage = findViewById(R.id.back);//返回按钮
        itemRemark = findViewById(R.id.item_remark);//对失物的描述
        imageView1 = findViewById(R.id.iv_1);
        imageView2 = findViewById(R.id.iv_2);
        imageView3 = findViewById(R.id.iv_3);
        btMoney = findViewById(R.id.bt_money);
        btPhone = findViewById(R.id.bt_phone);
        btHeadgar = findViewById(R.id.bt_headgar);
        btCard = findViewById(R.id.bt_card);
        btBag = findViewById(R.id.bt_bag);
        btBook = findViewById(R.id.bt_book);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        btBag.setOnClickListener(this);
        btBook.setOnClickListener(this);
        btCard.setOnClickListener(this);
        btHeadgar.setOnClickListener(this);
        btMoney.setOnClickListener(this);
        btPhone.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutParams.flags);
        }
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IssueActivity.this.finish();
            }
        });
        sureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item item = new Item();
                //item.setIamgeId(((File)map.get("photos")).getName());
                //Log.d(TAG, "onClick: "+((File)map.get("photos")).getName());
                item.setRemark(itemRemark.getText().toString());
                item.setType(type);
                if (userinfo != null) {
                    item.setUsername(userinfo.getUsername());
                    item.setCreatebyid(userinfo.getUserid());
                }
                map.put("itemJson", JSON.toJSONString(item));//修改
                uploadItem(map);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_1:
            case R.id.iv_2:
            case R.id.iv_3:
                new AlertDialog.Builder(IssueActivity.this).setItems(
                        new String[]{"拍摄照片", "从相册选择"},
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        takePhoto();
                                        break;
                                    case 1:
                                        getPhotoFromAlbum();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).show();
                break;
            case R.id.bt_book:
                btBag.setEnabled(true);
                btBook.setEnabled(false);
                btCard.setEnabled(true);
                btHeadgar.setEnabled(true);
                btPhone.setEnabled(true);
                btMoney.setEnabled(true);
                type = 1;
                break;
            case R.id.bt_money:
                btBag.setEnabled(true);
                btBook.setEnabled(true);
                btCard.setEnabled(true);
                btHeadgar.setEnabled(true);
                btPhone.setEnabled(true);
                btMoney.setEnabled(false);
                type = 2;
                break;
            case R.id.bt_headgar:
                btBag.setEnabled(true);
                btBook.setEnabled(true);
                btCard.setEnabled(true);
                btHeadgar.setEnabled(false);
                btPhone.setEnabled(true);
                btMoney.setEnabled(true);
                type = 3;
                break;
            case R.id.bt_bag:
                btBag.setEnabled(false);
                btBook.setEnabled(true);
                btCard.setEnabled(true);
                btHeadgar.setEnabled(true);
                btPhone.setEnabled(true);
                btMoney.setEnabled(true);
                type = 4;
                break;
            case R.id.bt_card:
                btBag.setEnabled(true);
                btBook.setEnabled(true);
                btCard.setEnabled(false);
                btHeadgar.setEnabled(true);
                btPhone.setEnabled(true);
                btMoney.setEnabled(true);
                type = 5;
                break;
            case R.id.bt_phone:
                btBag.setEnabled(true);
                btBook.setEnabled(true);
                btCard.setEnabled(true);
                btHeadgar.setEnabled(true);
                btPhone.setEnabled(false);
                btMoney.setEnabled(true);
                type = 6;
                break;
        }
    }

    private void takePhoto() {
        imageFile = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
        // 如果存在就删了重新创建
        try {
            if (imageFile.exists()) {
                imageFile.delete();
            }
            imageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(IssueActivity.this, "com.weareone.findlost.fileprovider", imageFile);

        } else {
            imageUri = Uri.fromFile(imageFile);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 设置相片的输出uri为刚才转化的imageUri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        // 开启相机程序，设置requestCode为TOKE_PHOTO
        startActivityForResult(intent, 1);

    }

    private void getPhotoFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "选择文件"), 0);
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), "亲，木有文件管理器啊-_-!!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    if (count == 0) {
                        Glide.with(this).load(imageUri).into(imageView1);
                        Glide.with(this).load(R.drawable.camera).into(imageView2);
                        Log.d(TAG, "onActivityResult: " + imageUri);
                        paths.add(imageFile.getAbsolutePath());

                        imageUri = null;
                        count++;
                    } else if (count == 1) {
                        Glide.with(this).load(imageUri).into(imageView2);
                        Glide.with(this).load(R.drawable.camera).into(imageView3);
                        imageUri = null;
                        paths.add(imageFile.getAbsolutePath());


                        count++;
                    } else if (count == 2) {
                        Glide.with(this).load(imageUri).into(imageView2);
                        imageUri = null;
                        paths.add(imageFile.getAbsolutePath());


                        count++;
                    } else {
                        Toast.makeText(getApplicationContext(), "最多添加三张", Toast.LENGTH_SHORT);
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT);
                }
                break;
            case 0:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        handleImageOnKitKat(data);
                    } else {
                        Toast.makeText(getApplicationContext(), "亲，你取消了文件选择-_-!!", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    private void handleImageOnKitKat(Intent data) {
        Uri uri = data.getData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    //Log.d(TAG, uri.toString());
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    //Log.d(TAG, uri.toString());
                    Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(docId));
                    imagePath = getImagePath(contentUri, null);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                //Log.d(TAG, "content: " + uri.toString());
                imagePath = getImagePath(uri, null);
            }
        }
        if (count == 0) {
            Glide.with(this).load(uri).into(imageView1);
            Glide.with(this).load(R.drawable.camera).into(imageView2);
            Log.d(TAG, "onActivityResult: " + imageUri);
            imageUri = null;
            paths.add(imagePath);
            Log.d(TAG, "handleImageOnKitKat: " + imagePath);
            count++;
        } else if (count == 1) {
            Glide.with(this).load(uri).into(imageView2);
            Glide.with(this).load(R.drawable.camera).into(imageView3);
            imageUri = null;
            paths.add(imagePath);
            count++;
        } else if (count == 2) {
            Glide.with(this).load(uri).into(imageView3);
            imageUri = null;
            paths.add(imagePath);
            count++;
        } else {
            Toast.makeText(getApplicationContext(), "最多添加三张", Toast.LENGTH_SHORT);
        }


    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public void uploadItem(Map<String, Object> param) {

        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (String path : paths
                ) {
            File file = new File(path);
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), file);
            bodyBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + "photos" + "\";filename=\"" + file.getName() + "\""), fileBody);//删除了multipart/
        }
        for (Map.Entry<String, Object> entry : param.entrySet()) {
//            if (entry.getValue() instanceof File) {
//                File file = (File) entry.getValue();
//                if (file != null)
//                    Log.i(TAG, file.getName() + "存在");//打印在上传中的文件名
//                RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), file);
//                bodyBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\";filename=\"" + file.getName() + "\""), fileBody);//删除了multipart/
//            } else
            if (entry.getValue() instanceof String) {
                //RequestBody jsonBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"),entry.getKey()+"="+entry.getValue().toString());


            }
        }
        bodyBuilder.addFormDataPart("username", userinfo.getUsername());
        bodyBuilder.addFormDataPart("createbyid", userinfo.getUserid());
        bodyBuilder.addFormDataPart("type", new Integer(type).toString());
        bodyBuilder.addFormDataPart("remark", itemRemark.getText().toString());

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(HttpUtil.BASEUEL + "/api/item/add")
                .post(bodyBuilder.build())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "upload fail", Toast.LENGTH_SHORT);
                    }
                });
                Log.d(TAG, "fail: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "upload success", Toast.LENGTH_SHORT);

                        IssueActivity.this.finish();
                    }
                });
                Log.d(TAG, "success: " + response.body().string());
            }

        });


    }
}
