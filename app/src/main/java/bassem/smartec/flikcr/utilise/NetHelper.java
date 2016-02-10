package bassem.smartec.flikcr.utilise;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by Bassem on 2/9/2016.
 *  This Class will contain all the network connection ...
 *  when we try to search the photo by the keywords or by the Owner
 *  Followed the Design patter to enhance the code maintaince ...
 *
 */
public class NetHelper {

    private static NetHelper mInstance ;
    private static RequestQueue mRequestQueue ;
    private static Context mContext;
    private SharedPreferences SHARED;



    /**
     *  Create new Instance from Volley Request Queue...
     *
     * @param context
     */
    private NetHelper(Context context)
    {
        mRequestQueue = MVolley.getInstance(context).getRequestQueue();
    }


    /**
     *  Create one static Instance from NetHelper to use once in the app life cycle .
     *
     * @return
     */
    public static NetHelper getInstance(Context context)
    {
        mContext = context;

        if( mInstance == null )
            mInstance =   new NetHelper(context);

        return mInstance;
    }


    /**
     * Do the request to the flickr..
     *
     * @param listener
     * @param errorListener
     */
    public void searchWordRequest(String keyWord ,Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        StringRequest request = new StringRequest(Request.Method.GET, makeKeyWordSearchUrl(keyWord), listener, errorListener);

        //then add the request to Volley Queue.
        mRequestQueue.add(request);
    }


    /**
     * Do the request to the flickr..
     *
     * @param listener
     * @param errorListener
     */
    public void ownerPhotosRequest(String owner, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        StringRequest request = new StringRequest(Request.Method.GET, makeOwnerPhotosUrl(owner), listener, errorListener);

        //then add the request to Volley Queue.
        mRequestQueue.add(request);
    }



    /**
     *  Repare the Url for search flikr by Keword.
     *
     *  Like this https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=d75d73393b8eeab963b14126a943e7f3&text=man&extras=url_m&format=json&nojsoncallback=1
     * @return
     */
    private String makeKeyWordSearchUrl(String keyWord)
    {
        return Urls.URL_SEARCH_PHOTOS  + "?method=" + Params.METHOD
                                       + "&api_key="+ Params.API_KEY
                                       + "&text=" + keyWord
                                       + "&extras=" + getImageSize()
                                       + "&format=" + Params.Format
                                       + "&nojsoncallback=" + Params.CALLBACK ;

    }



    /**
     *  Repare the Url for search flikr by Owners.
     *
     *  Like this https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=d75d73393b8eeab963b14126a943e7f3&user_id=man&extras=url_m&format=json&nojsoncallback=1
     * @return
     */
    private String makeOwnerPhotosUrl(String owner)
    {
        return Urls.URL_SEARCH_PHOTOS  + "?method=" + Params.METHOD
                + "&api_key="+ Params.API_KEY
                + "&user_id="+ owner
                + "&extras=" + getImageSize()
                + "&format=" + Params.Format
                + "&nojsoncallback=" + Params.CALLBACK ;

    }


    /**
     * we calcuate the Mobile Size Screen and Based On this Sized we Determinse the size of the photo which we will download it
     *
     * @return
     */
    private String getImageSize()
    {

       int size = mContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        String retunValue = "";
        switch (size)
        {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                retunValue =  PhotoSize.LARGE;
                saveUrlFormat(PhotoSize.LARGE);
                break;

            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                retunValue =  PhotoSize.LARGE;
                saveUrlFormat(PhotoSize.LARGE);
                break;

            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                retunValue =  PhotoSize.MEDUIM;
                saveUrlFormat(PhotoSize.MEDUIM);
                break;

            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                retunValue =  PhotoSize.SMALL;
                saveUrlFormat(PhotoSize.SMALL);
                break;

            default:
                retunValue =  PhotoSize.SMALL;
                saveUrlFormat(PhotoSize.SMALL);

        }

        return retunValue;
    }


    /**
     * Cause the fliker change the Json response when we change the image size
     * Like
     * if we set the image size to url_m then the url in the Json would be url_m
     * and if we set the image size to url_z then the url in the Json would be url_z
     * so
     * we need to save it and user later when we handle the json to avoid crache.
     *
     * @param format
     */
    private void saveUrlFormat( String format)
    {
        SHARED = mContext.getSharedPreferences("shared",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor =SHARED.edit();
        editor.putString("url_format",format);
        editor.apply();

    }
}

