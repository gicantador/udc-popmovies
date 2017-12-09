package com.pgcn.udcpopmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Giselle on 06/12/2017.
 */

class MoviesContentContract {

    public static final String AUTHORITY = "com.pgcn.udcpopmovies.data";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    public static final String PATH_MOVIES = "movies";

    public static final class MoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "favoriteMovies";

        public static final String COLUMN_API_ID = "apiId";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_ORIGINAL_TITLE = "title" ;
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_DTA_CRIACAO = "criadoEm";

    }
}
