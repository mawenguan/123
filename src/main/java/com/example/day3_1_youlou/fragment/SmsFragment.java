package com.example.day3_1_youlou.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.day3_1_youlou.ChatActivity;
import com.example.day3_1_youlou.R;
import com.example.day3_1_youlou.adapter.ConversationAdapter;
import com.example.day3_1_youlou.entity.Conversation;
import com.example.day3_1_youlou.entity.SMS;
import com.example.day3_1_youlou.manager.DialogManager;
import com.example.day3_1_youlou.manager.SMSManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmsFragment extends BaseFragment {

    public SmsFragment() {
        // Required empty public constructor
    }

    ListView listView_Con;
    ConversationAdapter adapter;
    boolean permissionFlag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(
                R.layout.fragment_sms,container,false);
        initialUI();
        refershConversation();
        return contentView;
    }

    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.
                    RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS},
                    103);
        }else{
            refershConversation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
               @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 103:
                if(grantResults.length>0){
                    if(grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(getActivity(),
                       "获得短信访问权限才可以使用", Toast.LENGTH_LONG).show();
                        return;
                    }
                    permissionFlag=true;
                    refershConversation();
                }else{
                    Toast.makeText(getActivity(), "没有进行相关权限的处理",
                            Toast.LENGTH_LONG).show();
                }
                break;
            default:break;
        }
    }

    @Override
    public void initialUI() {
        actionbar = contentView.findViewById(R.id.actionbar_Conversation);
        initialActionbar(-1,"短消息",-1);
        listView_Con= contentView.findViewById(R.id.listView_Conversation);
        adapter = new ConversationAdapter(getActivity());
        listView_Con.setAdapter(adapter);
        listView_Con.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Conversation conversation = adapter.getItem(position);
                int threadId = conversation.getThread_id();
                String name = conversation.getName();
                String address = conversation.getAddress();

                int read = conversation.getRead();
                if (read==0){
                    // 更新会话的状态为已读
                    SMSManager.updateConversation(getActivity(),threadId);
                }

                // 以下做intent的跳转
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("address",address);
                intent.putExtra("threadId",threadId);

                startActivity(intent);
            }
        });
        listView_Con.setOnItemLongClickListener(new MyOnItemLongClickListener());
        refershConversation();
    }

    public class MyOnItemLongClickListener implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Conversation con = adapter.getItem(position);
            DialogManager.showDeleteConversationDialog(getActivity(),con,adapter);
            return true;
        }
    }

    private void refershConversation() {
        // 获得所有的会话数据
        List<Conversation> cons = SMSManager.getAllConversation(getActivity());
        // 把数据加载到适配器集合中
        adapter.addDatas(cons,true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(permissionFlag){
            refershConversation();
        }else{
            permissionFlag=true;
        }
    }
}
