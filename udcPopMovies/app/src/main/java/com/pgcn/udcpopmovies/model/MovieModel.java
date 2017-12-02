package com.pgcn.udcpopmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pgcn.udcpopmovies.utils.NetworkUtils;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Classe modelo para Movie
 * <p>
 * Created by Giselle on 12/11/2017.
 * <p>
 * Classe gerada em http://www.jsonschema2pojo.org
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


    // marcado como favorito? por padrao eh false
    private Boolean favorito = false;

    /**
     * Construtor com principais atributos
     *
     * @param movieId
     * @param title
     * @param posterPath
     * @param backdropPath
     */
    public MovieModel(int movieId, String title, String posterPath, String backdropPath) {
        this.id = movieId;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
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

    /**
     * @return se filme foi marcado como favorito.
     */
    public Boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(Boolean favorito) {
        this.favorito = favorito;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Integer getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public Double getVoteAverage() {
        return voteAverage;
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


}