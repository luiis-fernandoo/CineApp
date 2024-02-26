package com.example.cineapp.Activities.Helpers;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyAsyncTask extends AsyncTask<Void, Void, String> {
    private AsyncTaskListener listener;
    private String filmId;
    private Activity mActivity;

    public MyAsyncTask(AsyncTaskListener listener, String filmId) {
        this.listener = listener;
        this.filmId = filmId;
    }

    public MyAsyncTask(AsyncTaskListener listener) {
        this.listener = listener;
    }

    @Override
        protected String doInBackground(Void... voids) {
            String url = null;
            if (this.listener.getClass().getSimpleName().equals("HomeFragment")) {
                url = "https://api.themoviedb.org/3/movie/top_rated?language=pt-BR";
            } else if (this.listener.getClass().getSimpleName().equals("detailsFilmActivity")) {
                url = "https://api.themoviedb.org/3/movie/"+filmId+"?language=pt-BR";
            }

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhNTc0YmRkOGM5ZTg4MTQxNGM0NzI2YmRlMTgxYTEyNyIsInN1YiI6IjY1OWZkZDhkOGRlMGFlMDEyNThjMmVkZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.VG61EHZUYapdl2lk_rnun2DQK7y25nz5C8Q1WqJFyhY")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();

            } catch (IOException e) {
                // Trate a exceção de maneira apropriada
                return "Erro de rede: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                if (listener != null) {
                    listener.onTaskComplete(json);
                }
            } catch (JSONException e) {
                // Lidar com erros de análise JSON
                e.printStackTrace();
                if (listener != null) {
                    listener.onTaskError(e.getMessage());
                }
            }
        }
        public interface AsyncTaskListener {
            void onTaskComplete(JSONObject result) throws JSONException;
            void onTaskError(String error);
        }
}
