package com.example.day3_1_youlou.fragment;


import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.day3_1_youlou.R;
import com.example.day3_1_youlou.adapter.CalllogAdapter;
import com.example.day3_1_youlou.entity.Calllog;
import com.example.day3_1_youlou.manager.ContactsManager;
import com.example.day3_1_youlou.manager.DialogManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalllogFragment extends BaseFragment {

    ListView listView_Calllog;
    CalllogAdapter adapter = null;

    public CalllogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_calllog, container, false);
        initialUI(view);
        // 运行时权限验证
        checkPermission();
        return view;
    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_CALL_LOG)!=
                PackageManager.PERMISSION_GRANTED){
            // 发起权限验证请求
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_CALL_LOG},101);
        }else {
            refreshCalllogs();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 101:
                if (grantResults.length>0){
                    if (grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                        // 授权未通过
                        Toast.makeText(getActivity(),"没有通过相关授权",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    refreshCalllogs();
                }else {
                    Toast.makeText(getActivity(),"没有去执行权限验证的处理",
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void initialUI(View view) {
        // 设置标题栏
        actionbar = view.findViewById(R.id.actionbar_Calllog);
        initialActionbar(-1, "通话记录", -1);

        // 初始化控件
        listView_Calllog = view.findViewById(R.id.listView_Calllog);
        adapter = new CalllogAdapter(getActivity());
        listView_Calllog.setAdapter(adapter);

        listView_Calllog.setOnItemLongClickListener(
                new MyOnItemLongClickListener());

        //refreshCalllogs();
    }

    private void refreshCalllogs() {
        List<Calllog> calllogs = ContactsManager.getAllCalllogs(getActivity());
//        for (Calllog calllog:calllogs) {
//            Log.i("calllog",calllog.toString());
        // 将通话记录添加到适配器中，通知UI进行更新
        adapter.addDatas(calllogs, true);
    }

    @Override
    public void initialUI() {

    }

    public class MyOnItemLongClickListener implements
            AdapterView.OnItemLongClickListener{

        @Override
        public boolean onItemLongClick(AdapterView<?> parent,
                                       View view, int position, long id) {
            // 获得被删除的通话记录对象
            Calllog calllog = adapter.getItem(position);
            DialogManager.showDeleteCalllogDialog(getActivity(),calllog,adapter);
            return true;
        }
    }

}

