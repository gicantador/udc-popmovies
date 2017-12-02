package com.pgcn.udcpopmovies.data;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pgcn.udcpopmovies.exceptions.MovieServiceException;
import com.pgcn.udcpopmovies.model.MovieModel;

/**
 * Created by Giselle on 02/12/2017.
 */

public class FavoriteMoviesDatabaseUtil {
    private static final String TAG = FavoriteMoviesDatabaseUtil.class.getSimpleName();

    /**
     * Cria uma nova linha na tabela de filmes favoritos do usu[ario. Salva os dados do filme para que nao seja
     * necessario buscar novamente ao exibir
     *
     * @param db    a base de dados
     * @param movie o filme
     * @throws MovieServiceException caso ocorra erro
     */

    public static void insertData(SQLiteDatabase db, MovieModel movie) throws MovieServiceException {

        Log.d(TAG, "=== insertData, filme " + movie);
        if (db == null) {
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(MoviesContract.FavoriteMovies.COLUMN_API_ID, movie.getId());
        cv.put(MoviesContract.FavoriteMovies.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        cv.put(MoviesContract.FavoriteMovies.COLUMN_POSTER_PATH, movie.getPosterPath());
        cv.put(MoviesContract.FavoriteMovies.COLUMN_OVERVIEW, movie.getOverview());
        cv.put(MoviesContract.FavoriteMovies.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        cv.put(MoviesContract.FavoriteMovies.COLUMN_VOTE_AVERAGE, (null != movie.getVoteAverage()) ? String.valueOf(movie.getVoteAverage()) : null);
        cv.put(MoviesContract.FavoriteMovies.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        try {
            db.beginTransaction();
            db.insert(MoviesContract.FavoriteMovies.TABLE_NAME, null, cv);
        } catch (SQLException e) {
            throw new MovieServiceException("ERRO AO INSERIR DADOS " + cv.toString(), e);
        } finally {
            db.endTransaction();
        }
    }

    public static void removeData(SQLiteDatabase db, Integer id) throws MovieServiceException {
        Log.d(TAG, "=== removeData, filme " + id);

        ContentValues cv = new ContentValues();
        cv.put(MoviesContract.FavoriteMovies.COLUMN_API_ID, id);
        try {
            db.beginTransaction();
            db.delete(MoviesContract.FavoriteMovies.TABLE_NAME,
                    MoviesContract.FavoriteMovies.COLUMN_API_ID + "=?",
                    new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            throw new MovieServiceException("ERRO AO remover DADOS " + cv.toString(), e);
        } finally {
            db.endTransaction();
        }
    }
}