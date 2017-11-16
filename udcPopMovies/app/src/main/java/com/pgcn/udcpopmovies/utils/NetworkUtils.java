package com.pgcn.udcpopmovies.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Giselle on 11/11/2017.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    // connection configuration
    private final static String API_ROOT = "http://api.themoviedb.org/3/discover/movie";
    private final static String KEY_PARAM = "api_key";
    private final static String SORT_BY = "sort_by";
    private final static String PAGE = "page";
    private final static int timeout = 20000; // 20 sec

    // filter configuration
    public static final String SORT_POPULAR_PARAM = "popularity";
    public static final String SORT_VOTE_PARAM = "vote_average";
    public static final String SORT_ASC = ".asc";
    public static final String SORT_DESC = ".desc";

   private static final String API_KEY = "";

    /**
     * Buld conection URL
     *
     * @param tipoLista popular or top rated
     * @param tipoSort  asc or desc
     * @param pageToGet the number of the page to be request
     * @return
     */
    public static URL buildMoviesUrl(String tipoLista, String tipoSort, int pageToGet) {

        /* there's no data for locale Brasil and language pt-br, so i'll comment this
          String localIso = context.getResources().getConfiguration().locale.getISO3Country();
          String languageIso = context.getResources().getConfiguration().locale.getISO3Language();
          */

        Uri builtUri = Uri.parse(API_ROOT).buildUpon()
                .appendQueryParameter(SORT_BY, tipoLista + tipoSort)
                .appendQueryParameter(KEY_PARAM, API_KEY)
                .appendQueryParameter(PAGE, String.valueOf(pageToGet))
                // .appendQueryParameter(REGION_PARAM, localIso)
                // .appendQueryParameter(LANGUAGE_PARAM, languageIso)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Built URI " + url);
        return url;
    }


    /**
     * Connects to the api and retrieve tje data
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setConnectTimeout(timeout);
            urlConnection.setReadTimeout(timeout);
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
