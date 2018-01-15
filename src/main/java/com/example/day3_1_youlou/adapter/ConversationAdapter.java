package com.example.day3_1_youlou.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.day3_1_youlou.R;
import com.example.day3_1_youlou.entity.Conversation;
import com.example.day3_1_youlou.manager.ContactsManager;
import com.example.day3_1_youlou.manager.ImageManager;

/**
 * Created by mwg on 2017/12/8.
 */

public class ConversationAdapter extends MyBaseAdapter<Conversation> {
    public ConversationAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.inflate_sms_item_layout, null);
            holder = new ViewHolder();
            holder.imageView_Header = convertView.findViewById(
                    R.id.imageView_Conversation_Header);
            holder.imageView_Warning = convertView.findViewById(
                    R.id.imageView_Conversation_Warning);
            holder.imageView_Read = convertView.findViewById(
                    R.id.imageView_Conversation_Read);

            holder.textView_Name = convertView.findViewById(
                    R.id.textView_Conversation_Name);
            holder.textView_Body = convertView.findViewById(
                    R.id.textView_Conversation_Body);
            holder.textView_Date = convertView.findViewById(
                    R.id.textView_Conversation_Date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 获得要适配的数据对象
        Conversation conversation = getItem(position);
        String name = conversation.getName();
        if (TextUtils.isEmpty(name)){
            holder.imageView_Warning.setVisibility(View.VISIBLE);
            holder.textView_Name.setText(conversation.getAddress());
            holder.textView_Name.setTextColor(Color.RED);
        }else {
            holder.imageView_Warning.setVisibility(View.INVISIBLE);
            holder.textView_Name.setText(name);
            holder.textView_Name.setTextColor(Color.BLACK);
        }
        String body = conversation.getBody();
        holder.textView_Body.setText(body);
        String date = conversation.getDateStr();
        holder.textView_Date.setText(date);

        int photoId = conversation.getPhotoId();
        //holder.imageView_Header.setImageResource(photoId);
        // 头像的格式化处理
        Bitmap header = ContactsManager.getPhotoByPhotoId(context,photoId);
        header = ImageManager.formatBitmap(context,header);
        holder.imageView_Header.setImageBitmap(header);

        // 获得会话的读取的状态
        int read = conversation.getRead();
        if (read==0){
            // 未读
            holder.imageView_Read.setVisibility(View.VISIBLE);
        }else if(read==1){
            holder.imageView_Read.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView_Header;
        ImageView imageView_Read;
        ImageView imageView_Warning;
        TextView textView_Name;
        TextView textView_Body;
        TextView textView_Date;
    }
}
