package com.example.myapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
import com.example.myapp.models.University;

import java.util.List;

public class UniversityAdapter extends RecyclerView.Adapter<UniversityAdapter.ViewHolder> {

    private Context context;
    private List<University> universityDataList;

    private WebView webView;

    public  UniversityAdapter(Context context, List<University> universityDataList, WebView webView) {
        this.context = context;
        this.universityDataList = universityDataList;
        this.webView =webView;
    }

    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.data_layout, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder( ViewHolder holder, int position) {
        University universityData = universityDataList.get(position);

        holder.universityNameTextView.setText(universityData.getName());
        holder.countryTextView.setText(universityData.getCountry());

        holder.websitesContainer.removeAllViews();

           String[] websites = universityData.getWebPages();
            for (String website : websites) {
                TextView websiteTextView = new TextView(context);
                websiteTextView.setText(website);
                websiteTextView.setPaintFlags(websiteTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                websiteTextView.setClickable(true);
                websiteTextView.setOnClickListener(v -> openWebsiteLink(website));
                holder.websitesContainer.addView(websiteTextView);
            }
    }

    public int getItemCount() {
        return universityDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView universityNameTextView;
        TextView countryTextView;

        LinearLayout websitesContainer;

        public ViewHolder( View itemView) {
            super(itemView);
            universityNameTextView = itemView.findViewById(R.id.universityNameTextView);
            countryTextView = itemView.findViewById(R.id.countryTextView);
            websitesContainer = itemView.findViewById(R.id.websitesContainer);
        }
    }

    private void openWebsiteLink(String url) {
        if (webView != null) {
            webView.setVisibility(View.VISIBLE);
            webView.loadUrl(url);

            Button closeButton = ((Activity) context).findViewById(R.id.closeButton);
            if (closeButton != null) {
                closeButton.setVisibility(View.VISIBLE);
                closeButton.setOnClickListener(v -> closeWebView());
            }
        }
    }

    private void closeWebView() {
        if (webView != null) {
            webView.setVisibility(View.GONE);

            Button closeButton = ((Activity) context).findViewById(R.id.closeButton);
            if (closeButton != null) {
                closeButton.setVisibility(View.GONE);
            }
        }
    }

    public void setData(List<University> universityList) {
        universityDataList.clear();

        universityDataList.addAll(universityList);
        notifyDataSetChanged();
    }
}