package com.github.bconzi.random_users.listings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.github.bconzi.random_users.R;
import com.github.bconzi.random_users.details.DetailActivity;
import com.github.bconzi.random_users.model.network.RandomUsersService;
import com.github.bconzi.random_users.model.network.data.RandomUser;
import com.github.bconzi.random_users.model.network.data.UsersResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Random users listings activity class.
 *
 * @author marlonlom
 */
public class ListingsActivity extends AppCompatActivity {

    public static final String TAG = ListingsActivity.class.getSimpleName();
    private RecyclerView mUsersRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupRecyclerView();
        prepareData();
    }

    /**
     * Setups toolbar.
     */
    private void setupToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar_listing);
        setSupportActionBar(toolbar);
    }

    /**
     * Prepares random user data.
     */
    private void prepareData() {
        RandomUsersService.getClient().getUsers(50,
                "login,name,email,picture,gender,phone,nat,location",
                "gb,us,es")
                .enqueue(new Callback<UsersResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<UsersResponse> call, @NonNull Response<UsersResponse> response) {
                        assert response.body() != null;
                        ((RandomUsersRecyclerAdapter) mUsersRecyclerView.getAdapter()).changeItems(response.body().getResults());
                    }

                    @Override
                    public void onFailure(@NonNull Call<UsersResponse> call, @NonNull Throwable t) {
                        Log.e(TAG, t.getMessage(), t);
                    }
                });
    }

    /**
     * Setups random users recyclerView.
     */
    private void setupRecyclerView() {
        final int spanCount = getResources().getInteger(R.integer.int_listings_columns);

        mUsersRecyclerView = findViewById(R.id.recyclerView_listing);
        mUsersRecyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        mUsersRecyclerView.setAdapter(new RandomUsersRecyclerAdapter());
        mUsersRecyclerView.setHasFixedSize(true);
        mUsersRecyclerView.addOnItemTouchListener(
                new RandomUsersRecyclerAdapter.RecyclerTouchListener(this, mUsersRecyclerView,
                        new RandomUsersRecyclerAdapter.RecyclerTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position, ImageView thumbnailImageView) {
                                final RandomUser randomUser =
                                        ((RandomUsersRecyclerAdapter) mUsersRecyclerView.getAdapter()).getItem(position);
                                navigateToDetails(randomUser, thumbnailImageView);
                            }

                            @Override
                            public void onLongClick(View view, int position) {

                            }
                        }));
        mUsersRecyclerView.addItemDecoration(new RandomUsersRecyclerAdapter.GridSpacingItemDecoration(
                spanCount, 2, false, 0));
    }

    /**
     * Navigates to details activity.
     *
     * @param randomUser         random user data.
     * @param thumbnailImageView thumbnail imageView from list.
     */
    private void navigateToDetails(final RandomUser randomUser, ImageView thumbnailImageView) {
        final Intent intent = DetailActivity.createIntent(this, randomUser);
        final String detailsTransitionName = ViewCompat.getTransitionName(thumbnailImageView);
        intent.putExtra(DetailActivity.PARAM_TRANSITION_REF, detailsTransitionName);
        final ActivityOptionsCompat detailsAnimationOpts =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, thumbnailImageView, detailsTransitionName);
        startActivity(intent, detailsAnimationOpts.toBundle());
    }
}
