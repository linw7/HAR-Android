package com.example.lele.protoui;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {

    private static final int REQUESTCODE_PICK = 0;        // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;    // 图片裁切标记
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
    private static final String IMAGE_FILE_CUT_NAME = "avatarImage_cut.jpg";
    private String path;
    private String uname;

    private ImageView backBtn;
    private TextView barTitle;
    private PopMenu_upld_protrait upldProtrait;//上传用户头像的弹出框

    private TableRow row0;
    private TableRow row1;
    private TableRow row2;
    private TableRow row3;
    private TableRow row4;
    private TableRow row5;
    private TableRow row6;
    private TableRow row7;
    private TableRow row8;
    private TableRow row9;
    private TextView row1txt;
    private TextView row2txt;
    private TextView row3txt;
    private TextView row4txt;
    private TextView row5txt;
    private TextView row6txt;
    private TextView row7txt;
    private TextView row8txt;
    private TextView row9txt;

    private PopupWindow popupWindow;
    private ImageView userProtrait;
    private String title;
    private String titleString;
    private String[] choice;
    private int size;
    private int[] result;
    private Map<String, int[]> SettingData = new HashMap<String, int[]>();
    boolean[] checkedItems;
    private SharedPrefsUtil sharedPrefsUtil = new SharedPrefsUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        backBtn = (ImageView) findViewById(R.id.title_bar_back_btn);
        barTitle = (TextView) findViewById(R.id.title_bar_name);
        barTitle.setText(R.string.barTitle_setting);

        row0 = (TableRow) findViewById(R.id.more_page_row0);
        row1 = (TableRow) findViewById(R.id.more_page_row1);
        row2 = (TableRow) findViewById(R.id.more_page_row2);
        row3 = (TableRow) findViewById(R.id.more_page_row3);
        row4 = (TableRow) findViewById(R.id.more_page_row4);
        row5 = (TableRow) findViewById(R.id.more_page_row5);
        row6 = (TableRow) findViewById(R.id.more_page_row6);
        row7 = (TableRow) findViewById(R.id.more_page_row7);
        row8 = (TableRow) findViewById(R.id.more_page_row8);
        row9 = (TableRow) findViewById(R.id.more_page_row9);
        row1txt = (TextView) findViewById(R.id.setting_uname);
        row2txt = (TextView) findViewById(R.id.setting_nickname);
        row3txt = (TextView) findViewById(R.id.setting_sex);
        row4txt = (TextView) findViewById(R.id.setting_clt_len);
        row5txt = (TextView) findViewById(R.id.setting_sle_len);
        row6txt = (TextView) findViewById(R.id.setting_clt_tight);
        row7txt = (TextView) findViewById(R.id.setting_nckl_shp);
        row8txt = (TextView) findViewById(R.id.setting_stmp_sty);
        row9txt = (TextView) findViewById(R.id.setting_prf_color);

        userProtrait = (ImageView) findViewById(R.id.setting_user_protrait);

        setListener();

        SettingData = sharedPrefsUtil.readPreferencesSetting(SettingActivity.this, "DataBase");
        if (!SettingData.isEmpty()) {
            updateSettingAttr(SettingData);
        }
        uname = sharedPrefsUtil.readPreferences_login(SettingActivity.this, "DataBase");
        if (!uname.isEmpty()) {
            row1txt.setText(uname);
            row2txt.setText(uname);
        }
//        if (sharedPrefsUtil.readPreferences(SettingActivity.this, "DataBase")) {
//            setPicToView();
//        }

        setPicToView();
    }

    private void setListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.title_bar_back_btn:
                        //保存用户设置
                        sharedPrefsUtil.savePreferences(SettingActivity.this, "DataBase", SettingData);
                        finish();
                        break;
                    case R.id.more_page_row0:
                        upldProtrait = new PopMenu_upld_protrait(SettingActivity.this, itemsOnClick);
                        upldProtrait.showAtLocation(findViewById(R.id.settingLayout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        break;
                    case R.id.more_page_row1:
                        break;
                    case R.id.more_page_row2:
                        break;
                    case R.id.more_page_row3:
                        openDialog(3, row3txt);
                        break;
                    case R.id.more_page_row4:
                        openDialog(4, row4txt);
                        break;
                    case R.id.more_page_row5:
                        openDialog(5, row5txt);
                        break;
                    case R.id.more_page_row6:
                        openDialog(6, row6txt);
                        break;
                    case R.id.more_page_row7:
                        openDialog(7, row7txt);
                        break;
                    case R.id.more_page_row8:
                        openDialog(8, row8txt);
                        break;
                    case R.id.more_page_row9:
                        openDialog(9, row9txt);
                        break;
                }
            }
        };
        backBtn.setOnClickListener(listener);
        row0.setOnClickListener(listener);
        row1.setOnClickListener(listener);
        row2.setOnClickListener(listener);
        row3.setOnClickListener(listener);
        row4.setOnClickListener(listener);
        row5.setOnClickListener(listener);
        row6.setOnClickListener(listener);
        row7.setOnClickListener(listener);
        row8.setOnClickListener(listener);
        row9.setOnClickListener(listener);
    }

    private void openDialog(int key, TextView v) {
        switch (key) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                title = getString(R.string.setting_sex);
                titleString = "sex";
                choice = new String[]{getString(R.string.male), getString(R.string.female)};
                break;
            case 4:
                title = getString(R.string.setting_clt_len);
                titleString = "clt_len";
                choice = new String[]{getString(R.string.clt_len_l), getString(R.string.clt_len_m), getString(R.string.clt_len_s)};
                break;
            case 5:
                title = getString(R.string.setting_sle_len);
                titleString = "sle_len";
                choice = new String[]{getString(R.string.clt_len_l), getString(R.string.clt_len_m), getString(R.string.clt_len_s)};
                break;
            case 6:
                title = getString(R.string.setting_clt_tight);
                titleString = "clt_tight";
                choice = new String[]{getString(R.string.clt_tight_l), getString(R.string.clt_tight_m), getString(R.string.clt_tight_s)};
                break;
            case 7:
                title = getString(R.string.setting_nckl_shp);
                titleString = "nckl_shp";
                choice = new String[]{getString(R.string.nckl_shp_1), getString(R.string.nckl_shp_2), getString(R.string.nckl_shp_3),
                        getString(R.string.nckl_shp_4), getString(R.string.nckl_shp_5), getString(R.string.nckl_shp_6), getString(R.string.nckl_shp_7), getString(R.string.nckl_shp_8)};
                break;
            case 8:
                title = getString(R.string.setting_stmp_sty);
                titleString = "stmp_sty";
                choice = new String[]{getString(R.string.stmp_sty_pure), getString(R.string.stmp_sty_grid), getString(R.string.stmp_sty_dot),
                        getString(R.string.stmp_sty_flower), getString(R.string.stmp_sty_cross), getString(R.string.stmp_sty_vertical), getString(R.string.stmp_sty_charnum), getString(R.string.stmp_sty_dup)};
                break;
            case 9:
                title = getString(R.string.setting_prf_color);
                titleString = "prf_color";
                choice = new String[]{getString(R.string.clt_len_l), getString(R.string.clt_len_m), getString(R.string.clt_len_s)};
                break;
        }
        size = choice.length;
        //初始化int[] result，从SettingData中读取
        if (SettingData.get(titleString) != null) {
            result = SettingData.get(titleString);
        } else {
            result = new int[size];
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle(title);
        if (result.length != 0) {
            checkedItems = new boolean[size];
            for (int i = 0; i < size; i++) {
                if (result[i] == 1) {
                    checkedItems[i] = true;
                } else {
                    checkedItems[i] = false;
                }
            }
        } else {
            checkedItems = null;
        }

        builder.setMultiChoiceItems(choice, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    result[which] = 1;
                } else {
                    result[which] = 0;
                }
            }
        });

        builder.setPositiveButton(R.string.AlertDialog_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SettingData.put(titleString, result);
                updateSettingAttr(result, choice, v);//确认后，更新项目属性
            }
        });
