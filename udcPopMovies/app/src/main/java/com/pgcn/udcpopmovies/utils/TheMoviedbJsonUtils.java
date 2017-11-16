package com.pgcn.udcpopmovies.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by Giselle on 11/11/2017.
 */

public class TheMoviedbJsonUtils {

    private static final String TAG = TheMoviedbJsonUtils.class.getSimpleName();
    private static final String TMDB_MESSAGE_CODE = "status_code";
    private static final String TMDB_LIST = "results";
    private static final String labelNroPage = "page";
    private static final String labelTotalResults = "total_results";
    private static final String labelTotalPages = "total_pages";

    public static ArrayList<MovieModel> getSimpleMovieStringsFromJson(Context context, String movieJsonStr)
            throws JSONException {

        ArrayList<MovieModel> movieModelArrayList = new ArrayList<MovieModel>();
        JSONObject moviesJson = new JSONObject(movieJsonStr);

        /* Is there an error? */
        verificaRetorno(moviesJson);

/*        int qtdMovies = 0;
        int qtdPag = 0;
        int nroPagAtual = 0;


        if (moviesJson.has(labelTotalResults)) {
            qtdMovies = moviesJson.getInt(labelTotalResults);
        }
        if (moviesJson.has(labelTotalPages)) {
            qtdPag = moviesJson.getInt(labelTotalPages);

        }
        if (moviesJson.has(labelNroPage)) {
            nroPagAtual = moviesJson.getInt(labelNroPage);
        }
*/
        if (moviesJson.has(TMDB_LIST)) {
            JSONArray moviesArray = moviesJson.getJSONArray(TMDB_LIST);
            Gson gson2 = new Gson();

            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject finalObject = moviesArray.getJSONObject(i);
                MovieModel inputMovie = gson2.fromJson(finalObject.toString(), MovieModel.class);
                movieModelArrayList.add(inputMovie);
            }


        }
        return movieModelArrayList;

    }

    private static void verificaRetorno(JSONObject moviesJson) throws JSONException {
        if (moviesJson.has(TMDB_MESSAGE_CODE)) {
            int errorCode = moviesJson.getInt(TMDB_MESSAGE_CODE);
            Log.d(TAG, "Codigo de retorno " + String.valueOf(errorCode));
            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    Log.d(TAG, "Tudo ok. ");
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    Log.d(TAG, "Location invalid. ");
                    throw new JSONException("Error code " + HttpURLConnection.HTTP_NOT_FOUND);
            //        return null;
                default:
                    Log.d(TAG, "Server probably down. ");
                    throw new JSONException("Error code " + HttpURLConnection.HTTP_NOT_FOUND);
                   // return null;
            }
        }
    }


}
