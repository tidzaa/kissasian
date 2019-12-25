package best.kissasian.drama;



import android.Manifest;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.ixidev.gdpr.GDPRChecker;
import com.oxoo.spagreen.R;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.startapp.android.publish.adsCommon.adListeners.AdDisplayListener;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import best.kissasian.drama.utils.ApiResources;

import static best.kissasian.drama.utils.ApiResources.fanInterid;
import static best.kissasian.drama.utils.MyAppClass.getContext;
import static com.facebook.ads.AudienceNetworkAds.TAG;


public class SplashscreenActivity extends AppCompatActivity {

    private int SPLASH_TIME = 2000;
    private SharedPreferences preferences;

    Button buttonstart;
    ProgressBar progressBar;
    LinearLayout layutprogressbar;
    String   splashinter,interadmob;
    InterstitialAd fanInterstitialAd;
    private com.google.android.gms.ads.InterstitialAd mInterstitialAd;


    public static String statusnotif,judulnotif,isinotif,link,buttontext,versionapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            } else {
            }
        } else {
        }






        //----dark mode----------
        preferences=getSharedPreferences("push",MODE_PRIVATE);
        if (preferences.getBoolean("dark",false)){
            AppCompatDelegate
                    .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate
                    .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        setContentView(R.layout.activity_splashscreen);
        getSupportActionBar().hide();
        layutprogressbar=findViewById(R.id.llProgressBar);



        progressBar=findViewById(R.id.progressBar2);
        buttonstart =findViewById(R.id.buttonstart);


        Stringrequest(new ApiResources().getNotifapp());
        getAdDetails(new ApiResources().getAdDetails());





        buttonstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                layutprogressbar.setVisibility(View.VISIBLE);


                loadinter(fanInterid);


            }
        });
