package com.moa.rxdemo.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moa.baselib.RoutePath;
import com.moa.baselib.base.net.mvp.SimpleValueCallback;
import com.moa.baselib.base.ui.BaseActivity;
import com.moa.baselib.utils.AppUtils;
import com.moa.baselib.utils.PermissionHelper;
import com.moa.rxdemo.R;
import com.moa.rxdemo.mvp.bean.CheckUpdateResponse;
import com.moa.rxdemo.mvp.model.CheckUpdateModel;
import com.moa.rxdemo.mvp.view.my.MyFragment;
import com.moa.rxdemo.utils.AppConfig;
import com.moa.rxdemo.utils.UpdateHelper;

import java.util.HashMap;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * 主界面
 * <p>
 * Created by：wangjian on 2017/12/20 16:25
 */
@Route(path = RoutePath.MODULE_APP_ENTER_ACTIVITY)
public class MainActivity extends BaseActivity {
    
    public static final String FRAG_TAG_HOME = "fragment.home";
    public static final String FRAG_TAG_RACE = "fragment.race";
    public static final String FRAG_TAG_GRADING = "fragment.grading";
    public static final String FRAG_TAG_SHOP = "fragment.shop";
    public static final String FRAG_TAG_MY = "fragment.my";
    
    private BottomNavigationView bottomNavigationView;
    private Fragment mCurrentFragment;

    private TextView tvRight = null;
    
    @Override
    protected int getLayoutId() {
        return R.layout.tt_activity_main;
    }
    
    @Override
    protected void initHeader() {
        super.initHeader();
        showBackButton(false);

        // tvRight = getRightTv();
        // tvRight.setText(R.string.finish);
        // tvRight.setOnClickListener(this);
    }
    
    @Override
    protected void initView() {
        mCurrentFragment = new DemosFragment();
        replaceFragment(mCurrentFragment, R.id.fragment_container, FRAG_TAG_HOME, false);
        
        bottomNavigationView = findViewById(R.id.navigation);
        // 清除底部图标tint变色
        // 默认使用文字active颜色作为图片的tint
        bottomNavigationView.setItemIconTintList(null);
        // 设置底部文字颜色
        // 这是相同的字体大小可清除缩放动画，但是第一个view初始化的时候，没有更新，最好在布局中设置
//        参考地址：https://blog.csdn.net/u013626215/article/details/85161957
//        bottomNavigationView.setItemTextAppearanceInactive(R.style.bottom_normal_text);
//        bottomNavigationView.setItemTextAppearanceActive(R.style.bottom_selected_text);

        // 控制当tab大于3个时不完全显示lable
        // 早期版本需要自行设置参考https://blog.csdn.net/qq_19973845/article/details/82151204
        // app:labelVisibilityMode="labeled"
        // bottomNavigationView.setLabelVisibilityMode(1);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        switchFragment(FRAG_TAG_HOME);
                        return true;
                    case R.id.navigation_races:
                        switchFragment(FRAG_TAG_RACE);
                        return true;
                    case R.id.navigation_grading:
                        switchFragment(FRAG_TAG_GRADING);
                        return true;
                    // case R.id.navigation_shop:
                    //     switchFragment(FRAG_TAG_SHOP);
                    //     return true;
                    case R.id.navigation_my:
                        switchFragment(FRAG_TAG_MY);
                        return true;
                    default:
                        return false;
                }
            }
        });
        
    }
    
    @Override
    protected void initData() {
        super.initData();
        // 检测是否有sdcard访问权限
        boolean hasPer = PermissionHelper.checkSDcardPermission(null, this, 0);

        // 是否需要检测更新
        if(AppConfig.getShowUpdateDialog()){
            //checkUpdate();
        }
    }

    private void checkUpdate(){
        new CheckUpdateModel().checkUpdate(AppUtils.getVersionName(), new SimpleValueCallback<CheckUpdateResponse>() {

            @Override
            public void onSuccess(CheckUpdateResponse value) {
                UpdateHelper.dealUpdateValue(MainActivity.this, value, true);
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    boolean showBadge;

    @Override
    public void onClick(View view) {
        super.onClick(view);
        // if(view == tvRight){
        //     showBadge = !showBadge;
        //     showBadgeView(0, showBadge ? 22:0);
        // }
    }

    /**
     * 用作缓存每一个tab上的badge防止重复创建，导致重复添加
     */
    HashMap<Integer, Badge> badgeHashMap = new HashMap<>();
    
    /**
     * BottomNavigationView显示角标
     *
     * @param viewIndex  tab索引
     * @param showNumber 显示的数字，小于等于0是将不显示
     */
    public void showBadgeView(int viewIndex, int showNumber) {
        
        if (badgeHashMap.get(viewIndex) != null) {
            badgeHashMap.get(viewIndex).setBadgeNumber(showNumber);
            return;
        }
        
        // 具体child的查找和view的嵌套结构请在源码中查看
        // 从bottomNavigationView中获得BottomNavigationMenuView
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        // 从BottomNavigationMenuView中获得childview, BottomNavigationItemView
        if (viewIndex < menuView.getChildCount()) {
            // 获得viewIndex对应子tab
            View view = menuView.getChildAt(viewIndex);
            // 从子tab中获得其中显示图片的ImageView
            View icon = view.findViewById(com.google.android.material.R.id.icon);
            // 获得图标的宽度
            int iconWidth = icon.getWidth();
            // 获得tab的宽度/2
            int tabWidth = view.getWidth() / 2;
            // 计算badge要距离右边的距离
            int spaceWidth = tabWidth - iconWidth;
            
            // 显示badegeview
            Badge badge = new QBadgeView(this).bindTarget(view).setGravityOffset(spaceWidth, 3, false).setBadgeNumber(
                showNumber);
            badgeHashMap.put(viewIndex, badge);
        }
    }
    
    /**
     * 处理fragment中切换
     *
     * @param tag
     */
    private void switchFragment(String tag) {
        FragmentManager manager = getSupportFragmentManager();
        
        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment == null) {
            if (FRAG_TAG_HOME.equals(tag)) {
                fragment = new DemosFragment();
            }
            else if (FRAG_TAG_RACE.equals(tag)) {
            }
            else if (FRAG_TAG_GRADING.equals(tag)) {
            }
            else if (FRAG_TAG_MY.equals(tag)) {
                fragment = new MyFragment();
            }
        }
        FragmentTransaction ft = manager.beginTransaction();
        if (fragment != null && fragment.isAdded()) {
            ft.hide(mCurrentFragment).show(fragment).commitAllowingStateLoss();
        }
        else {
            if (fragment != null) {
                ft.hide(mCurrentFragment).add(R.id.fragment_container, fragment, tag).commitAllowingStateLoss();
            }
        }
        mCurrentFragment = fragment;
    }


    // 记录当前点击时间
    private long currentTimeMills;

    @Override
    public void onBackPressed() {
        // 点击2次退出程序
        if(currentTimeMills <= 0 || (System.currentTimeMillis() - currentTimeMills > 1500)){
            currentTimeMills = System.currentTimeMillis();
            showToast(R.string.logout_click_back_more);
            return;
        }

        if(System.currentTimeMillis() - currentTimeMills < 1500){
            super.onBackPressed();
        }
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }
}
