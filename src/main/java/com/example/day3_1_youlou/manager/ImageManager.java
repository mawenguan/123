package com.example.day3_1_youlou.manager;

/**
 * Created by mwg on 2017/12/4.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;

/**
 * 实现的是原型头像的处理
 */
public class ImageManager {

    public static Bitmap formatBitmap(Context context, Bitmap bitmap){
        //获得原始头像的宽度和高度
        int width = bitmap.getWidth();
        int height =bitmap.getHeight();

        //创建一个画布的背景图片
        Bitmap backBitmap = Bitmap.createBitmap(
                width,height,Bitmap.Config.ARGB_8888);
        // 以背景为基础创建一个画布
        Canvas canvas = new Canvas(backBitmap);
        // 创建一个画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true);//设置抗锯齿属性
        paint.setColor(Color.BLACK);

        // 计算圆的半径
        int radius = Math.min(width,height)/2;
        canvas.drawCircle(width/2,height/2,radius,paint);

        // 设置画笔模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,0,0,paint);

        // 设置画笔的颜色
        paint.setColor(Color.WHITE);
        // 设置画出来的圆的样式
        paint.setStyle(Paint.Style.STROKE);

        // 计算一下圆环的宽度(将系统默认给的密度值（*dip）转化成一个绝对的像素值（*pix）)
        float strokeWidth = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                2,
                context.getResources().getDisplayMetrics());//将2dp转化为绝对的像素值
        // 给画笔设置宽度（以上面计算得出的像素值strokeWidth作为画笔的宽度值）
        paint.setStrokeWidth(strokeWidth);
        // 画头像圆环
        canvas.drawCircle(width/2,height/2,
                radius-strokeWidth/2,paint);

        return backBitmap;
    }
}

