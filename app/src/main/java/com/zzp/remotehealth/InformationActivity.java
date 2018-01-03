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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fragment.PatientFragment;
import fragment.TxtFragment;

import static utils.Constants.array;
import static utils.Constants.patients;
import static utils.Constants.zzpFile;

public class InformationActivity extends AppCompatActivity {

    ViewPager viewPager;
    boolean type;
    Timer timer = new Timer();
    int tempSize ;
    TabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_information);
        Intent intent = getIntent();
        type = intent.getBooleanExtra("type",true);
        // 绑定viewpager与tabLayout
        // 视图对象
        // 自定义类，导航布局的适配器
        viewPager = findViewById(R.id.viewPager);
        viewPager .setOffscreenPageLimit(1);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        // 新建适配器
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        // 设置适配器
        viewPager.setAdapter(tabAdapter);
        tempSize = patients.size();

        // 绑定viewpager
        tabLayout.setupWithViewPager(viewPager);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(patients.size()!= tempSize){
                    tempSize = patients.size();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tabAdapter = new TabAdapter(getSupportFragmentManager());
                        }
                    });
                }

            }
        },1000,1000);

    }

    // fragment的适配器类
    class TabAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<>();
        // 标题数组

        String[] titles ;

        private TabAdapter(FragmentManager fm) {
            super(fm);

                if(type){
                    int t = patients.size() ;
                    if(!patients.isEmpty()){
                        for(int i=0;i< t ;i++) {
                            titles = new String[patients.size()];
                            titles[i] = String.valueOf(i + 1);
                            fragmentList.add(new PatientFragment().newInstance(i));
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"暂无已连接的监护器",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    }

                else {
                    File file = new File(zzpFile);
                    // get the folder list
                    array = file.listFiles();
                    int i = array.length;
                    titles = new String[i];

                    for(int j = 0 ; j < i ; j++){
                        String fileName = array[j].getName();
                        titles[j] = fileName.substring(0,fileName.lastIndexOf("txt")-1);
                        fragmentList.add(new TxtFragment().newInstance(j));
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
