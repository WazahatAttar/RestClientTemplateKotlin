package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {
    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var  client: TwitterClient
    lateinit var tweetCount: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.editTextTweet)
        btnTweet = findViewById(R.id.btnTweet)
        client= TwitterApplication.getRestClient(this)
        tweetCount= findViewById(R.id.count_tweet)

        etCompose.addTextChangedListener(object : TextWatcher{
            val tweetContent = etCompose.text
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                btnTweet.isEnabled = tweetContent.isNotEmpty() && tweetContent.length <= 140
                tweetCount.text = (140 - etCompose.text.toString().trim().length).toString()
            }
        })

        btnTweet.setOnClickListener{
            val tweetContent= etCompose.text.toString()

            if(tweetContent.isEmpty()){
                Toast.makeText(this, "Empty tweet not allowed!", Toast.LENGTH_SHORT).show()

            }

            if (tweetContent.length > 140){
                Toast.makeText(this, "Tweet longer than 140 characters!", Toast.LENGTH_SHORT).show()

            }
            else{
                client.publishTweet(tweetContent,object : JsonHttpResponseHandler(){
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e("Wizzy", "Failed Publish", throwable)
                    }

                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                        val tweet = Tweet.fromJson(json.jsonObject)
                        val intent=Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()


                    }

                })

            }

        }
    }
}