package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    private var currentImageUrl : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }

    private fun loadMeme(){
        // Instantiate the RequestQueue.
        val progressBar = findViewById<ProgressBar>(R.id.pbProgressBar)
        progressBar.visibility = View.VISIBLE // so when ever this function will be called it will
        // be visible
        val url = "https://meme-api.herokuapp.com/gimme"
        val memeImage = findViewById<ImageView>(R.id.ivMemeImage)
        // Request a jsonObject response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                 currentImageUrl = response.getString("url") //here we define which string of json
            // object we will require
                Glide.with(this).load(currentImageUrl).listener(
                    object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                }).into(memeImage) //to use the url to get image we are using glide library
            },
            {

            })
        //Add the request to the RequestQueue by making only a single instance of the singleton class
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun shareMeme(view: View) { //this was created directly from the xml file using onClick
    // inside the buttons tag
         val intent = Intent(Intent.ACTION_SEND) //action send is used to
        intent.type = "text/plain" // this is to decide which app will be able to share this content
        // based on the type like text or image or mp3
        intent.putExtra(Intent.EXTRA_TEXT,"Hey checkout this new meme $currentImageUrl")
        //The text to show while sharing with in different app
        val chooser = Intent.createChooser(intent, "Share this meme using..") // we need chooser
        // to let user to choose the app of their choice
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme() //since we have already made a function where we are loading new memes
    }
}