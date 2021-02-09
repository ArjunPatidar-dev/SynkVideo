package com.example.synkvideo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.synkvideo.R;
import com.example.synkvideo.adapter.VideoAdapter;
import com.example.synkvideo.utils.Constants;
import com.example.synkvideo.utils.VideoDetails;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class VideoStreamingActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private VideoAdapter videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_streaming);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewPager2 = findViewById(R.id.vp_video);

        FirebaseRecyclerOptions<VideoDetails> options =
                new FirebaseRecyclerOptions.Builder<VideoDetails>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_DATABASE_CHILD_NAME), VideoDetails.class)
                        .build();

        videoAdapter =new VideoAdapter(options, getApplication());
        viewPager2.setAdapter(videoAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        videoAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        videoAdapter.stopListening();
    }
}