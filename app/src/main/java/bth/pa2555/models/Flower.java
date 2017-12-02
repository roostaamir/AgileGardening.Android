package bth.pa2555.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Flower implements Parcelable {
    private int PlantId;
    private String Name;
    private String ImageUrl;
    private String Info;
    private boolean IsFavorite;

    public Flower(int id, String name, String imageUrl, String info, boolean isFavorite) {
        this.PlantId = id;
        this.Name = name;
        this.ImageUrl = imageUrl;
        this.Info = info;
        this.IsFavorite = isFavorite;
    }

    public int getId() {
        return PlantId;
    }

    public void setId(int id) {
        this.PlantId = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.ImageUrl = imageUrl;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        this.Info = info;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.PlantId);
        dest.writeString(this.Name);
        dest.writeString(this.ImageUrl);
        dest.writeString(this.Info);
        dest.writeByte(this.IsFavorite ? (byte) 1 : (byte) 0);
    }

    protected Flower(Parcel in) {
        this.PlantId = in.readInt();
        this.Name = in.readString();
        this.ImageUrl = in.readString();
        this.Info = in.readString();
        this.IsFavorite = in.readByte() != 0;
    }

    public static final Creator<Flower> CREATOR = new Creator<Flower>() {
        @Override
        public Flower createFromParcel(Parcel source) {
            return new Flower(source);
        }

        @Override
        public Flower[] newArray(int size) {
            return new Flower[size];
        }
    };

    public boolean isFavorite() {
        return IsFavorite;
    }

    public void setFavorite(boolean favorite) {
        IsFavorite = favorite;
    }
}
