package bassem.smartec.flikcr.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bassem on 2/9/2016.
 *
 *  This is the Photo Model which will contain the data for each photo Object ...
 *  and it's good to use it to enhance the performance when we need to pass the object between the activities .
 *
 */
public class PhotoItem  implements Parcelable{


    //properties of the photo Object
    private String title;
    private String url;
    private String owener;


    public String getUrl() {
        return url;
    }

    public String getOwener() {
        return owener;
    }

    public String getTitle() {
        return title;
    }


    /**
     * This is the public constructor , any object can accesss it when we need to intialize one Photo Object .
     *
     * @param title
     * @param url
     * @param owener
     */
    public PhotoItem(String title, String url, String owener)
    {
        this.title  = title;
        this.url    = url;
        this.owener = owener;
    }



    /**
     * we can retierve the data which we wrote into the Parcel Object.
     * and this constructor is private so Only the creator can access it .
     *
     * @param in
     */
    protected PhotoItem(Parcel in)
    {
        title  = in.readString();
        url    = in.readString();
        owener = in.readString();
    }



    public static final Creator<PhotoItem> CREATOR = new Creator<PhotoItem>()
    {
        @Override
        public PhotoItem createFromParcel(Parcel in)
        {
            return new PhotoItem(in);
        }

        @Override
        public PhotoItem[] newArray(int size)
        {
            return new PhotoItem[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }


    /**
     * Here, we can write our properties of the object photo inot the Parcel .
     *
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(owener);
    }
}
