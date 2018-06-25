package com.github.bconzi.random_users.model.network;

import com.github.bconzi.random_users.model.network.data.UsersResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Random users api service class.
 *
 * @author marlonlom
 */
public class RandomUsersService {

    private static Retrofit retrofit = null;

    /**
     * Returns Retrofit singleton client.
     *
     * @return api client.
     */
    public static ApiService getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://randomuser.me/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }

    /**
     * Random users api service definition.
     *
     * @author marlonlom
     */
    public interface ApiService {

        /**
         * Query random users
         *
         * @param results        total results number for listing.
         * @param includedFields fields to be included in api response.
         * @param nationalities  nationalities to be filtered in api response.
         * @return random users api response.
         */
        @GET("api?noinfo")
        Call<UsersResponse> getUsers(@Query("results") int results,
                                     @Query("inc") String includedFields,
                                     @Query("nat") String nationalities);
    }
}
