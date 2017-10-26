package com.quduquxie.base.module.main.activity.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.quduquxie.R;
import com.quduquxie.application.QuApplication;
import com.quduquxie.application.SharedPreferencesUtil;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.BaseActivity;
import com.quduquxie.base.bean.Update;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.listener.MainInteractiveListener;
import com.quduquxie.base.module.main.activity.MainInterface;
import com.quduquxie.base.module.main.activity.component.DaggerMainComponent;
import com.quduquxie.base.module.main.activity.module.MainModule;
import com.quduquxie.base.module.main.activity.presenter.MainPresenter;
import com.quduquxie.base.module.main.fragment.selected.view.SelectedFragment;
import com.quduquxie.base.module.main.fragment.shelf.view.BookShelfFragment;
import com.quduquxie.base.module.main.fragment.library.view.LibraryFragment;
import com.quduquxie.base.module.main.fragment.mine.view.MineFragment;
import com.quduquxie.base.util.AppUpdateDialogHelper;
import com.quduquxie.base.util.AppUpdateHelper;
import com.quduquxie.base.widget.dialog.CustomDialogFragment;
import com.quduquxie.base.widget.dialog.CustomDialogListener;
import com.quduquxie.base.widget.dialog.CustomLoadingDialog;
import com.quduquxie.communal.utils.UserUtil;
import com.quduquxie.db.LiteratureDao;
import com.quduquxie.db.UserDao;
import com.quduquxie.function.creation.create.view.LiteratureCreateActivity;
import com.quduquxie.function.creation.literature.view.LiteratureListActivity;
import com.quduquxie.model.User;
import com.quduquxie.revise.view.ReviseUserActivity;
import com.quduquxie.wxapi.WXEntryActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

public class MainActivity extends BaseActivity<MainPresenter> implements MainInterface.View, AppUpdateHelper.CheckApplicationUpdateListener, AppUpdateDialogHelper.UpdateDialogCallback, MainInteractiveListener {

    @BindView(R.id.main_bottom)
    LinearLayout main_bottom;
    @BindView(R.id.main_bottom_shelf)
    RelativeLayout main_bottom_shelf;
    @BindView(R.id.main_bottom_selected)
    RelativeLayout main_bottom_selected;
    @BindView(R.id.main_bottom_literature)
    RelativeLayout main_bottom_literature;
    @BindView(R.id.main_bottom_store)
    RelativeLayout main_bottom_store;
    @BindView(R.id.main_bottom_mine)
    RelativeLayout main_bottom_mine;
    @BindView(R.id.main_bottom_mine_count)
    ImageView main_bottom_mine_count;

    @BindView(R.id.main_shelf_guide)
    ViewStub main_shelf_guide;

    private View main_shelf_guide_view;

    @Inject
    MainPresenter mainPresenter;

    private FragmentManager fragmentManager;

    private UserDao userDao;
    private LiteratureDao literatureDao;

    public int position = 0;

    private BookShelfFragment bookShelfFragment;
    private SelectedFragment selectedFragment;
    private LibraryFragment libraryFragment;
    private MineFragment mineFragment;

    private AppUpdateHelper appUpdateHelper;

    private CustomDialogFragment customDialogFragment;

    private CustomLoadingDialog customLoadingDialog;

    private static int BACK_COUNT;

    private boolean closing = false;

    private final static int BACK = 0x80;

    private SharedPreferencesUtil sharedPreferencesUtil;

