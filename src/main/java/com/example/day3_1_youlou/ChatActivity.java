package com.example.day3_1_youlou;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.tv.TvContentRating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.day3_1_youlou.adapter.SMSAdapter;
import com.example.day3_1_youlou.constant.IConstant;
import com.example.day3_1_youlou.entity.SMS;
import com.example.day3_1_youlou.manager.SMSManager;

import java.util.List;

public class ChatActivity extends Activity {
    static ListView listView_Chat;
    static SMSAdapter adapter=null;
    String name;
    static String address;
    static int threadId;
    EditText editText_Message;

    ImageView imageView_Left;
    TextView textView_Title;
    ImageView imageView_Right;

    static Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        context = getApplicationContext();
        initialData();
        initialUI();
        
    }

    private void initialData() {
        // 获得上一个Fragment传过来的数据
        Intent intent = getIntent();
        name= intent.getStringExtra("name");
        address= intent.getStringExtra("address");
        threadId = intent.getIntExtra("threadId",0);
    }

    private void initialUI() {
        imageView_Left=findViewById(R.id.imageView_ActionBar_left);
        textView_Title = findViewById(R.id.textView_ActionBar_Title);
        imageView_Right = findViewById(R.id.imageView_ActionBar_Right);
        imageView_Right.setVisibility(View.INVISIBLE);
        imageView_Left.setImageResource(R.drawable.ic_back);
        // 标题文本的处理
        if (TextUtils.isEmpty(name)){
            textView_Title.setText(address);
        }else {
            textView_Title.setText(name);
        }

        // 左边返回键注册监听事件
        imageView_Left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        editText_Message = findViewById(R.id.editText_Chat_SMS);
        listView_Chat = findViewById(R.id.listView_Chat);
        adapter = new SMSAdapter(this);
        listView_Chat.setAdapter(adapter);
        refreshSMS();
    }

    private static void refreshSMS() {
        // 查询指定会话的短信
        List<SMS> smses = SMSManager.getAllSMS(context,threadId);
        adapter.addDatas(smses,true);
        // 每次数据适配完成后显示（或者定位到）最后一项
        listView_Chat.setSelection(smses.size()-1);
    }

    public static class SMSReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (IConstant.RECEIVED_SMS.equals(action)){
                // 说明收到的广播的类型是发来短信的广播
                //            Log.i("TAG:",action);
                // 拦截到了短信
                // 进行短信的解析

                SMS sms = SMSManager.onReceiveSMS(context,intent);
                Log.i("TAG:",sms.toString());
                if (sms.getAddress().equals(address)){
                    // 把收到的短信存入到收件箱
                    SMSManager.saveReceivedSMS(context,sms,threadId);
                    // 刷新UI
                    refreshSMS();
                }
            }else if (IConstant.SEND_SMS.equals(action)){
                // 有短信发出去了
                Log.i("TAG:",action);
                String body = intent.getStringExtra("body");
                String address = intent.getStringExtra("address");
                // 把发出的短信存入到发件箱中
                SMSManager.saveSendSMS(context,body,address);
                // UI刷新
                refreshSMS();
            }
        }
    }

    public void send(View view){
        // 获得发送的短信的内容
        String message = editText_Message.getText().toString();
        // 发送短信
        SMSManager.sendSMS(this,message,address);
        // 清空文本框
        editText_Message.setText("");
    }
}
