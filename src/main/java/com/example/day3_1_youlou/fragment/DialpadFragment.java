package com.example.day3_1_youlou.fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.day3_1_youlou.R;
import com.example.day3_1_youlou.adapter.CalllogAdapter;
import com.example.day3_1_youlou.entity.Calllog;
import com.example.day3_1_youlou.manager.ContactsManager;
import com.example.day3_1_youlou.manager.MediaManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialpadFragment extends BaseFragment {
    ListView listView_Dialpad;
    CalllogAdapter adapter = null;
    RelativeLayout relativeLayout_Dialpad = null;

    TextView textView_Title = null;
    ImageView imageView_BackSpace = null;
    ImageButton imageButton_Call = null;

    public DialpadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(
                R.layout.fragment_dialpad_layout, container, false);
        initialUI();
        //initialDialpad();
        checkPermission();
        return contentView;
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 102);
        } else {
            initialDialpad();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 102:
                if (grantResults.length > 0) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "必须授予打电话的权限",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    initialDialpad();
                } else {
                    Toast.makeText(getActivity(), "没有相关权限的处理",
                            Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    private static String[] keys = new String[]{"1", "2", "3",
            "4", "5", "6", "7", "8", "9", "*", "0", "#"};

    private void initialDialpad() {
        // 获得键盘布局
        relativeLayout_Dialpad = contentView.
                findViewById(R.id.relativeLayout_Dialpad);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // 计算按键的宽度
        int width = metrics.widthPixels / 3;
        // 计算按键的高度
        int heihgt = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                55, getResources().getDisplayMetrics());


        for (int i = 0; i < keys.length; i++) {
            final TextView key = new TextView(getActivity());
            key.setText(keys[i]);
            // 设置按键控件的id（是一个非零的正数）
            key.setId((i + 1));
            key.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            key.setGravity(Gravity.CENTER);

            RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(width, heihgt);
            // 创建一个布局参数
            if (i % 3 != 0) {
                // 需要设置右对齐的规则
                params.addRule(RelativeLayout.RIGHT_OF, i);
            }
            if (i >= 3) {
                params.addRule(RelativeLayout.BELOW, (i - 2));
            }
            // 把控件添加到布局上面
            relativeLayout_Dialpad.addView(key, params);

            key.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaManager.playMusic(getActivity(), R.raw.b);
                    // 获得标题文本的内容
                    String title = textView_Title.getText().toString();
                    if ("拨打电话".equals(title)) {
                        // 获得案件文本设置在标题上
                        textView_Title.setText(key.getText().toString());
                    } else if (title.length() == 3 || title.length() == 8) {
                        // 在标题文本上先追加分割线，再追加数字键
                        textView_Title.append("-");
                        textView_Title.append(key.getText().toString());
                    } else {
                        // 直接在标题上追加数字键
                        textView_Title.append(key.getText().toString());
                    }
                }
            });
        }
    }

    @Override
    public void initialUI() {
        actionbar = contentView.findViewById(R.id.actionbar_Dialpad);
        initialActionbar(R.drawable.ic_add_icon,
                "拨打电话", R.drawable.ic_backspace);
        listView_Dialpad = contentView.findViewById(
                R.id.listView_Dialpad);

        textView_Title = actionbar.findViewById(R.id.textView_ActionBar_Title);
        imageView_BackSpace = actionbar.findViewById(R.id.imageView_ActionBar_Right);

        adapter = new CalllogAdapter(getActivity());
        listView_Dialpad.setAdapter(adapter);
        imageButton_Call = contentView.findViewById(R.id.imageButton_Call);

        List<Calllog> calllogs = ContactsManager.getAllCalllogs(getActivity());
        adapter.addDatas(calllogs, true);

        /**
         *  实现电话号码长度限制为13个字符
         */
        textView_Title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 13) {
                    s.delete(13, s.length());
                }
            }
        });

        /**
         *  实现按回退键删除字符
         */
        imageView_BackSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 每按一次可以删除一个字符
                // 可以使用截取子字符串的方式
                String title = textView_Title.getText().toString();
                if ("拨打电话".equals(title)) {
                    return;
                } else if (title.length() == 1) {
                    textView_Title.setText("拨打电话");
                } else if (title.length() == 5 || title.length() == 10) {
                    textView_Title.setText(title.substring(0, title.length() - 2));
                } else {
                    textView_Title.setText(title.substring(0, title.length() - 1));
                }
            }
        });
        // 实现拨打电话的监听的注册
        imageButton_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaManager.playMusic(getActivity(), R.raw.a);
                String number = textView_Title.getText().toString();
                if (!number.equals("拨打电话")) {
                    number = textView_Title.getText().toString();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri uri = Uri.parse("tel:" + number);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    imageButton_Call.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 回收音乐资源
        MediaManager.release();
    }
}
