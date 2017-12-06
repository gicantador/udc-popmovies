package com.pgcn.udcpopmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pgcn.udcpopmovies.data.MoviesDbHelper;
import com.pgcn.udcpopmovies.enums.SortOrder;
import com.pgcn.udcpopmovies.enums.TipoFiltro;
import com.pgcn.udcpopmovies.model.MovieFilter;
import com.pgcn.udcpopmovies.model.MovieModel;
import com.pgcn.udcpopmovies.service.AsyncTaskDelegate;
import com.pgcn.udcpopmovies.service.MovieService;
import com.pgcn.udcpopmovies.utils.NetworkUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class ShowMoviesActivity extends AppCompatActivity implements AsyncTaskDelegate,
        MoviesAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = ShowMoviesActivity.class.getSimpleName() + " ===== ";

    private ArrayList<MovieModel> mMovieModelArrayList = new ArrayList<MovieModel>();
    private MoviesAdapter mMoviestAdapter;
    private RecyclerView mRecyView;
    private ProgressBar mPbLoadingIndicator;
    private CoordinatorLayout coordinatorLayout;
    private TextView mFilterTextView;

    // inicia com filmes populares desc
    // private String mTipoLista = NetworkUtils.SORT_POPULAR_PARAM;
    private TipoFiltro mTipoFiltro = TipoFiltro.POPULAR;
    private SortOrder mSortOrder = SortOrder.DESC;

    private int mCurrentPage = 0;
    private final boolean mRecarregaLista = true;

    private static final String KEY_TIPO_FILTRO = "KEY_TIPO_FILTRO";
    private static final String KEY_SORT_FILTRO = "KEY_SORT_FILTRO";

    private MoviesDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_show_movies);

        recuperaDados(savedInstanceState);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        mFilterTextView = findViewById(R.id.text_filter);

        mRecyView = findViewById(R.id.rv_movies);
        mPbLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        if (mRecarregaLista) {
            loadMovieData();
        }

        mMoviestAdapter = new MoviesAdapter(mMovieModelArrayList, this);
        mRecyView.setAdapter(mMoviestAdapter);
        montaGrid();
        montaTextoAlerta();
    }

    /**
     * Recupera os dados salvos na memória para recriar a tela
     *
     * @param savedInstanceState
     */
    private void recuperaDados(Bundle savedInstanceState) {
        Log.d(TAG, "recuperaDados");

        if (null != savedInstanceState) {
            if (savedInstanceState.containsKey(KEY_TIPO_FILTRO)) {
                String tipo = savedInstanceState.getString(KEY_TIPO_FILTRO);
                if (null == tipo || TextUtils.isEmpty(tipo)) {
                    mTipoFiltro = TipoFiltro.POPULAR;
                } else {
                    mTipoFiltro = TipoFiltro.getByName(tipo);
                }
            }
            if (savedInstanceState.containsKey(KEY_SORT_FILTRO)) {
                String tipo = savedInstanceState.getString(KEY_SORT_FILTRO);
                if (null == tipo || TextUtils.isEmpty(tipo)) {
                    mSortOrder = SortOrder.DESC;
                } else {
                    mSortOrder = SortOrder.getByName(tipo);
                }
            }
            if (mTipoFiltro.equals(TipoFiltro.FAVORITES)) {
                mDbHelper = new MoviesDbHelper(this);
            }
        }
    }

    /**
     * Monta do GridLayout
     */
    private void montaGrid() {
        Log.d(TAG, "montaGrid");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        mRecyView.setLayoutManager(gridLayoutManager);
        mRecyView.setHasFixedSize(false);
        mRecyView.setAdapter(mMoviestAdapter);
    }


    /**
     * Carrega os dados de filmes de forma assíncrona
     */
    private void loadMovieData() {
        Log.d(TAG, "loadMovieData");
        if (NetworkUtils.isOnline((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
            if (0 == mCurrentPage) {
                mCurrentPage++;
            }

            new MovieService(getApplicationContext(), this).execute(new MovieFilter(mTipoFiltro, mSortOrder,
                    mCurrentPage, mMovieModelArrayList, mDbHelper));

        } else {
            mostrarFeedback(getString(R.string.erro_conexao));
        }
    }


    /**
     * Chama tela de detalhes de filme através de intent
     *
     * @param movie o filme escolhido
     */
    @Override
    public void onClick(MovieModel movie) {
        Log.d(TAG, "onClick");
        Context context = this;
        Class destinationClass = DetailMovieActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(MovieModel.LB_MOVIE, movie);
        startActivity(intentToStartDetailActivity);
    }

    /**
     * Limpa a tela para nova busca
     */
    private void invalidateData() {
        Log.d(TAG, "invalidateData()");
        mMoviestAdapter.setMovieData();
        if (null != mMovieModelArrayList) {
            mMovieModelArrayList.clear();
        }
        loadMovieData();
    }

    @Override
    public void processFinish(Object output) {
        Log.d(TAG, "processFinish");

        mPbLoadingIndicator.setVisibility(View.INVISIBLE);
        mMovieModelArrayList = (ArrayList<MovieModel>) output;

        mMoviestAdapter = new MoviesAdapter(mMovieModelArrayList, this);
        mRecyView.setAdapter(mMoviestAdapter);
        montaGrid();

    }

    @Override
    public void preExecute() {
        Log.d(TAG, "preExecute");
        mPbLoadingIndicator.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TIPO_FILTRO, mTipoFiltro.getValue());
        outState.putString(KEY_SORT_FILTRO, mSortOrder.getValue());
    }


    // menus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        int id = item.getItemId();

        switch (id) {
            case R.id.action_sort_popular:
                mTipoFiltro = TipoFiltro.POPULAR;
                break;
            case R.id.action_sort_rated:
                mTipoFiltro = TipoFiltro.TOP_RATED;
                break;
            case R.id.action_sort_asc:
                mSortOrder = SortOrder.ASC;
                break;
            case R.id.action_sort_desc:
                mSortOrder = SortOrder.DESC;
                break;
            case R.id.action_favoritos:
                mTipoFiltro = TipoFiltro.FAVORITES;
                mDbHelper = new MoviesDbHelper(this);
                break;

            default:
                mTipoFiltro = TipoFiltro.POPULAR;
                mSortOrder = SortOrder.ASC;

        }

        Snackbar snackbar = Snackbar.make(coordinatorLayout, montaTextoAlerta(), Snackbar.LENGTH_SHORT);
        snackbar.show();
        invalidateData();
        return true;
    }


    /**
     * Monta o texto que é exibido na tela para mostrar o filtro selecionado
     *
     * @return texto
     */
    private String montaTextoAlerta() {
        Log.d(TAG, "montaTextoAlerta");

        final String nm = "action_name_";
        int resId1Lista = getResources().getIdentifier(nm + mTipoFiltro.getValue(), "string", this.getPackageName());
        String txtLista = getString(resId1Lista);

        int resId1Sort = getResources().getIdentifier(nm + mSortOrder.getValue(), "string", this.getPackageName());
        String txtSort = getString(resId1Sort);

        String txt = getString(R.string.label_filtro_usado) + StringUtils.SPACE + txtLista + StringUtils.SPACE + txtSort;

        mFilterTextView.setText(txt);
        mFilterTextView.setVisibility(View.VISIBLE);
        return txt;
    }

    private void mostrarFeedback(String message) {
        Log.d(TAG, "mostrarFeedback");
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
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
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume mTipoLista" + mTipoFiltro.getValue());
        if (mTipoFiltro.equals(TipoFiltro.FAVORITES)) {
            invalidateData();
        }
        super.onResume();
    }
}

