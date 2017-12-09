package com.pgcn.udcpopmovies.service;

import android.os.AsyncTask;
import android.util.Log;

import com.pgcn.udcpopmovies.model.MovieDetailBox;
import com.pgcn.udcpopmovies.model.ReviewModel;
import com.pgcn.udcpopmovies.utils.NetworkUtils;
import com.pgcn.udcpopmovies.utils.TheMoviedbJsonUtils;
import com.pgcn.udcpopmovies.utils.TiposDefinidos;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Giselle on 30/11/2017.
 */

public class ReviewService extends AsyncTask<Object, String, MovieDetailBox> {
    private static final String TAG = ReviewService.class.getSimpleName();
    private AsyncTaskDelegate delegate = null;

    /**
     * No construtor da classe, passamos uma classe responsável por "responder" a requisição após a
     * sua execução Esse responsável é o AsyncTaskDelegate
     *
     * @param responder
     */
    public ReviewService(AsyncTaskDelegate responder) {
        this.delegate = responder;
    }


    @Override
    protected MovieDetailBox doInBackground(Object... objects) {
        Log.d(TAG, "=== Inicio Async ReviewService - doInBackground");
        try {
            int movieId = (int) objects[0];
            if (0 != movieId) {
                URL reviewRequestUrl = NetworkUtils.buildListasUrl(movieId, TiposDefinidos.REVIEWS);
                String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(reviewRequestUrl);

                if (jsonReviewResponse != null) {
                    ArrayList<ReviewModel> listaReviews;
                    listaReviews = TheMoviedbJsonUtils.getSimpleReviewStringsFromJson(jsonReviewResponse);
                    if (null != listaReviews) {
                        Log.d(TAG, "Reviews recuperados: " + listaReviews.size());
                    }
                    return new MovieDetailBox(null, listaReviews, TiposDefinidos.REVIEWS);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, " doInBackground ", e);
            e.printStackTrace();
            return new MovieDetailBox(null, null, TiposDefinidos.REVIEWS);
        }

        return new MovieDetailBox(null, null, TiposDefinidos.REVIEWS);
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
