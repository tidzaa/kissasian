package best.kissasian.drama.adapters;

import android.app.DownloadManager;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import best.kissasian.drama.DetailsActivity;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.oxoo.spagreen.R;

import best.kissasian.drama.MainActivity;
import best.kissasian.drama.models.EpiModel;
import best.kissasian.drama.models.SubtitleModel;
import best.kissasian.drama.utils.ApiResources;
import best.kissasian.drama.utils.ToastMsg;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;
import static android.content.Context.DOWNLOAD_SERVICE;

public class DirectorApater extends RecyclerView.Adapter<DirectorApater.OriginalViewHolder> {
    public static String DOWNLOAD_DIRECTORY="/KISSASIAN";

    public static List<SubtitleModel> newsub;
    public static String nowtitle;
    private List<EpiModel> listepi;
    private Context ctx;
    final DirectorApater.OriginalViewHolder[] viewHolderArray = {null};
    private DirectorApater.OnItemClickListener mOnItemClickListener;
    DirectorApater.OriginalViewHolder viewHolder;

    public interface OnItemClickListener {
        void onItemClick(View view, EpiModel obj, int position, OriginalViewHolder holder);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public DirectorApater(Context context, List<EpiModel> items,String name) {
        ArrayList<EpiModel> arrayList=new ArrayList<>();
        for(int i=0;i<items.size();i++){
            if(items.get(i).getSeson().equals(name)){
                arrayList.add(items.get(i));
            }
        }

        this.listepi = arrayList;
        ctx = context;
    }


    @Override
    public DirectorApater.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DirectorApater.OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_director_name, parent, false);
        vh = new DirectorApater.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final DirectorApater.OriginalViewHolder holder, final int position) {

        final EpiModel obj = listepi.get(position);
        holder.name.setText("Episode : "+obj.getEpi());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                chanColor(viewHolderArray[0],position);
                holder.name.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));



                if (holder.linearLayout.getVisibility()==View.GONE){
                    holder.linearLayout.setVisibility(View.VISIBLE);

                }
                else {
                    holder.linearLayout.setVisibility(View.GONE);
                }



               holder.play.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {




                       if (ctx instanceof DetailsActivity) {
                           ((DetailsActivity)ctx).showprogress();
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

                                   if (ctx instanceof DetailsActivity) {
                                       ((DetailsActivity)ctx).hideprogress();
                                   }
                                   new DetailsActivity().iniMoviePlayer(obj.getStreamURL(),obj.getServerType(),ctx,obj.getServerlabel());

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

                                   if (ctx instanceof DetailsActivity) {
                                       ((DetailsActivity)ctx).hideprogress();
                                   }
                                   new DetailsActivity().iniMoviePlayer(obj.getStreamURL(),obj.getServerType(),ctx,obj.getServerlabel());

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
                                   Log.e(AudienceNetworkAds.TAG, "Interstitial ad displayed.");
                               }

                               @Override
                               public void onInterstitialDismissed(Ad ad) {
                                   // Interstitial dismissed callback
                                   Log.e(AudienceNetworkAds.TAG, "Interstitial ad dismissed.");


                                   if (ctx instanceof DetailsActivity) {
                                       ((DetailsActivity)ctx).hideprogress();
                                   }
                                   new DetailsActivity().iniMoviePlayer(obj.getStreamURL(),obj.getServerType(),ctx,obj.getServerlabel());

                               }

                               @Override
                               public void onError(Ad ad, AdError adError) {
                                   // Ad error callback
                                   if (ctx instanceof DetailsActivity) {
                                       ((DetailsActivity)ctx).hideprogress();
                                   }
                                   new DetailsActivity().iniMoviePlayer(obj.getStreamURL(),obj.getServerType(),ctx,obj.getServerlabel());

                                   Log.e(AudienceNetworkAds.TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                               }

                               @Override
                               public void onAdLoaded(Ad ad) {

                                   // Interstitial ad is loaded and ready to be displayed
                                   Log.d(AudienceNetworkAds.TAG, "Interstitial ad is loaded and ready to be displayed!");
                                   // Show the ad

                               }

                               @Override
                               public void onAdClicked(Ad ad) {
                                   // Ad clicked callback
                                   Log.d(AudienceNetworkAds.TAG, "Interstitial ad clicked!");
                               }

                               @Override
                               public void onLoggingImpression(Ad ad) {
                                   // Ad impression logged callback
                                   Log.d(AudienceNetworkAds.TAG, "Interstitial ad impression logged!");
                               }
                           });

                           fanInterstitialAd.loadAd();



                       }




                       newsub=(obj.getListsubtv(position));
                            nowtitle=obj.getEpi();

                   }
               });


                holder.download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        long downloadFileRef = downloadFile(Uri.parse(obj.getLinkdownload()), DOWNLOAD_DIRECTORY, obj.getSeson()+obj.getEpi()+".mp4");
                            if (downloadFileRef != 0) {
                                Toast.makeText(ctx,"Starting download",Toast.LENGTH_LONG).show();

                            }else {
                                Toast.makeText(ctx,"File is not available for download",Toast.LENGTH_LONG).show();

                            }




                            return;

                    }
                });



                viewHolderArray[0] =holder;

            }
        });

    }

    @Override
    public int getItemCount() {
        return listepi.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public CardView cardView;
        public LinearLayout linearLayout;
        public Button download,play;



        public OriginalViewHolder(View v) {
            super(v);
            linearLayout=v.findViewById(R.id.lineardownload);
            download=v.findViewById(R.id.download);
            play=v.findViewById(R.id.play);
            name = v.findViewById(R.id.name);
            cardView=v.findViewById(R.id.card_view_home);
            Random rnd = new Random();
            int color = Color.argb(80, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            cardView.setCardBackgroundColor(color);
        }
    }

    private void chanColor(DirectorApater.OriginalViewHolder holder, int pos){

        if (holder!=null){
            holder.linearLayout.setVisibility(View.GONE);


            holder.name.setTextColor(ctx.getResources().getColor(R.color.grey_60));
        }



    }

    private long downloadFile(Uri uri, String fileStorageDestinationUri, String fileName) {

        long downloadReference = 0;

        DownloadManager downloadManager = (DownloadManager)ctx.getSystemService(DOWNLOAD_SERVICE);
        try {
            DownloadManager.Request request = new DownloadManager.Request(uri);

            //Setting title of request
            request.setTitle(fileName);

            //Setting description of request
            request.setDescription("Your file is downloading");

            //set notification when download completed
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            //Set the local destination for the downloaded file to a path within the application's external files directory
            request.setDestinationInExternalPublicDir(fileStorageDestinationUri, fileName);

            request.allowScanningByMediaScanner();

            //Enqueue download and save the referenceId
            downloadReference = downloadManager.enqueue(request);
        } catch (IllegalArgumentException e) {
            Toast.makeText(ctx,"Download link is broken or not availale for download",Toast.LENGTH_LONG).show();

            Log.e(TAG, "Line no: 455,Method: downloadFile: Download link is broken");

        }
        return downloadReference;
    }


}