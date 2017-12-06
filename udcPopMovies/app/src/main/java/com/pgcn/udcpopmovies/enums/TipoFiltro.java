package com.pgcn.udcpopmovies.enums;

import com.pgcn.udcpopmovies.data.FavoriteMoviesDatabaseUtil;
import com.pgcn.udcpopmovies.utils.NetworkUtils;

import java.util.HashMap;

/**
 * Created by Giselle on 05/12/2017.
 */

public enum TipoFiltro {

    // sao alimentados por constantes externas pois serão utilizados na busca de fato
    POPULAR(NetworkUtils.SORT_POPULAR_PARAM),
    TOP_RATED(NetworkUtils.SORT_VOTE_PARAM),
    FAVORITES(FavoriteMoviesDatabaseUtil.KEY_FAVORITOS);


    private static final HashMap<String, TipoFiltro> MAP = new HashMap<String, TipoFiltro>();

    private String value;

    TipoFiltro(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static TipoFiltro getByName(String name) {
        return MAP.get(name);
    }

    static {
        for (TipoFiltro field : TipoFiltro.values()) {
            MAP.put(field.getValue(), field);
        }
    }

}
