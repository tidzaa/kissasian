package com.kissasian.drama.adapters;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kissasian.drama.DetailsActivity;
import com.oxoo.spagreen.R;
import com.kissasian.drama.models.CommonModels;

import java.util.List;

public class ServerApater extends RecyclerView.Adapter<ServerApater.OriginalViewHolder> {

    private List<CommonModels> items;
    private Context ctx;

    private ServerApater.OnItemClickListener mOnItemClickListener;

    private ServerApater.OriginalViewHolder viewHolder;



    public interface OnItemClickListener {
        void onItemClick(View view, CommonModels obj, int position, OriginalViewHolder holder);
    }

    public void setOnItemClickListener(ServerApater.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }



    public ServerApater(Context context, List<CommonModels> items) {
        this.items = items;
        ctx = context;
    }


    @Override
    public ServerApater.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ServerApater.OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_server, parent, false);
        vh = new ServerApater.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ServerApater.OriginalViewHolder holder, final int position) {

        CommonModels obj = items.get(position);
        holder.name.setText(obj.getTitle());

        if (position==0){
            viewHolder=holder;
            new DetailsActivity().iniMoviePlayer(obj.getStremURL(),obj.getServerType(),ctx,obj.getTitle());
//            new DetailsActivity().getlistsubnow(obj.getListSubtv());






            holder.name.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, items.get(position), position,holder);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public CardView cardView;
        public TextView judulatas;

        public OriginalViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            cardView=v.findViewById(R.id.card_view_home);
            judulatas=v.findViewById(R.id.judulfilm);
        }
    }

    public void chanColor(ServerApater.OriginalViewHolder holder,int pos){

        if (pos!=0){
            viewHolder.name.setTextColor(ctx.getResources().getColor(R.color.grey_60));
        }

        if (holder!=null){
            holder.name.setTextColor(ctx.getResources().getColor(R.color.grey_60));
        }



    }

}