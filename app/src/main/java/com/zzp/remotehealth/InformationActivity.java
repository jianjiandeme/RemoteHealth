package com.zzp.remotehealth;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fragment.PatientFragment;
import fragment.TxtFragment;

import static utils.Constants.patients;

public class InformationActivity extends AppCompatActivity {

    ViewPager viewPager;
    boolean type;

    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Intent intent = getIntent();
        type = intent.getBooleanExtra("type",true);
        // 绑定viewpager与tabLayout
        // 视图对象
        // 自定义类，导航布局的适配器
        viewPager = findViewById(R.id.viewPager);
        viewPager .setOffscreenPageLimit(2);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        // 新建适配器
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        // 设置适配器
        viewPager.setAdapter(tabAdapter);

        // 绑定viewpager
        tabLayout.setupWithViewPager(viewPager);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tabAdapter.notifyDataSetChanged();
                    }
                });
            }
        },1000,1000);

    }

    // fragment的适配器类
    class TabAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<>();
        // 标题数组

        String[] titles = new String[patients.size()];

        //主界面三个Fragment
        private TabAdapter(FragmentManager fm) {
            super(fm);
            for(int i=0;i< patients.size();i++){
                if(type){
                    titles[i] = String.valueOf(i+1);
                    fragmentList.add(new PatientFragment().newInstance(i));
                }
                else {
                    titles[i] = patients.get(i).number;
                    fragmentList.add(new TxtFragment().newInstance(i));
                }
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer!=null)timer.cancel();
    }
}
