package com.pgcn.udcpopmovies.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Classe modelo para Movie
 * <p>
 * Created by Giselle on 12/11/2017.
 */

public class MovieModel implements Parcelable {


    public static final String LB_MOVIE = "Movie";

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("adult")
    @Expose
    private Boolean adult;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @SerializedName("original_language")
    @Expose
    private String originalLanguage;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @SerializedName("popularity")
    @Expose
    private Double popularity;

    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;

    @SerializedName("video")
    @Expose
    private Boolean video;

    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    /**
     * Construtor com principais atributos
     *
     * @param movieId
     * @param title
     * @param posterPath
     * @param backdropPath
     * @param dataRelease
     */
    public MovieModel(int movieId, String title, String posterPath, String backdropPath, String dataRelease) {
        this.id = movieId;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
    }

    /**
     * Retorna a url da imagem do poster já completa
     *
     * @return
     */
    public String getPosterPath() {
        return NetworkUtils.buildImageUrl(posterPath);
    }

    @Override
    public String toString() {


        return new ToStringBuilder(this).append("title", title).append("posterPath", posterPath)
                .append("adult", adult).append("overview", overview).append("releaseDate", releaseDate)
                .append("id", id).append("originalTitle", originalTitle)
                .append("originalLanguage", originalLanguage).append("backdropPath", backdropPath)
                .append("popularity", popularity).append("voteCount", voteCount)
                .append("video", video).append("voteAverage", voteAverage).toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeString(backdropPath);
        dest.writeDouble(popularity);
        dest.writeDouble(voteAverage);
    }

    private MovieModel(Parcel in) {
        id = in.readInt();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        backdropPath = in.readString();
        popularity = in.readDouble();
        voteAverage = in.readDouble();

    }

    public MovieModel(String posterPath, Boolean adult, String overview, String releaseDate,
                      Integer id, String originalTitle, String originalLanguage,
                      String title, String backdropPath, Double popularity, Integer voteCount,
                      Boolean video, Double voteAverage) {
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
    }


    public static final Parcelable.Creator<MovieModel>
            CREATOR = new Parcelable.Creator<MovieModel>() {

        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };


}