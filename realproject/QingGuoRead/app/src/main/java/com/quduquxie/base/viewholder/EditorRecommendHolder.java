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
import com.bumptech.glide.signature.StringSignature;
import com.quduquxie.R;
import com.quduquxie.base.bean.Book;
import com.quduquxie.communal.widget.GlideCircleTransform;
import com.quduquxie.communal.widget.GlideRoundTransform;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/21.
 * Created by crazylei.
 */

public class EditorRecommendHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.editor_recommend_content)
    public RelativeLayout editor_recommend_content;
    @BindView(R.id.editor_recommend_cover)
    ImageView editor_recommend_cover;
    @BindView(R.id.editor_recommend_avatar)
    ImageView editor_recommend_avatar;
    @BindView(R.id.editor_recommend_author)
    TextView editor_recommend_author;
    @BindView(R.id.editor_recommend_name)
    TextView editor_recommend_name;

    public EditorRecommendHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void initializeView(Context context, Book book) {
        if (book != null) {
            if (!TextUtils.isEmpty(book.image)) {
                Glide.with(context)
                        .load(book.image)
                        .skipMemoryCache(true)
                        .signature(new StringSignature(book.image))
                        .transform(new GlideRoundTransform(context))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .error(R.drawable.icon_cover_default)
                        .placeholder(R.drawable.icon_cover_default)
                        .into(editor_recommend_cover);
            } else {
                editor_recommend_cover.setImageResource(R.drawable.icon_cover_default);
            }

            if (book.author != null) {
                if (!TextUtils.isEmpty(book.author.avatar)) {
                    Glide.with(context)
                            .load(book.author.avatar)
                            .skipMemoryCache(true)
                            .signature(new StringSignature(book.author.avatar))
                            .transform(new GlideCircleTransform(context))
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .error(R.drawable.icon_avatar_default)
                            .placeholder(R.drawable.icon_avatar_default)
                            .into(editor_recommend_avatar);
                } else {
                    editor_recommend_avatar.setImageResource(R.drawable.icon_avatar_default);
                }

                editor_recommend_author.setText(TextUtils.isEmpty(book.author.name) ? "青果作家" : book.author.name);
            }

            editor_recommend_name.setText(TextUtils.isEmpty(book.name) ? "《青果座屏》" : "《" + book.name + "》");
        }
    }
}