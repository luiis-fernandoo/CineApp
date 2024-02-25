package com.example.cineapp.Activities.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.cineapp.Activities.Activities.detailsFilmActivity;
import com.example.cineapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private final Context context;
    private final JSONArray cardList;

    public Adapter(Context context, JSONArray cardList) {
        this.context = context;
        this.cardList = cardList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            JSONObject card = cardList.getJSONObject(position); // Obtenha o objeto JSON na posição especificada
            holder.bind(card);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return cardList.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void bind(JSONObject card) {
            try {
                String filmId = card.getString("id");
                imageView.setTag(filmId);

                String poster_path = "https://image.tmdb.org/t/p/original/" + card.getString("poster_path");
                Glide.with(itemView.getContext())
                        .load(poster_path)
                        .into(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent it = new Intent(context, detailsFilmActivity.class);
                        it.putExtra("tag", (String)imageView.getTag());
                        context.startActivity(it);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}


