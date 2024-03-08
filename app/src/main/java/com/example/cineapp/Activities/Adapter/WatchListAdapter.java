package com.example.cineapp.Activities.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cineapp.Activities.Activities.DetailsWatchlistActivity;
import com.example.cineapp.Activities.Models.WatchList;
import com.example.cineapp.R;

import java.util.List;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.WatchlistViewHolder> {

    private List<WatchList> watchlists;
    private Context context;
    public void setWatchlists(List<WatchList> watchlists) {
        this.watchlists = watchlists;
        notifyDataSetChanged();
    }

    public WatchListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public WatchlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.watchlist_item, parent, false);
        return new WatchlistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchlistViewHolder holder, int position) {
        WatchList watchlist = watchlists.get(position);
        holder.bind(watchlist, this.context);
        // Configurar outros detalhes da watchlist, se necessário
    }

    @Override
    public int getItemCount() {
        return watchlists == null ? 0 : watchlists.size();
    }

    public class WatchlistViewHolder extends RecyclerView.ViewHolder {
        public TextView watchlistName;

        public WatchlistViewHolder(View view) {
            super(view);
            watchlistName = view.findViewById(R.id.reminder);
        }
        public void bind(WatchList watchList, Context context) {
            try {
                watchlistName.setText(watchList.getName());
                watchlistName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {;
                        Intent it = new Intent(context, DetailsWatchlistActivity.class);
                        it.putExtra("watchlist_id", String.valueOf(watchList.getId()));
                        context.startActivity(it);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}