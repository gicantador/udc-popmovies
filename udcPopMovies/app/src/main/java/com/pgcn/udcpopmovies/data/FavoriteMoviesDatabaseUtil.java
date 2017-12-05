package com.pgcn.udcpopmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
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
    public static final String KEY_FAVORITOS = "favoritos";

    /**
     * Cria uma nova linha na tabela de filmes favoritos do usu[ario. Salva os dados do filme para que nao seja
     * necessario buscar novamente ao exibir
     *
     * @param db    a base de dados
     * @param movie o filme
     * @throws MovieServiceException caso ocorra erro
     */

    public static void insertData(SQLiteDatabase db, MovieModel movie) throws MovieServiceException {

        Log.d(TAG, "=== insertData, filme " + movie.getOriginalTitle());
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
            long teste = db.insert(MoviesContract.FavoriteMovies.TABLE_NAME, null, cv);
            Log.d(TAG, "=== insertData, teste " + teste);
            if (teste <= 0) {
                throw new MovieServiceException("ERRO AO INSERIR DADOS " + cv.toString());
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            throw new MovieServiceException("ERRO AO INSERIR DADOS " + cv.toString(), e);
        } finally {
            db.endTransaction();
        }
    }

    public static void removeData(SQLiteDatabase db, Integer id) throws MovieServiceException {
        Log.d(TAG, "=== removeData, filme " + id);

        try {
            db.beginTransaction();
            int teste = db.delete(MoviesContract.FavoriteMovies.TABLE_NAME,
                    MoviesContract.FavoriteMovies._ID + "=?",
                    new String[]{Integer.toString(id)});
            if (teste <= 0) {
                throw new MovieServiceException("ERRO AO REMOVER DADOS ");
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            throw new MovieServiceException("ERRO AO remover DADOS filme" + id, e);
        } finally {
            db.endTransaction();
        }
    }

    /**
     * retorna todos os filmes favoritos guardados no banco de dados
     *
     * @param db
     * @return
     */
    public static Cursor getAllFavoriteMovies(SQLiteDatabase db) {
        return db.query(
                MoviesContract.FavoriteMovies.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MoviesContract.FavoriteMovies.COLUMN_DTA_CRIACAO
        );
    }

    /**
     * Verifica se um determinado filme já esta marcado como favorito
     *
     * @param mDb
     * @param id
     * @return
     */
    public static boolean buscaFavorito(SQLiteDatabase mDb, Integer id) {
        Cursor cursor;
        cursor = mDb.query(
                MoviesContract.FavoriteMovies.TABLE_NAME,
                null,
                MoviesContract.FavoriteMovies.COLUMN_API_ID + "=?",
                new String[]{Integer.toString(id)},
                null,
                null,
                MoviesContract.FavoriteMovies.COLUMN_DTA_CRIACAO);
        boolean isfav = false;
        if (null != cursor) {
            isfav = cursor.getCount() > 0;
            cursor.close();
        }

        return isfav;
    }
}