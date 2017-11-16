package com.mar.lib.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.mar.lib.enviews.ENDownloadView;
import com.mar.lib.enviews.ENLoadingView;
import com.mar.lib.enviews.ENPlayView;
import com.mar.lib.enviews.ENRefreshView;
import com.mar.lib.enviews.ENScrollView;
import com.mar.lib.enviews.ENSearchView;
import com.mar.lib.enviews.ENVolumeView;
import com.mar.lib.widget.VerticalSwitchTextView;

import java.util.ArrayList;

public class EnviesExample extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_enviews);

        final ENDownloadView downloadView = (ENDownloadView) findViewById(R.id.view_download);
        Button btnStart = (Button) findViewById(R.id.btn_start);
        Button btnReset = (Button) findViewById(R.id.btn_reset);
        downloadView.setDownloadConfig(2000, 20 , ENDownloadView.DownloadUnit.MB);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadView.start();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadView.reset();
            }
        });

        final ENLoadingView loadingView = (ENLoadingView) findViewById(R.id.view_loading);
        Button btnShow = (Button) findViewById(R.id.btn_show);
        Button btnHide = (Button) findViewById(R.id.btn_hide);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingView.show();
            }
        });
        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingView.hide();
            }
        });

        final ENPlayView playView = (ENPlayView) findViewById(R.id.view_play);
        Button btnPause = (Button) findViewById(R.id.btn_pause);
        Button btnPlay = (Button) findViewById(R.id.btn_play);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playView.pause();
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playView.play();
            }
        });

        final ENVolumeView volumeView = (ENVolumeView) findViewById(R.id.view_volume);
        SeekBar sbVolume = (SeekBar) findViewById(R.id.sb_volume);

        sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                volumeView.updateVolumeValue(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final ENSearchView searchView = (ENSearchView) findViewById(R.id.view_search);
        Button btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.start();
            }
        });

        final ENRefreshView refreshView = (ENRefreshView) findViewById(R.id.view_refresh);
        Button btnRefresh = (Button) findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshView.startRefresh();
            }
        });

        final ENScrollView scrollView = (ENScrollView) findViewById(R.id.view_scroll);
        Button btnSwitch = (Button) findViewById(R.id.btn_switch);
        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scrollView.isSelected()) {
                    scrollView.unSelect();
                } else {
                    scrollView.select();
                }
            }
        });
    }
}
