package com.pgcn.udcpopmovies.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.pgcn.udcpopmovies.model.ReviewModel;
import com.pgcn.udcpopmovies.utils.NetworkUtils;
import com.pgcn.udcpopmovies.utils.TheMoviedbJsonUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Giselle on 30/11/2017.
 */

public class ReviewService extends AsyncTask<Object, String, ArrayList<ReviewModel>> {
    private static final String TAG = ReviewService.class.getSimpleName();
    private AsyncTaskDelegate delegate = null;

    /**
     * No construtor da classe, passamos uma classe responsável por "responder" a requisição após a
     * sua execução Esse responsável é o AsyncTaskDelegate
     *
     * @param context
     * @param responder
     */
    public ReviewService(Context context, AsyncTaskDelegate responder) {
        this.delegate = responder;
    }


    @Override
    protected ArrayList<ReviewModel> doInBackground(Object... objects) {
        Log.d(TAG, "=== Inicio Async ReviewService - doInBackground");
        try {
            int movieId = (int) objects[0];
            if (0 != movieId) {
                URL reviewRequestUrl = NetworkUtils.buildReviewUrl(movieId);
                String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(reviewRequestUrl);

                if (jsonReviewResponse != null) {
                    ArrayList<ReviewModel> listaReviews;
                    listaReviews = TheMoviedbJsonUtils.getSimpleReviewStringsFromJson(jsonReviewResponse);
                    if (null != listaReviews) {
                        Log.d(TAG, "Reviews recuperados: " + listaReviews.size());
                    }
                    return listaReviews;
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
    protected void onPostExecute(ArrayList<ReviewModel> lreview) {

        super.onPostExecute(lreview);
        if (delegate != null)
            delegate.processFinish(lreview);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (delegate != null)
            delegate.preExecute();
    }
}
