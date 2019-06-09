package com.example.playerv2

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.SeekBar
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var mediaPlayer: MediaPlayer
    private var userIsSeeking = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaPlayer = MediaPlayer.create(this, R.raw.mus)
        seekBar.max = mediaPlayer.duration

        val seconds = (mediaPlayer.duration / 1000) % 60
        val minutes = (mediaPlayer.duration / (1000 * 60) % 60)
        range.text = "$minutes:${"%02d".format(seconds)}"

        playBtn.setOnClickListener { play() }
        stopBtn.setOnClickListener { stop() }
        initializeSeekBar()

        handler.postDelayed(runUpdates,1000)
    }

    private fun stop() {
        mediaPlayer.pause()
        mediaPlayer.seekTo(0)
        seekBar.progress = 0
        playBtn.setBackgroundResource(R.drawable.play)
    }

    private fun play() {
        if(!mediaPlayer.isPlaying){
            mediaPlayer.start()
            playBtn.setBackgroundResource(R.drawable.pause)
        }else{
            mediaPlayer.pause()
            playBtn.setBackgroundResource(R.drawable.play)
        }
    }

    private fun initializeSeekBar() {
        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                var userSelectedPosition = 0

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    userIsSeeking = true
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        userSelectedPosition = progress
                    }
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    userIsSeeking = false
                    mediaPlayer.seekTo(userSelectedPosition)
                }
            }
        )
    }

    private val handler = Handler()

    private val runUpdates = object: Runnable{
        override fun run() {
            val currentPosition = mediaPlayer.currentPosition
            val seconds = (currentPosition / 1000) % 60
            val minutes = (currentPosition / (1000 * 60) % 60)

            seekBar.progress = currentPosition
            currentTimeLabel.text = "$minutes:${"%02d".format(seconds)}"

            handler.postDelayed(this, 1000)
        }
    }

}
