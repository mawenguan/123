package com.example.day3_1_youlou.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.day3_1_youlou.R;
import com.example.day3_1_youlou.constant.IConstant;
import com.example.day3_1_youlou.entity.Calllog;
import com.example.day3_1_youlou.entity.Contact;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by mwg on 2017/12/1.
 */

public class ContactsManager {
    //实现联系人的检索
    //创建查询联系人的方法
    public static List<Contact> getAllContacts(Context context) {
        List<Contact> contacts = new ArrayList<>();
        Uri contact_Uri = ContactsContract.Contacts.CONTENT_URI;

        //获得访问内容提供者的内容解析器
        ContentResolver resolver = context.getContentResolver();
        String[] selection = new String[]{"_id", "photo_id"};
        Cursor cursor = resolver.query(contact_Uri, selection,
                null, null, null);

        //循环读取
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            //把数据提取出来的联系人的编号和该联系人的账户ID是一致的
            int _id = cursor.getInt(cursor.getColumnIndex("_id"));
            int photoId = cursor.getInt(cursor.getColumnIndex("photo_id"));
            contact.set_id(_id);
            contact.setPhoneId(photoId);

            //获取DATA的Uri
            Uri data_Uri = ContactsContract.Data.CONTENT_URI;
            String[] dataProjection = new String[]{
                    ContactsContract.Data.MIMETYPE, ContactsContract.Data.DATA1};
            //设置查询条件
            //根据账户编号查询某一编号的账户的所有数据
            String where = ContactsContract.Data.RAW_CONTACT_ID + "=?";
            String args[] = new String[]{String.valueOf(_id)};

            Cursor dataCursor = resolver.query(data_Uri,
                    dataProjection, where, args, null);
            while (dataCursor.moveToNext()) {
                String mimeType = dataCursor.getString(0);
                String data1 = dataCursor.getString(1);
                if (mimeType.equals(IConstant.MIMETYPE_NAME)) {
                    contact.setName(data1);
                } else if (mimeType.equals(IConstant.MIMETYPE_PHONE)) {
                    contact.setPhone(data1);
                } else if (mimeType.equals(IConstant.MIMETYPE_ADDRESS)) {
                    contact.setAddress(data1);
                } else if (mimeType.equals(IConstant.MIMETYPE_EMAIL)) {
                    contact.setEmail(data1);
                }
            }
            dataCursor.close();

            //再把联系人信息添加到集合中
            contacts.add(contact);
        }
        cursor.close();
        return contacts;
    }

    /**
     * 根据头像编号查询联系人头像
     *
     * @param context
     * @param photoId 联系人的头像编号
     * @return
     */
    public static Bitmap getPhotoByPhotoId(Context context, int photoId) {
        Bitmap photo = null;
        if (photoId <= 0) {
            // 没有为联系人提供头像信息
            // 为联系人设置一个默认头像
            photo = BitmapFactory.decodeResource(
                    context.getResources(), R.drawable.ic_contact);
        } else {
            ContentResolver resolver = context.getContentResolver();
            Uri data_Uri = ContactsContract.Data.CONTENT_URI;
            String[] projection = new String[]{ContactsContract.Data.DATA15};
            String selection = ContactsContract.Data._ID + "=?";
            String[] args = new String[]{String.valueOf((photoId))};
            Cursor cursor = resolver.query(data_Uri,
                    projection, selection, args, null);
            if (cursor.moveToNext()) {
                byte[] bytes = cursor.getBlob(0);
                // 将字节数组解码成一个图片
                photo = BitmapFactory.decodeByteArray(
                        bytes, 0, bytes.length);
            }
        }
        return photo;
    }

    /**
     * @param context 删除联系人
     */
    public static void deleteContact(Context context, Contact contact) {
        // 创建解析器
        ContentResolver resolver = context.getContentResolver();
        Uri uri = ContactsContract.RawContacts.CONTENT_URI;
        String where = ContactsContract.RawContacts.CONTACT_ID + "=?";
        String[] args = new String[]{String.valueOf(contact.get_id())};
        resolver.delete(uri, where, args);
    }

    /**
     * 实现通话记录的查询
     *
     * @param context
     * @return
     */
    public static List<Calllog> getAllCalllogs(Context context) {
        List<Calllog> calllogs = new ArrayList<Calllog>();
        try {
            ContentResolver resolver = context.getContentResolver();
            Uri uri = CallLog.Calls.CONTENT_URI;
            String[] projection = new String[]{
                    CallLog.Calls._ID,
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.TYPE,
                    CallLog.Calls.DATE,};
            String order = CallLog.Calls.DATE + " desc";
            Cursor cursor = resolver.query(
                    uri, projection, null, null, null);
            while (cursor.moveToNext()) {
                Calllog calllog = new Calllog();
                int _id = cursor.getInt(0);
                String number = cursor.getString(1);
                int type = cursor.getInt(2);
                long date = cursor.getLong(3);

                int photoId = getPhotoIdByNumber(context, number);
                String name = getNameByNumber(context, number);

                calllog.set_id(_id);
                calllog.setPhone(number);
                calllog.setType(type);
                calllog.setDate(date);
                calllog.setPhotoId(photoId);
                calllog.setName(name);
                calllog.setDateStr(formatDate(date));

                calllogs.add(calllog);
            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
        return calllogs;
    }

    /**
     * 计算通话时间和当前系统时间的天数差
     *
     * @return 天数差
     */
    public static int daydiff(long stamp) {
        int diff = 0;
        // 获得当前的系统时间的日历对象
        Calendar calendar1 = Calendar.getInstance();
        // 创建一个表示通话时间的日历对象
        Calendar calendar2 = Calendar.getInstance();
        // 把通话时间戳设置给calendar2
        calendar2.setTimeInMillis(stamp);

        diff = calendar1.get(Calendar.DAY_OF_YEAR) -
                calendar2.get(Calendar.DAY_OF_YEAR);
        return diff;
    }

    /**
     * @param stamp 通话时间的时间戳
     * @return 格式化后的日期字符串
     */
    public static String formatDate(long stamp) {

        String dateStr = null;
        // 获得天数差
        int diff = daydiff(stamp);
        if (diff == 0) {
            // 说明通话时间为当天
            SimpleDateFormat dateFormat =
                    new SimpleDateFormat("HH:mm:ss");
            dateStr = dateFormat.format(new Date(stamp));
        } else if (diff == 1) {
            // 说明通话时间是昨天
            SimpleDateFormat dateFormat =
                    new SimpleDateFormat("昨天 HH:mm:ss");
            dateStr = dateFormat.format(new Date(stamp));
        } else if (diff <= 7) {
            // 说明通话时间是一周以内的
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(stamp);
            int weekday = calendar.get(Calendar.DAY_OF_WEEK);
            switch (weekday) {
                case Calendar.MONDAY:
                    dateStr = "星期一";
                    break;
                case Calendar.TUESDAY:
                    dateStr = "星期二";
                    break;
                case Calendar.WEDNESDAY:
                    dateStr = "星期三";
                    break;
                case Calendar.THURSDAY:
                    dateStr = "星期四";
                    break;
                case Calendar.FRIDAY:
                    dateStr = "星期五";
                    break;
                case Calendar.SATURDAY:
                    dateStr = "星期六";
                    break;
                case Calendar.SUNDAY:
                    dateStr = "星期日";
                    break;
                default:
                    break;
            }
        } else {
            SimpleDateFormat dateFormat =
                    new SimpleDateFormat("yyyy-MM-dd");
            dateStr = dateFormat.format(new Date(stamp));
        }
        return dateStr;
    }

    /**
     * 根据电话号码查询头像编号
     *
     * @return
     */
    public static int getPhotoIdByNumber(Context context, String number) {
        int phoyoId = 0;
        ContentResolver resolver = context.getContentResolver();
        // 创建查询的Uri和条件
        Uri uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
//        Log.i("TAG", uri.toString());
//        if (number.isEmpty()){
//            return 24;
//        }
        // 构建要查询的联系人的字段信息
        String[] projection = new String[]{
                ContactsContract.PhoneLookup.PHOTO_ID};
        Cursor cursor = resolver.query(uri, projection,
                null, null, null);
        if (cursor.moveToNext()) {
            phoyoId = cursor.getInt(0);
        }
        cursor.close();
        return phoyoId;
    }

    public static String getNameByNumber(Context context, String number) {
        String name = null;
        ContentResolver resolver = context.getContentResolver();

        Uri uri = Uri.withAppendedPath(ContactsContract.
                PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        if (number.isEmpty()){
            return "未知号码";
        }
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.
                PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor.moveToNext()) {
            name = cursor.getString(0);
        }
        cursor.close();
        return name;
    }

    /**
     * 删除通话记录
     *
     * @param context
     * @param calllog 被删除的通话记录对象
     */
    public static void deleteCalllog(Context context, Calllog calllog) {
        // 获得内容解析器ContentResolver
        ContentResolver resolver = context.getContentResolver();
        try {
            Uri uri = CallLog.Calls.CONTENT_URI;
            // 删除条件
            String where = CallLog.Calls._ID + "=?";
            String args[] = new String[]{String.valueOf(calllog.get_id())};
            resolver.delete(uri, where, args);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }
}
