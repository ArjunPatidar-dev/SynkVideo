package com.example.synkvideo.adapter;

import android.app.Application;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.synkvideo.R;
import com.example.synkvideo.utils.VideoDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.ybq.android.spinkit.SpinKitView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class VideoAdapter extends FirebaseRecyclerAdapter<VideoDetails, VideoAdapter.myViewHolder>
{
    @NonNull
    @Override
    public VideoDetails getItem(int position) {
        return super.getItem(getItemCount() - position - 1);
    }

    Application application;

    public VideoAdapter(@NonNull FirebaseRecyclerOptions<VideoDetails> options, Application application) {
        super(options);
        this.application = application;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull VideoDetails model) {
         holder.setData(model);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.video_adapter_item,parent,false);
        return new myViewHolder(view);
    }


    class myViewHolder extends RecyclerView.ViewHolder
    {
        VideoView videoView;
        TextView tvTitle;
        SpinKitView progressBar;

        public myViewHolder(@NonNull View itemView)
        {
            super(itemView);

            videoView = itemView.findViewById(R.id.video_view);
            tvTitle = itemView.findViewById(R.id.tv_video_title);
            progressBar = itemView.findViewById(R.id.video_progress_bar);
        }

        void setData(VideoDetails videoDetails)
        {
            videoView.setVideoURI(Uri.parse(videoDetails.getVideoUri()));
            tvTitle.setText(videoDetails.getVideoName());

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    progressBar.setVisibility(View.GONE);
                    mediaPlayer.start();
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        }
    }
}
