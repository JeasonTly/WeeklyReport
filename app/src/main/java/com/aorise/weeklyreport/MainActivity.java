package com.aorise.weeklyreport;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.RadioGroup;

import com.aorise.weeklyreport.activity.fragment.NewHomeFragment;
import com.aorise.weeklyreport.activity.fragment.PersonalFragment;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.databinding.ActivityMainBinding;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private ActivityMainBinding mViewDataBinding;

    private NewHomeFragment mNewHomeFragment;
    private PersonalFragment mPersonalFragment;

    private List<Fragment> fragmentList = new ArrayList<>();

    /**
     * 按键双击退出应用
     */
    private long[] mHints = new long[2];
    private static final long EXIT_INTERVAL = 2000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        WRApplication.getInstance().addActivity(this);
        SharedPreferences sp = getSharedPreferences("UserInfo",MODE_PRIVATE);
        mViewDataBinding.pageIndex.setOnCheckedChangeListener(this);
        mViewDataBinding.groupHome.setChecked(true);
    //    LogT.d("Time xxxxxxx"+ TimeUtil.getInstance().getWorkDateList("2019-08-09 18:06:33","2019-08-19 23:56:33"));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            // 将mHints数组内的所有元素左移一个位置
            System.arraycopy(mHints, 1, mHints, 0, mHints.length - 1);
            // 获得当前系统已经启动的时间
            mHints[mHints.length - 1] = SystemClock.uptimeMillis();
            if ((SystemClock.uptimeMillis() - mHints[0]) > EXIT_INTERVAL) {
                ToastUtils.show("再按一次退出应用!");
            } else {
                finish();
                WRApplication.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /*添加fragment*/
    private void showFragment(Fragment fragment) {
        LogT.d("显示" + fragment + " fragment is Add " + fragment.isAdded());
        /*判断该fragment是否已经被添加过  如果没有被添加  则添加*/
        if (!fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().add(R.id.main_vp, fragment).commit();
            /*添加到 fragmentList*/
            fragmentList.add(fragment);
        }
        LogT.i("fragmentList数量：" + fragmentList.size());
        for (Fragment frag : fragmentList) {
            LogT.d(" fragment is " + frag);
            if (frag != fragment) {
                /*先隐藏其他fragment*/
                LogT.i("隐藏" + frag);
               // getSupportFragmentManager().beginTransaction().remove(f)
                getSupportFragmentManager().beginTransaction().hide(frag).commit();
            }
        }
        LogT.d("Now fragment is " + fragment);
        getSupportFragmentManager().beginTransaction().show(fragment).commit();

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.group_home:
                if(mNewHomeFragment == null){
                    mNewHomeFragment = new NewHomeFragment();
                }
                showFragment(mNewHomeFragment);
                break;
            case R.id.group_pesonal:
                if (mPersonalFragment == null) {
                    LogT.i("personalFragment 为空  创建");
                    mPersonalFragment = new PersonalFragment();
                }
                showFragment(mPersonalFragment);
                break;
        }
    }
}
