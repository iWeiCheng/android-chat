package com.caijia.chat.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.caijia.chat.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LargestLimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.io.File;

public class ImageLoaderUtil {

    public static DisplayImageOptions.Builder getTransparentOptions(boolean useCache) {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.shape_transparent)
                .showImageOnFail(R.drawable.shape_transparent)
                .showImageForEmptyUri(R.drawable.shape_transparent)
                .cacheInMemory(useCache)
                .cacheOnDisk(useCache)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer());
    }

    public static File getCacheImage(String imageUrl) {
        return ImageLoader.getInstance().getDiskCache().get(imageUrl);
    }

    public static void deleteImage(String imageUri) {
        File imageFile = getCacheImage(imageUri);
        if (imageFile.exists()) {
            imageFile.delete();
        }
        MemoryCacheUtils.removeFromCache(imageUri, ImageLoader.getInstance().getMemoryCache());
    }

    public static void recyclerBitmap(String imageUri) {
        Bitmap bitmap = ImageLoader.getInstance().getMemoryCache().remove(imageUri);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public static void initImageLoader(Context context) {
        File cacheDir = FileUtil.getDiskCacheDir(context, "ImageLoader_Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 1) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(10 * 1024 * 1024))
                .memoryCacheSize(10 * 1024 * 1024)
                .memoryCacheSizePercentage(25) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheFileCount(300)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static void applyScrollListener(AbsListView adapterView) {
        if (adapterView != null) {
            adapterView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
        }
    }

    public static void loadImage(final ImageView imageView, String url,
                                 final DisplayImageOptions options, final int defaultImage, int width, int height) {
        if (width == 0 && height == 0) {
            ImageLoader.getInstance().displayImage(url, imageView, options);
        } else {
            ImageAware aware = new ImageViewAware(imageView);
            final ImageSize imageSize = new ImageSize(width, height);
            ImageLoader.getInstance().displayImage(url, aware, options, imageSize, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    imageView.setBackgroundResource(defaultImage);
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    imageView.setBackgroundResource(defaultImage);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    imageView.setBackgroundDrawable(null);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    imageView.setBackgroundResource(defaultImage);
                }
            }, null);
        }
    }

    public static void loadImage(ImageView imageView, String url, int defaultImage, int width, int height) {
        loadImage(imageView, url, getTransparentOptions(true).build(), defaultImage, width, height);
    }

    public static void loadImage(ImageView imageView, String url, int defaultImage, int size) {
        loadImage(imageView, url, defaultImage, size, size);
    }

    public static void loadImage(ImageView imageView, String url, int defaultImage) {
        loadImage(imageView, url, defaultImage, 0, 0);
    }
}
