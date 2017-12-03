package com.pgcn.udcpopmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Giselle on 02/12/2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String TAG = MoviesDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "popmovies.db";
    private static final int DATABASE_VERSION = 2;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate ");
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MoviesContract.FavoriteMovies.TABLE_NAME + " (" +
                MoviesContract.FavoriteMovies._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.FavoriteMovies.COLUMN_API_ID + " INTEGER UNIQUE NOT NULL," +
                MoviesContract.FavoriteMovies.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL," +
                MoviesContract.FavoriteMovies.COLUMN_POSTER_PATH + " TEXT ," +
                MoviesContract.FavoriteMovies.COLUMN_OVERVIEW + " TEXT," +
                MoviesContract.FavoriteMovies.COLUMN_RELEASE_DATE + " TEXT," +
                MoviesContract.FavoriteMovies.COLUMN_VOTE_AVERAGE + " TEXT," +
                MoviesContract.FavoriteMovies.COLUMN_DTA_CRIACAO + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        Log.d(TAG, "query -- " + SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade ");
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.

        // TODO: ALTER TABLE
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.FavoriteMovies.TABLE_NAME);
        onCreate(db);
    }
}
