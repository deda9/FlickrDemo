package bassem.smartec.flikcr;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import bassem.smartec.flikcr.models.PhotoItem;
import bassem.smartec.flikcr.recyclerview.PhotoAdapter;
import bassem.smartec.flikcr.utilise.JsonParser;
import bassem.smartec.flikcr.utilise.NetHelper;

import static android.util.Log.d;

/**
 * Created by Bassem on 2/9/2016.
 *  This class is responsible for show the owner photos ....
 */
public class showOwnerPhotosActivity extends AppCompatActivity
{

    private RecyclerView rcPhoto;
    private PhotoAdapter mPhotoAdapter;
    private NetHelper mNetHelper;
    private JsonParser mJsonParser;
    private String owner;
    private boolean isGrid = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_owner_photos);

        intialize();

    }

    private void intialize()
    {
        rcPhoto    = (RecyclerView) findViewById(R.id.rc_owner_photos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get the onwer value where we passed it from the MainActivity.
        Bundle bundle = getIntent().getExtras();
        if( bundle != null)
        {
            owner = bundle.getString("owner");
            getSupportActionBar().setTitle("Photos By " + owner);

            mNetHelper = NetHelper.getInstance(this);
            mJsonParser = JsonParser.getInstance(this);

            getOwnerPhotos(owner);
        }
    }

    /**
     * get the owner photos from Flikr .
     *
     * @param owner
     */
    private void getOwnerPhotos(String owner)
    {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);


        mNetHelper.ownerPhotosRequest(owner, new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        d("hello", "suer photos" +  response);

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
                        Toast.makeText(showOwnerPhotosActivity.this, "Something is went wrong , Plz Try again", Toast.LENGTH_SHORT).show();
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

        //create adapter
        mPhotoAdapter = new PhotoAdapter(this, photos,"owner");

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
     * We Need to setup our Menu to get the grid Icon.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_owner, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if( id == android.R.id.home)
            super.onBackPressed();

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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
