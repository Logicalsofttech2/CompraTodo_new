package com.logicals.compratodo.customer.mainhomeactivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.logicals.compratodo.BuildConfig;
import com.logicals.compratodo.customer.activity.CurrentaddressActivity;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.activity.AddToCartActivity;
import com.logicals.compratodo.customer.UI.home.MainActivity2;
import com.logicals.compratodo.customer.activity.NotificationActivity;
import com.logicals.compratodo.databinding.ActivityUserHomeBinding;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.login_signup.CustomerLoginActivity;
import com.logicals.compratodo.model.HomeDTO;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.profile.ProfileFragment;
import com.logicals.compratodo.utils.GPSTracker;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class UserHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    GifImageView iv_notif;
    ImageView iv_search;
    TextView iv_location;
    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    CircleImageView imageView;
    NavigationView navigationView;
    ActivityUserHomeBinding binding;
    ProfileFragment profileFragment;
    MenuItem profile;
    SharedPrefrence sharedPrefrence;
    Context mContext;
    GPSTracker gps;
    double Lattitude,Longitude;
    TextView mLogin,tv_total_count;
    HashMap<String,String> param=new HashMap<>();
    UserDTO userDTO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this, R.layout.activity_user_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        iv_search= findViewById(R.id.iv_search);
        iv_location= findViewById(R.id.iv_location);
        sharedPrefrence=SharedPrefrence.getInstance(UserHomeActivity.this);
        mContext=UserHomeActivity.this;
        tv_total_count=findViewById(R.id.tv_total_count);
        View hearlayout_= binding.navView2.getHeaderView(0);
        mLogin = hearlayout_.findViewById(R.id.login);
        userDTO = sharedPrefrence.getParentUser(Consts.USER_DTO);
        iv_notif=findViewById(R.id.iv_notif);


        iv_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

startActivity(new Intent(mContext, NotificationActivity.class));


            }
        });

        if(sharedPrefrence.isLoggedIn()){
            mLogin.setVisibility(View.GONE);
            getHomeData();
        }

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, CustomerLoginActivity.class));
            }
        });

        try {
            getLocation();
        } catch (IOException e) {
            e.printStackTrace();
        }


        findViewById(R.id.iv_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                Intent intent = new Intent(UserHomeActivity.this, AddToCartActivity.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.iv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                Intent intent = new Intent(UserHomeActivity.this, MainActivity2.class);
                startActivity(intent);

            }
        });


        findViewById(R.id.iv_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                Intent intent = new Intent(UserHomeActivity.this, CurrentaddressActivity.class);
                intent.putExtra("lat",Lattitude);
                intent.putExtra("lng",Longitude);
                startActivity(intent);

            }
        });








        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        drawer = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view2);
         profile = (MenuItem)findViewById(R.id.nav_profile);


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home2, R.id.nav_profile, R.id.nav_orders,R.id.nav_completeorders,
                R.id.nav_offers, R.id.nav_wishlist , R.id.nav_about,
                R.id.nav_terms,R.id.nav_invite, R.id.nav_help ,R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment2);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id=menuItem.getItemId();
                //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
                if (id==R.id.nav_logout){
                    if(sharedPrefrence.isLoggedIn()){
                        sharedPrefrence.logout();
                    }else {
                        Toast.makeText(mContext, "You Are Not Login , Please Login First!!! ", Toast.LENGTH_SHORT).show();
                    }

                }


                else if (id==R.id.nav_invite){
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                        String shareMessage= "\nLet me recommend you this application\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                    } catch(Exception e) {
                        //e.toString();
                    }

                }



                //This is for maintaining the behavior of the Navigation view
                NavigationUI.onNavDestinationSelected(menuItem,navController);
                //This is for closing the drawer after acting on it
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });









        View headerLayout = navigationView.getHeaderView(0);
        imageView= headerLayout.findViewById(R.id.imageView);



       imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){


            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment2);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_profile,
                        new ProfileFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getLocation() throws IOException {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            gps = new GPSTracker(mContext);
            if (gps.canGetLocation()) {
                Lattitude = gps.getLatitude();
                Longitude = gps.getLongitude();
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());
                      try {
                          addresses = geocoder.getFromLocation(Lattitude, Longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                          String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                          String city = addresses.get(0).getLocality();
                          String state = addresses.get(0).getAdminArea();
                          String country = addresses.get(0).getCountryName();
                          String postalCode = addresses.get(0).getPostalCode();
                          String knownName = addresses.get(0).getFeatureName();
                          // Only if available else return NULL
                          iv_location.setText(address);

                      }catch (Exception e){

                      }



            } else {
                gps.showSettingsAlert();
            }

        }
    }
    private void getHomeData() {
        param.put(Consts.USER_ID,userDTO.getId());
        new HttpsRequest(Consts.USERHOME,param,mContext).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if(flag) {
                    try {
                        HomeDTO homeDTO = new Gson().fromJson(response.getJSONObject("data").toString(), HomeDTO.class);
                        tv_total_count.setText(homeDTO.getCart_count());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {

        if(sharedPrefrence.isLoggedIn()){
            mLogin.setVisibility(View.GONE);
            getHomeData();
        }

        super.onResume();
    }
}