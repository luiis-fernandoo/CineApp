package com.example.cineapp.Activities.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.cineapp.Activities.Adapter.Adapter;
import com.example.cineapp.Activities.Decoration.ItemDecoration;
import com.example.cineapp.Activities.Decoration.SpaceItemDecoration;
import com.example.cineapp.Activities.Helpers.MyAsyncTask;
import com.example.cineapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements MyAsyncTask.AsyncTaskListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText editTextSearch;
    private String reference;
    private RecyclerView recycleViewSearch;
    private Button buttonConfirm;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        recycleViewSearch = view.findViewById(R.id.recycleViewSearch);

        // Supondo que 'buttonConfirm' seja o ID do botão de confirmação
        Button buttonConfirm = view.findViewById(R.id.buttonConfirm);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = editTextSearch.getText().toString().trim();
                performSearch(query);
            }
        });

        return view;
    }

    @Override
    public void onTaskComplete(JSONObject result, String reference) {
        if (isAdded() && getContext() != null) {
            try {
                JSONArray results = result.getJSONArray("results");
                if (results.length() > 0) {
                    Adapter adapter = new Adapter(requireContext(), results);
                    GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
                    recycleViewSearch.removeAllViews();
                    recycleViewSearch.setLayoutManager(layoutManager);
                    //recycleViewSearch.addItemDecoration(new ItemDecoration(30));
                    recycleViewSearch.setAdapter(adapter);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onTaskError(String error) {

    }

    private void performSearch(String query){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!TextUtils.isEmpty(query)){
                    String urlSearch = "https://api.themoviedb.org/3/search/movie?query="+query+"&language=pt-BR\n";
                    reference = "Busca";
                    MyAsyncTask myAsyncTaskSearch = new MyAsyncTask(SearchFragment.this, urlSearch, "SearchResults");
                    myAsyncTaskSearch.execute();
                }else{
                    //
                }
            }
        }).start();
    }
}