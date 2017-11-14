package com.pgcn.udcpopmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pgcn.udcpopmovies.utils.EndlessRecyclerOnScrollListener;
import com.pgcn.udcpopmovies.utils.MovieModel;
import com.pgcn.udcpopmovies.utils.NetworkUtils;
import com.pgcn.udcpopmovies.utils.TheMoviedbJsonUtils;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.ArrayList;

public class ShowMovies extends AppCompatActivity
        implements MoviesAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = ShowMovies.class.getSimpleName();
    private final int NRO_COLUNAS = 2;
    ArrayList<MovieModel> mMovieModelArrayList = new ArrayList<MovieModel>();
    private MoviesAdapter mMoviestAdapter;
    private RecyclerView mRecyView;
    private ProgressBar mPbLoadingIndicator;
    private String mTipoLista;
    private String mTipoSort;
    private TextView mFilterTextView;
    final private MoviesAdapter.MovieAdapterOnClickHandler mClickHandler = this;
    private CoordinatorLayout coordinatorLayout;

    public int mCurrentPage = 0;

    //    final Activity showmoviesActivity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_movies);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        // inicia com filmes populares desc
        mTipoLista = NetworkUtils.SORT_POPULAR_PARAM;
        mTipoSort = NetworkUtils.SORT_DESC;

        mFilterTextView = (TextView) findViewById(R.id.text_filter);

        Log.d(TAG, "=== Inicio ShowMovies");
        mRecyView = (RecyclerView) findViewById(R.id.rv_movies);
        mPbLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadMovieData();
        montaGrid();
        montaTextoAlerta();

        mMoviestAdapter = new MoviesAdapter(mMovieModelArrayList, this);
        mRecyView.setAdapter(mMoviestAdapter);
    }

    private void montaGrid() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NRO_COLUNAS);
        mRecyView.setLayoutManager(gridLayoutManager);
        mRecyView.setHasFixedSize(false);
        mRecyView.setOnScrollListener(new EndlessRecyclerOnScrollListener((GridLayoutManager) mRecyView.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                Log.d(TAG, "onLoadMore" + current_page);
                mCurrentPage++;
                if (0 != current_page) {
                    loadMovieData();
                }
            }

        });
    }


    /**
     * Carrega os dados de filmes de forma assíncrona
     */
    private void loadMovieData() {

        if (isOnline()) {
            if (0 == mCurrentPage) {
                mCurrentPage++;
            }
            new FetchMovieTask().execute();
        } else {
            mostrarFeedback(getString(R.string.erro_conexao), Snackbar.LENGTH_INDEFINITE);
        }
        //  montaGrid();
    }

    /**
     * Verifica se device est[a conectado
     *
     * @return
     */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    @Override
    public void onClick(MovieModel movie) {

        Context context = this;
        Class destinationClass = DetailMovieActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("Movie", movie);
        startActivity(intentToStartDetailActivity);
    }

    private void invalidateData() {
        mMoviestAdapter.setMovieData(null);
        loadMovieData();
    }


    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<MovieModel>> {

        @Override
        protected void onPreExecute() {
            mPbLoadingIndicator.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<MovieModel> movieModels) {
            super.onPostExecute(movieModels);
            mPbLoadingIndicator.setVisibility(View.INVISIBLE);

            if (movieModels != null) {
                mMovieModelArrayList = movieModels;
            }
            mMoviestAdapter = new MoviesAdapter(mMovieModelArrayList, mClickHandler);

            mRecyView.setAdapter(mMoviestAdapter);
        }

        @Override
        protected ArrayList<MovieModel> doInBackground(String... strings) {

            Log.d(TAG, "=== Inicio Asynnc FetchMovieTask - doInBackground");

            URL movieRequestUrl = NetworkUtils.buildMoviesUrl(mTipoLista, mTipoSort, mCurrentPage);
            try {
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                if (jsonMoviesResponse != null) {
                    if (null == mMovieModelArrayList || mMovieModelArrayList.isEmpty()) {
                        mMovieModelArrayList = TheMoviedbJsonUtils
                                .getSimpleMovieStringsFromJson(ShowMovies.this, jsonMoviesResponse);
                    } else if (!mMovieModelArrayList.isEmpty()) {
                        mMovieModelArrayList.addAll(TheMoviedbJsonUtils
                                .getSimpleMovieStringsFromJson(ShowMovies.this, jsonMoviesResponse));
                    }
//                    if (mMovieModelArrayList != null) {
//                        Log.d(TAG, "Tamanho mMovieModelArrayList: " + mMovieModelArrayList.size());
//                    }
                    return mMovieModelArrayList;
                }

            } catch (Exception e) {
                Log.e(TAG, " doInBackground ", e);
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }
    }

    // menus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_sort_popular:
                mTipoLista = NetworkUtils.SORT_POPULAR_PARAM;
                break;
            case R.id.action_sort_rated:
                mTipoLista = NetworkUtils.SORT_VOTE_PARAM;
                break;
            case R.id.action_sort_asc:
                mTipoSort = NetworkUtils.SORT_ASC;
                break;
            case R.id.action_sort_desc:
                mTipoSort = NetworkUtils.SORT_DESC;
                break;
            default:
                mTipoLista = NetworkUtils.SORT_POPULAR_PARAM;
                mTipoSort = NetworkUtils.SORT_ASC;

        }
        // updatefilter;

        Snackbar snackbar = Snackbar.make(coordinatorLayout, montaTextoAlerta(), Snackbar.LENGTH_SHORT);
        snackbar.show();
        invalidateData();
        return true;
    }


    private String montaTextoAlerta() {

        final String nm = "action_name_";
        int resId1Lista = getResources().getIdentifier(nm + mTipoLista, "string",
                this.getPackageName());
        String txtLista =
                getString(resId1Lista);

        int resId1Sort = getResources().getIdentifier(nm + mTipoSort, "string",
                this.getPackageName());
        String txtSort =
                getString(resId1Sort);

        String txt = getString(R.string.label_filtro_usado) + StringUtils.SPACE + txtLista + StringUtils.SPACE + txtSort;

        mFilterTextView.setText(txt);
        mFilterTextView.setVisibility(View.VISIBLE);
        return txt;
    }

    private void mostrarFeedback(String message, int lengthIndefinite) {

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, lengthIndefinite)
                .setAction(R.string.reload, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadMovieData();
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();

    }
}

