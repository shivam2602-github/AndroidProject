package com.example.myapp.models;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class University implements Parcelable{
    @SerializedName("name")
    @Expose(serialize = true, deserialize = true)
    private String name ;

    @SerializedName("country")
    @Expose(serialize = true, deserialize = true)
    private String country ;

    @SerializedName("web_pages")
    @Expose(serialize = true, deserialize = true)
    private String []webPages ;

    public University(){
    }

    public University(String name, String country,String []webPages){
        this.name = name;
        this.country = country;
        this.webPages =webPages ;
    }

    public String getName(){
        return name ;
    }

    public String getCountry(){
        return country ;
    }

    public String[] getWebPages(){
        return webPages ;
    }


    protected University(Parcel in) {
        name = in.readString();
        country = in.readString();
        webPages = in.createStringArray();
    }

    public static final Creator<University> CREATOR = new Creator<University>() {
        public University createFromParcel(Parcel in) {
            return new University(in);
        }

        public University[] newArray(int size) {
            return new University[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(country);
        dest.writeStringArray(webPages);
    }
}
