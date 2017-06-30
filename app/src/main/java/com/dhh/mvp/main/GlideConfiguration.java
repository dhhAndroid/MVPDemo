package com.dhh.mvp.main;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.module.GlideModule;

/**
 * 修改Glide加载的图片为8888
 */
public class GlideConfiguration implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.DEFAULT);
        MemoryCache memoryCache =new LruResourceCache(23);
        memoryCache.trimMemory(100);
        builder.setMemoryCache(memoryCache);
    }
    @Override  
    public void registerComponents(Context context, Glide glide) {
        glide.setMemoryCategory(MemoryCategory.LOW);
    }
}