package com.cpulse.tagfinder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cpulse.tagfinder.R;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.CustomItemViewAdapter> {

    private String[] mTopics;
    private OnItemClicked mOnClickListener;

    public TopicAdapter(String[] iTopics) {
        mTopics = iTopics;
    }

    @NonNull
    @Override
    public TopicAdapter.CustomItemViewAdapter onCreateViewHolder(@NonNull ViewGroup iViewGroup, int iViewType) {
        LinearLayout lLayout = (LinearLayout) LayoutInflater.from(iViewGroup.getContext()).inflate(
                R.layout.list_item_tag, iViewGroup, false);

        return new CustomItemViewAdapter(lLayout,
                (TextView) lLayout.findViewById(R.id.list_item_topic_primary_text));
    }

    @Override
    public void onBindViewHolder(@NonNull TopicAdapter.CustomItemViewAdapter iHolder, final int iPosition) {
        String lTopic = mTopics[iPosition];

        // Trim + Upper first letter
        lTopic = lTopic.trim();
        lTopic = lTopic.substring(0, 1).toUpperCase() + lTopic.substring(1);

        iHolder.mPrimaryText.setText(lTopic);
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
        return mTopics.length;
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
