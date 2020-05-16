package com.cpulse.tagfinder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cpulse.tagfinder.R;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.CustomItemViewAdapter> {

    private String[] mTags;
    private OnItemClicked mOnClickListener;

    public TagAdapter(String[] iTags) {
        mTags = iTags;
    }

    @NonNull
    @Override
    public TagAdapter.CustomItemViewAdapter onCreateViewHolder(@NonNull ViewGroup iViewGroup, int iViewType) {
        LinearLayout lLayout = (LinearLayout) LayoutInflater.from(iViewGroup.getContext()).inflate(
                R.layout.list_item_tag, iViewGroup, false);

        return new CustomItemViewAdapter(lLayout,
                (TextView) lLayout.findViewById(R.id.list_item_tag_primary_text));
    }

    @Override
    public void onBindViewHolder(@NonNull TagAdapter.CustomItemViewAdapter iHolder, final int iPosition) {
        String lTags = mTags[iPosition];

        // Trim + Upper first letter
        lTags = lTags.trim();
        lTags = lTags.substring(0, 1).toUpperCase() + lTags.substring(1);

        iHolder.mPrimaryText.setText(lTags);
        iHolder.mMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null)
                    mOnClickListener.onItemClicked(iPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTags.length;
    }

    public void setOnClickListener(OnItemClicked iOnClickListener) {
        mOnClickListener = iOnClickListener;
    }

    public interface OnItemClicked {
        void onItemClicked(int iPosition);
    }

    public static class CustomItemViewAdapter extends RecyclerView.ViewHolder {
        View mMainLayout;
        TextView mPrimaryText;

        public CustomItemViewAdapter(@NonNull View iMainLayout, TextView iPrimaryText) {
            super(iMainLayout);
            mMainLayout = iMainLayout;
            mPrimaryText = iPrimaryText;
        }
    }
}
