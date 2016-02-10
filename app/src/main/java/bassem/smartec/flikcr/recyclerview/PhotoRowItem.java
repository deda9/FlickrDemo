package bassem.smartec.flikcr.recyclerview;

import android.widget.ImageView;

/**
 * Created by Bassem on 2/9/2016.
 * This class is the row for the recyclerview ..
 *
 */
public class PhotoRowItem
{

    //properties for each row in the recyclerview.
    private ImageView photo;
    private String title;
    private String Owner ;


    public String getOwner() {
        return Owner;
    }


    public void setOwner(String owner) {
        Owner = owner;
    }


    public ImageView getPhoto() {
        return photo;
    }


    public void setPhoto(ImageView photo) {
        this.photo = photo;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }




}
