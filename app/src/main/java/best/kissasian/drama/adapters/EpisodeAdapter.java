package best.kissasian.drama.adapters;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.oxoo.spagreen.R;

import best.kissasian.drama.models.CommonModels;
import best.kissasian.drama.models.EpiModel;


import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.OriginalViewHolder> {

    private List<CommonModels> items;
    private Context ctx;

    private EpisodeAdapter.OriginalViewHolder viewHolder;
    private OnItemClickListener mOnItemClickListener;

    // A menu item view type.
    private static final int MENU_ITEM_VIEW_TYPE = 0;

    // The unified native ad view type.
    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;


    public void chanColor(EpisodeAdapter.OriginalViewHolder holder,int pos){

        if (pos!=0){
            viewHolder.name.setTextColor(ctx.getResources().getColor(R.color.grey_60));
        }

        if (holder!=null){
            holder.name.setTextColor(ctx.getResources().getColor(R.color.grey_60));
        }



    }








    public EpisodeAdapter(Context context, List<CommonModels> items) {
        this.items = items;
        ctx = context;

    }


    @Override
    public EpisodeAdapter.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        EpisodeAdapter.OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_episode, parent, false);
        vh = new EpisodeAdapter.OriginalViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(final EpisodeAdapter.OriginalViewHolder holder, final int position) {







        CommonModels obj = items.get(position);
        holder.name.setText("Season : "+obj.getTitle());

        Log.e("Season Name::",obj.getTitle());


        DirectorApater directorApater=new DirectorApater(ctx,obj.getListEpi(),obj.getTitle());
        Log.e("List", String.valueOf(obj.getTitle()));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setAdapter(directorApater);








    }





    public void setOnItemClickListener(EpisodeAdapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, EpiModel obj, int position, EpisodeAdapter.OriginalViewHolder holder);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {



        public TextView name;
        public RecyclerView recyclerView;


        public OriginalViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            recyclerView=v.findViewById(R.id.recyclerView);


        }
    }





}