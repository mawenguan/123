package com.example.day3_1_youlou.manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.day3_1_youlou.R;
import com.example.day3_1_youlou.adapter.CalllogAdapter;
import com.example.day3_1_youlou.adapter.ContactsAdapter;
import com.example.day3_1_youlou.adapter.ConversationAdapter;
import com.example.day3_1_youlou.entity.Calllog;
import com.example.day3_1_youlou.entity.Contact;
import com.example.day3_1_youlou.entity.Conversation;

/**
 * Created by mwg on 2017/12/4.
 */

public class DialogManager {
    /**
     * 显示添加联系人的对话框
     *
     * @param context
     */
    public static void showAddContactDialog(final Context context) {
        // 安卓中的一种模式——构建器（Builder）
        // 即委托AlertDialog的构建器来创建一个Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        dialog.show();
        // 把定义好的对话框的布局解析成一个View对象，添加到对话框上去
        // 该方法提供了两种途径：布局文件的资源ID和View对象
        // 使用后者是为了更方便的对各个控件进行设置或者监听
        View dialogView = View.inflate(context,
                R.layout.inflate_contactadd_dialog_layout, null);
        dialog.setContentView(dialogView);
        // 完成对话框上面的控件的初始化设置
        ImageView imageView_Close = dialogView.
                findViewById(R.id.imageView_ContactAdd_Close);
        ImageView imageView_Confirm = dialogView.
                findViewById(R.id.imageView_ContactAdd_Confirm);
        final EditText editText_Name = dialog.
                findViewById(R.id.editText_ContactAdd_Name);
        final EditText editText_Email = dialog.
                findViewById(R.id.editText_ContactAdd_Email);
        final EditText editText_Phone = dialog.
                findViewById(R.id.editText_ContactAdd_Phone);
        final EditText editText_Company = dialog.
                findViewById(R.id.editText_ContactAdd_CompanyName);
        final EditText editText_Address = dialog.
                findViewById(R.id.editText_ContactAdd_Address);

        // 添加点击事件的监听
        imageView_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        imageView_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 添加联系人的功能——通过调用系统联系人添的加模块
                String name = editText_Name.getText().toString();
                String phone = editText_Phone.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
                    Toast.makeText(context, "联系人或电话号码不能为空！",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                String postal = editText_Address.getText().toString();
                String email = editText_Email.getText().toString();
                String company = editText_Company.getText().toString();

                // 创建用来激活系统联系人添加功能的Activity的Intent
                Intent intent = new Intent();
                intent.setAction(ContactsContract.
                        Intents.SHOW_OR_CREATE_CONTACT);
                Uri uri = Uri.parse("tel:" + phone);
                intent.setData(uri);
                // 在Intent中添加姓名的附加信息
                intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                intent.putExtra(ContactsContract.Intents.Insert.POSTAL, postal);
                intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);

                // 激活系统Activity
                context.startActivity(intent);
                // 关闭当前对话框
                dialog.dismiss();
            }
        });

    }

    /**
     * 显示编辑联系人的对话框
     *
     * @param context
     * @param contact
     */
    public static void showEditContactDialog(final Context context, final Contact contact) {
        // 创建构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // 创建对话框
        final AlertDialog editDialog = builder.create();
        editDialog.show();
        // 将自定义的布局文件解析后转换成一个View对象
        View editView = View.inflate(context,
                R.layout.inflate_contactdetail_dialog_layout, null);
        // 将解析后的View设置给对话框
        editDialog.setContentView(editView);

        // 获得对话框布局上的各个控件
        ImageView imageView_Edit = editView.findViewById(
                R.id.imageView_ContactDetail_Edit);
        final ImageView imageView_Close = editView.findViewById(
                R.id.imageView_ContactDetail_Close);
        ImageView imageView_Header = editView.findViewById(
                R.id.imageView_ContactDetail_Header);
        TextView textView_Name = editView.findViewById(
                R.id.textView_ContactDetail_Name);
        TextView textView_Phone = editView.findViewById(
                R.id.textView_ContactDetail_Phone);
        TextView textView_Postal = editView.findViewById(
                R.id.textView_ContactDetail_postal);
        TextView textView_Email = editView.findViewById(
                R.id.textView_ContactDetail_Email);

        // 显示联系人详情
        textView_Name.setText(contact.getName());
        textView_Phone.setText(contact.getPhone());
        if (!TextUtils.isEmpty(contact.getEmail())) {
            textView_Email.setText(contact.getEmail());
        } else {
            textView_Email.setText("没有邮箱信息！");
        }
        if (!TextUtils.isEmpty(contact.getAddress())) {
            textView_Postal.setText(contact.getAddress());
        } else {
            textView_Email.setText("没有通讯地址！");
        }
        // 获得联系人头像
        Bitmap phone = ContactsManager.
                getPhotoByPhotoId(context, contact.getPhoneId());
        // 将头像进行和格式化处理
        phone = ImageManager.formatBitmap(context, phone);
        // 将头像设置到对应的控件上
        imageView_Header.setImageBitmap(phone);

        // 设置点击事件监听
        imageView_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
            }
        });

        // 实现联系人的编辑的处理
        imageView_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                // 设置意图的data
                Uri uri = Uri.parse(
                        "content://contacts/people/" + contact.get_id());
                intent.setData(uri);
                intent.putExtra("finishActivityOnSaveCompleted",true);
                context.startActivity(intent);
                editDialog.dismiss();

            }
        });
    }


    public static void showDeleteContactDialog(final Context context,
                         final Contact contact, final ContactsAdapter adapter){
        // 创建构建器对象
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_warning);
        builder.setTitle("系统提示");
        builder.setMessage("确定要删除当前联系人吗？");
        builder.setNegativeButton("再想想",null);
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 删除联系人
                ContactsManager.deleteContact(context,contact);
                adapter.removeDatas(contact);
                // 关闭对话框
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     *  显示删除通话记录的对话框
     * @param context
     * @param calllog 要删除的通话记录对象
     * @param adapter 适配器对象
     */
    public static void showDeleteCalllogDialog(
            final Context context, final Calllog calllog, final CalllogAdapter adapter){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_warning);
        builder.setTitle("系统提示");
        builder.setMessage("确定要删除当前对话框吗？");
        builder.setNegativeButton("再想想",null);
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContactsManager.deleteCalllog(context,calllog);
                // 将适配器中的通话记录给移除掉
                adapter.removeDatas(calllog);
                // 关闭对话框
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public static void showDeleteConversationDialog(final Context context,
                        final Conversation conversation, final ConversationAdapter adapter){
        // 显示删除对话的对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_warning);
        builder.setTitle("系统提示");
        builder.setMessage("确定要删除当前会话吗？");
        builder.setNegativeButton("再想想",null);
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SMSManager.deleteConversation(context,conversation.getThread_id());
                adapter.removeDatas(conversation);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
