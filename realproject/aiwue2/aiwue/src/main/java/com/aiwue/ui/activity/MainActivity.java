package com.aiwue.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiwue.R;
import com.aiwue.base.BaseActivity;
import com.aiwue.ui.fragment.FragmentController;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;
    @BindView(R.id.ivIconHome)
    ImageView ivIconHome;
    @BindView(R.id.tvTextHome)
    TextView tvTextHome;
    @BindView(R.id.tvBadgeHome)
    TextView tvBadgeHome;
    @BindView(R.id.ivIconVideo)
    ImageView ivIconVideo;
    @BindView(R.id.tvTextVideo)
    TextView tvTextVideo;
    @BindView(R.id.ivIconAttention)
    ImageView ivIconAttention;
    @BindView(R.id.tvTextAttention)
    TextView tvTextAttention;
    @BindView(R.id.ivIconMe)
    ImageView ivIconMe;
    @BindView(R.id.tvTextMe)
    TextView tvTextMe;
    //    @BindView(R.id.bottomNav)
//    BottomNavigationLayout bottomNav;
    private FragmentController mController;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void bindViews() {
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mController = FragmentController.getInstance(this, R.id.fl_content, true);
        mController.showFragment(0);
    }

    private void initBottomNav() {
//        bottomNav.builder().setItems(
//                new NavigationItem( this, new int[]{R.drawable.b_newhome_tabbar, R.drawable.b_newhome_tabbar_press}, "首页", new int[]{getColorRes(R.color.font_main), getColorRes(R.color.font_main_p)}),
//                new NavigationItem( this, new int[]{R.drawable.b_newvideo_tabbar, R.drawable.b_newvideo_tabbar_press}, "视频", new int[]{getColorRes(R.color.font_main), getColorRes(R.color.font_main_p)}),
//                new NavigationItem( this, new int[]{R.drawable.b_newcare_tabbar, R.drawable.b_newcare_tabbar_press}, "关注", new int[]{getColorRes(R.color.font_main), getColorRes(R.color.font_main_p)}),
//                new NavigationItem( this, new int[]{R.drawable.b_newmine_tabbar, R.drawable.b_newmine_tabbar_press}, "我的", new int[]{getColorRes(R.color.font_main), getColorRes(R.color.font_main_p)})
//        ).setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onSelected(int position, View itemView) {
//                mController.showFragment(position);
//            }
//        }).build();
    }

    private View lastSelectedIcon;
    private View lastSelectedText;

    public void onPublishBtnClick(View view) {
        showToast("click publish button");
    }

    @Override
    protected void setListener() {
        for (int i = 0; i < llBottom.getChildCount(); i++) {
            if (i == 0) {
                //默认选中首页
                setSelectIcon(ivIconHome, tvTextHome);
            }
            final int position = i;
            llBottom.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastSelectedIcon != null) lastSelectedIcon.setSelected(false);
                    if (lastSelectedText != null) lastSelectedText.setSelected(false);

                    RelativeLayout rl = (RelativeLayout) v;
                    ImageView icon = (ImageView) rl.getChildAt(0);
                    TextView text = (TextView) rl.getChildAt(1);
                    mController.showFragment(position);
                    setSelectIcon(icon, text);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        int id = getIntent().getIntExtra("userloginflag", 0);
        if (id == 1) {
            mController.showFragment(0);
        }
        super.onResume();
    }

    private void setSelectIcon(ImageView iv, TextView tv) {
        iv.setSelected(true);
        tv.setSelected(true);
        lastSelectedIcon = iv;
        lastSelectedText = tv;
    }

    @Subscribe
    public void onEvent(String event) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mController.onDestroy();
    }
}
