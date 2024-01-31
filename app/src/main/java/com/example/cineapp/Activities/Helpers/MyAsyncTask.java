package com.example.cineapp.Activities.Helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.core.Tag;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyAsyncTask extends AsyncTask<Void, Void, String> {
    private AsyncTaskListener listener;

    public MyAsyncTask(AsyncTaskListener listener) {
        this.listener = listener;
    }
        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/popular")
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhNTc0YmRkOGM5ZTg4MTQxNGM0NzI2YmRlMTgxYTEyNyIsInN1YiI6IjY1OWZkZDhkOGRlMGFlMDEyNThjMmVkZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.VG61EHZUYapdl2lk_rnun2DQK7y25nz5C8Q1WqJFyhY")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();

            }catch (IOException e) {
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
            void onTaskComplete(JSONObject result);
            void onTaskError(String error);
        }
}
