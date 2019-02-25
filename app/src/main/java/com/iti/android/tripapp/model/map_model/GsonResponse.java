package com.iti.android.tripapp.model.map_model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ayman on 2019-02-04.
 */

public interface GsonResponse {
//    @GET("json?origin=31.308860000000003%2C30.057857199999997&destination=31.23982149999999%2C30.023186100000004&key=AIzaSyCeYHDhDctqGmb5APIdyWrd-imDO2DkQHc&fbclid=IwAR0SzqGJcx4O8HkvLij_sXZuFCgkad_lntijQD05XybFpDPdIuJWmtn5aeQ")

    @GET("json")
    Call<MapResponse> getCountries(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("key") String key    );

}
