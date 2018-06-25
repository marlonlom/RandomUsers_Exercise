package com.github.bconzi.random_users.details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.bconzi.random_users.R;
import com.github.bconzi.random_users.model.network.data.Coordinates;
import com.github.bconzi.random_users.model.network.data.Location;
import com.github.bconzi.random_users.model.network.data.RandomUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Objects;

/**
 * Details activity class.
 *
 * @author marlonlom
 */
public class DetailActivity extends AppCompatActivity {

    public static final String PARAM_USER = "randomUser";
    public static final String PARAM_TRANSITION_REF = "sharedTransitionName";

    /**
     * Creates intent for entering this activity.
     *
     * @param context    activity context.
     * @param randomUser random user data.
     * @return intent for navigation to this activity.
     */
    public static Intent createIntent(Context context, final RandomUser randomUser) {
        final Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.PARAM_USER, randomUser);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        supportPostponeEnterTransition();

        setupToolbar();
        prepareUserData();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Prepares user detail data.
     */
    private void prepareUserData() {
        final RandomUser randomUser = getIntent().getParcelableExtra(PARAM_USER);
        final String transitionName = getIntent().getStringExtra(PARAM_TRANSITION_REF);
        final Picasso picasso = Picasso.get();

        ((TextView) findViewById(R.id.textView_detail_fullname)).setText(randomUser.getName().getFullName());
        ((TextView) findViewById(R.id.textView_detail_email)).setText(randomUser.getEmail());
        findViewById(R.id.textView_detail_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToEmail(randomUser.getEmail());
            }
        });


        ((TextView) findViewById(R.id.textView_detail_phone)).setText(randomUser.getPhone());
        findViewById(R.id.textView_detail_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToPhoneCall(randomUser.getPhone());
            }
        });

        final Location userLocation = randomUser.getLocation();
        final String addressText = String.format("%s, %s, %s", userLocation.getStreet(),
                userLocation.getCity(), userLocation.getState());
        ((TextView) findViewById(R.id.textView_detail_address)).setText(addressText);
        findViewById(R.id.textView_detail_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String formattedAddress = String.format("%s, %s", userLocation.getStreet(),
                        userLocation.getCity());
                navigateToMaps(userLocation.getCoordinates(), formattedAddress);
            }
        });


        if (randomUser.getGender().equalsIgnoreCase("female")) {
            picasso.load(R.drawable.bg_detail_female)
                    .fit().centerCrop()
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into((ImageView) findViewById(R.id.imageView_detail_bg));
        }

        findViewById(R.id.imageView_detail_avatar).setTransitionName(transitionName);

        picasso.load(randomUser.getPicture().getLarge())
                .fit().centerCrop()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .transform(new CropCircleTransformation())
                .into((ImageView) findViewById(R.id.imageView_detail_avatar), new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError(Exception e) {
                        supportStartPostponedEnterTransition();
                    }
                });
    }

    /**
     * Setups toolbar.
     */
    private void setupToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Navigates to phone call app.
     *
     * @param userPhone user phone number.
     */
    private void navigateToPhoneCall(String userPhone) {
        final Intent phoneCallIntent = new Intent(Intent.ACTION_DIAL);
        phoneCallIntent.setData(Uri.parse("tel:".concat(userPhone)));
        startActivity(phoneCallIntent);
    }

    /**
     * Navigates to email app.
     *
     * @param userEmail user email address.
     */
    private void navigateToEmail(final String userEmail) {
        final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:".concat(userEmail)));
        startActivity(emailIntent);
    }

    /**
     * Open google map for user location.
     *
     * @param coordinates user address location.
     * @param addressText user address text.
     */
    private void navigateToMaps(final Coordinates coordinates, final String addressText) {
        final String locationUriPart = String.format("geo:%s,%s?q=%s",
                coordinates.getLatitude(), coordinates.getLongitude(),
                Uri.encode(addressText));
        final Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(locationUriPart));
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
        startActivity(mapIntent);
    }

}
