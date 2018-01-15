package com.example.day3_1_youlou.fragment;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.day3_1_youlou.R;

/**
 * Created by mwg on 2017/12/1.
 */

public abstract class BaseFragment extends Fragment {

    protected View contentView=null;
    protected LinearLayout actionbar;

    //定义一个方法，实现对标题三个控件的初始化设置
    public void initialActionbar(int leftId, String title, int rightId) {
        if (actionbar == null) {
            return;
        }
        //获得标题栏当中的各个控件
        ImageView imageView_Left = actionbar.findViewById(
                R.id.imageView_ActionBar_left);
        TextView textView_Title = actionbar.findViewById(
                R.id.textView_ActionBar_Title);
        ImageView imageView_Right = actionbar.findViewById(
                R.id.imageView_ActionBar_Right);

        //如果第一个参数小于等于0，说明左边图片不显示
        if (leftId <= 0) {
            imageView_Left.setVisibility(View.INVISIBLE);
        } else {
            imageView_Left.setVisibility(View.VISIBLE);
            imageView_Left.setImageResource(leftId);
        }
        //如果第二个参数小于等于0，说明中间文字不显示
        if (TextUtils.isEmpty(title)) {
            textView_Title.setVisibility(View.INVISIBLE);
        }else {
            textView_Title.setVisibility(View.VISIBLE);
            textView_Title.setText(title);
        }
        //如果第三个参数小于等于0，说明右边图片不显示
        if (rightId <= 0) {
            imageView_Right.setVisibility(View.INVISIBLE);
        } else {
            imageView_Right.setVisibility(View.VISIBLE);
            imageView_Right.setImageResource(rightId);
        }
    }
    public abstract void initialUI();
}
