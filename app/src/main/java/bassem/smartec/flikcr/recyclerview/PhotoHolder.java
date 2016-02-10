package bassem.smartec.flikcr.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import bassem.smartec.flikcr.R;

/**
 * Created by Bassem on 2/9/2016.
 * This class is responsible for hold the view for each row to pass it to the recycler adapter...
 *
 */
public class PhotoHolder extends RecyclerView.ViewHolder
{

    // the fields of the photo from the row item xml layout...
    TextView title, owner;
    ImageView photo;

    //set each field to the holder.
    public PhotoHolder(View itemView)
    {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.et_photo_title);
        owner = (TextView) itemView.findViewById(R.id.et_photo_owner);
        photo = (ImageView) itemView.findViewById(R.id.iv_photo);

    }
}
