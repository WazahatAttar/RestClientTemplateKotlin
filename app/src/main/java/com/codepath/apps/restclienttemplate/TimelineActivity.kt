package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class TimelineActivity : AppCompatActivity() {
    lateinit var client: TwitterClient
    lateinit var rvTweets: RecyclerView
    lateinit var adapter: TweetsAdapter
    lateinit var swipeContainer: SwipeRefreshLayout
    val tweets = ArrayList<Tweet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        client=TwitterApplication.getRestClient(this)
        swipeContainer= findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            Log.i("Wizzy","Refreshing timeline")
            populateHomeTimeline()
        }
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);

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
                adapter.clear()
                val jsnArray = json.jsonArray

                val newTweets = Tweet.fromJsonArray(jsnArray)
                tweets.addAll(newTweets)
                adapter.notifyDataSetChanged()
                swipeContainer.setRefreshing(false)

            }

        })
    }
}