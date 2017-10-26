package com.quduquxie.base.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;
import com.quduquxie.base.util.Initialization;

public class CustomGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

        MemorySizeCalculator memorySizeCalculator = new MemorySizeCalculator(context);

        int bitmapPoolSize = memorySizeCalculator.getBitmapPoolSize();
        int memoryCacheSize = memorySizeCalculator.getMemoryCacheSize();

        int customBitmapPoolSize = (int) (1.2 * bitmapPoolSize);
        int customMemoryCacheSize = (int) (1.2 * memoryCacheSize);

        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));

        int diskCacheSize = 100 * 1024 * 1024;

        builder.setDiskCache(new DiskLruCacheFactory(Initialization.APP_PATH_GLIDE, diskCacheSize));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}