//        Thread thread = new Thread(){
//            @Override
//            public void run() {
//                try {
//                    synchronized (this) {
//                        wait(5000);
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                               buttonstart.setVisibility(View.VISIBLE);
//                               progressBar.setVisibility(View.GONE);
//                            }
//                        });
//
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            };
//        };
//        thread.start();


    }

       private void Stringrequest(String url){

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {




                  try {
                    JSONObject jsonObject=response.getJSONObject("statusapp");

                    statusnotif = jsonObject.getString("statusnotif");
                      judulnotif = jsonObject.getString("judulnotif");
                    isinotif = jsonObject.getString("isinotif");
                      link = jsonObject.getString("link");
                      buttontext=jsonObject.getString("buttontext");
                      versionapp=jsonObject.getString("versionapp");









                  } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);


    }


    public void loadinter (String inter){

    fanInterstitialAd = new InterstitialAd(this,inter);

        fanInterstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

                layutprogressbar.setVisibility(View.GONE);
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                layutprogressbar.setVisibility(View.GONE);

                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");

                Intent intent = new Intent(SplashscreenActivity.this,MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(Ad ad, AdError adError) {

                loadinteradmob(ApiResources.adMobInterstitialId);


                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                layutprogressbar.setVisibility(View.GONE);

                fanInterstitialAd.show();



                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad

            }

            @Override
            public void onAdClicked(Ad ad) {
                layutprogressbar.setVisibility(View.GONE);

                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });

        fanInterstitialAd.loadAd();
    }


    private void getAdDetails(String url){



        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject=response.getJSONObject("startapp");
                    ApiResources.startappid = jsonObject.getString("startappid");
                    ApiResources.startappstatus = jsonObject.getString("startappstatus");

                    StartAppSDK.init(getContext(), ApiResources.startappid, true);
                    StartAppSDK.setUserConsent (getContext(),
                            "pas",
                            System.currentTimeMillis(),
                            true);
                    StartAppAd.disableSplash();

                } catch (JSONException e) {
                    Log.e("json", "ERROR");


                    e.printStackTrace();
                }


                try {
                    JSONObject jsonObject=response.getJSONObject("admob");

                    ApiResources.admobstatus = jsonObject.getString("status");
                    ApiResources.adMobBannerId = jsonObject.getString("admob_banner_ads_id");
                    ApiResources.adMobInterstitialId = jsonObject.getString("admob_interstitial_ads_id");
                    ApiResources.adMobPublisherId = jsonObject.getString("admob_publisher_id");

//                    interadmob=jsonObject.getString("admob_interstitial_ads_id");

//                    Toast.makeText(getContext(),"coba"+interadmob,Toast.LENGTH_LONG).show();

                    new GDPRChecker()
                            .withContext(getContext())
                            .withPrivacyUrl(Config.TERMS_URL) // your privacy url
                            .withPublisherIds(ApiResources.adMobPublisherId) // your admob account Publisher id
                            .withTestMode("9424DF76F06983D1392E609FC074596C") // remove this on real project
                            .check();




                } catch (JSONException e) {
                    Log.e("json", "ERROR");

                    e.printStackTrace();
                }


                try {
                    JSONObject jsonObject=response.getJSONObject("fan");

                    ApiResources.fanadStatus = jsonObject.getString("status");
                    ApiResources.fanBannerid = jsonObject.getString("fan_banner_ads_id");
                    fanInterid = jsonObject.getString("fan_interstitial_ads_id");

//                    Toast.makeText(getContext(),ApiResources.fanadStatus+ApiResources.fanBannerid+ApiResources.fanInterid , Toast.LENGTH_LONG).show();
//
//
                    if (!fanInterid.equals("")){
                        buttonstart.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }











                } catch (JSONException e) {
                    Log.e("json", "ERROR");
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", String.valueOf(error));


            }
        });

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);





    }


    public void loadinteradmob(String inter){

        mInterstitialAd = new com.google.android.gms.ads.InterstitialAd(this);
        mInterstitialAd.setAdUnitId(inter);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                layutprogressbar.setVisibility(View.GONE);

                mInterstitialAd.show();

                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

                StartAppAd startAppAd = new StartAppAd(getContext());
                 startAppAd.showAd(new AdDisplayListener() {
                     @Override
                     public void adHidden(com.startapp.android.publish.adsCommon.Ad ad) {
                         layutprogressbar.setVisibility(View.GONE);

                         Intent intent = new Intent(SplashscreenActivity.this,MainActivity.class);

                         startActivity(intent);

                     }

                     @Override
                     public void adDisplayed(com.startapp.android.publish.adsCommon.Ad ad) {

                     }

                     @Override
                     public void adClicked(com.startapp.android.publish.adsCommon.Ad ad) {
                         layutprogressbar.setVisibility(View.GONE);

                         Intent intent = new Intent(SplashscreenActivity.this,MainActivity.class);

                         startActivity(intent);

                     }

                     @Override
                     public void adNotDisplayed(com.startapp.android.publish.adsCommon.Ad ad) {

                         layutprogressbar.setVisibility(View.GONE);

                        Intent intent = new Intent(SplashscreenActivity.this,MainActivity.class);

                        startActivity(intent);

                     }
                 })   ;


//                final Handler   handler = new Handler();
//
//                final Runnable r = new Runnable() {
//                    public void run() {
//                        StartAppAd.showAd(getContext());
//
//                        layutprogressbar.setVisibility(View.GONE);
//
//                        Intent intent = new Intent(SplashscreenActivity.this,MainActivity.class);
//
//                        startActivity(intent);
//                    }
//                };
//
//                handler.postDelayed(r, 3000);



                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                layutprogressbar.setVisibility(View.GONE);

                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                layutprogressbar.setVisibility(View.GONE);


                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                layutprogressbar.setVisibility(View.GONE);

                Intent intent = new Intent(SplashscreenActivity.this,MainActivity.class);

                startActivity(intent);

                // Code to be executed when the interstitial ad is closed.
            }
        });
    }







}
