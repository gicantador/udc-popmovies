package com.pgcn.udcpopmovies.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.pgcn.udcpopmovies.model.MovieModel;

import java.util.ArrayList;

/**
 * Created by Giselle on 02/12/2017.
 */

public class MovieFromDataUtil {
    private static final String TAG = MovieFromDataUtil.class.getSimpleName();

    /**
     * Retorna a lista de filmes favoritos
     *
     * @param contentResolver
     * @return
     */
    public static ArrayList<MovieModel> retrieveAllFavoriteMovies(ContentResolver contentResolver) {
        Log.d(TAG, "retrieveAllFavoriteMovies ");
        ArrayList<MovieModel> listaFilmes = new ArrayList<MovieModel>();

        Cursor cursor = getAllmovies(contentResolver);
        if (null != cursor) {
            int totalMovies = cursor.getCount();
            Log.d(TAG, "filmes recuperados  " + totalMovies);

            for (int position = 0; position <= totalMovies; position++) {
                Log.d(TAG, " cursor position  " + position);
                if (!cursor.moveToPosition(position)) {
                    return listaFilmes;
                }
                MovieModel movie = getMovieModelFromCursor(cursor);
                listaFilmes.add(movie);
                Log.d(TAG, "listaFilmes tem " + listaFilmes.size());
            }
            cursor.close();
        }
        return listaFilmes;
    }

    public static MovieModel getMovieModelFromCursor(Cursor cursor) {
        if (null != cursor) {
            int dataId = cursor.getInt(cursor.getColumnIndex(MoviesContentContract.MoviesEntry
                    ._ID));
            int apiId = cursor.getInt(cursor.getColumnIndex(MoviesContentContract.MoviesEntry.COLUMN_API_ID));
            String title = cursor.getString(cursor.getColumnIndex(MoviesContentContract.MoviesEntry.COLUMN_ORIGINAL_TITLE));
            String overview = cursor.getString(cursor.getColumnIndex(MoviesContentContract.MoviesEntry.COLUMN_OVERVIEW));
            String posterPath = cursor.getString(cursor.getColumnIndex(MoviesContentContract.MoviesEntry.COLUMN_POSTER_PATH));
            String releaseDate = cursor.getString(cursor.getColumnIndex(MoviesContentContract.MoviesEntry.COLUMN_RELEASE_DATE));
            Double averageVoted = Double.valueOf(cursor.getString(cursor.getColumnIndex(MoviesContentContract.MoviesEntry.COLUMN_VOTE_AVERAGE)));
            Log.d(TAG, " movie title " + title);
            return new MovieModel(apiId, title, overview, posterPath, releaseDate, averageVoted,
                    dataId, true);
        }
        return null;
    }


    /**
     * Busca os filmes favoritos
     *
     * @param contentResolver
     * @return
     */
    private static Cursor getAllmovies(ContentResolver contentResolver) {

        Log.d(TAG, "getAllmovies ");

        Cursor cs = contentResolver.query(MoviesContentContract.MoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                MoviesContentContract.MoviesEntry.COLUMN_DTA_CRIACAO);

        Log.d(TAG, "Cursor cs " + cs);

        return cs;

    }
}
