package bassem.smartec.flikcr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import bassem.smartec.flikcr.models.PhotoItem;
import bassem.smartec.flikcr.recyclerview.PhotoAdapter;
import bassem.smartec.flikcr.utilise.JsonParser;
import bassem.smartec.flikcr.utilise.NetHelper;


/**
 * Created by Bassem on 2/9/2016.
 *  This class is the main class for the flekr App ....
 */

public class FlekrMainActivity extends AppCompatActivity
{

    TextView noItems ;
    private RecyclerView rcPhoto;
    private PhotoAdapter mPhotoAdapter;
    private MaterialSearchView searchView;
    private NetHelper mNetHelper;
    private JsonParser mJsonParser;
    private boolean isGrid = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flekr_main);

        intialize();
    }


    /**
     * Intialize the view when the activity start..
     *
     */
    private void intialize()
    {
        rcPhoto    = (RecyclerView) findViewById(R.id.rc_photos);
        noItems    = (TextView) findViewById(R.id.no_items_available);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);

        mNetHelper  = NetHelper.getInstance(this);
        mJsonParser = JsonParser.getInstance(this);


        //when the user need to search
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                if(isOnline())
                    searchByKeyword(query);
                else
                    Toast.makeText(FlekrMainActivity.this, "No Network Available", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });

    }

    /**
     *  Connect to the fleckr Link to get the photos list.
     *
     * @param query Keyword which the user will search it
     */
    private void searchByKeyword(String query)
    {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);

        // intialize the reuqest buy the volley lib.
        mNetHelper.searchWordRequest(query, new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response)
                            {
                                Log.d("hello",  "json return" + response );

                                ArrayList<PhotoItem> list = mJsonParser.searchKeyWord(response);

                                //checj the list not empty
                                if( list != null && !list.isEmpty())
                                {
                                    dialog.dismiss();
                                    setRecyclerAdapter(list);
                                }
                            }
                        }, new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError volleyError)
                            {
                                dialog.dismiss();
                                Toast.makeText(FlekrMainActivity.this, "Something is went wrong , Plz Try again", Toast.LENGTH_SHORT).show();
                                Log.d("hello", "Error of Response" + volleyError.toString());
                            }
                        });
    }



    /**
     * When we load the photos from the fliker we need to set this list to the adpater
     * enable the RecyclerView
     * disable the no items View.
     *
     * @param photos
     */
    private void setRecyclerAdapter(List<PhotoItem> photos)
    {
        rcPhoto.setVisibility(View.VISIBLE);
        noItems.setVisibility(View.GONE);

        //create adapter
        mPhotoAdapter = new PhotoAdapter(this, photos,"keywords");

        //set the adapter to recyclerview
        rcPhoto.setAdapter(mPhotoAdapter);

        //set the linearLayout.
        //check if it landscape and then do the grid.
        if( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            rcPhoto.setLayoutManager(new GridLayoutManager(this, 2));

        //if PORTRAIT
        else
            rcPhoto.setLayoutManager(new LinearLayoutManager(this));
    }


    /**
     * We Need to setup our Menu to get the serach Icon.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    /**
     * The Bonus Task
     * Switch the RecyclerView from LinearLayout inot Grid
     * and from Grid inot Linear...
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if( id == R.id.action_grid && mPhotoAdapter != null && !isGrid)
        {
            rcPhoto.setLayoutManager(new GridLayoutManager(this, 2));
            isGrid = true;
        }
        else if( id == R.id.action_grid && mPhotoAdapter != null && isGrid)
        {
            rcPhoto.setLayoutManager(new LinearLayoutManager(this));
            isGrid = false;
        }

        return super.onOptionsItemSelected(item);

    }

    /**
     * Check we are online
     * @return
     */
    public boolean isOnline()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo                     = connectivityManager.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        } else {
            return false;
        }
    }

}
