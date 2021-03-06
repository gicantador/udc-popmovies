package com.pgcn.udcpopmovies.service;

import android.os.AsyncTask;
import android.util.Log;

import com.pgcn.udcpopmovies.model.MovieDetailBox;
import com.pgcn.udcpopmovies.model.TrailerModel;
import com.pgcn.udcpopmovies.utils.NetworkUtils;
import com.pgcn.udcpopmovies.utils.TheMoviedbJsonUtils;
import com.pgcn.udcpopmovies.utils.TiposDefinidos;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Giselle on 29/11/2017.
 */

public class TrailerService extends AsyncTask<Object, String, MovieDetailBox> {

    private static final String TAG = TrailerService.class.getSimpleName();
    private AsyncTaskDelegate delegate = null;


    /**
     * No construtor da classe, passamos uma classe responsável por "responder" a requisição após a
     * sua execução Esse responsável é o AsyncTaskDelegate
     *
     * @param responder
     */
    public TrailerService(AsyncTaskDelegate responder) {
        this.delegate = responder;
    }

    @Override
    protected MovieDetailBox doInBackground(Object... objects) {
        Log.d(TAG, "=== Inicio Async TrailerService - doInBackground");

        try {
            int movieId = (int) objects[0];
            if (0 != movieId) {
                URL trailerRequestUrl = NetworkUtils.buildListasUrl(movieId, TiposDefinidos.TRAILERS);
                String jsonTrailersResponse = NetworkUtils.getResponseFromHttpUrl(trailerRequestUrl);

                if (jsonTrailersResponse != null) {
                    ArrayList<TrailerModel> listaTrailers;
                    listaTrailers = TheMoviedbJsonUtils.getSimpleTrailerStringsFromJson(jsonTrailersResponse);
                    if (null != listaTrailers) {
                        Log.d(TAG, "Trailers recuperados: " + listaTrailers.size());
                    }
                    return new MovieDetailBox(listaTrailers, null, TiposDefinidos.TRAILERS);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, " doInBackground ", e);
            e.printStackTrace();
            return new MovieDetailBox(null, null, TiposDefinidos.TRAILERS);
        }

        return new MovieDetailBox(null, null, TiposDefinidos.TRAILERS);
    }


    @Override
    protected void onPostExecute(MovieDetailBox detailBox) {

        super.onPostExecute(detailBox);
        if (delegate != null)
            delegate.processFinish(detailBox);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (delegate != null)
            delegate.preExecute();
    }
}

