package com.example.lele.protoui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitiateActivity extends AppCompatActivity {
    private TextView barTitle;
    private ImageView backBtn;
    private ImageView initYes;
    private ImageView initNo;
    private LinearLayout scene_init;
    private LinearLayout scene_1;
    private LinearLayout scene_2;
    private TextView scene_1_btn;
    private TextView scene_2_btn_back;
    private TextView scene_2_btn_next;
    private TextView scene_1_txt;
    private TextView scene_2_txt;
    private TextView scene_2_title;
    private ImageView scene_1_male;
    private ImageView scene_1_female;
    private GridView scene_2_grid;
    //各种时装属性
    private int[] res;
    private String[] attr;
    private int[] result;//暂时保存当且scene下的选择
    private Map<String, int[]> UploadData = new HashMap<String, int[]>();
    //gridview分辨率
    private int length = 150;
    private int size;
    private float density;
    //色板
    private HorizontalScrollView scroll_view;
    private int r, g, b = 0;
    //最终上传SharedPreferences
    private SharedPrefsUtil sharedPrefsUtil = new SharedPrefsUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiate);

        barTitle = (TextView) findViewById(R.id.title_bar_name);
        barTitle.setText(R.string.init_bar_title);
        backBtn = (ImageView) findViewById(R.id.title_bar_back_btn);
        initYes = (ImageView) findViewById(R.id.init_yes);
        initNo = (ImageView) findViewById(R.id.init_no);
        scene_init = (LinearLayout) findViewById(R.id.init_scene_init);
        scene_1 = (LinearLayout) findViewById(R.id.init_scene_1);
        scene_2 = (LinearLayout) findViewById(R.id.init_scene_2);
        scroll_view = (HorizontalScrollView) findViewById(R.id.scroll_view);

        DisplayMetrics dm = new DisplayMetrics();
        InitiateActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        density = dm.density;

        setInitSceneListener();
    }

    private void setInitSceneListener() {
        View.OnClickListener initSceneListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.init_yes:
                        scene1();
                        break;
                    case R.id.init_no:
                        AlertDialog.Builder builder = new AlertDialog.Builder(InitiateActivity.this);
                        builder.setTitle(R.string.init_alertdialog_title);
                        builder.setMessage(R.string.init_alertdialog_content);
                        builder.setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(InitiateActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });
                        builder.setNegativeButton(R.string.AlertDialog_no, null);
                        builder.show();
                        break;
                    case R.id.title_bar_back_btn:
                        finish();
                        break;
                    default:
                        break;
                }
            }
        };
        backBtn.setOnClickListener(initSceneListener);
        initYes.setOnClickListener(initSceneListener);
        initNo.setOnClickListener(initSceneListener);
    }

    private void scene1() {
        scene_init.setVisibility(View.INVISIBLE);
        setVisibility(1);
        scene_1.setVisibility(View.VISIBLE);
        scene_1_btn = (TextView) findViewById(R.id.scene_1_btn);
        scene_1_txt = (TextView) findViewById(R.id.scene_1_txt);
        scene_1_male = (ImageView) findViewById(R.id.scene_1_male);
        scene_1_female = (ImageView) findViewById(R.id.scene_1_female);
        barTitle.setText(this.getString(R.string.init_bar_title) + "(1/7)");
        setScene1Listener();
    }

    private void setScene1Listener() {
        result = new int[2];
        View.OnClickListener scene1Listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.scene_1_btn:
                        UploadData.put("sex", result);
                        scene2();
                        break;
                    case R.id.scene_1_male:
                        scene_1_txt.setText(R.string.male);
                        scene_1_male.setPressed(true);
                        scene_1_female.setPressed(false);
                        result[0] = 1;
                        result[1] = 0;
                        break;
                    case R.id.scene_1_female:
                        scene_1_txt.setText(R.string.female);
                        scene_1_male.setPressed(false);
                        scene_1_female.setPressed(true);
                        result[1] = 1;
                        result[0] = 0;
                        break;
                    default:
                        break;
                }
            }
        };
        scene_1_btn.setOnClickListener(scene1Listener);
        scene_1_male.setOnClickListener(scene1Listener);
        scene_1_female.setOnClickListener(scene1Listener);
    }

    private void scene2() {
        setVisibility(2);
        scene_2_btn_next = (TextView) findViewById(R.id.scene_2_btn_next);
        scene_2_btn_back = (TextView) findViewById(R.id.scene_2_btn_back);
        scene_2_txt = (TextView) findViewById(R.id.scene_2_txt);
        scene_2_grid = (GridView) findViewById(R.id.scene_2_grid);
        scene_2_title = (TextView) findViewById(R.id.scene_2_title);
        barTitle.setText(this.getString(R.string.init_bar_title) + "(2/7)");
        scene_2_title.setText(R.string.setting_clt_len);
        setScene2Listener();
        setGridView("clt_len");
    }

    private void setScene2Listener() {
        View.OnClickListener scene2Listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.scene_2_btn_next:
                        UploadData.put("clt_len", result);
                        scene3();
                        break;
                    case R.id.scene_2_btn_back:
                        UploadData.put("clt_len", result);
                        scene1();
                        break;
                    default:
                        break;
                }
            }
        };
        scene_2_btn_next.setOnClickListener(scene2Listener);
        scene_2_btn_back.setOnClickListener(scene2Listener);
    }

    private void scene3() {
        barTitle.setText(this.getString(R.string.init_bar_title) + "(3/7)");
        scene_2_title.setText(R.string.setting_sle_len);
        setScene3Listener();
        setGridView("sle_len");
    }

    private void setScene3Listener() {
        View.OnClickListener scene3Listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.scene_2_btn_next:
                        UploadData.put("sle_len", result);
                        scene4();
                        break;
                    case R.id.scene_2_btn_back:
                        UploadData.put("sle_len", result);
                        scene2();
                        break;
                    default:
                        break;
                }
            }
        };
        scene_2_btn_next.setOnClickListener(scene3Listener);
        scene_2_btn_back.setOnClickListener(scene3Listener);
    }

    private void scene4() {
        barTitle.setText(this.getString(R.string.init_bar_title) + "(4/7)");
        scene_2_title.setText(R.string.setting_clt_tight);
        setScene4Listener();
        setGridView("clt_tight");
    }

    private void setScene4Listener() {
        View.OnClickListener scene4Listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.scene_2_btn_next:
                        UploadData.put("clt_tight", result);
                        scene5();
                        break;
                    case R.id.scene_2_btn_back:
                        UploadData.put("clt_tight", result);
                        scene3();
                        break;
                    default:
                        break;
                }
            }
        };
        scene_2_btn_next.setOnClickListener(scene4Listener);
        scene_2_btn_back.setOnClickListener(scene4Listener);
    }

    private void scene5() {
        barTitle.setText(this.getString(R.string.init_bar_title) + "(5/7)");
        scene_2_title.setText(R.string.setting_nckl_shp);
        setScene5Listener();
        setGridView("nckl_shp");
    }

    private void setScene5Listener() {
        View.OnClickListener scene5Listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.scene_2_btn_next:
                        UploadData.put("nckl_shp", result);
                        scene6();
                        break;
                    case R.id.scene_2_btn_back:
                        UploadData.put("nckl_shp", result);
                        scene4();
                        break;
                    default:
                        break;
                }
            }
        };
        scene_2_btn_next.setOnClickListener(scene5Listener);
        scene_2_btn_back.setOnClickListener(scene5Listener);
    }

    private void scene6() {
        barTitle.setText(this.getString(R.string.init_bar_title) + "(6/7)");
        scene_2_title.setText(R.string.setting_stmp_sty);
        setScene6Listener();
        setGridView("stmp_sty");
    }

    private void setScene6Listener() {
        View.OnClickListener scene6Listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.scene_2_btn_next:
                        UploadData.put("stmp_sty", result);
                        scene7();
                        break;
                    case R.id.scene_2_btn_back:
                        UploadData.put("stmp_sty", result);
                        scene5();
                        break;
                    default:
                        break;
                }
            }
        };
        scene_2_btn_next.setOnClickListener(scene6Listener);
        scene_2_btn_back.setOnClickListener(scene6Listener);
    }

    private void scene7() {
        barTitle.setText(this.getString(R.string.init_bar_title) + "(7/7)");
        scene_2_title.setText(R.string.setting_prf_color);
        scene_2_txt.setText("");
        scene_2_btn_next.setText(R.string.succeed);
        scroll_view.setVisibility(View.INVISIBLE);
        setScene7Listener();
    }

    private void setScene7Listener() {
        View.OnClickListener scene7Listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.scene_2_btn_next:
                        //完成初始化，将信息存储在sharedpreferences中

                        result = new int[3];
                        result[0] = r;
                        result[1] = g;
                        result[2] = b;
                        UploadData.put("prf_color", result);

                        sharedPrefsUtil.savePreferences(InitiateActivity.this, "DataBase", UploadData);

                        //进入主界面
                        Toast.makeText(InitiateActivity.this, R.string.init_succeed, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(InitiateActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.scene_2_btn_back:
                        scroll_view.setVisibility(View.VISIBLE);
                        scene6();
                        scene_2_btn_next.setText(R.string.next);
                        break;
                    default:
                        break;
                }
            }
        };
        scene_2_btn_next.setOnClickListener(scene7Listener);
        scene_2_btn_back.setOnClickListener(scene7Listener);
    }

    private void setGridView(String type) {
        switch (type) {
            case "clt_len":
                res = new int[]{R.drawable.clt_l, R.drawable.clt_m, R.drawable.clt_s};
                attr = new String[]{getString(R.string.clt_len_l), getString(R.string.clt_len_m), getString(R.string.clt_len_s)};
                break;
            case "sle_len":
                res = new int[]{R.drawable.sle_l, R.drawable.sle_m, R.drawable.sle_s};
                attr = new String[]{getString(R.string.clt_len_l), getString(R.string.clt_len_m), getString(R.string.clt_len_s)};
                break;
            case "clt_tight":
                res = new int[]{R.drawable.tight_l, R.drawable.tight_m, R.drawable.tight_s};
                attr = new String[]{getString(R.string.clt_tight_l), getString(R.string.clt_tight_m), getString(R.string.clt_tight_s)};
                break;
            case "nckl_shp":
                res = new int[]{R.drawable.shp_1, R.drawable.shp_2, R.drawable.shp_3, R.drawable.shp_4, R.drawable.shp_5, R.drawable.shp_6,
                        R.drawable.shp_7, R.drawable.shp_8};
                attr = new String[]{getString(R.string.nckl_shp_1), getString(R.string.nckl_shp_2), getString(R.string.nckl_shp_3),
                        getString(R.string.nckl_shp_4), getString(R.string.nckl_shp_5), getString(R.string.nckl_shp_6), getString(R.string.nckl_shp_7), getString(R.string.nckl_shp_8)};
                break;
            case "stmp_sty":
                res = new int[]{R.drawable.sty_pure, R.drawable.sty_grid, R.drawable.sty_dot, R.drawable.sty_flower, R.drawable.sty_cross, R.drawable.sty_vertical,
                        R.drawable.sty_charnum, R.drawable.sty_dup};
                attr = new String[]{getString(R.string.stmp_sty_pure), getString(R.string.stmp_sty_grid), getString(R.string.stmp_sty_dot),
                        getString(R.string.stmp_sty_flower), getString(R.string.stmp_sty_cross), getString(R.string.stmp_sty_vertical), getString(R.string.stmp_sty_charnum), getString(R.string.stmp_sty_dup)};
                break;
            case "prf_color":
                break;

        }
        size = res.length;
        //初始化int[] result 保存该scene下gridview的选项
        if (UploadData.get(type) != null) {
            result = UploadData.get(type);
        } else {
            result = new int[size];
        }
        StringBuffer description = new StringBuffer();
        //初始化标题串
        if (UploadData.get(type) != null) {
            for (int i = 0; i < result.length; i++) {
                if (result[i] == 1) {
                    description.append(attr[i] + " ");
                }
            }
            scene_2_txt.setText(description.toString());
        } else {
            scene_2_txt.setText("");
        }
        int gridviewWidth = (int) (size * (length + 10) * density);
        int itemWidth = (int) (length * density);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < res.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("gridImageCell", res[i]);
            data.add(map);
        }
        GridviewAdapter simpleAdapter = new GridviewAdapter(this, data, R.layout.grid_image_cell, new String[]{"gridImageCell"}, new int[]{R.id.gridImageCell}, result);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        scene_2_grid.setLayoutParams(params);
        scene_2_grid.setColumnWidth(itemWidth);
        scene_2_grid.setHorizontalSpacing(5);
        scene_2_grid.setStretchMode(GridView.NO_STRETCH);
        scene_2_grid.setNumColumns(size);
        simpleAdapter.notifyDataSetChanged();
        scene_2_grid.setAdapter(simpleAdapter);
        //GridView选择监听
        scene_2_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Resources resources = getBaseContext().getResources();
                Drawable drawable1 = resources.getDrawable(R.drawable.editor_rec);
                Drawable drawable2 = resources.getDrawable(R.drawable.editor_null);
                if (result[position] == 0) {
                    view.setBackgroundDrawable(drawable1);
                    result[position] = 1;
                    description.append(attr[position] + " ");
                } else {
                    view.setBackgroundDrawable(drawable2);
                    result[position] = 0;
                    int i = description.toString().indexOf(attr[position] + " ");
                    description.delete(i, i + (attr[position] + " ").length());
                }
                scene_2_txt.setText(description.toString());
            }
        });
    }

    private void setVisibility(int i) {
        switch (i) {
            case 1:
                scene_1.setVisibility(View.VISIBLE);
                scene_2.setVisibility(View.INVISIBLE);
                break;
            case 2:
                scene_1.setVisibility(View.INVISIBLE);
                scene_2.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
