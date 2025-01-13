package com.example.ecosynergy;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

public class CheckThisOutAdapter extends RecyclerView.Adapter<CheckThisOutAdapter.ViewHolder> {

    private List<Video> videos;

    public CheckThisOutAdapter(List<Video> videos) {
        this.videos = videos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_this_out, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Video video = videos.get(position);
        holder.title.setText(video.getTitle());

        // Initialize YouTube player with video URL
        String videoUrl = video.getUrl();
        holder.setUpYouTubePlayer(videoUrl);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        YouTubePlayerView youTubePlayerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.video_title);
            youTubePlayerView = itemView.findViewById(R.id.video_thumbnail);
        }

        public void setUpYouTubePlayer(String videoUrl) {
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    String videoId = extractVideoId(videoUrl);
                    if (videoId != null && !videoId.isEmpty()) {
                        try {
                            youTubePlayer.cueVideo(videoId, 0);
                        } catch (Exception e) {
                            Log.e("CheckThisOutAdapter", "Error initializing YouTube Player", e);
                        }
                    } else {
                        Toast.makeText(itemView.getContext(), "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private String extractVideoId(String videoUrl) {
            String videoId = null;
            // Check if the URL is in the shortened format (youtu.be)
            if (videoUrl.contains("youtu.be")) {
                String[] urlParts = videoUrl.split("/");
                if (urlParts.length > 1) {
                    videoId = urlParts[1].split("\\?")[0]; // Extract video ID before any query parameters
                }
            }
            // Check if the URL is in the standard format (youtube.com)
            else if (videoUrl.contains("youtube.com")) {
                String[] urlParts = videoUrl.split("v=");
                if (urlParts.length > 1) {
                    videoId = urlParts[1].split("&")[0]; // Extract video ID before any additional query parameters
                }
            }
            return videoId;
        }
    }
}
