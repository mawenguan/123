package com.example.day3_1_youlou.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.input.InputManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.day3_1_youlou.R;
import com.example.day3_1_youlou.entity.SMS;
import com.example.day3_1_youlou.manager.ContactsManager;
import com.example.day3_1_youlou.manager.ImageManager;

/**
 * Created by mwg on 2017/12/10.
 */

public class SMSAdapter extends MyBaseAdapter<SMS> {
    public SMSAdapter(Context context) {
        super(context);
    }

    public static final int LEFT_TYPE = 0;
    public static final int RIGHT_TYPE= 1;

    // 通过重写该方法实现数据适配时，获得不同的布局类型
    @Override
    public int getItemViewType(int position) {
        int type = getItem(position).getType();
        return type - 1;
    }

    // 通过重写该方法实现返回多个布局数目
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = getItemViewType(position);
        // 优化适配器对内存的使用
        if (convertView==null){
            if (type==LEFT_TYPE){
                // 使用左边布局来做数据适配
            convertView = layoutInflater.inflate(
                    R.layout.inflate_smsleft_item_layout,null);
            }else if(type==RIGHT_TYPE){
                // 使用右边布局来做数据适配
            convertView = layoutInflater.inflate(
                    R.layout.inflate_smsright_item_layout,null);
            }
            holder = new ViewHolder();
            holder.textView_Date = convertView.
                    findViewById(R.id.textView_SMS_Date);
            holder.imageView_Header = convertView.
                    findViewById(R.id.imageView_SMS_Header);
            holder.textView_Body = convertView.
                    findViewById(R.id.textView_SMS_Body);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 获得要适配的数据对象
        SMS sms = getItem(position);
        holder.textView_Date.setText(sms.getDateStr());
        holder.textView_Body.setText(sms.getBody());
        // 头像的设置
        // 如果是收到的短信
        if (type==LEFT_TYPE){
            int photoId = sms.getPhotoId();
            Bitmap header = ContactsManager.getPhotoByPhotoId(context,photoId);
            // 格式化头像
            header= ImageManager.formatBitmap(context,header);
            holder.imageView_Header.setImageBitmap(header);
        }else if (type==RIGHT_TYPE){
            // 如果是发出去的短信
            // 头像使用默认值
            Bitmap header = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ic_contact_selected);

            header = ImageManager.formatBitmap(context,header);
            holder.imageView_Header.setImageBitmap(header);
        }
        return convertView;
    }

    private class ViewHolder{
        ImageView imageView_Header;
        TextView textView_Date;
        TextView textView_Body;
    }
}
