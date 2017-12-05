package com.pgcn.udcpopmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Classe gerada em http://www.jsonschema2pojo.org
 */
public class ReviewModel implements Parcelable {

    public final static Parcelable.Creator<ReviewModel> CREATOR = new Creator<ReviewModel>() {

        public ReviewModel createFromParcel(Parcel in) {
            return new ReviewModel(in);
        }

        public ReviewModel[] newArray(int size) {
            return (new ReviewModel[size]);
        }

    };
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("url")
    @Expose
    private String url;

    protected ReviewModel(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.author = ((String) in.readValue((String.class.getClassLoader())));
        this.content = ((String) in.readValue((String.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ReviewModel() {
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }


    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("author", author)
                .append("content", content).append("url", url).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(author);
        dest.writeValue(content);
        dest.writeValue(url);
    }

    public int describeContents() {
        return 0;
    }

}