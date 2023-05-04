package com.example.project2_gameshop_v2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
    private ArrayList<gameItem> mGameItemArrayList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDeleteClick (int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView;
        public TextView mTextView2;
        public ImageView mReturnImage;

        public GameViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView = itemView.findViewById(R.id.gameTitleTextView);
            mTextView2 = itemView.findViewById(R.id.gameDescriptionTextView);
            mReturnImage = itemView.findViewById(R.id.image_return);

            mReturnImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(position);
                    }
                    }
                }
            });
        }
    }

    public GameAdapter(ArrayList<gameItem> gameList) {
        mGameItemArrayList = gameList;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_card_item, parent, false);
        GameViewHolder gameViewHolder = new GameViewHolder(v, mListener);
        return gameViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        gameItem currentItem = mGameItemArrayList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView.setText(currentItem.getGameTitle());
        holder.mTextView2.setText(currentItem.getGameDescription());
    }

    @Override
    public int getItemCount() {
        return mGameItemArrayList.size();
    }
}
