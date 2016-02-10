package bassem.smartec.flikcr.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;

import java.util.Collections;
import java.util.List;

import bassem.smartec.flikcr.R;
import bassem.smartec.flikcr.models.PhotoItem;
import bassem.smartec.flikcr.showOwnerPhotosActivity;
import bassem.smartec.flikcr.utilise.MVolley;

/**
 * Created by Bassem on 2/9/2016.
 *  This class is responsible for set the view inside the recycler ....
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder>
{
    private List<PhotoItem> photos = Collections.emptyList() ; // to avoid null Exception Error.
    private LayoutInflater inflater ;
    private Context mContext;
    private String type ; // we use type to determine it's search by keywords or owners



    /**
     * Constructor where we set out context and the list of the photos...
     *
     * @param context
     * @param photos photo list which we get from the flikr link....
     *
     */
    public PhotoAdapter(Context context, List<PhotoItem> photos ,String type)
    {
        this.mContext = context;
        this.type     = type;
        this.photos   = photos;
        inflater      = LayoutInflater.from(context); //Instantiates a layout XML file . It is never used directly so u must get LayoutInflater

    }


    /**
     * Create the Photo holder and set it the view .
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.photo_item_row, parent, false);

        PhotoHolder photoHolder = new PhotoHolder(view);

        return photoHolder;
    }


    @Override
    public void onBindViewHolder(PhotoHolder holder, final int position)
    {
        // first we need to get the current photo by the position from the photos list
        PhotoItem currentPhoto = photos.get(position);

        //second set the holer the propertie for each photo
        holder.title.setText( currentPhoto.getTitle());
        holder.owner.setText( currentPhoto.getOwener());

        // we use Volley Imageloader to download the image Efficiency
        MVolley.getInstance(mContext)
                .getImagerLoader()   //get the imagloader
                .get( currentPhoto.getUrl(), // set the  url for each image to dowload it from network or cache..
                        ImageLoader.getImageListener( holder.photo, // the photo ID  to set the image to it
                                                      R.drawable.flicker_logo, // default image
                                                      R.drawable.flicker_logo)); //error_image..



        // Handle when we chooose one Item from Recyclerview
        //we need to enable this if the Type is keywords
        if( type.equals("keywords"))
        {
            holder.photo.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    PhotoItem item = photos.get(position);
                    Intent intent = new Intent(mContext,showOwnerPhotosActivity.class );
                    intent.putExtra("owner", item.getOwener());
                    mContext.startActivity(intent);

                }
            });
        }

    }


    @Override
    public int getItemCount()
    {
        return photos.size();
    }
}
