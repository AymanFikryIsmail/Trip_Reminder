package com.iti.android.tripapp.model.map_model;

import java.util.List;

/**
 * Created by ayman on 2019-02-25.
 */

public class MapRoute {
    private List<MapStep> routes;

    public List<MapStep> getRoutes() {
        return routes;
    }

    public void setRoutes(List<MapStep> routes) {
        this.routes = routes;
    }
}
