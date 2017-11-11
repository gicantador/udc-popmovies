package com.pgcn.popmovies.popmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Giselle on 11/11/2017.
 */

public  final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String THEMOVIE_URL ="https://api.themoviedb.org/3/authentication/guest_session/new";
    final static String QUERY_PARAM_API_KEY = "api_key";


    // FIXME esconder token nao publico
    final static String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmMjgwYmU2NTBmZTc5NjA1M2I5NTMzMTUxYTE4YzViNyIsInN1YiI6IjVhMDc0NzNjOTI1MTQxMDc4NTAwMzhiMiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.y-P4J33BFP7ebN7wUL4Dez4sWuXVLerbsomttF2crWM";


    public static URL buildUrl() {
        Uri builtUri = Uri.parse(THEMOVIE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM_API_KEY, TOKEN)

                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }



//    HttpResponse<String> response = Unirest.get("https://api.themoviedb.org/4/list/%7Blist_id%7D?sort_by=vote_average.asc&api_key=%3C%3Capi_key%3E%3E&page=1")
//            .header("content-type", "application/json;charset=utf-8")
//            .header("authorization", "Bearer <<access_token>>")
//            .body("{}")
//            .asString();

//    HttpResponse<String> response =
//            Unirest.get("https://api.themoviedb.org/3/authentication/guest_session/new?api_key=%3C%3Capi_key%3E%3E")
//            .body("{}")
//            .asString();
}
