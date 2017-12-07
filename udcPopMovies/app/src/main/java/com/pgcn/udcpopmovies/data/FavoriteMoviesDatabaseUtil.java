package com.pgcn.udcpopmovies.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.pgcn.udcpopmovies.exceptions.MovieServiceException;
import com.pgcn.udcpopmovies.model.MovieModel;

import static com.pgcn.udcpopmovies.data.MoviesContentContract.MoviesEntry;

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
     * @param movie           o filme
     * @param contentResolver
     * @throws MovieServiceException caso ocorra erro
     */
    public static Integer insertData(MovieModel movie, ContentResolver contentResolver) {

        Log.d(TAG, "=== insertData, filme " + movie.getOriginalTitle());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesEntry.COLUMN_API_ID, movie.getId());
        contentValues.put(MoviesEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MoviesEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        contentValues.put(MoviesEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MoviesEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(MoviesEntry.COLUMN_POSTER_PATH, movie.getPosterPath());

        Uri uri = contentResolver.insert(MoviesEntry.CONTENT_URI, contentValues);

        if (uri != null) {
            Log.d(TAG, "URI " + uri);
            String theMovieid = uri.getPathSegments().get(1);

            Log.d(TAG, "theMovieid " + theMovieid);
            Integer theIntegetMovieId = Integer.parseInt(theMovieid);
            Log.d(TAG, "theIntegetMovieId " + theIntegetMovieId);
            return theIntegetMovieId;
        }


        return 0;
    }


    /**
     * remove o filme da lista de favoritos
     *
     * @param contentResolver contentResolver
     * @param movieDbId       id do filme
     * @throws MovieServiceException
     */
    public static void removeData(Integer movieDbId, ContentResolver contentResolver) throws MovieServiceException {
        Log.d(TAG, "=== removeData, filme movieDbId " + movieDbId);

        // Build appropriate uri with String row id appended
        String stringId = Integer.toString(movieDbId);
        Uri uri = MoviesContentContract.MoviesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        // COMPLETED (2) Delete a single row of data using a ContentResolver
        int teste = contentResolver.delete(uri, null, null);
        if (teste <= 0) {
            throw new MovieServiceException("ERRO AO REMOVER DADOS ");
        }

    }


    /**
     * Verifica se um determinado filme já esta marcado como favorito
     *
     * @param apiId
     * @param contentResolver
     * @return
     */
    public static Integer buscaFavorito(Integer apiId, ContentResolver contentResolver) {

        Cursor cursor = contentResolver.query(MoviesContentContract.MoviesEntry.CONTENT_URI,
                null,
                MoviesEntry.COLUMN_API_ID + "=? ",
                new String[]{Integer.toString(apiId)},
                MoviesContentContract.MoviesEntry.COLUMN_DTA_CRIACAO);

        if (null != cursor && cursor.getCount() > 0 && cursor.moveToFirst()) {
            MovieModel movie = MovieFromDataUtil.getMovieModelFromCursor(cursor);
            cursor.close();
            if (0 != movie.getDatabaseId()) {
                return movie.getDatabaseId();
            }

            return null;

        }
        return null;
    }
}