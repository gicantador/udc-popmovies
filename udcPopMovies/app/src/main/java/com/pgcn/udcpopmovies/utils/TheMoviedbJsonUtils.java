package com.pgcn.udcpopmovies.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.pgcn.udcpopmovies.model.MovieModel;
import com.pgcn.udcpopmovies.model.ReviewModel;
import com.pgcn.udcpopmovies.model.TrailerModel;

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


    /**
     * Monta uma lista de objetos MovieModel a partir do JSON retornado pelo serviço
     *
     * @param movieJsonStr JSON retornado pelo serviço
     * @return lista de objetos MovieModel
     * @throws JSONException
     */
    public static ArrayList<MovieModel> getSimpleMovieStringsFromJson(String movieJsonStr) throws JSONException {

        ArrayList<MovieModel> movieModelArrayList = new ArrayList<MovieModel>();
        JSONObject moviesJson = new JSONObject(movieJsonStr);

        verificaRetorno(moviesJson);

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

    /**
     * Monta uma lista de objetos TrailerModel a partir do JSON retornado pelo serviço
     *
     * @param trailerJsonStr JSON retornado pelo serviço
     * @return lista de objetos TrailerModel
     * @throws JSONException
     */
    public static ArrayList<TrailerModel> getSimpleTrailerStringsFromJson(String trailerJsonStr) throws JSONException {
        ArrayList<TrailerModel> trailerModelArrayList = new ArrayList<TrailerModel>();
        JSONObject trailerJson = new JSONObject(trailerJsonStr);

        verificaRetorno(trailerJson);

        if (trailerJson.has(TMDB_LIST)) {
            JSONArray trailerArray = trailerJson.getJSONArray(TMDB_LIST);
            Gson gson2 = new Gson();

            for (int i = 0; i < trailerArray.length(); i++) {
                JSONObject finalObject = trailerArray.getJSONObject(i);
                TrailerModel inpuTrailer = gson2.fromJson(finalObject.toString(), TrailerModel.class);
                trailerModelArrayList.add(inpuTrailer);
            }

        }
        return trailerModelArrayList;

    }


    /**
     * Monta uma lista de objetos ReviewModela partir do JSON retornado pelo serviço
     *
     * @param jsonStr JSON retornado pelo serviço
     * @return lista de objetos ReviewModel
     * @throws JSONException
     */
    public static ArrayList<ReviewModel> getSimpleReviewStringsFromJson(String jsonStr) throws JSONException {


        ArrayList<ReviewModel> anyMoodelArrayList = new ArrayList<ReviewModel>();
        JSONObject anyJson = new JSONObject(jsonStr);

        verificaRetorno(anyJson);

        if (anyJson.has(TMDB_LIST)) {
            JSONArray trailerArray = anyJson.getJSONArray(TMDB_LIST);
            Gson gson2 = new Gson();

            for (int i = 0; i < trailerArray.length(); i++) {
                JSONObject finalObject = trailerArray.getJSONObject(i);
                ReviewModel inpuTrailer = gson2.fromJson(finalObject.toString(), ReviewModel.class);
                anyMoodelArrayList.add(inpuTrailer);
            }
        }
        return anyMoodelArrayList;

    }

    /**
     * Verifica o c[odigo de retorno
     *
     * @param jsonObject
     * @throws JSONException
     */
    private static void verificaRetorno(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has(TMDB_MESSAGE_CODE)) {
            int errorCode = jsonObject.getInt(TMDB_MESSAGE_CODE);
            Log.d(TAG, "Codigo de retorno " + String.valueOf(errorCode));
            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    Log.d(TAG, "Tudo ok. ");
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    Log.d(TAG, "Location invalid. ");
                    throw new JSONException("Error code " + HttpURLConnection.HTTP_NOT_FOUND);
                default:
                    Log.d(TAG, "Server probably down. ");
                    throw new JSONException("Error code " + errorCode);
            }
        }
    }


}
