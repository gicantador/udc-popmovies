package com.pgcn.udcpopmovies.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Classe modelo para Movie
 * <p>
 * Created by Giselle on 12/11/2017.
 */

public class MovieModel implements Serializable {


    private static final String ROOT_PATH = "http://image.tmdb.org/t/p/w780//";


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

    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = null;

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

    private Date dtaReleaseDate;

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

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
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

    public Date getDtaReleaseDate() {
        return getReleaseDateFromString(this.releaseDate);
    }

    public void setDtaReleaseDate(Date dtaReleaseDate) {
        this.dtaReleaseDate = dtaReleaseDate;
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
        this.dtaReleaseDate = getReleaseDateFromString(dataRelease);
    }

    /**
     * Formata data recebida no Json como texto para java.utl.Date
     *
     * @param textoData
     * @return
     */
    private Date getReleaseDateFromString(String textoData) {
        if (textoData != null && !textoData.isEmpty()) {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                return formatter.parse(textoData);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        return null;
    }


    public String getPosterPath() {
        return ROOT_PATH + posterPath;
    }

    @Override
    public String toString() {

        return new ToStringBuilder(this).append("title", title).append("posterPath", posterPath).append("adult", adult).append("overview", overview).append("releaseDate", releaseDate).append("genreIds", genreIds).append("id", id).append("originalTitle", originalTitle).append("originalLanguage", originalLanguage).append("backdropPath", backdropPath).append("popularity", popularity).append("voteCount", voteCount).append("video", video).append("voteAverage", voteAverage).toString();
    }
}