    public static Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case BACK:
                    BACK_COUNT = 0;
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);
        initializeParameter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.hasExtra("position")) {
            int position = intent.getIntExtra("position", 0);
            if (position > -1 && position < 4) {
                showFragment(position);
            }
        }
    }

    @Override
    protected void setActivityComponent(ApplicationComponent applicationComponent) {
        DaggerMainComponent.builder()
                .applicationComponent(applicationComponent)
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initializeParameter() {
        ButterKnife.bind(this);

        fragmentManager = getSupportFragmentManager();

        sharedPreferencesUtil = this.loadSharedPreferencesUtil();

        userDao = UserDao.getInstance(getApplicationContext());

        literatureDao = LiteratureDao.getInstance(getApplicationContext());

        if (applicationUtil.getDownloadService() == null) {
            applicationUtil.bindDownloadService();
        }

        if (main_bottom_mine_count != null) {
            QuApplication application = QuApplication.getInstance();
            if (application.loadCommentCount() > 0 || application.loadReplyCount() > 0) {
                main_bottom_mine_count.setVisibility(View.VISIBLE);
            } else {
                main_bottom_mine_count.setVisibility(View.GONE);
            }
        }

        showFragment(position);

        if (appUpdateHelper == null) {
            appUpdateHelper = new AppUpdateHelper(this);
        }

        appUpdateHelper.setOnCheckUpdateListener(this);
        appUpdateHelper.checkApiUpdate();
    }

    @Override
    public void recycle() {

    }

    @Override
    public void startLoginActivity() {
        UserUtil.deleteUser(getApplicationContext());
        Intent intent = new Intent();
        intent.putExtra("exit_login", true);
        intent.setClass(this, WXEntryActivity.class);
        startActivity(intent);
    }

    @Override
    public void startLiteratureCreateActivity() {
        startActivity(new Intent(this, LiteratureCreateActivity.class));
    }

    @Override
    public void startLiteratureActivity() {
        startActivity(new Intent(this, LiteratureListActivity.class));
    }

    @Override
    public void showLoadingDialog(String message) {
        if (customLoadingDialog == null) {
            customLoadingDialog = new CustomLoadingDialog();
        }

        customLoadingDialog.setPrompt(message);

        if (!isFinishing()) {
            if (customLoadingDialog.isAdded()) {
                customLoadingDialog.setShowsDialog(true);
            } else {
                customLoadingDialog.show(getSupportFragmentManager(), "CustomLoadingDialog");
            }
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (!isFinishing() && customLoadingDialog != null && customLoadingDialog.getShowsDialog()) {
            customLoadingDialog.dismiss();
        }
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void applicationUpdateSuccess(Update update) {
        AppUpdateDialogHelper appUpdateDialogHelper = new AppUpdateDialogHelper(MainActivity.this);
        appUpdateDialogHelper.setUpdateDialogCallback(this);
        appUpdateDialogHelper.apiUpdate(update, true, isFinishing(), fragmentManager);
    }

    @Override
    public void applicationHasBeenUpdated() {
        Logger.e("应用检查更新: 已更新至最新版本！");
    }

    @Override
    public void applicationUpdateError() {
        Logger.e("应用检查更新: 检查更新异常！");
    }

    @Override
    public void appUpdateDialogCancel(boolean cancel) {
        finish();
    }

    @OnClick({R.id.main_bottom_shelf, R.id.main_bottom_selected, R.id.main_bottom_literature, R.id.main_bottom_store, R.id.main_bottom_mine})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.main_bottom_shelf:
                if (position != 0) {
                    showFragment(0);
                }
                break;
            case R.id.main_bottom_selected:
                if (position != 1) {
                    showFragment(1);
                }
                break;
            case R.id.main_bottom_store:
                if (position != 2) {
                    showFragment(2);
                }
                break;
            case R.id.main_bottom_mine:
                if (position != 3) {
                    changeCommentCountState(false);
                    showFragment(3);
                }
                break;
            case R.id.main_bottom_literature:
                checkUserLoginState();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (bookShelfFragment != null && bookShelfFragment.showBottomView()) {
                bookShelfFragment.hideDeleteView();
            } else if (position != 0) {
                showFragment(0);
            } else {
                doubleClickFinish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void changeTitle(String title, String subtitle) {
        if (bookShelfFragment != null) {
            bookShelfFragment.changeTitle(title, subtitle);
        }
    }

    @Override
    public void showEmptyPrompt() {
        if (bookShelfFragment != null) {
            bookShelfFragment.showShelfPrompt();
        }
    }

    @Override
    public void hideEmptyPrompt() {
        if (bookShelfFragment != null) {
            bookShelfFragment.hideShelfPrompt();
        }
    }

    @Override
    public void cancelUpdateStatus(String id) {
        if (bookShelfFragment != null) {
            bookShelfFragment.cancelUpdateStatus(id);
        }
    }

    @Override
    public void resetDeleteSelected(int count) {
        if (bookShelfFragment != null) {
            bookShelfFragment.resetDeleteSelected(count);
        }
    }

    @Override
    public void showFragment(int position) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        hideFragment(fragmentTransaction);

        switch (position) {
            case 0:
                if (bookShelfFragment == null) {
                    bookShelfFragment = new BookShelfFragment();
                    fragmentTransaction.add(R.id.main_content, bookShelfFragment);
                } else {
                    fragmentTransaction.show(bookShelfFragment);
                }

                switchNavigationState(position);
                break;
            case 1:
                if (selectedFragment == null) {
                    selectedFragment = new SelectedFragment();
                    fragmentTransaction.add(R.id.main_content, selectedFragment);
                } else {
                    fragmentTransaction.show(selectedFragment);
                }

                switchNavigationState(position);
                break;
            case 2:
                if (libraryFragment == null) {
                    libraryFragment = new LibraryFragment();
                    fragmentTransaction.add(R.id.main_content, libraryFragment);
                } else {
                    fragmentTransaction.show(libraryFragment);
                }
                switchNavigationState(position);
                break;
            case 3:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    fragmentTransaction.add(R.id.main_content, mineFragment);
                } else {
                    fragmentTransaction.show(mineFragment);
                }
                switchNavigationState(position);
                break;
        }

        this.position = position;

        if (!isFinishing()) {
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void showBottomView() {
        if (main_bottom != null && main_bottom.getVisibility() == View.GONE) {
            main_bottom.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideBottomView(int count) {
        if (main_bottom != null && main_bottom.getVisibility() == View.VISIBLE) {
            main_bottom.setVisibility(View.GONE);
        }

        if (bookShelfFragment != null) {
            bookShelfFragment.changeDeleteView(true, count);
        }
    }

    @Override
    public void checkShelfGuide() {
        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = this.loadSharedPreferencesUtil();
        }

        boolean guide = sharedPreferencesUtil.loadBoolean(BaseConfig.FLAG_SHELF_GUIDE, false);

        if (!guide) {
            if (main_shelf_guide_view == null) {
                main_shelf_guide_view = main_shelf_guide.inflate();
            }

            final ImageView shelf_guide_book_date = (ImageView) main_shelf_guide_view.findViewById(R.id.shelf_guide_book_date);
            final ImageView shelf_guide_slide = (ImageView) main_shelf_guide_view.findViewById(R.id.shelf_guide_slide);
            final ImageView shelf_guide_delete = (ImageView) main_shelf_guide_view.findViewById(R.id.shelf_guide_delete);

            ImageView shelf_guide_close = (ImageView) main_shelf_guide_view.findViewById(R.id.shelf_guide_close);

            Glide.with(this)
                    .load(R.drawable.icon_shelf_guide_slide)
                    .asGif()
                    .placeholder(R.drawable.icon_shelf_guide_slide)
                    .error(R.drawable.icon_shelf_guide_slide)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(shelf_guide_slide);

            shelf_guide_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (shelf_guide_book_date.getVisibility() == View.VISIBLE && shelf_guide_slide.getVisibility() == View.VISIBLE) {
                        shelf_guide_book_date.setVisibility(View.GONE);
                        shelf_guide_slide.setVisibility(View.GONE);

                        shelf_guide_delete.setVisibility(View.VISIBLE);
                    } else {

                        if (sharedPreferencesUtil == null) {
                            sharedPreferencesUtil = loadSharedPreferencesUtil();
                        }

                        sharedPreferencesUtil.insertBoolean(BaseConfig.FLAG_SHELF_GUIDE, true);

                        main_shelf_guide_view.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @Override
    public void showShelfSlideMore() {
        if (bookShelfFragment != null) {
            bookShelfFragment.showShelfSlideMore();
        }
    }

    @Override
    public void hideShelfSlideMore() {
        if (bookShelfFragment != null) {
            bookShelfFragment.hideShelfSlideMore();
        }
    }

    @Override
    public void storeContentScrollDistance(int distance) {
        switch (position) {
            case 0:
                bookShelfFragment.storeContentScrollDistance(distance);
                break;
        }
    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (bookShelfFragment != null) {
            fragmentTransaction.hide(bookShelfFragment);
        }

        if (selectedFragment != null) {
            fragmentTransaction.hide(selectedFragment);
        }

        if (libraryFragment != null) {
            fragmentTransaction.hide(libraryFragment);
        }

        if (mineFragment != null) {
            fragmentTransaction.hide(mineFragment);
        }
    }

    private void switchNavigationState(int position) {
        if (main_bottom_shelf != null) {
            main_bottom_shelf.setSelected(position == 0);
        }

        if (main_bottom_selected != null) {
            main_bottom_selected.setSelected(position == 1);
        }

        if (main_bottom_store != null) {
            main_bottom_store.setSelected(position == 2);
        }

        if (main_bottom_mine != null) {
            main_bottom_mine.setSelected(position == 3);
        }
    }

    private void checkUserLoginState() {
        if (userDao == null) {
            userDao = UserDao.getInstance(getApplicationContext());
        }

        User user = userDao.getUser();

        if (user == null) {
            Intent intent = new Intent();
            intent.putExtra("literature", true);
            intent.setClass(this, WXEntryActivity.class);
            startActivity(intent);
        } else {
            if (!TextUtils.isEmpty(user.user_name)) {
                if (literatureDao == null) {
                    literatureDao = LiteratureDao.getInstance(getApplicationContext());
                }
                if (literatureDao.getLiteratureList().size() == 0) {
                    mainPresenter.initLiteratureParameter();
                } else {
                    startLiteratureActivity();
                }
            } else {
                showBindingDialog();
            }
        }
    }

    private void showBindingDialog() {
        if (customDialogFragment == null) {
            customDialogFragment = new CustomDialogFragment();
        }

        customDialogFragment.setPrompt("因为您当前登录账号未绑定手机号码，您的作品将无法进行签约。如果您希望成为青果阅读的签约作家获得收益，请您绑定手机号码。\n" + "您绑定的手机号码和密码同样可以在青果官网：www.qingoo.cn登录写作。");
        customDialogFragment.setFirstOption("绑定手机");
        customDialogFragment.setSecondOption("立即写作");
        customDialogFragment.setCustomDialogListener(new CustomDialogListener() {
            @Override
            public void onFirstOptionClicked() {
                if (customDialogFragment.getShowsDialog()) {
                    Intent intent = new Intent();
                    intent.putExtra("information", "binding");
                    intent.setClass(getApplicationContext(), ReviseUserActivity.class);
                    startActivity(intent);
                    hideCustomDialogFragment();
                }
            }

            @Override
            public void onSecondOptionClicked() {
                if (customDialogFragment.getShowsDialog()) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), LiteratureListActivity.class);
                    startActivity(intent);
                    hideCustomDialogFragment();
                }
            }
        });

        if (!isFinishing()) {
            if (customDialogFragment.isAdded()) {
                customDialogFragment.setShowsDialog(true);
            } else {
                customDialogFragment.show(getSupportFragmentManager(), "CustomDialogBindingFragment");
            }
        }
    }

    public void hideCustomDialogFragment() {
        if (!isFinishing() && customDialogFragment != null && customDialogFragment.getShowsDialog()) {
            customDialogFragment.dismiss();
        }
    }

    private void changeCommentCountState(boolean state) {
        if (main_bottom_mine_count != null) {
            main_bottom_mine_count.setVisibility(state ? View.VISIBLE : View.GONE);
        }
    }

    private void doubleClickFinish() {
        BACK_COUNT++;
        if (BACK_COUNT == 1) {
            showPromptMessage("再按一次退出青果阅读！");
        } else if (BACK_COUNT > 1 && !closing) {
            closing = true;

            if (userDao == null) {
                userDao = UserDao.getInstance(getApplicationContext());
            }

            User user = userDao.getUser();

            if (user != null && !TextUtils.isEmpty(UserDao.getToken(this))) {
                showPromptMessage("正在同步书架数据...");
                mainPresenter.updateBookshelf();
            } else {
                finish();
            }
        }

        Message message = handler.obtainMessage(0);
        message.what = BACK;
        handler.sendMessageDelayed(message, 2000);
    }
}