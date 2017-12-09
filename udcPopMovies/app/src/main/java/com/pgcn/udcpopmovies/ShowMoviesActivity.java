package com.pgcn.udcpopmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pgcn.udcpopmovies.model.MovieFilter;
import com.pgcn.udcpopmovies.model.MovieModel;
import com.pgcn.udcpopmovies.service.AsyncTaskDelegate;
import com.pgcn.udcpopmovies.service.MovieService;
import com.pgcn.udcpopmovies.utils.NetworkUtils;
import com.pgcn.udcpopmovies.utils.TiposDefinidos;

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

    @TiposDefinidos.Tipos
    private int mTipoFiltro;

    private int mCurrentPage = 0;
    private boolean mRecarregaLista = true;

    private static final String KEY_BOOL_RECARREGA = "KEY_BOOL_RECARREGAR";
    private static final String KEY_TIPO_LISTA = "KEY_TIPO_LISTAGEM";
    private static final String KEY_MOVIELIST = "KEY_LISTA_MOVIES";
    private static final String KEY_LIST_STATE = "KEY_LISTA_ESTADO";
    private RecyclerView.LayoutManager mLayoutManager;
    private int mAdapterPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_show_movies);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        mFilterTextView = findViewById(R.id.text_filter);
        mRecyView = findViewById(R.id.rv_movies);
        mPbLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mLayoutManager = new GridLayoutManager(this, numberOfColumns());

        recuperaDados(savedInstanceState);

        if (mRecarregaLista) {
            loadMovieData();
        }
        mRecyView.setAdapter(mMoviestAdapter);
        mMoviestAdapter = new MoviesAdapter(mMovieModelArrayList, this);
        mRecyView.setLayoutManager(mLayoutManager);
        mRecyView.setHasFixedSize(false);
        mRecyView.setAdapter(mMoviestAdapter);

        // montaGrid();
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
            if (savedInstanceState.containsKey(KEY_TIPO_LISTA)) {
                int tipo = savedInstanceState.getInt(KEY_TIPO_LISTA);
                mTipoFiltro = tipo;
            }

            if (savedInstanceState.containsKey(KEY_LIST_STATE)) {
                Parcelable listState = savedInstanceState.getParcelable(KEY_LIST_STATE);
                if (listState != null) {
                    Log.d(TAG, "listState != null" + listState.toString());
                    if (null == mLayoutManager)
                        mLayoutManager.onRestoreInstanceState(listState);
                }
            }


            if (savedInstanceState.containsKey(KEY_MOVIELIST)) {
                mMovieModelArrayList = savedInstanceState.getParcelableArrayList(KEY_MOVIELIST);
                mRecarregaLista = !(null != mMovieModelArrayList && !mMovieModelArrayList.isEmpty());
            }
        } else {
            lerTipoOrdenacaoPreferencial();
        }
    }


    /**
     * Monta do GridLayout
     */
 /*   private void montaGrid() {
        Log.d(TAG, "montaGrid");

        mRecyView.setLayoutManager(mLayoutManager);
        mRecyView.setHasFixedSize(false);
        mRecyView.setAdapter(mMoviestAdapter);
    }
*/
    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        Log.d(TAG, "nro colunas " + nColumns);
        if (nColumns < 2) return 2;
        return nColumns;
    }


    /**
     * Carrega os dados de filmes de forma assíncrona
     */
    private void loadMovieData() {
        Log.d(TAG, "loadMovieData");
        boolean isOnline = NetworkUtils.isOnline(
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));

        if (isOnline || (TiposDefinidos.LISTA_FAVORITES == mTipoFiltro)) {
            if (0 == mCurrentPage) {
                mCurrentPage++;
            }

            new MovieService(this).execute(new MovieFilter(mTipoFiltro,
                    mCurrentPage, mMovieModelArrayList, getContentResolver()));

        } else {
            mostrarFeedback(getString(R.string.erro_conexao));
        }
    }


    /**
     * Chama tela de detalhes de filme através de intent
     *
     * @param movie o filme escolhido
     * @param adapterPosition
     */
    @Override
    public void onClick(MovieModel movie, int adapterPosition) {
        Log.d(TAG, "onClick");
        mAdapterPosition = adapterPosition;
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
        //montaGrid();

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
        outState.putInt(KEY_TIPO_LISTA, mTipoFiltro);
        outState.putParcelableArrayList(KEY_MOVIELIST, mMovieModelArrayList);
        outState.putParcelable(KEY_LIST_STATE, mLayoutManager.onSaveInstanceState());
        outState.putBoolean(KEY_BOOL_RECARREGA, false);
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
                mudaTipoOrdenacao(TiposDefinidos.LISTA_POPULAR);
                break;
            case R.id.action_sort_rated:
                mudaTipoOrdenacao(TiposDefinidos.LISTA_TOP_RATED);
                break;
            case R.id.action_favoritos:
                mudaTipoOrdenacao(TiposDefinidos.LISTA_FAVORITES);
                break;

            default:
                mudaTipoOrdenacao(TiposDefinidos.LISTA_POPULAR);

        }

        return true;
    }


    /**
     * Monta o texto que é exibido na tela para mostrar o filtro selecionado
     *
     * @return texto
     */
    private String montaTextoAlerta() {
        Log.d(TAG, "montaTextoAlerta");

        String txtLista;
        switch (mTipoFiltro) {
            case TiposDefinidos.LISTA_POPULAR:
                txtLista = getString(R.string.action_name_popular);
                break;
            case TiposDefinidos.LISTA_TOP_RATED:
                txtLista = getString(R.string.action_name_top_rated);
                break;
            case TiposDefinidos.LISTA_FAVORITES:
                txtLista = getString(R.string.action_name_favoritos);
                break;
            default:
                txtLista = getString(R.string.action_name_popular);
        }

        String txt = getString(R.string.label_filtro_usado) + StringUtils.SPACE + txtLista;

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
                        mudaTipoOrdenacao(TiposDefinidos.LISTA_FAVORITES);
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
        Log.e(TAG, "onResume mTipoLista" + mTipoFiltro);
        if (TiposDefinidos.LISTA_FAVORITES == mTipoFiltro) {
            invalidateData();
        }
        super.onResume();
    }

    private void mudaTipoOrdenacao(int tpo) {
        Log.d(TAG, "mudaTipoOrdenacao(" + tpo + ")" + tpo);
        Log.d(TAG, "mTipoFiltro antes" + mTipoFiltro);
        // so faz se mudou o tipo da lista
        if (!(tpo == mTipoFiltro)) {
            mTipoFiltro = tpo;
            invalidateData();
            // guarda a ultima ordenação para abrir com ela
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(KEY_TIPO_LISTA, mTipoFiltro);
            editor.commit();
        }
        Log.d(TAG, "mTipoFiltro depois" + mTipoFiltro);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, montaTextoAlerta(), Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void lerTipoOrdenacaoPreferencial() {
        Log.d(TAG,"lerTipoOrdenacaoPreferencial()");
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        mTipoFiltro = sharedPref.getInt(KEY_TIPO_LISTA, TiposDefinidos.LISTA_POPULAR);
    }

}

