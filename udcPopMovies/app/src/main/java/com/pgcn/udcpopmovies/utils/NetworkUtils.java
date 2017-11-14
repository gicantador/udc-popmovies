package com.pgcn.udcpopmovies.utils;

import android.content.Context;
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

    // config de conexao
    public static final String SORT_POPULAR_PARAM = "popularity";
    public static final String SORT_VOTE_PARAM = "vote_average";
    public static final String SORT_ASC = ".asc";
    public static final String SORT_DESC = ".desc";
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private final static String API_ROOT = "http://api.themoviedb.org/3/discover/movie";
    private final static String KEY_PARAM = "api_key";
    private final static String SORT_BY = "sort_by";



    private static final String API_KEY = "<PUT YOUR API KEY HERE>";


    public static URL buildMoviesUrl(String tipoLista, String tipoSort, Context context) {

        //  String localIso = context.getResources().getConfiguration().locale.getISO3Country();
        //  String languageIso = context.getResources().getConfiguration().locale.getISO3Language();

        //.getResources().getConfiguration().locale


        Uri builtUri = Uri.parse(API_ROOT).buildUpon()
                .appendQueryParameter(SORT_BY, tipoLista + tipoSort)
                .appendQueryParameter(KEY_PARAM, API_KEY)
                // There`s no data for Brasil ptbr
                //   .appendQueryParameter(REGION_PARAM, localIso)
                // .appendQueryParameter(LANGUAGE_PARAM, languageIso)

                .build();
        Log.d(TAG, " ++++++ +++ +++ +++ +++ Built URI " + builtUri.toString());
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Built URI " + url);

        return url;

    }

    public static String getResponseFromHttpUrl(URL movieRequestUrl) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) movieRequestUrl.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                String item = scanner.next();
                //    Log.d(TAG, "hasInput "+item);
                return item;
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
