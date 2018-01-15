package com.example.day3_1_youlou.fragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.day3_1_youlou.R;
import com.example.day3_1_youlou.adapter.ContactsAdapter;
import com.example.day3_1_youlou.entity.Contact;
import com.example.day3_1_youlou.manager.ContactsManager;
import com.example.day3_1_youlou.manager.DialogManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends BaseFragment {

    public ContactFragment() {
        // Required empty public constructor
    }

    GridView gridView_Contact;
    ContactsAdapter adapter;
    // 是否做过授权的标识
    boolean permissionFlag = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_contact, container, false);
        initialUI(view);
        // 实现运行时权限的授权的确认
        requestMyPermissions();
        return view;
    }

    private void requestMyPermissions() {
        // 判断有没有对联系人访问权限进行授权确认
        // 没有的话，就发起对该权限进行授权确认的请求
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_CONTACTS)!=
                PackageManager.PERMISSION_GRANTED){
            // 如果没有对该权限进行授权处理，向系统发起授权请求
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_CONTACTS}, 100);
        } else {
            refreshContact();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                   @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0) {
                    //如果授权结果不等于同意
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(),
                                "必须通过联系人访问授权才能访问", Toast.LENGTH_LONG).show();
                        return;
                    }
                    permissionFlag=true;
                    refreshContact();
                }else {
                    Toast.makeText(getActivity(),
                            "没有执行授权处理",Toast.LENGTH_LONG).show();
                }
                break;
            default:

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (permissionFlag) {
            refreshContact();
        }
    }
    public void initialUI(View view) {
        actionbar = view.findViewById(R.id.actionBar_Contact);
        initialActionbar(-1, "联系人", R.drawable.ic_search);
        gridView_Contact = view.findViewById(
                R.id.gridView_Contact);

        adapter = new ContactsAdapter(getActivity());
        //将gridview和适配器进行关联
        gridView_Contact.setAdapter(adapter);

        // 注册gridview项的点击事件的监听器
        gridView_Contact.setOnItemClickListener(new MyOnItemClickListener());

        // 注册长按事件的监听
        gridView_Contact.setOnItemLongClickListener(
                new MyOnItemLongClickListener());
    }

    private void refreshContact() {
        //获得联系人的信息
        List<Contact> contacts = ContactsManager.
                getAllContacts(getActivity());
        Contact contact = new Contact(0, "添加联系人",
                null, null, null, 0);
        //将自定义的联系人插入到集合中作为集合的第一个元素
        contacts.add(0, contact);
        //将集合数据添加到适配器当中
        adapter.addDatas(contacts, true);
    }

    @Override
    public void initialUI() {

    }

    public class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent,
                                View view, int position, long id) {
            if (position == 0) {
                // 说明了添加联系人的项被点击
                // 此时弹出添加联系人的对话框
                DialogManager.showAddContactDialog(getActivity());
            } else {
                // 获得被选中的联系人
                Contact contact = adapter.getItem(position);
                DialogManager.showEditContactDialog(getActivity(), contact);
            }
        }
    }

    public class MyOnItemLongClickListener implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent,
                                       View view, int position, long id) {
            // 获得被删除的联系人的对象
            Contact contact = adapter.getItem(position);
            DialogManager.showDeleteContactDialog(getActivity(),
                    contact, adapter);
            return true;
        }
    }
}
