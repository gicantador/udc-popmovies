package com.pgcn.udcpopmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pgcn.udcpopmovies.model.MovieModel;
import com.pgcn.udcpopmovies.model.ReviewModel;
import com.pgcn.udcpopmovies.model.TrailerModel;
import com.pgcn.udcpopmovies.service.AsyncTaskDelegate;
import com.pgcn.udcpopmovies.service.ReviewService;
import com.pgcn.udcpopmovies.service.TrailerService;
import com.pgcn.udcpopmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailMovieActivity extends AppCompatActivity implements AsyncTaskDelegate,
        TrailersAdapter.TrailersAdapterOnClickHandler, ReviewAdapter.ReviewAdapterOnClickHandler {
    private static final String TAG = DetailMovieActivity.class.getSimpleName();
    private static final String RETURNED_PATTERN = "yyyy-MM-dd";
    private static final String PATTERN_TO_SHOW = "EEE, d MMM yyyy";
    private static final String KEY_LISTA_TRAILER = "KEY_LISTA_TRAILER";
    private static final String KEY_LISTA_REVIEW = "KEY_LISTA_REVIEW";

    private ProgressBar mTrailersProgressBar;
    private ProgressBar mReviewsProgressBar;
    private CoordinatorLayout coordinatorLayout;

    private ArrayList<TrailerModel> mTrailerList = new ArrayList<TrailerModel>();
    // --Commented out by Inspection (01/12/2017 15:57):private MovieModel mMovie;
    private TrailersAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private RecyclerView mTrailerRecyView;
    private RecyclerView mReviewRecyView;
    private TextView mMsgErroTrailer;
    private TextView mMsgErroReview;
    private ArrayList<ReviewModel> mReviewList = new ArrayList<ReviewModel>();
    private boolean mRecarregaListaReview = true;
    private boolean mRecarregaListaTrailer = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        Log.d(TAG, " onCreate");

        TextView mOriginalTitle = findViewById(R.id.tv_movie_title);
        TextView mSynopsis = findViewById(R.id.tv_synopsis);
        TextView mRating = findViewById(R.id.tv_rating);
        TextView mReleaseDate = findViewById(R.id.tv_release_date);
        ImageView mPoster = findViewById(R.id.iv_poster);

        iniciaVariaveis();
        recuperaDados(savedInstanceState);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(MovieModel.LB_MOVIE)) {

                MovieModel movie = intentThatStartedThisActivity.getParcelableExtra(MovieModel.LB_MOVIE);
                if (null != movie) {
                    Log.d(TAG, " LB_MOVIE SELECIONADO :" + movie.toString());

                    if (0 != movie.getId()) {
                        if (mRecarregaListaTrailer) {
                            obterTrailers(movie.getId());
                        }
                        if (mRecarregaListaReview) {
                            obterReviews(movie.getId());
                        }
                        //montaGridTrailer();
                    }
                    mOriginalTitle.setText((String) getCleanField(movie.getOriginalTitle()));
                    mSynopsis.setText((String) getCleanField(movie.getOverview()));
                    mRating.setText((String) getCleanField(String.valueOf(movie.getVoteAverage())));
                    mReleaseDate.setText(formataDataRelease(movie.getReleaseDate()));

                    // monta a imagem do poster
                    String imagePath = movie.getPosterPath();
                    if (imagePath != null && !imagePath.isEmpty()) {
                        Picasso.with(this).load(imagePath).placeholder(R.drawable.placeholder_empty).into(mPoster);
                    }


                }
            }
        }

        mTrailerAdapter = new TrailersAdapter(mTrailerList, this);
        mTrailerRecyView.setAdapter(mTrailerAdapter);
        montaGridTrailer();

        mReviewAdapter = new ReviewAdapter(mReviewList, this);
        mReviewRecyView.setAdapter(mReviewAdapter);
        montaGridReview();
    }

    private void recuperaDados(Bundle savedInstanceState) {
        Log.d(TAG, "recuperaDados");

        if (null != savedInstanceState) {
            if (savedInstanceState.containsKey(KEY_LISTA_TRAILER)) {
                mTrailerList = savedInstanceState.getParcelableArrayList(KEY_LISTA_TRAILER);
                mRecarregaListaTrailer = !(null != mTrailerList && !mTrailerList.isEmpty());
            }

            if (savedInstanceState.containsKey(KEY_LISTA_REVIEW)) {
                mReviewList = savedInstanceState.getParcelableArrayList(KEY_LISTA_REVIEW);
                mRecarregaListaReview = !(null != mReviewList && !mReviewList.isEmpty());
            }
        }
    }


    private void iniciaVariaveis() {
        coordinatorLayout = findViewById(R.id.dt_coordinator_layout);
        mMsgErroTrailer = findViewById(R.id.tv_msg_erro_trailer);
        mMsgErroReview = findViewById(R.id.tv_msg_erro_review);

        mTrailerRecyView = findViewById(R.id.rv_trailers);
        mReviewRecyView = findViewById(R.id.rv_reviews);

        mTrailersProgressBar = findViewById(R.id.pb_loading_indicator_trailers);
        mReviewsProgressBar = findViewById(R.id.pb_loading_indicator_reviews);
    }

    private void obterTrailers(int movieId) {
        Log.d(TAG, " obterTrailers");
        if (NetworkUtils.isOnline((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
            new TrailerService(getApplicationContext(), this).execute(movieId);
        } else {
            mTrailersProgressBar.setVisibility(View.INVISIBLE);
            mostrarFeedback();
        }


    }

    private void obterReviews(int movieId) {
        Log.d(TAG, " obterTrailers");
        if (NetworkUtils.isOnline((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
            new ReviewService(getApplicationContext(), this).execute(movieId);
        } else {
            mReviewsProgressBar.setVisibility(View.INVISIBLE);
            mMsgErroReview.setVisibility(View.VISIBLE);
            mostrarFeedback();
        }
    }

    /**
     * Formata a data de release conforme um pattern
     *
     * @param txtReleaseDate data de release em texto
     * @return data de release formatada
     */
    private String formataDataRelease(String txtReleaseDate) {

        if (null != txtReleaseDate && !txtReleaseDate.isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat(RETURNED_PATTERN);
            try {
                Date data = formatter.parse(txtReleaseDate);
                if (null != data) {
                    final DateFormat df = new SimpleDateFormat(PATTERN_TO_SHOW);
                    return df.format(data);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return getString(R.string.txt_empty_data);
    }


    /**
     * Verifica se o dado está preenchido e em caso negativo, coloca um texto avisando
     *
     * @param obj o dado a ser verificado
     * @return o dado ou o texto
     */
    private Object getCleanField(Object obj) {
        if (null == obj) {
            return getString(R.string.txt_empty_data);
        } else if (String.class == obj.getClass()) {
            String tst = (String) obj;
            if (tst.trim().isEmpty()) {
                return getString(R.string.txt_empty_data);
            }
        }
        return obj;
    }

    @Override
    public void processFinish(Object output) {
        Log.d(TAG, "processFinish");

        ArrayList<Object> lista = (ArrayList<Object>) output;
        if (null != lista && !lista.isEmpty()) {
            Object ob = lista.get(0);
            if (null != ob && ob.getClass().equals(TrailerModel.class)) {
                mTrailerList = (ArrayList<TrailerModel>) output;
                mTrailersProgressBar.setVisibility(View.INVISIBLE);
                if (null != mTrailerList) {
                    if (mTrailerList.isEmpty()) {
                        mMsgErroTrailer.setVisibility(View.VISIBLE);

                    } else {
                        mTrailerAdapter = new TrailersAdapter(mTrailerList, this);
                        mTrailerRecyView.setAdapter(mTrailerAdapter);
                        montaGridTrailer();
                    }
                } else
                    mMsgErroTrailer.setVisibility(View.VISIBLE);
            } else if (null != ob && ob.getClass().equals(ReviewModel.class)) {
                mReviewList = (ArrayList<ReviewModel>) output;
                mReviewsProgressBar.setVisibility(View.INVISIBLE);
                if (null != mReviewList) {
                    if (mReviewList.isEmpty()) {
                        mMsgErroReview.setVisibility(View.VISIBLE);
                    }
                    mReviewAdapter = new ReviewAdapter(mReviewList, this);
                    mReviewRecyView.setAdapter(mReviewAdapter);
                    montaGridReview();

                } else
                    mMsgErroReview.setVisibility(View.VISIBLE);
            }
        }

    }


    private void montaGridTrailer() {
        Log.d(TAG, "montaGridTrailer");
        mTrailerRecyView = montaGridGenerico(mTrailerRecyView);
        mTrailerRecyView.setAdapter(mTrailerAdapter);
    }

    private void montaGridReview() {
        Log.d(TAG, "montaGridReview");
        mReviewRecyView = montaGridGenerico(mReviewRecyView);
        mReviewRecyView.setAdapter(mReviewAdapter);
    }


    private RecyclerView montaGridGenerico(RecyclerView anyRecyView) {
        Log.d(TAG, "montaGridGenerico");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        anyRecyView.setLayoutManager(layoutManager);
        anyRecyView.setHasFixedSize(false);
        return anyRecyView;
    }

    @Override
    public void preExecute() {
        Log.d(TAG, "preExecute");
        mTrailersProgressBar.setVisibility(View.VISIBLE);


    }

    /**
     *
     */
    private void mostrarFeedback() {
        Log.d(TAG, "mostrarFeedback");
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, getString(R.string.erro_conexao), Snackbar.LENGTH_INDEFINITE);

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();

    }

    @Override
    public void onClick(TrailerModel trailer) {
        Log.d(TAG, "onClicktrailer");
        Context context = this;

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }


    }

    @Override
    public void onClick(ReviewModel review) {
        Log.d(TAG, "onClickreview");
        if (null != review) {
            Uri webpage = Uri.parse(review.getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_LISTA_TRAILER, mTrailerList);
        outState.putParcelableArrayList(KEY_LISTA_REVIEW, mReviewList);
    }
}
