package com.aorise.weeklyreport;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;

import com.aorise.weeklyreport.activity.BaseActivity;
import com.aorise.weeklyreport.activity.fragment.HomeFragment;
import com.aorise.weeklyreport.activity.fragment.MemberFragment;
import com.aorise.weeklyreport.activity.fragment.PersonalFragment;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private ActivityMainBinding mViewDataBinding;

    private HomeFragment mHomeFragment;
    private MemberFragment mMemberFragment;
    private PersonalFragment mPersonalFragment;
    private Fragment currentFragment;

    private List<Fragment> fragmentList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mHomeFragment = new HomeFragment();
        mMemberFragment = new MemberFragment();
        mPersonalFragment = new PersonalFragment();
        addToList(mHomeFragment);
        addToList(mMemberFragment);
        addToList(mPersonalFragment);
        mViewDataBinding.pageIndex.setOnCheckedChangeListener(this);
        mViewDataBinding.groupHome.setChecked(true);

    }

    private void addToList(Fragment fragment) {
        if (fragment != null) {
            fragmentList.add(fragment);
        }
        LogT.i("fragmentList数量" + fragmentList.size());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*添加fragment*/
    private void showFragment(Fragment fragment) {
        LogT.d("显示" + fragment);
        /*判断该fragment是否已经被添加过  如果没有被添加  则添加*/
        if (!fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().add(R.id.main_vp, fragment).commit();
            /*添加到 fragmentList*/
            fragmentList.add(fragment);
        }
        LogT.i("fragmentList数量：" + fragmentList.size());
        for (Fragment frag : fragmentList) {

            if (frag != fragment) {
                /*先隐藏其他fragment*/
                LogT.i("隐藏" + fragment);
                getSupportFragmentManager().beginTransaction().hide(frag).commit();
            }
        }
        currentFragment = fragment;
//        LogT.d("Now fragment is " + fragment + " home hide ? " + mHomeFragment.isHidden());
        getSupportFragmentManager().beginTransaction().show(fragment).commit();

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.group_home:
                if (mHomeFragment == null) {
                    LogT.i("homeFragment 为空  创建");
                    mHomeFragment = new HomeFragment();
                }
                showFragment(mHomeFragment);
                break;
            case R.id.group_member:
                if (mMemberFragment == null) {
                    LogT.i("contactsFragment 为空  创建");
                    mMemberFragment = new MemberFragment();
                }
                showFragment(mMemberFragment);
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
