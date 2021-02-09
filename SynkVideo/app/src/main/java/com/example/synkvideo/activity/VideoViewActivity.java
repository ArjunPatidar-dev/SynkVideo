package com.example.synkvideo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.synkvideo.R;
import com.example.synkvideo.utils.Constants;
import com.example.synkvideo.utils.VideoDetails;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class VideoViewActivity extends AppCompatActivity {

    private final String TAG = VideoViewActivity.this.getClass().getSimpleName();
    private VideoView vvVideo;
    private MediaController mediaController;
    private Button btnPost;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private Uri videoUri;
    private ProgressDialog progressDialog;
    private String videoName;
    private String videoNameExt;
    private UploadTask uploadTask;
    private String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteVideoFile(videoPath);
    }

    private void UploadVideos(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);

        if (videoUri != null){
            progressDialog.show();
            Log.i(TAG, "UploadVideosNew: video extension ::==>>"+ videoNameExt);
            final StorageReference reference = storageReference.child(videoNameExt);
            uploadTask = reference.putFile(videoUri);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                progressDialog.dismiss();
                                Toast.makeText(VideoViewActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                                VideoDetails videoDetails = new VideoDetails(videoName, downloadUrl.toString());
                                Log.i(TAG, "onComplete: videoDetails name ::==>>"+videoDetails.getVideoName());
                                Log.i(TAG, "onComplete: videoDetails videoUri ::==>>"+videoDetails.getVideoUri());

                                String i = databaseReference.push().getKey();
                                databaseReference.child(i).setValue(videoDetails);
                                deleteVideoFile(videoPath);
                                setResult(Constants.RESULT_CODE_POSTED);
                                finish();


                            }
                        }
                    });
        }
    }

    private void initViews() {

        Intent intent = getIntent();
        videoPath = intent.getStringExtra(Constants.VIDEOS_PATH_KEY);
        videoName = intent.getStringExtra(Constants.VIDEOS_NAME_KEY);
        videoNameExt = intent.getStringExtra(Constants.VIDEOS_EXT_KEY);
        Log.i(TAG, "onCreate: videoPath ::" + videoPath);
        Log.i(TAG, "onCreate: videoName ::" + videoName);
        Log.i(TAG, "onCreate: videoNameExt ::" + videoNameExt);

        storageReference = FirebaseStorage.getInstance().getReference(Constants.FIREBASE_DATABASE_CHILD_NAME);
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DATABASE_CHILD_NAME);

        mediaController = new MediaController(this);
        vvVideo = findViewById(R.id.vv_video);
        btnPost = findViewById(R.id.btn_post);
        vvVideo.setMediaController(mediaController);
        mediaController.setAnchorView(vvVideo);

        videoUri = Uri.fromFile(new File(videoPath));
        vvVideo.setVideoURI(videoUri);
        vvVideo.start();

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //uploadVideo();
                UploadVideos();
            }
        });
    }


    private void deleteVideoFile(String videoPath) {
        File delFile = null;
        if (videoPath != null) {
            delFile = new File(videoPath);
        }
        if (delFile == null) {
            return;
        }
        final File file = new File(delFile.getAbsolutePath());
        delFile = null;
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (file.exists()) {
                    file.delete();
                }
            }
        }.start();
    }
}