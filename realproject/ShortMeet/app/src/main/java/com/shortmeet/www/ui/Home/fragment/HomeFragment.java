package com.shortmeet.www.ui.Home.fragment;


import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.shortmeet.www.Base.BaseFragment;
import com.shortmeet.www.R;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.ui.Home.adapter.HomeFragsViewPagerAdapter;
import com.shortmeet.www.utilsUsed.StatusBarUtil;
import com.shortmeet.www.utilsUsed.UiUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
// home  页面   包括推荐和关注
public class HomeFragment extends BaseFragment implements IMVPView {
    //标识
    private static final String TAG = "HomeFragment";
    // Fly 注： 导航条
    private ScrollIndicatorView scrollIndicatorHome;
    //Viewpager
    private ViewPager vpHome;
    //fragviewpager 特别适配器
    HomeFragsViewPagerAdapter mHomeFragsViewPagerAdapter;
    //指示器数据集合
    private static final String[] themes = {"推荐","关注"};
    //指示器
    private IndicatorViewPager mIndicatorViewPager;
    //frag 集合
    private List<Fragment> frags = new ArrayList<>();
    // Fly 注：代替状态栏
    private View topStusbar;
    @Override
    public int setFragRootView() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView() {
    scrollIndicatorHome = (ScrollIndicatorView)contentView.findViewById(R.id.scroll_indicator_home);
    vpHome = (ViewPager)contentView.findViewById(R.id.vp_home);
    topStusbar = (View) contentView.findViewById(R.id.top_stusbar);
    initStusBar();
    initPager();
    }
    public void initPager(){
        float unSelectSize = 12;
        float selectSize = unSelectSize * 1.3f;
        scrollIndicatorHome.setOnTransitionListener(new OnTransitionTextListener().setColor(UiUtils.getColor(R.color.black_1), UiUtils.getColor(R.color.black_2)).setSize(selectSize, selectSize));
        ColorBar  colorBar=new ColorBar(mContext,UiUtils.getColor(R.color.black_1), 10);
        colorBar.getSlideView().setBackgroundResource(R.drawable.shape_guideline_mywork_minefrag);
        colorBar.setWidth(30);
        colorBar.setGravity(ScrollBar.Gravity.BOTTOM);
        scrollIndicatorHome.setScrollBar(colorBar);
        mIndicatorViewPager = new IndicatorViewPager(scrollIndicatorHome, vpHome);
        mHomeFragsViewPagerAdapter= new HomeFragsViewPagerAdapter(this.getChildFragmentManager(),mContext, Arrays.asList(themes),frags);
        mIndicatorViewPager.setAdapter(mHomeFragsViewPagerAdapter);
    }
    public  void  initStusBar(){
        int stusBarHeight= StatusBarUtil.getStatusBarHeight(mActivity);
        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) topStusbar.getLayoutParams();
        layoutParams.height=stusBarHeight;
        topStusbar.setBackgroundColor(Color.BLACK);
    }
    @Override
    public void initListener() {  }
    @Override
    public void initData() {
        int length=themes.length;
        if(frags.size()!=0){
            frags.clear();
        }
        for(int i=0;i<length;i++){
            if(i==0){
                frags.add(RecomandFragment.newInstance());
            }else if(i==1){
                frags.add(CareZhuFragment.newInstance());
            }
        }
        mHomeFragsViewPagerAdapter.notifyDataSetChanged();
    }
    @Override
    public void setData(Object o, int id) {

    }

}
