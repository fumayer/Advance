package com.aiwue.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.aiwue.R;
import com.aiwue.base.AiwueApplication;
import com.aiwue.base.AiwueConfig;
import com.aiwue.base.BaseMvpActivity;
import com.aiwue.base.BaseMvpFragment;
import com.aiwue.iview.IHotspotView;
import com.aiwue.model.Article;
import com.aiwue.model.Banner;
import com.aiwue.model.RecommendFriends;
import com.aiwue.presenter.HotspotPresenter;
import com.aiwue.ui.activity.ArticleDetailActivity;
import com.aiwue.ui.adapter.ArticleListAdapter;
import com.aiwue.ui.adapter.hotspotadapter.BannerListAdapter;
import com.aiwue.ui.adapter.hotspotadapter.RecFriendsListAdapter;
import com.aiwue.ui.view.ImageBrowseView;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotspotFragment extends BaseMvpFragment<HotspotPresenter> implements IHotspotView {
    @BindView(R.id.scrollview_hotspot_fragment)
    public ScrollView mScrollView;
    @BindView(R.id.banner_hotspot_fragment)
    public ConvenientBanner mConvenientBanner;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.horizontal_recyclerview)
    public RecyclerView recFriendsRecyclerView;
    //获取banner列表
    private List<String> mBannerUrls = new ArrayList<>();
    //精选文章
    protected List<Article> mArticleDatas = new ArrayList<>();
    //推荐武友
    protected List<RecommendFriends> mRecommendFriendsDatas = new ArrayList<>();
    protected BaseQuickAdapter mArticleListAdapter;
    protected BaseQuickAdapter mRecFriendsListAdapter;
    private  int pIndex = 1;
    //加载文章个数
    private int pArticleSize = 2;
    //加载武友个数
    private int pRecFriendsSize = 10;

    @Override
    protected HotspotPresenter createPresenter() {
        return new HotspotPresenter(this);
    }
    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_hotspot, null);
        ButterKnife.bind(this, v);
        return v;
    }
    @Override
    protected void bindViews(View view) {
    }
    @Override
    protected void processLogic() {
        initCommonRecyclerView(createArticleListAdapter(), null);
        initHorizontalRecyclerView(createRecFriendsListAdapter(), null);
    }

    private BaseQuickAdapter createArticleListAdapter() {
        mArticleListAdapter = new ArticleListAdapter(mArticleDatas);
//        mArticleListAdapter.setEnableLoadMore(true);
//        mArticleListAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        return mArticleListAdapter;
    }
    private BaseQuickAdapter createRecFriendsListAdapter() {
        mRecFriendsListAdapter = new RecFriendsListAdapter(mRecommendFriendsDatas);
        mRecFriendsListAdapter.setEnableLoadMore(true);
        mRecFriendsListAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        return mRecFriendsListAdapter;
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        //banner参数
        mvpPresenter.getBannerList(100);
        //热点文章参数
        pIndex = 1;
        mvpPresenter.getArticleList(pArticleSize);
        mvpPresenter.getRecommendFriendsList(pRecFriendsSize);
    }
    @Override
    protected void setListener() {

        mArticleListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter var1, View var2, int i){

                Article article = (Article)mArticleListAdapter.getData().get(i);
                Intent intent = new Intent(mContext, ArticleDetailActivity.class);
                intent.putExtra("articleId",article.getId());
                startActivity(intent);
            }
        });
    }
    @Override
    public void onGetBannerListSuccess(Boolean success, String err, List<Banner> response) {

        if(success){
            for (Banner banner1 : response) {
                mBannerUrls.add(AiwueConfig.AIWUE_API_PIC_URL + banner1.getPicName());
            }
            mConvenientBanner.setPages(new CBViewHolderCreator() {
                @Override
                public Object createHolder() {
                    return new LocalImageHolderView() ;
                }
            },mBannerUrls).setPageIndicator(new int[]{R.drawable.circle_banner_noemal, R.drawable.circle_banner_selected});

        }else {
            Toast.makeText(AiwueApplication.getAppContext(), err, Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public void onGetArticleListSuccess(Boolean success, String err, List<Article> response) {

        if (success) {
            if (response == null) { //如果返回null，表明到末尾了
                if (pIndex > 1)
                    mArticleListAdapter.loadMoreEnd();
                return;
            }
            if (response.size() < pArticleSize) { //如果返回的记录数比期望的小，则表明到末尾了
                mArticleListAdapter.loadMoreEnd();
            }
            if (pIndex == 1) {
                mArticleListAdapter.getData().clear();
            }

            mArticleListAdapter.addData(response);
            mArticleListAdapter.notifyDataSetChanged();

        }else {
            mArticleListAdapter.loadMoreFail();
        }

    }

    @Override
    public void onGetRecFriendsListSuccess(Boolean success, String err, List<RecommendFriends> response) {
        if(success){
            if(response!=null){
                mRecFriendsListAdapter.addData(response);
                mRecFriendsListAdapter.notifyDataSetChanged();
            }else {
                Log.e("sss", "sddsds");
            }

        }else {
            mRecFriendsListAdapter.loadMoreFail();
        }
    }

    public class LocalImageHolderView implements Holder<String> {
        private ImageView imageView;
        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }
        @Override
        public void UpdateUI(Context context, int i, String s) {
            Glide.with(HotspotFragment.this).load(s).into(imageView);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mConvenientBanner.startTurning(2000);
    }
    @Override
    public void onPause() {
        super.onPause();
        mConvenientBanner.stopTurning();
    }
}
