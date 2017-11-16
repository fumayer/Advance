package com.shortmeet.www.utilsUsed;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 * Created by SHM on 2017/05/03.
 */
public class GlideLoader {
    private static GlideLoader instance = new GlideLoader();

    private GlideLoader() {
    }

    public static GlideLoader getInstance() {
        return instance;
    }

    public void loadImg(Context context, String url, boolean isCenterCrop, boolean isCrossFade, ImageView targetImg) {
        if (isCenterCrop && isCrossFade) {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .crossFade()
                    .into(targetImg);
        }

        if (!isCenterCrop && isCrossFade) {
            Glide.with(context)
                    .load(url)
                    .crossFade()
                    .into(targetImg);
        }

        if (isCenterCrop && !isCrossFade) {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .into(targetImg);
        }

        if (!isCenterCrop && !isCrossFade) {
            Glide.with(context)
                    .load(url)
                    .into(targetImg);
        }

    }

    public void loadImg(Context context, String url, int placeHolderResId, ImageView targetImg) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .placeholder(placeHolderResId)
                .crossFade()
                .into(targetImg);
    }

    /**
     * 加载头像的时候清除缓存
     *
     * @param context
     * @param url
     * @param targetImg
     */
    public void loadWithoutCache(Context context, String url, ImageView targetImg) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .crossFade()
                .into(targetImg);
    }

    /**
     * 加载图片(正常,没有圆角的ImageView)  占位图与错误图片相同
     *
     * @param context
     * @param url
     * @param placeErrResId
     * @param targetImg
     */
    public void loadImgPlaceAndErrSame(Context context, String url, int placeErrResId, ImageView targetImg) {
        Glide.with(context)
                .load(url)
                .placeholder(placeErrResId)
                .error(placeErrResId)
                .centerCrop()
                .crossFade()
                .into(targetImg);
    }

    /**
     * 加载圆形图片  占位图与错误图片相同
     *
     * @param context
     * @param url
     * @param placeErrResId
     * @param targetImg
     */
    public void loadCircleImg(final Context context, String url, int placeErrResId, final ImageView targetImg) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(placeErrResId)
                .error(placeErrResId)
                .centerCrop()
                .into(new BitmapImageViewTarget(targetImg) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        targetImg.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }
    /**
     * 加载圆形图片  占位图与错误图片相同
     *
     * @param context
     * @param url
     * @param
     * @param targetImg
     */
    public void loadRoundImg(final Context context, String url, final ImageView targetImg) {
        Glide.with(context)
                .load(url)
               .transform(new GlideRoundTransform(context))
                .into(targetImg);

    }
}
