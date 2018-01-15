package com.example.day3_1_youlou.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.CallLog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.day3_1_youlou.R;
import com.example.day3_1_youlou.entity.Calllog;
import com.example.day3_1_youlou.manager.ContactsManager;
import com.example.day3_1_youlou.manager.ImageManager;

/**
 * Created by mwg on 2017/12/6.
 */

public class CalllogAdapter extends MyBaseAdapter<Calllog> {
    public CalllogAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            convertView = layoutInflater.inflate(
                    R.layout.inflate_calllog_item_layout,null);
            holder = new ViewHolder();
            holder.imageView_Header = convertView.findViewById(
                    R.id.imageView_Calllog_Photo);
            holder.imageView_Warning= convertView.findViewById(
                    R.id.imageView_Calllog_Warning);
            holder.imageView_OutGoing = convertView.findViewById(
                    R.id.imageView_Calllog_outgoing);
            holder.textView_Name = convertView.findViewById(
                    R.id.textView_Calllog_Name);
            holder.textView_Number = convertView.findViewById(
                    R.id.textView_Calllog_Number);
            holder.textView_Date = convertView.findViewById(
                    R.id.textView_Calllog_Date);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 获得适配的数据项
        Calllog calllog = getItem(position);
        if (TextUtils.isEmpty(calllog.getName())){
            // 如果为空，说明对话对象是陌生来电
            holder.textView_Name.setText("未知号码");
            holder.textView_Name.setTextColor(Color.RED);
            // 为未知号码的来电显示警告的小图标
            holder.imageView_Warning.setVisibility(View.VISIBLE);
        }else {
            // 不是陌生来电
            holder.textView_Name.setText(calllog.getName());
            holder.textView_Name.setTextColor(Color.BLACK);
            // 警告的图标隐藏
            holder.imageView_Warning.setVisibility(View.INVISIBLE);
        }
        holder.textView_Number.setText(calllog.getPhone());
        holder.textView_Date.setText(calllog.getDateStr());
        // 设置头像
        int photoId = calllog.getPhotoId();
        // 获得头像
        Bitmap photo= ContactsManager.getPhotoByPhotoId(context,photoId);
        // 格式化头像
        photo = ImageManager.formatBitmap(context,photo);
        // 把格式化后的头像设置上去
        holder.imageView_Header.setImageBitmap(photo);

        // 电话类型处理
        // 如果是播出的电话，显示小图标
        if (calllog.getType()== CallLog.Calls.OUTGOING_TYPE){
            holder.imageView_OutGoing.setVisibility(View.VISIBLE);
        }else {
            holder.imageView_OutGoing.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    private class ViewHolder{
        ImageView imageView_Header;
        ImageView imageView_Warning;
        ImageView imageView_OutGoing;
        TextView textView_Name;
        TextView textView_Number;
        TextView textView_Date;
    }
}
