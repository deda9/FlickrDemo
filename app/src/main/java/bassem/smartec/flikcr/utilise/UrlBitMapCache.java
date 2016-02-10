package bassem.smartec.flikcr.utilise;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Deda on 2/9/2016.
 *  This class is responsible for caching the images to reduce using the bandwidth..
 *
 */
public class UrlBitMapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache{


    /**
     * get the maximum number of entries in the cache.
     *
     */
    public UrlBitMapCache()
    {
        super(getCacheSize());
    }


    /**
     *  Calculate the system memory and return the size / 7 . to be used in the caching...
     *
     * @return
     */
    private static int getCacheSize()
    {
        final int maxMemory = (int) ((Runtime.getRuntime().maxMemory() )/ 1024) ; // to convert it to MB
        return maxMemory / 7;
    }


    /**
     * Check if the id of the image in the cache then get the map from the cache...
     *
     * @param id the id of the image
     * @return
     */
    @Override
    public Bitmap getBitmap(String id)
    {
        return get(id);
    }


    /**
     *  If the bitmap not in the cache then Add it to our Cache ,,,
     *
     * @param id
     * @param bitmap
     */
    @Override
    public void putBitmap(String id, Bitmap bitmap)
    {
        put(id, bitmap);
    }
}
