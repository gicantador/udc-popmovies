package com.pgcn.udcpopmovies.enums;

import com.pgcn.udcpopmovies.utils.NetworkUtils;

import java.util.HashMap;

/**
 * Created by Giselle on 05/12/2017.
 */


public enum SortOrder {
    // sao alimentados por constantes externas pois serão utilizados na busca de fato

    ASC(NetworkUtils.SORT_ASC),
    DESC(NetworkUtils.SORT_DESC);

    private static final HashMap<String, SortOrder> MAP = new HashMap<String, SortOrder>();

    private String value;

    private SortOrder(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static SortOrder getByName(String name) {
        return MAP.get(name);
    }

    static {
        for (SortOrder field : SortOrder.values()) {
            MAP.put(field.getValue(), field);
        }
    }
}
