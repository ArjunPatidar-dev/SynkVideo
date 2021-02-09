package com.example.synkvideo.utils;

public class VideoDetails {

    private String videoName;
    private String videoUri;

    private VideoDetails(){}

    public VideoDetails(String pVideoName, String pVideoUri){
        if (pVideoName.trim().equals("")){
            pVideoName = "Not available";
        }
        videoName = pVideoName;
        videoUri = pVideoUri;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }
}
