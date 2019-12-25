package best.kissasian.drama;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.ixidev.gdpr.GDPRChecker;
import com.oxoo.spagreen.R;
import com.startapp.android.publish.adsCommon.AutoInterstitialPreferences;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONException;
import org.json.JSONObject;

import best.kissasian.drama.adapters.NavigationAdapter;
import best.kissasian.drama.fragments.LiveTvFragment;
import best.kissasian.drama.fragments.MoviesFragment;
import best.kissasian.drama.fragments.TvSeriesFragment;
import best.kissasian.drama.models.NavigationModel;
import best.kissasian.drama.nav_fragments.CountryFragment;
import best.kissasian.drama.nav_fragments.FavoriteFragment;
import best.kissasian.drama.nav_fragments.GenreFragment;
import best.kissasian.drama.nav_fragments.MainHomeFragment;
import best.kissasian.drama.utils.ApiResources;
import best.kissasian.drama.utils.BannerAds;
import best.kissasian.drama.utils.SpacingItemDecoration;
import best.kissasian.drama.utils.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static best.kissasian.drama.utils.MyAppClass.getContext;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RatingDialogListener {


    private static final int TAG =0 ;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private NavigationAdapter mAdapter;
    private List<NavigationModel> list =new ArrayList<>();
    private NavigationView navigationView;
    private String[] navItemImage;

    private String[] navItemName2;
    private String[] navItemImage2;
    private boolean status=false;
    private RelativeLayout adView;

    private SharedPreferences preferences;
    private FirebaseAnalytics mFirebaseAnalytics;

    private AdView fanadView;
     LinearLayout progressbar;


    public static String fanadStatus,fanBannerid="0",fanInterid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AudienceNetworkAds.initialize(this);
        StartAppSDK.init(this, ApiResources.startappid, true);
        StartAppSDK.setUserConsent (this,
                "pas",
                System.currentTimeMillis(),
                true);
        StartAppAd.disableSplash();





        //---analytics-----------
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "main_activity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        final SharedPreferences preferences = getSharedPreferences("push", MODE_PRIVATE);
        if (preferences.getBoolean("dark",false)){
            AppCompatDelegate
                    .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate
                    .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }



        new GDPRChecker()
                .withContext(getContext())
                .withPrivacyUrl(Config.TERMS_URL) // your privacy url
                .withPublisherIds(ApiResources.adMobPublisherId) // your admob account Publisher id
                .withTestMode("9424DF76F06983D1392E609FC074596C") // remove this on real project
                .check();



        //----init---------------------------
        navigationView = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        progressbar=findViewById(R.id.llProgressBar);
        //----navDrawer------------------------
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        navigationView.setNavigationItemSelectedListener(this);
        adView=findViewById(R.id.adView);


        //----fetch array------------
        String[] navItemName = getResources().getStringArray(R.array.nav_item_name);
        navItemImage=getResources().getStringArray(R.array.nav_item_image);
        navItemImage2=getResources().getStringArray(R.array.nav_item_image_2);


        navItemName2=getResources().getStringArray(R.array.nav_item_name_2);







        //----navigation view items---------------------
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(this, 1), true));
        recyclerView.setHasFixedSize(true);


        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        status = prefs.getBoolean("status",false);

        if (status){
            for (int i = 0; i< navItemName.length; i++){
                NavigationModel models =new NavigationModel( navItemName[i],navItemImage[i]);

                list.add(models);
            }
        }else {
            for (int i=0;i<navItemName2.length;i++){
                NavigationModel models =new NavigationModel(navItemName2[i],navItemImage2[i]);
                list.add(models);
            }
        }







        //set data and list adapter
        mAdapter = new NavigationAdapter(this, list);
        recyclerView.setAdapter(mAdapter);


        final NavigationAdapter.OriginalViewHolder[] viewHolder = {null};

        mAdapter.setOnItemClickListener(new NavigationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, NavigationModel obj, int position, NavigationAdapter.OriginalViewHolder holder) {

                Log.e("POSITION OF NAV:::", String.valueOf(position));

                //----action for click items nav---------------------

                if (position==0){
                    loadFragment(new MainHomeFragment());
                }

                else if (position==1){
                    Intent intent=new Intent(MainActivity.this, ItemMovieActivity.class);
                    intent.putExtra("id","21");
                    intent.putExtra("title",obj.getTitle());
                    intent.putExtra("type","country");
                    MainActivity.this.startActivity(intent);
                }
                else if (position==2){
                    Intent intent=new Intent(MainActivity.this, ItemMovieActivity.class);
                    intent.putExtra("id","5");
                    intent.putExtra("title",obj.getTitle());
                    intent.putExtra("type","country");
                    MainActivity.this.startActivity(intent);
                }
                else if (position==3){
                    Intent intent=new Intent(MainActivity.this, ItemMovieActivity.class);
                    intent.putExtra("id","2");
                    intent.putExtra("title",obj.getTitle());
                    intent.putExtra("type","country");
                    MainActivity.this.startActivity(intent);
                }
                else if (position==4){
                    Intent intent=new Intent(MainActivity.this, ItemMovieActivity.class);
                    intent.putExtra("id","4");
                    intent.putExtra("title",obj.getTitle());
                    intent.putExtra("type","country");
                    MainActivity.this.startActivity(intent);
                }
                else if (position==5){
                    loadFragment(new GenreFragment());
                }
                else if (position==6){
                    loadFragment(new CountryFragment());
                }
                else {


                    if (status){

                        if (position==7){
                            Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
                            startActivity(intent);
                        }
                        else if (position==8){
                            loadFragment(new FavoriteFragment());
                        }
                        else if (position==9){
                            new AlertDialog.Builder(MainActivity.this).setMessage("Are you sure to logout ?")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                                            editor.putBoolean("status",false);
                                            editor.apply();

                                            Intent intent=new Intent(MainActivity.this,MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }).create().show();
                        }
                        else if (position==10){
                            Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
                            startActivity(intent);
                        }
                        else if (position==11){
                            String url = "https://api.whatsapp.com/send?phone=+6281275941178&text=Please help, I have a Problem";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                    }else {
                        if (position==7){
                            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                        else if (position==8){
                            Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
                            startActivity(intent);
                        }
                        else if (position==9){
                            String url = "https://api.whatsapp.com/send?phone=+6281275941178&text=Please help, I have a Problem";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                    }

                }




                //----behaviour of bg nav items-----------------
                if (!obj.getTitle().equals("Settings") && !obj.getTitle().equals("Login") && !obj.getTitle().equals("Sign Out")){




                    if (preferences.getBoolean("dark",false)){
                        mAdapter.chanColor(viewHolder[0],position,R.color.nav_bg);
                    }else {
                        mAdapter.chanColor(viewHolder[0],position,R.color.white);
                    }


                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    holder.name.setTextColor(getResources().getColor(R.color.white));
                    viewHolder[0] =holder;
                }

                mDrawerLayout.closeDrawers();
            }
        });

        //----external method call--------------
        loadFragment(new MainHomeFragment());

