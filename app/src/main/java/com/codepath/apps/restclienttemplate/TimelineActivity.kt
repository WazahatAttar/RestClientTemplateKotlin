package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class TimelineActivity : AppCompatActivity() {
    lateinit var client: TwitterClient
    lateinit var rvTweets: RecyclerView
    lateinit var adapter: TweetsAdapter
    val tweets = ArrayList<Tweet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        client=TwitterApplication.getRestClient(this)
        adapter= TweetsAdapter(tweets)
        rvTweets= findViewById(R.id.rvTweets)
        rvTweets.layoutManager= LinearLayoutManager(this)
        rvTweets.adapter = adapter


        populateHomeTimeline()

    }

    fun populateHomeTimeline() {
        client.getHomeTimeline(object : JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {

            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i("Wizzy","Success yo!! ")
                val jsnArray = json.jsonArray

                val newTweets = Tweet.fromJsonArray(jsnArray)
                tweets.addAll(newTweets)
                adapter.notifyDataSetChanged()

            }

        })
    }
}