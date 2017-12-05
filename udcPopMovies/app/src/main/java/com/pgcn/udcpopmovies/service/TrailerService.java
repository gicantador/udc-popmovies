package com.pgcn.udcpopmovies.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.pgcn.udcpopmovies.model.TrailerModel;
import com.pgcn.udcpopmovies.utils.NetworkUtils;
import com.pgcn.udcpopmovies.utils.TheMoviedbJsonUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Giselle on 29/11/2017.
 */

public class TrailerService extends AsyncTask<Object, String, ArrayList<TrailerModel>> {

    private static final String TAG = TrailerService.class.getSimpleName();
    private AsyncTaskDelegate delegate = null;


    /**
     * No construtor da classe, passamos uma classe responsável por "responder" a requisição após a
     * sua execução Esse responsável é o AsyncTaskDelegate
     *
     * @param context
     * @param responder
     */
    public TrailerService(Context context, AsyncTaskDelegate responder) {
        this.delegate = responder;
    }

    @Override
    protected ArrayList<TrailerModel> doInBackground(Object... objects) {
        Log.d(TAG, "=== Inicio Async TrailerService - doInBackground");

        try {
            int movieId = (int) objects[0];
            if (0 != movieId) {
                URL trailerRequestUrl = NetworkUtils.buildTrailersUrl(movieId);
                String jsonTrailersResponse = NetworkUtils.getResponseFromHttpUrl(trailerRequestUrl);

                if (jsonTrailersResponse != null) {
                    ArrayList<TrailerModel> listaTrailers;
                    listaTrailers = TheMoviedbJsonUtils.getSimpleTrailerStringsFromJson(jsonTrailersResponse);
                    if (null != listaTrailers) {
                        Log.d(TAG, "Trailers recuperados: " + listaTrailers.size());
                    }
                    return listaTrailers;
                }
            }

        } catch (Exception e) {
            Log.e(TAG, " doInBackground ", e);
            e.printStackTrace();
            return null;
        }

        return null;
    }


    @Override
    protected void onPostExecute(ArrayList<TrailerModel> trailers) {

        super.onPostExecute(trailers);
        if (delegate != null)
            delegate.processFinish(trailers);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (delegate != null)
            delegate.preExecute();
    }
}

