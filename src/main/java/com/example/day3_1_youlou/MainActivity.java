package com.example.day3_1_youlou;

import android.app.Fragment;
import android.content.Intent;
import android.provider.Telephony;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.day3_1_youlou.adapter.MyFragmentPagerAdapter;
import com.example.day3_1_youlou.fragment.CalllogFragment;
import com.example.day3_1_youlou.fragment.ContactFragment;
import com.example.day3_1_youlou.fragment.DialpadFragment;
import com.example.day3_1_youlou.fragment.SmsFragment;

public class MainActivity extends FragmentActivity {

    //初始化ViewPager
    //创建适配器对象
    //创建Fragment对象
    //将Fragment添加到适配器中
    //将ViewPager和适配器进行关联
    ViewPager viewPager;
    MyFragmentPagerAdapter adapter = null;
    CalllogFragment calllogFragment;
    ContactFragment contactFragment;
    DialpadFragment dialpadFragment;
    SmsFragment smsFragment;

    RadioGroup radioGroup;
    // 保存默认短信的包名
    String defaultSmsApp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(this);

        initialFragments();
        setListener();
        // 设置默认的短信的应用为当前的应用
        setDefaultSMS(getPackageName());
    }

    private void setDefaultSMS(String packagename) {
        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packagename);
        startActivity(intent);
    }

    public void initialFragments() {
        radioGroup = findViewById(R.id.radioGroup_Bottom);
        viewPager = findViewById(R.id.viewPager_Main);
        adapter = new MyFragmentPagerAdapter(
                getSupportFragmentManager());
        calllogFragment = new CalllogFragment();
        contactFragment = new ContactFragment();
        dialpadFragment = new DialpadFragment();
        smsFragment = new SmsFragment();

        viewPager.setAdapter(adapter);

        //将创建好的fragment添加到适配器集合中
        adapter.addFragment(calllogFragment);
        adapter.addFragment(contactFragment);
        adapter.addFragment(dialpadFragment);
        adapter.addFragment(smsFragment);
        //设置viewpager当前选中项
        viewPager.setCurrentItem(1, true);

    }

    private void setListener() {

        //为radio_group添加监听
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_CallLog:
                        viewPager.setCurrentItem(0, false);
                        break;
                    case R.id.radio_Contacts:
                        viewPager.setCurrentItem(1, false);
                        break;
                    case R.id.radio_Dialpad:
                        viewPager.setCurrentItem(2, false);
                        break;
                    case R.id.radio_SMS:
                        viewPager.setCurrentItem(3, false);
                        break;
                }
            }
        });

        //为viewpager添加监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        //第一个页面被选中
                        //把第一个单选按钮的选中状态设为真
                        radioGroup.check(R.id.radio_CallLog);
                        break;
                    case 1:
                        radioGroup.check(R.id.radio_Contacts);
                        break;
                    case 2:
                        radioGroup.check(R.id.radio_Dialpad);
                        break;
                    case 3:
                        radioGroup.check(R.id.radio_SMS);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 再把系统短信应用还原为默认应用
        setDefaultSMS(defaultSmsApp);
    }
}
