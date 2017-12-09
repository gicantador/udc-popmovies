package com.pgcn.udcpopmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Giselle on 02/12/2017.
 */

class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String TAG = MoviesDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "popmovies.db";
    private static final int DATABASE_VERSION = 4;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate ");
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MoviesContentContract.MoviesEntry.TABLE_NAME + " (" +
                MoviesContentContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContentContract.MoviesEntry.COLUMN_API_ID + " INTEGER UNIQUE NOT NULL," +
                MoviesContentContract.MoviesEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL," +
                MoviesContentContract.MoviesEntry.COLUMN_POSTER_PATH + " TEXT ," +
                MoviesContentContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT," +
                MoviesContentContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT," +
                MoviesContentContract.MoviesEntry.COLUMN_VOTE_AVERAGE + " TEXT," +
                MoviesContentContract.MoviesEntry.COLUMN_DTA_CRIACAO + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        Log.d(TAG, "query -- " + SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade ");
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContentContract.MoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
