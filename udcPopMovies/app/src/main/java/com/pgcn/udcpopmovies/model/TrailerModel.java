package com.pgcn.udcpopmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Classe gerada em http://www.jsonschema2pojo.org
 */

public class TrailerModel implements Parcelable {

    public final static Parcelable.Creator<TrailerModel> CREATOR = new Creator<TrailerModel>() {


        public TrailerModel createFromParcel(Parcel in) {
            return new TrailerModel(in);
        }

        public TrailerModel[] newArray(int size) {
            return (new TrailerModel[size]);
        }

    };
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("iso_639_1")
    @Expose
    private String iso6391;
    @SerializedName("iso_3166_1")
    @Expose
    private String iso31661;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("site")
    @Expose
    private String site;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("type")
    @Expose
    private String type;

    protected TrailerModel(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.iso6391 = ((String) in.readValue((String.class.getClassLoader())));
        this.iso31661 = ((String) in.readValue((String.class.getClassLoader())));
        this.key = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.site = ((String) in.readValue((String.class.getClassLoader())));
        this.size = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
    }

    public TrailerModel() {
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("iso6391",
                iso6391).append("iso31661", iso31661).append("key", key)
                .append("name", name).append("site", site).append("size", size)
                .append("type", type).toString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(iso6391);
        dest.writeValue(iso31661);
        dest.writeValue(key);
        dest.writeValue(name);
        dest.writeValue(site);
        dest.writeValue(size);
        dest.writeValue(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }


}