//        builder.setNegativeButton(R.string.AlertDialog_cancel, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                return;
//            }
//        });
        builder.setCancelable(false);
        builder.show();
    }

    private void updateSettingAttr(int[] Array, String[] str, TextView v) {
        String attr = new String();
        int count = 0;
        for (int i = 0; i < str.length; i++) {
            if (Array[i] == 1) {
                count++;
                if (count <= 2) {
                    attr = attr + " " + str[i];
                } else {
                    attr = attr + getString(R.string.setting_plus);
                    break;
                }
            }
        }
        v.setText(attr);
    }

    private void updateSettingAttr(Map<String, int[]> Data) {
        for (Map.Entry<String, int[]> entry : Data.entrySet()) {

            String key = entry.getKey();
            int[] value = entry.getValue();

            switch (key) {
                case "sex":
                    choice = new String[]{getString(R.string.male), getString(R.string.female)};
                    TextView v = (TextView) findViewById(R.id.setting_sex);
                    updateSettingAttr(value, choice, v);
                    break;
                case "clt_len":
                    choice = new String[]{getString(R.string.clt_len_l), getString(R.string.clt_len_m), getString(R.string.clt_len_s)};
                    TextView v1 = (TextView) findViewById(R.id.setting_clt_len);
                    updateSettingAttr(value, choice, v1);
                    break;
                case "sle_len":
                    choice = new String[]{getString(R.string.clt_len_l), getString(R.string.clt_len_m), getString(R.string.clt_len_s)};
                    TextView v2 = (TextView) findViewById(R.id.setting_sle_len);
                    updateSettingAttr(value, choice, v2);
                    break;
                case "clt_tight":
                    choice = new String[]{getString(R.string.clt_tight_l), getString(R.string.clt_tight_m), getString(R.string.clt_tight_s)};
                    TextView v3 = (TextView) findViewById(R.id.setting_clt_tight);
                    updateSettingAttr(value, choice, v3);
                    break;
                case "nckl_shp":
                    choice = new String[]{getString(R.string.nckl_shp_1), getString(R.string.nckl_shp_2), getString(R.string.nckl_shp_3),
                            getString(R.string.nckl_shp_4), getString(R.string.nckl_shp_5), getString(R.string.nckl_shp_6), getString(R.string.nckl_shp_7), getString(R.string.nckl_shp_8)};
                    TextView v4 = (TextView) findViewById(R.id.setting_nckl_shp);
                    updateSettingAttr(value, choice, v4);
                    break;
                case "stmp_sty":
                    choice = new String[]{getString(R.string.stmp_sty_pure), getString(R.string.stmp_sty_grid), getString(R.string.stmp_sty_dot),
                            getString(R.string.stmp_sty_flower), getString(R.string.stmp_sty_cross), getString(R.string.stmp_sty_vertical), getString(R.string.stmp_sty_charnum), getString(R.string.stmp_sty_dup)};
                    TextView v5 = (TextView) findViewById(R.id.setting_stmp_sty);
                    updateSettingAttr(value, choice, v5);
                    break;
                case "prf_color":
                    break;
            }

        }
    }

    //太难看------------------------------------------------------
    class popupDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

    protected void initPopupWindow() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.pop, null);
        //内容，高度，宽度
        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //动画效果
        popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);

        popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_main, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //设置背景半透明
