package com.cpulse.tagfinder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cpulse.tagfinder.R;
import com.cpulse.tagfinder.newsapi.ArticleObject;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.CustomItemViewAdapter> {

    private ArticleObject[] mArticleObjects;

    public ArticleAdapter(ArticleObject[] iArticleObjects) {
        mArticleObjects = iArticleObjects;
    }

    @NonNull
    @Override
    public CustomItemViewAdapter onCreateViewHolder(@NonNull ViewGroup iViewGroup, int iViewType) {
        LinearLayout lLayout = (LinearLayout) LayoutInflater.from(iViewGroup.getContext()).inflate(
                R.layout.list_item_article, iViewGroup, false);

        return new ArticleAdapter.CustomItemViewAdapter(lLayout,
                (ImageView) lLayout.findViewById(R.id.article_item_image),
                (TextView) lLayout.findViewById(R.id.article_item_primary_text),
                (TextView) lLayout.findViewById(R.id.article_item_secondary_text));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomItemViewAdapter iHolder, int iPosition) {
        ArticleObject lArticleObject = mArticleObjects[iPosition];

        iHolder.mPrimaryText.setText(lArticleObject.getTitle());
        iHolder.mSecondaryText.setText(lArticleObject.getDescription());
    }

    @Override
    public int getItemCount() {
        return mArticleObjects.length;
    }

    public void setArticleObjectList(ArticleObject[] iArticleObjects) {
        mArticleObjects = iArticleObjects;
        notifyDataSetChanged();
    }

    public static class CustomItemViewAdapter extends RecyclerView.ViewHolder {
        View mMainLayout;
        ImageView mImageView;
        TextView mPrimaryText;
        TextView mSecondaryText;

        public CustomItemViewAdapter(@NonNull View iMainLayout, ImageView iImageView, TextView iPrimaryText, TextView iSecondaryText) {
            super(iMainLayout);
            mMainLayout = iMainLayout;
            mImageView = iImageView;
            mPrimaryText = iPrimaryText;
            mSecondaryText = iSecondaryText;
        }
    }
}