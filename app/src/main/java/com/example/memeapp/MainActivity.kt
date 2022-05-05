package com.example.memeapp

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    var currentImageUrl : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadmeme()
    }

   private fun loadmeme(){
       // Instantiate the RequestQueue.
       binding.progressBar.visibility = View.VISIBLE

       val queue = Volley.newRequestQueue(this)
       val url = "https://meme-api.herokuapp.com/gimme"
        Log.d("Success","Called")
       val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
           { response ->
               Log.d("successRequest" , "Success -> ${response.toString()}")
               currentImageUrl = response.getString("url")
               Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable> {
                   override fun onLoadFailed(
                       e: GlideException?,
                       model: Any?,
                       target: Target<Drawable>?,
                       isFirstResource: Boolean
                   ): Boolean {
                       binding.progressBar.visibility = View.GONE
                       return false
                   }

                   override fun onResourceReady(
                       resource: Drawable?,
                       model: Any?,
                       target: Target<Drawable>?,
                       dataSource: DataSource?,
                       isFirstResource: Boolean
                   ): Boolean {
                       binding.progressBar.visibility = View.GONE
                       return false
                   }


               }).into(binding.ivMemeDisplay)
           },
           {
               Log.d("Error" , "Error -> ${it.toString()}")
           }
       )

       // Add the request to the RequestQueue.
       queue.add(jsonObjectRequest)
   }

    fun nextMeme(view: View) {

        loadmeme()

    }

    fun shareMeme(view: View) {

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey , Check out this meme I got from reddit $currentImageUrl")
        val chooser = Intent.createChooser(intent,"Share this meme with...")
        startActivity(chooser)

    }



}

