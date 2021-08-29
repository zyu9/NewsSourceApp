package com.example.newssourceapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsDetailFragment extends Fragment {
    private static final String ARG_PARAM = "mSource";

    private Source mSource;

    final OkHttpClient client = new OkHttpClient();

    public NewsDetailFragment() {
        // Required empty public constructor
    }

    public static NewsDetailFragment newInstance(Source source) {
        NewsDetailFragment fragment = new NewsDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, source);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSource = (Source) getArguments().getSerializable(ARG_PARAM);
        }
    }

    RecyclerView recyclerView;
    NewsDetailAdapter adapter;
    LinearLayoutManager mLayoutManager;
    ArrayList<Source.Articles> articles = new ArrayList<>();
    ParseSource parseSource = new ParseSource();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(mSource.getNews().getName());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_detail, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewForDetails);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        String apiKey = "98d8ff9c192e411db09b752b171222a9";

        HttpUrl url = HttpUrl.parse("https://newsapi.org/v2/everything").newBuilder()
                .addQueryParameter("q", mSource.getNews().getId())
                .addQueryParameter("apiKey", apiKey)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    articles = parseSource.parseArticles(response.body().string());

                    adapter = new NewsDetailAdapter(articles);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(adapter);
                        }
                    });
                }
            }
        });


        return view;
    }

    class NewsDetailAdapter extends RecyclerView.Adapter<NewsDetailAdapter.NewsDetailsViewHolder>{
        ArrayList<Source.Articles> articles;

        public NewsDetailAdapter(ArrayList<Source.Articles> articles) {
            this.articles = articles;
        }

        @NonNull
        @Override
        public NewsDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_item_for_news_details_recycler, parent, false);
            return new NewsDetailsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NewsDetailsViewHolder holder, int position) {
            Source.Articles article = articles.get(position);
            holder.getViews(article);
        }


        @Override
        public int getItemCount() {
            return this.articles.size();
        }

        class NewsDetailsViewHolder extends RecyclerView.ViewHolder{
            Source.Articles article;
            TextView textViewDescription, textViewTitle, textViewDate, textViewAuthor;
            ImageView imageView;

            public NewsDetailsViewHolder(@NonNull View itemView) {
                super(itemView);

                textViewTitle = itemView.findViewById(R.id.textViewNewsDetailTitle);
                textViewDescription = itemView.findViewById(R.id.textViewNewsDetailDescription);
                textViewAuthor = itemView.findViewById(R.id.textViewNewsDetailAuthor);
                textViewDate = itemView.findViewById(R.id.textViewNewsDetailDate);
                imageView = itemView.findViewById(R.id.imageView);
            }

            public void getViews(Source.Articles data) {
                this.article = data;

                textViewTitle.setText(article.getTitle());
                textViewDescription.setText(article.getDescription());
                textViewAuthor.setText(article.getAuthor());
                textViewDate.setText(article.getPublishedAt());

                String imageUrl = article.getUrlToImage();
                Picasso.get().load(imageUrl).into(imageView);
            }
        }
    }
}