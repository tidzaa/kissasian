package com.kissasian.drama.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.kissasian.drama.DetailsActivity;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.oxoo.spagreen.R;

import com.kissasian.drama.MainActivity;
import com.kissasian.drama.models.CommonModels;
import com.kissasian.drama.utils.ApiResources;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.ads.AudienceNetworkAds.TAG;

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.OriginalViewHolder> {

    private List<CommonModels> items = new ArrayList<>();
    private Context ctx;
    public  LinearLayout progresbar;

    public HomePageAdapter(Context context, List<CommonModels> items) {
        this.items = items;
        this.ctx = context;

    }


    @Override
    public HomePageAdapter.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HomePageAdapter.OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_home_view, parent, false);



        vh = new HomePageAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final HomePageAdapter.OriginalViewHolder holder, final int position) {

        final CommonModels obj = items.get(position);
        holder.name.setText(obj.getTitle());
        Picasso.get().load(obj.getImageUrl()).into(holder.image);


        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if (ctx instanceof MainActivity) {
                    ((MainActivity)ctx).showprogress();
                }

                if (ApiResources.admobstatus.equals("1")){

                    final InterstitialAd mInterstitialAd = new InterstitialAd(ctx);
                    mInterstitialAd.setAdUnitId(ApiResources.adMobInterstitialId);
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());


                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            mInterstitialAd.show();
                            // Code to be executed when an ad finishes loading.
                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {

                            if (ctx instanceof MainActivity) {
                                ((MainActivity)ctx).hideprogress();
                            }
                            Intent intent=new Intent(ctx, DetailsActivity.class);
                            intent.putExtra("vType","tvseries");
                            intent.putExtra("id",obj.getId());

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ctx.startActivity(intent);

                            // Code to be executed when an ad request fails.
                        }

                        @Override
                        public void onAdOpened() {
                            // Code to be executed when the ad is displayed.
                        }

                        @Override
                        public void onAdClicked() {
                            // Code to be executed when the user clicks on an ad.
                        }

                        @Override
                        public void onAdLeftApplication() {
                            // Code to be executed when the user has left the app.
                        }

                        @Override
                        public void onAdClosed() {

                            if (ctx instanceof MainActivity) {
                                ((MainActivity)ctx).hideprogress();
                            }
                            Intent intent=new Intent(ctx, DetailsActivity.class);
                            intent.putExtra("vType","tvseries");
                            intent.putExtra("id",obj.getId());

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ctx.startActivity(intent);

                            // Code to be executed when the interstitial ad is closed.
                        }
                    });


                }




                if (ApiResources.fanadStatus.equals("1")){

                    final com.facebook.ads.InterstitialAd fanInterstitialAd = new com.facebook.ads.InterstitialAd(ctx,ApiResources.fanInterid);

                    fanInterstitialAd.setAdListener(new InterstitialAdListener() {
                        @Override
                        public void onInterstitialDisplayed(Ad ad) {
                            // Interstitial ad displayed callback
                            Log.e(TAG, "Interstitial ad displayed.");
                        }

                        @Override
                        public void onInterstitialDismissed(Ad ad) {
                            if (ctx instanceof MainActivity) {
                                ((MainActivity)ctx).hideprogress();
                            }
                            Intent intent=new Intent(ctx, DetailsActivity.class);
                            intent.putExtra("vType","tvseries");
                            intent.putExtra("id",obj.getId());

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ctx.startActivity(intent);
                            // Interstitial dismissed callback
                            Log.e(TAG, "Interstitial ad dismissed.");
                        }

                        @Override
                        public void onError(Ad ad, AdError adError) {
                            if (ctx instanceof MainActivity) {
                                ((MainActivity)ctx).hideprogress();
                            }
                            Intent intent=new Intent(ctx, DetailsActivity.class);
                            intent.putExtra("vType","tvseries");
                            intent.putExtra("id",obj.getId());

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ctx.startActivity(intent);
                            // Ad error callback
                            Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                        }

                        @Override
                        public void onAdLoaded(Ad ad) {

                            // Interstitial ad is loaded and ready to be displayed
                            Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                            // Show the ad

                        }

                        @Override
                        public void onAdClicked(Ad ad) {
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




            }
        });

    }




    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name;
        public MaterialRippleLayout lyt_parent;


        public OriginalViewHolder(View v) {
            super(v);

            image = v.findViewById(R.id.image);
            name = v.findViewById(R.id.name);
            lyt_parent=v.findViewById(R.id.lyt_parent);
        }
    }

}