//        backgroundAlpha(0.5f);
        //关闭事件
        popupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
    //-----------------------------------------------------------

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            upldProtrait.dismiss();
            switch (v.getId()) {
                // 拍照
                case R.id.takePhotoBtn:
                    //下面这句指定调用相机拍照后的照片存储的路径
                    String status = Environment.getExternalStorageState();
                    if (status.equals(Environment.MEDIA_MOUNTED)) {
                        try {
                            //建立保存照片的路径
                            File dir = new File(Environment.getExternalStorageDirectory().toString() + "/ProtoUI/");
                            if (!dir.exists()) {
                                dir.mkdir();
                            }
                            Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File mPhotoFile = new File(dir, IMAGE_FILE_NAME);//保存拍摄的图片
                            Uri uri = getImageContentUri(SettingActivity.this, mPhotoFile);
                            takeIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                            takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(SettingActivity.this, "photo oom", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_PICK:// 直接从相册获取
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case REQUESTCODE_TAKE:// 调用相机拍照
//                Bitmap bm = (Bitmap) data.getExtras().get("data");
//                String status = Environment.getExternalStorageState();
//                if (status.equals(Environment.MEDIA_MOUNTED)) {
//                    try {
//                        File dir = new File(Environment.getExternalStorageDirectory().toString() + "/ProtoUI/");
//                        if (!dir.exists()) {
//                            dir.mkdir();
//                        }
//                        File mPhotoFile = new File(dir, IMAGE_FILE_NAME);//保存拍摄的图片
//                        FileOutputStream out = new FileOutputStream(mPhotoFile);
//                        if (bm.compress(Bitmap.CompressFormat.PNG, 90, out)) {
//                            out.flush();
//                            out.close();
//                        }
//
//                        Uri uri = Uri.fromFile(mPhotoFile);
//                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
//                        uri = getImageContentUri(this, mPhotoFile);
                try {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/ProtoUI/" + "avatarImage.jpg");
                    Uri uri = Uri.fromFile(temp);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    uri = getImageContentUri(this, temp);
                    startPhotoZoom(uri);
                } catch (Exception e) {

                }
                break;
            case REQUESTCODE_CUTTING:// 取得裁剪后的图片
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
            //将裁减的图片保存到手机中
            String status = Environment.getExternalStorageState();
            if (status.equals(Environment.MEDIA_MOUNTED)) {
                try {
                    File dir = new File(Environment.getExternalStorageDirectory().toString() + "/ProtoUI/");
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    File mPhotoFile = new File(dir, IMAGE_FILE_CUT_NAME);//保存拍摄的图片
                    FileOutputStream out = new FileOutputStream(mPhotoFile);
                    if (photo.compress(Bitmap.CompressFormat.PNG, 90, out)) {
                        out.flush();
                        out.close();
                    }
                    Uri uri = Uri.fromFile(mPhotoFile);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
//                    sharedPrefsUtil.savePreferences(SettingActivity.this, "DataBase", true);
                } catch (Exception e) {

                }
            }
            Drawable drawable = new BitmapDrawable(null, photo);
            userProtrait.setImageDrawable(drawable);
        }
    }

    private void setPicToView() {
        Bitmap avator;
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                File dir = new File(Environment.getExternalStorageDirectory().toString() + "/ProtoUI/");
                File mPhotoFile = new File(dir, IMAGE_FILE_CUT_NAME);//保存拍摄的图片
                path = dir.toString() + IMAGE_FILE_CUT_NAME;
                if (mPhotoFile.exists()) {
                    avator = BitmapFactory.decodeFile(path);
                    userProtrait.setImageBitmap(avator);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
    }

    //绝对路径转Uri
    public Uri getImageContentUri(Context context, java.io.File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}

