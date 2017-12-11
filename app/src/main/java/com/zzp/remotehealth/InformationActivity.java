package com.zzp.remotehealth;

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

import fragment.PatientFragment;

import static utils.Constants.patients;

public class InformationActivity extends AppCompatActivity {

    ViewPager viewPager;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        // 绑定viewpager与tablayout
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
                titles[i] = patients.get(i).number;
                fragmentList.add(new PatientFragment().newInstance(i));
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
}
