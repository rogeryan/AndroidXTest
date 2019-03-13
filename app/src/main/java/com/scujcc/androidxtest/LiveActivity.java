package com.scujcc.androidxtest;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class LiveActivity extends AppCompatActivity {
    private SimpleExoPlayer player;
    private PlayerView playerView;
    private DataSource.Factory factory;
    private MediaSource mediaSource;
    private Channel channel;
    //TODO 动态传入此URL
    private final static String VIDEO_URL = "http://223.110.245.167/ott.js.chinamobile.com/PLTV/3/224/3221226942/index.m3u8";
//    private final static String VIDEO_URL = "http://223.110.245.159/ott.js.chinamobile.com/PLTV/3/224/3221225852/index.m3u8";
    private String TAG = "FFPLAYER";
    private String userAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        playerView = findViewById(R.id.playerView);
        userAgent = Util.getUserAgent(this, "FFPlayerDemo");
        Log.d(TAG, "userAgent=" + userAgent);
        channel = (Channel) getIntent().getSerializableExtra("channel");
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializePlayer();
        if (playerView != null) {
            playerView.onResume();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player == null) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (playerView != null) {
            playerView.onPause();
        }
        releasePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (playerView != null) {
            playerView.onPause();
        }
        releasePlayer();
    }

    private Uri getUri(String URL) {
        return Uri.parse(channel.getUrl());
    }

    private void initializePlayer() {
        //初始化播放器
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(this);
            player.addListener(new MyEventListener());
            player.setPlayWhenReady(true);
            playerView.setPlayer(player);

            factory = new DefaultDataSourceFactory(this, userAgent);
            mediaSource = new HlsMediaSource.Factory(factory).createMediaSource(getUri(channel.getUrl()));
        }
        player.prepare(mediaSource);
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
            mediaSource = null;
        }
    }

    class MyEventListener implements Player.EventListener {
        @Override
        public void onLoadingChanged(boolean isLoading) {
            Log.d(TAG, "onLoadingChanged isLoading=" + isLoading);
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case Player.STATE_BUFFERING:
                    Log.d(TAG, "正在缓冲...");
                    break;
                case Player.STATE_READY:
                    Log.d(TAG, "缓冲完成，可以播放了...");
                    break;
                case Player.STATE_IDLE:
                    Log.d(TAG, "闲置状态，无事可干...");
                    break;
                case Player.STATE_ENDED:
                    Log.d(TAG, "已经结束了。");
                    break;
                default:
                    Log.d(TAG, "无效状态:" + playbackState);
            }
            Log.d(TAG, "onPlayerStateChanged playWhenReady=" + playWhenReady);
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            Log.e(TAG, "onPlayerError 出错了，再次准备播放" + error);
            initializePlayer();
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            Log.d(TAG, "onPositionDiscontinuity reason=" + reason);
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            Log.d(TAG, "onPlaybackParametersChanged playbackParameters=" + playbackParameters);
        }
    }
}
