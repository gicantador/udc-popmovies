package com.pgcn.udcpopmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pgcn.udcpopmovies.data.FavoriteMoviesDatabaseUtil;
import com.pgcn.udcpopmovies.data.MoviesDbHelper;
import com.pgcn.udcpopmovies.enums.TipoListaRetorno;
import com.pgcn.udcpopmovies.exceptions.MovieServiceException;
import com.pgcn.udcpopmovies.model.MovieDetailBox;
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
    private static final String KEY_FAVORITO = "KEY_FAVORITO";

    private ProgressBar mTrailersProgressBar;
    private ProgressBar mReviewsProgressBar;
    private CoordinatorLayout coordinatorLayout;

    private ArrayList<TrailerModel> mTrailerList = new ArrayList<TrailerModel>();
    private ArrayList<ReviewModel> mReviewList = new ArrayList<ReviewModel>();

    private TrailersAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private RecyclerView mTrailerRecyView;
    private RecyclerView mReviewRecyView;
    private TextView mMsgErroTrailer;
    private TextView mMsgErroReview;

    private ImageButton mBtStar;

    private boolean mRecarregaListaReview = true;
    private boolean mRecarregaListaTrailer = true;
    private boolean mFavorito = false;
    private MovieModel mMovie;
    private SQLiteDatabase mDb;

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

                mMovie = intentThatStartedThisActivity.getParcelableExtra(MovieModel.LB_MOVIE);
                if (null != mMovie) {
                    Log.d(TAG, " LB_MOVIE SELECIONADO :" + mMovie.toString());

                    if (0 != mMovie.getId()) {
                        if (mRecarregaListaTrailer) {
                            obterTrailers(mMovie.getId());
                        }
                        if (mRecarregaListaReview) {
                            obterReviews(mMovie.getId());
                        }
                    }
                    verificaSeFavorito(mMovie.getId());
                    mudaBotaoEstrela(mMovie.isFavorito());
                    mOriginalTitle.setText((String) getCleanField(mMovie.getOriginalTitle()));
                    mSynopsis.setText((String) getCleanField(mMovie.getOverview()));
                    mRating.setText((String) getCleanField(String.valueOf(mMovie.getVoteAverage())));
                    mReleaseDate.setText(formataDataRelease(mMovie.getReleaseDate()));

                    // monta a imagem do poster
                    String imagePath = mMovie.getRootPosterPath();
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

    private void verificaSeFavorito(Integer id) {
        if (!mMovie.isFavorito()) {
            MoviesDbHelper dbHelper = new MoviesDbHelper(this);
            mDb = dbHelper.getReadableDatabase();
            if (FavoriteMoviesDatabaseUtil.buscaFavorito(mDb, id)) {
                mMovie.setFavorito(true);
            }
        }
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
            if (savedInstanceState.containsKey(KEY_FAVORITO)) {
                mFavorito = savedInstanceState.getBoolean(KEY_FAVORITO);
                mudaBotaoEstrela(mFavorito);
            }
        }
    }

    private void mudaBotaoEstrela(boolean isFavorito) {
        Log.d(TAG, " mudaBotaoEstrela " + isFavorito);
        if (isFavorito) {
            mBtStar.setImageDrawable(getDrawable(R.drawable.estrela_amarela_small));
        } else {
            mBtStar.setImageDrawable(getDrawable(R.drawable.estrela_contorno_small));
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

        mBtStar = findViewById(R.id.bt_star);
        mBtStar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean fav = mMovie.isFavorito();
                mMovie.setFavorito(!fav);
                try {
                    salvarFilmeComoFavorito(mMovie.isFavorito());
                    mudaBotaoEstrela(mMovie.isFavorito());
                    mostrarFeedbackStar(mMovie.isFavorito());
                } catch (MovieServiceException e) {
                    Toast.makeText(mBtStar.getContext(), getString(R.string.txt_feeedback_erro), Toast.LENGTH_SHORT).show();
                    mMovie.setFavorito(fav);
                    e.printStackTrace();
                }

            }
        });
    }


    private void salvarFilmeComoFavorito(boolean fav) throws MovieServiceException {

        MoviesDbHelper dbHelper = new MoviesDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        if (fav) {
            FavoriteMoviesDatabaseUtil.insertData(mDb, mMovie);
        } else {
            FavoriteMoviesDatabaseUtil.removeData(mDb, mMovie.getId());
        }
        mostrarFeedbackStar(fav);

    }

    private void mostrarFeedbackStar(boolean mFavorito) {
        String msg;
        if (mFavorito) {
            msg = getString(R.string.txt_feeedback_star_add);
        } else {
            msg = getString(R.string.txt_feeedback_star_rem);
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void obterTrailers(int movieId) {
        Log.d(TAG, " obterTrailers");
        if (NetworkUtils.isOnline((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
            new TrailerService(getApplicationContext(), this).execute(movieId);
        } else {
            mTrailersProgressBar.setVisibility(View.INVISIBLE);
            mMsgErroTrailer.setVisibility(View.VISIBLE);
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

        MovieDetailBox movieDetailBox = null;
        try {
            movieDetailBox = (MovieDetailBox) output;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (null != movieDetailBox && TipoListaRetorno.TRAILERS.equals(movieDetailBox.getTipoListaRetorno())) {
            mTrailerList = movieDetailBox.getMovieTrailersList();
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
        }
        if (null != movieDetailBox && TipoListaRetorno.REVIEWS.equals(movieDetailBox.getTipoListaRetorno())) {
            mReviewList = movieDetailBox.getMovieReviewList();
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
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
                .make(coordinatorLayout, getString(R.string.erro_conexao),
                        Snackbar.LENGTH_INDEFINITE);

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
        outState.putBoolean(KEY_FAVORITO, mFavorito);
    }

}
