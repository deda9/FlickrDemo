package bassem.smartec.flikcr.utilise;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bassem.smartec.flikcr.models.PhotoItem;

/**
 * Created by Bassem on 2/9/2016.
 * This class is responsible for parsing the json which come back from the Flikr RESTful ...
 *
 */
public class JsonParser {

    private static JsonParser mInstance ;
    private static Context mContext;


    /**
     * Create new Instance from JsonParser...
     *
     * @return
     */
    public static JsonParser getInstance(Context context)
    {
        mContext = context;

        if(mInstance == null)
            mInstance = new JsonParser();

        return mInstance;
    }


    /**
     *  Parse the response which back from the Volley Request on the Flikr.
     *
     * @param response
     * @return
     */
    public ArrayList<PhotoItem> searchKeyWord(String response )
    {

        try
        {
            JSONObject Json = new JSONObject(response);
            String status = Json.optString("stat");

            //check it's Ok
            if( status.equals("ok"))
            {
                JSONArray photosArray = Json.optJSONObject("photos").optJSONArray("photo");
                ArrayList<PhotoItem> photosList  = new ArrayList<>();

                for ( int i = 0; i<photosArray.length(); i++ )
                {
                    JSONObject currentIrem = photosArray.getJSONObject(i);

                    PhotoItem newItem = new PhotoItem(currentIrem.optString("title"), currentIrem.optString(getUrlFormat()),currentIrem.optString("owner"));
                    photosList.add(newItem);
                }

                return photosList;
            }
            else
            {
                Toast.makeText(mContext, "Something is went wrong , Plz Try again", Toast.LENGTH_SHORT).show();
                return null;
            }


        } catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the user format which we save in the shared..
     *
     * @return
     */
    private String getUrlFormat()
    {
        SharedPreferences Shared = mContext.getSharedPreferences("shared",Context.MODE_PRIVATE);
        return Shared.getString("url_format",PhotoSize.SMALL);
    }
}
