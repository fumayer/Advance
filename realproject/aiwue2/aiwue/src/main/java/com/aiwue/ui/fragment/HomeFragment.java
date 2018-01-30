package com.aiwue.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwue.R;
import com.aiwue.base.BaseFragment;
import com.aiwue.base.BaseMvpFragment;
import com.aiwue.model.CaptionTitle;
import com.aiwue.presenter.HomePresenter;
import com.aiwue.ui.activity.ChannelActivity;
import com.aiwue.ui.adapter.TitlePagerAdapter;
import com.aiwue.ui.view.colortrackview.ColorTrackTabViewIndicator;
import com.aiwue.ui.view.colortrackview.ColorTrackView;
import com.aiwue.utils.ConstantValue;
import com.aiwue.iview.IHomeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/17 0017.
 */
public class HomeFragment extends BaseMvpFragment<HomePresenter> implements IHomeView {//就是为了兼容
    @BindView(R.id.feed_top_search_hint)
    TextView feedTopSearchHint;
    @BindView(R.id.tab)
    ColorTrackTabViewIndicator tab;
    @BindView(R.id.icon_category)
    ImageView iconCategory;
    @BindView(R.id.vp)
    ViewPager vp;
    private CaptionTitle[] categoryList = {
                                new CaptionTitle(0, "热点"),
                                new CaptionTitle(-1, "朋友"),
                                new CaptionTitle(-2, "藏经"),
                                new CaptionTitle(-3, "视频"),
                                new CaptionTitle(-4, "图集")};
    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);//就是为了将view传到presenter中
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_home, null);
    }
    @Override
    protected void bindViews(View view) {
        ButterKnife.bind(this, rootView);
    }

    @Override
    protected void processLogic() {
        List<BaseFragment> fragments = new ArrayList<>();
        for (int i = 0; i < categoryList.length; i++) {
           if(categoryList[i].getId() == -3) {
               VideoListFragment fragment = new VideoListFragment();
               Bundle bundle = new Bundle();
               bundle.putString(ConstantValue.DATA, categoryList[i].getName());
               fragment.setArguments(bundle);
               fragments.add(fragment);
           }else if (categoryList[i].getId()==-4){

               AlbumListFragment fragment=new AlbumListFragment();
               Bundle bundle = new Bundle();
               bundle.putString(ConstantValue.DATA, categoryList[i].getName());
               fragment.setArguments(bundle);
               fragments.add(fragment);
           }else if (categoryList[i].getId()==0){
               HotspotFragment fragment = new HotspotFragment();
               Bundle bundle = new Bundle();
               bundle.putString(ConstantValue.DATA, categoryList[i].getName());
               fragment.setArguments(bundle);
               fragments.add(fragment);

           }
           else{
               ArticleListFragment fragment = new ArticleListFragment();
               Bundle bundle = new Bundle();
               bundle.putString(ConstantValue.DATA, categoryList[i].getName());
               fragment.setArguments(bundle);
               fragments.add(fragment);
           }

        }
        ///给ViewPager设置fragment的适配器
        vp.setAdapter(new TitlePagerAdapter(getChildFragmentManager(), fragments, categoryList));
        //传入fragment，出入位置
        tab.setTitles(categoryList, new ColorTrackTabViewIndicator.CorlorTrackTabBack() {
            @Override
            public void onClickButton(Integer position, ColorTrackView colorTrackView) {
                vp.setCurrentItem(position, false);
            }
        });
        final View tabChild = tab.getChildAt(0);
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        //重新测量
        tabChild.measure(w, h);
        //设置最小宽度，使其可以在滑动一部分距离
        tabChild.setMinimumWidth(tabChild.getMeasuredWidth() + tab.getTabWidth());
        vp.setOffscreenPageLimit(categoryList.length);
        tab.setupViewPager(vp);
    }
    @Override
    protected void setListener() {

    }
    static final int REQUEST_CHANNEL = 111;
    @OnClick({R.id.feed_top_search_hint, R.id.icon_category})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_category:
//                intent2Activity(ChannelActivity.class);
                Intent intent = new Intent(mContext, ChannelActivity.class);
                startActivityForResult(intent,REQUEST_CHANNEL);
                break;
        }
    }
}
