package com.cpulse.tagfinder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cpulse.tagfinder.R;
import com.cpulse.tagfinder.core.LogManager;
import com.cpulse.tagfinder.newsapi.ArticleObject;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.CustomItemViewAdapter> {

    static private final String TAG = "ARTICLE ADAPTER";

    private ArticleObject[] mArticleObjects;

    private OnArticleClicked mOnArticleClicked;

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
                (TextView) lLayout.findViewById(R.id.article_item_secondary_text),
                (ProgressBar) lLayout.findViewById(R.id.article_item_loading_logo));
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomItemViewAdapter iHolder, final int iPosition) {
        final ArticleObject lArticleObject = mArticleObjects[iPosition];

        if (lArticleObject.getBitmap() == null) {
            lArticleObject.setOnBitmapReady(new ArticleObject.OnBitmapReady() {
                @Override
                public void onBitmapReady() {
                    LogManager.info(TAG, "Update bitmap ready for position : " + iPosition);
                    iHolder.mImageView.setImageBitmap(lArticleObject.getBitmap());
                    showImageView(iHolder.mImageView, iHolder.mProgressBar);
                    iHolder.mImageView.invalidate();
                }
            });
            lArticleObject.startImageDownload();
            iHolder.mImageView.setVisibility(View.INVISIBLE);
            iHolder.mProgressBar.setVisibility(View.VISIBLE);
        } else {
            iHolder.mImageView.setVisibility(View.VISIBLE);
            iHolder.mImageView.setImageBitmap(lArticleObject.getBitmap());
        }

        iHolder.mPrimaryText.setText(lArticleObject.getTitle());
        iHolder.mSecondaryText.setText(lArticleObject.getDescription());
        iHolder.mMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View iV) {
                if (mOnArticleClicked != null)
                    mOnArticleClicked.onArticleClicked(lArticleObject);

            }
        });
    }

    private void showImageView(final ImageView iImageView, final ProgressBar iProgressBar) {
        AlphaAnimation lFadeIn = new AlphaAnimation(0, 1);
        lFadeIn.setInterpolator(new AccelerateInterpolator());
        lFadeIn.setDuration(800);
        lFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation iAnimation) {
                iImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation iAnimation) {
                iProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation iAnimation) {
            }
        });

        iImageView.startAnimation(lFadeIn);
    }

    @Override
    public void onViewRecycled(@NonNull CustomItemViewAdapter iHolder) {
        ArticleObject lArticleObject = mArticleObjects[iHolder.getAdapterPosition()];
        lArticleObject.setOnBitmapReady(null);
        iHolder.mProgressBar.setVisibility(View.VISIBLE);
        super.onViewRecycled(iHolder);
    }

    @Override
    public int getItemCount() {
        return mArticleObjects.length;
    }

    public void setArticleObjectList(ArticleObject[] iArticleObjects) {
        mArticleObjects = iArticleObjects;
        notifyDataSetChanged();
    }

    public void setOnArticleClicked(OnArticleClicked iOnArticleClicked) {
        mOnArticleClicked = iOnArticleClicked;
    }

    public interface OnArticleClicked {
        void onArticleClicked(ArticleObject iArticleObject);
    }

    public static class CustomItemViewAdapter extends RecyclerView.ViewHolder {
        View mMainLayout;
        ImageView mImageView;
        TextView mPrimaryText;
        TextView mSecondaryText;
        ProgressBar mProgressBar;

        public CustomItemViewAdapter(@NonNull View iMainLayout, ImageView iImageView,
                                     TextView iPrimaryText, TextView iSecondaryText,
                                     ProgressBar iProgressBar) {
            super(iMainLayout);
            mMainLayout = iMainLayout;
            mImageView = iImageView;
            mPrimaryText = iPrimaryText;
            mSecondaryText = iSecondaryText;
            mProgressBar = iProgressBar;
        }
    }
}
