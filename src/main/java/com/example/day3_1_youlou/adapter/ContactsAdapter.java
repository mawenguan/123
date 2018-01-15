package com.example.day3_1_youlou.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.day3_1_youlou.R;
import com.example.day3_1_youlou.entity.Contact;
import com.example.day3_1_youlou.manager.ContactsManager;
import com.example.day3_1_youlou.manager.ImageManager;

/**
 * Created by mwg on 2017/12/2.
 */

public class ContactsAdapter extends MyBaseAdapter<Contact> {
    public ContactsAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            //如果convertView为空，说明适配的是第一屏的数据
            //在内存缓存中还没有可以重用的Item convertView项
            convertView = layoutInflater.inflate(
                    R.layout.inflate_contact_item_layout,null);
            holder = new ViewHolder();
            holder.imageView_Header= convertView.findViewById(
                    R.id.imageView_Contact_Header);
            holder.textView_Name= convertView.findViewById(
                    R.id.textView_Contact_Name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        //获得适配器集合中的一个数据对象
        Contact contact = getItem(position);
        if (position==0){
            //说明当前适配器的联系人是添加联系人项（联系人添加图标）
            //对他的头像做一个特殊处理
            holder.imageView_Header.setImageResource(
                    R.drawable.ic_add_contact);
        }else {
            //获得联系人的头像的信息
            Bitmap photo = ContactsManager.getPhotoByPhotoId(
                    context,contact.getPhoneId());
            // 对头像进行圆形格式化
            photo = ImageManager.formatBitmap(context,photo);
            holder.imageView_Header.setImageBitmap(photo);
        }
        //获得联系人的姓名的信息
        holder.textView_Name.setText(contact.getName());
        return convertView;
    }

    private class ViewHolder{
        ImageView imageView_Header;
        TextView textView_Name;
    }
}
