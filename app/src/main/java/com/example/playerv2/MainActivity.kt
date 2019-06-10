package com.example.playerv2

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    lateinit var mediaPlayer: MediaPlayer
    private var userIsSeeking = false

    private var songIndex: Int = 0

    private val handler = Handler()

    var myDataset = ArrayList<Song>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myDataset.add(Song("Ptrzykładowy song","autor songa",R.raw.mus))
        myDataset.add(Song("skrzypce","skrzypaczek",R.raw.mus2))
        myDataset.add(Song("inne skrzypce","wilonczelista",R.raw.mus3))
        myDataset.add(Song("chyba wiolonczela","nie wiem",R.raw.mus4))
        myDataset.add(Song("wilollolonczela","co robię",R.raw.mus5))
        myDataset.add(Song("sia 1","sia",R.raw.mus6))
        myDataset.add(Song("bird set free","sia",R.raw.mus7))
        myDataset.add(Song("szandelrelier","sia",R.raw.mus8))
        myDataset.add(Song("tanie thrillsy","sia",R.raw.mus9))

        mediaPlayer = MediaPlayer.create(this, R.raw.mus)
        songIndex = 0

        playBtn.setOnClickListener { play() }
        stopBtn.setOnClickListener { stop() }
        nextBtn.setOnClickListener { next() }
        prevBtn.setOnClickListener { previous() }
        initializeSeekBar()
        prepareLabels()

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(this,myDataset)

        recyclerView = my_recycler.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        labelSonga.isSelected = true

        handler.postDelayed(runUpdates,1000)
    }


    /*
    override fun onStart() {
        super.onStart()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = LibraryFragment()
        fragmentTransaction.add(R.id.activity_main, fragment)
        fragmentTransaction.commit()
    }
    */

    private fun prepareLabels() {
        seekBar.max = mediaPlayer.duration
        val seconds = (mediaPlayer.duration / 1000) % 60
        val minutes = (mediaPlayer.duration / (1000 * 60) % 60)
        range.text = "$minutes:${"%02d".format(seconds)}"

        labelSonga.text = myDataset[songIndex].name + " - " + myDataset[songIndex].band

    }

    fun playSong(song: Song, index: Int){
        mediaPlayer?.stop()
        mediaPlayer = MediaPlayer.create(this, song.uri)
        songIndex = index
        prepareLabels()
        play()
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

    private fun previous() {
        val prevIndex = songIndex - 1
        if(!(prevIndex<0)){
            playSong(myDataset[prevIndex],prevIndex)
            songIndex = prevIndex
        }else{
            playSong(myDataset[myDataset.size-1],myDataset.size-1)
            songIndex = myDataset.size-1
        }

    }

    private fun next() {
        val nextIndex = songIndex +1
        if(nextIndex<myDataset.size){
            playSong(myDataset[nextIndex],nextIndex)
            songIndex = nextIndex
        }else{
            playSong(myDataset[0],0)
            songIndex = 0
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

    private val runUpdates = object: Runnable{
        override fun run() {
            val currentPosition = mediaPlayer.currentPosition

            if(currentPosition == seekBar.max){
                next()
            }

            val seconds = (currentPosition / 1000) % 60
            val minutes = (currentPosition / (1000 * 60) % 60)

            if(!userIsSeeking){
                seekBar.progress = currentPosition
            }
            currentTimeLabel.text = "$minutes:${"%02d".format(seconds)}"

            handler.postDelayed(this, 1000)
        }
    }

}
