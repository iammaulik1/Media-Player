package com.example.a17mediaplayer

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer:MediaPlayer
    private lateinit var runnable: Runnable
    private var handler:Handler = Handler()
    private var pause:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playButton = findViewById<Button>(R.id.playButton)
        val pauseButton =findViewById<Button>(R.id.pauseButton)
        val stopButton = findViewById<Button>(R.id.stopButton)
        val seekBar =findViewById<SeekBar>(R.id.seekBar)
        val tv_pass = findViewById<TextView>(R.id.tv_pass)
        val tv_due = findViewById<TextView>(R.id.tv_due)


        playButton.setOnClickListener{
            if (pause){
                mediaPlayer.seekTo(mediaPlayer.currentPosition)
                mediaPlayer.start()
                pause=false
                Toast.makeText(this,"Media Playing",Toast.LENGTH_SHORT).show()
            }else{
                mediaPlayer = MediaPlayer.create(applicationContext,R.raw.song)
                mediaPlayer.start()
                Toast.makeText(this,"Media Playing",Toast.LENGTH_SHORT).show()
            }
            initializeseekBar()
            playButton.isEnabled=false
            pauseButton.isEnabled=true
            stopButton.isEnabled=true

            mediaPlayer.setOnCompletionListener {
                playButton.isEnabled=true
                pauseButton.isEnabled=false
                stopButton.isEnabled=false
                Toast.makeText(this,"END",Toast.LENGTH_SHORT).show()
            }
        }
        pauseButton.setOnClickListener{
            if (mediaPlayer.isPlaying){
                mediaPlayer.pause()
                pause=true
                playButton.isEnabled=true
                pauseButton.isEnabled=false
                stopButton.isEnabled=true
                Toast.makeText(this,"Media Paused",Toast.LENGTH_SHORT).show()
            }
        }
        stopButton.setOnClickListener {
            if (mediaPlayer.isPlaying||pause.equals(true)){
                pause=false
                seekBar.setProgress(0)
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.release()
                handler.removeCallbacks(runnable)

                playButton.isEnabled=true
                pauseButton.isEnabled=false
                stopButton.isEnabled=false
                tv_pass.text = ""
                tv_due.text = ""
                Toast.makeText(this,"Media Stop",Toast.LENGTH_SHORT).show()
            }
        }

        seekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if(b){
                    mediaPlayer.seekTo(i*1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
    }
    private fun initializeseekBar(){
        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val tv_pass = findViewById<TextView>(R.id.tv_pass)
        val tv_due = findViewById<TextView>(R.id.tv_due)

        seekBar.max = mediaPlayer.seconds

        runnable = Runnable{
            seekBar.progress = mediaPlayer.currentSeconds

            tv_pass.text = "${mediaPlayer.currentSeconds}sec"
            val diff = mediaPlayer.seconds - mediaPlayer.currentSeconds
            tv_due.text = "$diff sec"

            handler.postDelayed(runnable,1000)
        }
        handler.postDelayed(runnable,1000)
    }
}
val MediaPlayer.seconds:Int
    get(){
        return this.duration/1000
    }
val MediaPlayer.currentSeconds:Int
    get(){
        return this.currentPosition/1000
    }