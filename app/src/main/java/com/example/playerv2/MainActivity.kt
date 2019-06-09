package com.example.playerv2

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        startUpdatingCallbackWithPosition()
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

    private lateinit var executor: ScheduledExecutorService
    private lateinit var updateTask: Runnable

    private fun startUpdatingCallbackWithPosition() {
        executor = Executors.newSingleThreadScheduledExecutor()
        updateTask = Runnable { updateSeekBar() }
        executor.scheduleAtFixedRate(
            updateTask,
            0,
            1000,
            TimeUnit.MILLISECONDS
        )
    }

    private fun updateRange() {
        if (mediaPlayer.isPlaying) {
            val currentPosition = mediaPlayer.currentPosition
            val seconds = (currentPosition / 1000) % 60
            val minutes = (currentPosition / (1000 * 60) % 60)
            range.text = "$minutes:${"%02d".format(seconds)}"
        }
    }

    private fun updateSeekBar() {
        if (mediaPlayer.isPlaying) {
            val currentPosition = mediaPlayer.currentPosition
            seekBar.progress = currentPosition
        }
    }

}
