package easypromo.com.easypromo.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import easypromo.com.easypromo.R;

public class UniversalImageLoader {
    /**
     * Configurações da ImageLoader para ser chamada nos metodos
     */

    private static final int defaultImage = R.drawable.banner;
    private Context mContext;

    public UniversalImageLoader(Context context){

        mContext = context;

    }

    public ImageLoaderConfiguration getConfig(){

        DisplayImageOptions defaultOptions =  new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImage)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .cacheOnDisk(true).cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();

        return configuration;


    }
    /**
     * Este Metodo pode ser usado para setar imagens estaticas, não pode ser usado se as imagens estão sendo modificadas no Fragment/Actvity
     * Ou  se estão sendo setadas em uma lista ou em um GridView
     * @param imgURL
     * @param image
     * @param mProgressBar
     * @param append
     */

    public static void setImage(String imgURL, ImageView image, final ProgressBar mProgressBar, String append){

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(append + imgURL, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(mProgressBar != null){

                    mProgressBar.setVisibility(View.VISIBLE); //Visible = 0
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if(mProgressBar != null){

                    mProgressBar.setVisibility(View.GONE);//Gone = 8
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(mProgressBar != null){

                    mProgressBar.setVisibility(View.GONE);//Gone = 8
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if(mProgressBar != null){

                    mProgressBar.setVisibility(View.GONE);//Gone = 8
                }
            }
        });
}
}
