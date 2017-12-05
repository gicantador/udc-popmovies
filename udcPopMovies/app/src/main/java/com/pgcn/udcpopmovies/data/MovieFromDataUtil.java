package com.pgcn.udcpopmovies.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pgcn.udcpopmovies.model.MovieModel;

import java.util.ArrayList;

/**
 * Created by Giselle on 02/12/2017.
 */

public class MovieFromDataUtil {
    private static final String TAG = MovieFromDataUtil.class.getSimpleName();

    public static ArrayList<MovieModel> retrieveAllFavoriteMovies(MoviesDbHelper dbHelper) {
        Log.d(TAG, "retrieveAllFavoriteMovies ");
        ArrayList<MovieModel> listaFilmes = new ArrayList<MovieModel>();
        Cursor cursor = getAllmovies(dbHelper);
        if (null != cursor) {
            int totalMovies = cursor.getCount();
            Log.d(TAG, "filmes recuperados  " + totalMovies);

            for (int position = 0; position <= totalMovies; position++) {
                Log.d(TAG, " cursor position  " + position);
                if (!cursor.moveToPosition(position)) {
                    return listaFilmes;
                }
                int dataId = cursor.getInt(cursor.getColumnIndex(MoviesContract.FavoriteMovies._ID));
                int apiId = cursor.getInt(cursor.getColumnIndex(MoviesContract.FavoriteMovies.COLUMN_API_ID));
                String title = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteMovies.COLUMN_ORIGINAL_TITLE));
                String overview = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteMovies.COLUMN_OVERVIEW));
                String posterPath = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteMovies.COLUMN_POSTER_PATH));
                String releaseDate = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteMovies.COLUMN_RELEASE_DATE));
                Double averageVoted = Double.valueOf(cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteMovies.COLUMN_VOTE_AVERAGE)));
                Log.d(TAG, " movie title " + title);
                MovieModel movie = new MovieModel(apiId, title, overview, posterPath, releaseDate, averageVoted, dataId, true);
                listaFilmes.add(movie);
                Log.d(TAG, "listaFilmes tem " + listaFilmes.size());
            }
            cursor.close();
        }
        return listaFilmes;
    }

    private static Cursor getAllmovies(MoviesDbHelper dbHelper) {
        Log.d(TAG, "getAllmovies ");
        SQLiteDatabase db;
        db = dbHelper.getReadableDatabase();
        Log.d(TAG, "isOpen() " + db.isOpen());

        Cursor cs = FavoriteMoviesDatabaseUtil.getAllFavoriteMovies(db);

        Log.d(TAG, "Cursor cs " + cs);
        SQLiteDatabase db2 = dbHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + MoviesContract.FavoriteMovies.TABLE_NAME, null);
        Log.d(TAG, "Cursor res " + res);
        return res;

    }
}
