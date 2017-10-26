package com.quduquxie.base.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quduquxie.R;
import com.quduquxie.base.bean.Category;
import com.quduquxie.communal.widget.GlideRoundTransform;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/24.
 * Created by crazylei.
 */

public class MainLibraryCategoryHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.library_category)
    public RelativeLayout library_category;

    @BindView(R.id.library_category_image)
    ImageView library_category_image;
    @BindView(R.id.library_category_title)
    TextView library_category_title;

    private ArrayList<String> category_boy = new ArrayList<String>() {
        {
            add("玄幻奇幻");
            add("都市小说");
            add("悬疑惊悚");
            add("武侠仙侠");
            add("科幻灵异");
            add("热血青春");
            add("历史军事");
            add("游戏体育");
            add("乡村生活");
            add("官场职场");
            add("二次元");
        }
    };

    private ArrayList<String> category_girl = new ArrayList<String>() {
        {
            add("总裁豪门");
            add("穿越重生");
            add("古典架空");
            add("宫斗宅斗");
            add("青春校园");
            add("幻想灵异");
            add("都市高干");
            add("女尊王朝");
            add("仙侠种田");
            add("婚恋职场");
            add("同人衍生");
            add("耽美小说");
            add("二次元");
        }
    };

    private ArrayList<Integer> category_boy_resources = new ArrayList<Integer>() {
        {
            add(R.drawable.icon_channel_boy_xuan);
            add(R.drawable.icon_channel_boy_du);
            add(R.drawable.icon_channel_boy_jing);
            add(R.drawable.icon_channel_boy_xian);
            add(R.drawable.icon_channel_boy_ke);
            add(R.drawable.icon_channel_boy_re);
            add(R.drawable.icon_channel_boy_jun);
            add(R.drawable.icon_channel_boy_you);
            add(R.drawable.icon_channel_boy_xiang);
            add(R.drawable.icon_channel_boy_guan);
            add(R.drawable.icon_channel_boy_er);
        }
    };

    private ArrayList<Integer> category_girl_resources = new ArrayList<Integer>() {
        {
            add(R.drawable.icon_channel_girl_zong);
            add(R.drawable.icon_channel_girl_chuan);
            add(R.drawable.icon_channel_girl_gu);
            add(R.drawable.icon_channel_girl_gong);
            add(R.drawable.icon_channel_girl_qing);
            add(R.drawable.icon_channel_girl_huan);
            add(R.drawable.icon_channel_girl_du);
            add(R.drawable.icon_channel_girl_nv);
            add(R.drawable.icon_channel_girl_xian);
            add(R.drawable.icon_channel_girl_hun);
            add(R.drawable.icon_channel_girl_tong);
            add(R.drawable.icon_channel_girl_dan);
            add(R.drawable.icon_channel_girl_er);
        }
    };


    public MainLibraryCategoryHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void initializeView(Context context, String title, Category category) {

        if (category != null) {

            if ("女频书库".equals(title)) {
                int index = category_girl.indexOf(category.title);
                boolean state = checkCategoryHot(category.title);

                if (index != -1) {
                    Glide.with(context)
                            .load(category_girl_resources.get(index))
                            .skipMemoryCache(true)
                            .transform(new GlideRoundTransform(context))
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .error(state ? R.drawable.icon_category_hot_default : R.drawable.icon_category_default)
                            .placeholder(state ? R.drawable.icon_category_hot_default : R.drawable.icon_category_default)
                            .into(library_category_image);
                } else {
                    if (state) {
                        library_category_image.setImageResource(R.drawable.icon_category_hot_default);
                    } else {
                        library_category_image.setImageResource(R.drawable.icon_category_default);
                    }
                }
            } else {
                int index = category_boy.indexOf(category.title);
                boolean state = checkCategoryHot(category.title);

                if (index != -1) {
                    Glide.with(context)
                            .load(category_boy_resources.get(index))
                            .skipMemoryCache(true)
                            .transform(new GlideRoundTransform(context))
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .error(state ? R.drawable.icon_category_hot_default : R.drawable.icon_category_default)
                            .placeholder(state ? R.drawable.icon_category_hot_default : R.drawable.icon_category_default)
                            .into(library_category_image);
                } else {
                    if (state) {
                        library_category_image.setImageResource(R.drawable.icon_category_hot_default);
                    } else {
                        library_category_image.setImageResource(R.drawable.icon_category_default);
                    }
                }
            }

            library_category_title.setText(TextUtils.isEmpty(category.title) ? "作品类型" : category.title);
        }
    }

    private boolean checkCategoryHot(String title) {
        return TextUtils.equals(title, "玄幻奇幻") || TextUtils.equals(title, "都市小说") || TextUtils.equals(title, "悬疑惊悚") || TextUtils.equals(title, "总裁豪门") || TextUtils.equals(title, "穿越重生") || TextUtils.equals(title, "古典架空");
    }
}