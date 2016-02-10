package bassem.smartec.flikcr.utilise;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Bassem on 2/9/2016.
 *
 * This class is an instance for the Volley to enhace the network connection
 * we use the Volley libaray which is support by Google
 * and handle the Images caching
 */
public class MVolley {

    private static MVolley mInstance;
    private RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;
    Context mContext;


    /**
     * The Instructor
     *
     * @param context
     */
    private MVolley(Context context)
    {
        this.mContext = context;

        mRequestQueue = getRequestQueue();
    }



    /**
     *  Create Inastance from Google Volley to be used in all the app life cycle Once ..
     *
     * @param context
     * @return
     */
    public static synchronized  MVolley getInstance(Context context)
    {
        if ( mInstance == null )
            mInstance = new MVolley(context);

        return mInstance;
    }



    /**
     *  Create Instance from the RequestQueue..including the cache and the network..
     *
     * @return
     */
    public RequestQueue getRequestQueue()
    {
        if( mRequestQueue == null )
        {
            // Instantiate the cache
            Cache cache = new DiskBasedCache(mContext.getCacheDir(), 50 * 1024 * 1024); // 50MB cap)

            // Set up the network to use HttpURLConnection as the HTTP client.
            Network network = new BasicNetwork(new HurlStack());

            mRequestQueue = new RequestQueue(cache, network);

            // Start the queue
            mRequestQueue.start();

        }
        return mRequestQueue;
    }


    public ImageLoader getImagerLoader()
    {
        if( mImageLoader == null )
            mImageLoader = new ImageLoader(getRequestQueue(),new UrlBitMapCache() );

        return mImageLoader;
    }

}