//        BannerAds.ShowBannerAds(getContext(), adView);
        PackageManager manager = getContext().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = info.versionName;



        if (SplashscreenActivity.statusnotif.equals("1")){
            dialognew();



        }


        if (!(version.equals(SplashscreenActivity.versionapp))){

            dialognew();

        }


    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action, menu);
        return true;
    }


    private boolean loadFragment(Fragment fragment){

        if (fragment!=null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .commit();

            return true;
        }
        return false;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_search:

                final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {

                        Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                        intent.putExtra("q",s);
                        startActivity(intent);

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return false;
                    }
                });

                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawers();
        }else {

            new AppRatingDialog.Builder()
                    .setPositiveButtonText("Submit")
                    .setNegativeButtonText("Cancel")
                    .setNeutralButtonText("Later")
                    .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                    .setDefaultRating(2)
                    .setTitle("Rate this application")
                    .setDescription("Please select some stars and give your feedback")
                    .setCommentInputEnabled(true)
                    .setDefaultComment("This app is pretty cool !")
                    .setStarColor(R.color.orange_400)
                    .setNoteDescriptionTextColor(R.color.blue_grey_300)
                    .setTitleTextColor(R.color.colorPrimary)
                    .setDescriptionTextColor(R.color.white)
                    .setHint("Please write your comment here ...")
                    .setHintTextColor(R.color.blue_grey_300)
                    .setCommentTextColor(R.color.common_google_signin_btn_text_dark)
                    .setCommentBackgroundColor(R.color.colorPrimaryDark)
                    .setWindowAnimation(R.style.MyDialogFadeAnimation)
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .create(MainActivity.this)
                    .show();



        }
    }

    //----nav menu item click---------------
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // set item as selected to persist highlight
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawers();


        return true;
    }









//    private void getAdDetails(String url){
//
//        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//
//
//
//                try {
//                    JSONObject jsonObject=response.getJSONObject("fan");
//
//                    fanadStatus = jsonObject.getString("status");
//                      fanBannerid = jsonObject.getString("fan_banner_ads_id");
//                    fanInterid = jsonObject.getString("fan_interstitial_ads_id");
//
//
//
//
//                    new GDPRChecker()
//                            .withContext(getContext())
//                            .withPrivacyUrl(Config.TERMS_URL) // your privacy url
//                            .withPublisherIds(ApiResources.adMobPublisherId) // your admob account Publisher id
//                            .withTestMode("9424DF76F06983D1392E609FC074596C") // remove this on real project
//                            .check();
//
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
//
//
//    }


    public void showprogress(){

        progressbar.setVisibility(View.VISIBLE);
    }


    public void hideprogress(){

        progressbar.setVisibility(View.GONE);
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {
        finish();
        finishAffinity();
        System.exit(0);



    }

    @Override
    public void onPositiveButtonClicked(int i, String s) {


        Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
        }

    }


    public void dialognew(){

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);


        //then we will inflate the custom alert dialog xml that we created
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_layout, viewGroup, false);

        TextView judulnotif = dialogView.findViewById(R.id.judulpesan);
        TextView isinotif = dialogView.findViewById(R.id.isipesan);

        Button button = dialogView.findViewById(R.id.buttonOk);

        button.setText(SplashscreenActivity.buttontext);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated

        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SplashscreenActivity.link.equals("")){

                    alertDialog.dismiss();


                }
                else{
                    Uri uri = Uri.parse(SplashscreenActivity.link); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

            }
        });

        judulnotif.setText(SplashscreenActivity.judulnotif+"("+SplashscreenActivity.versionapp+")");
        isinotif.setText(SplashscreenActivity.isinotif);






        alertDialog.show();
    }

}
