package com.example.wikkipiedia

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.wikkipiedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NewsItemClicked, NewsItemShare {
    private lateinit var binding: ActivityMainBinding

    private lateinit var nAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var selectedCategory :String = Category[0].drop(1).dropLast(1)
        var selectedCountry :String = Country[0].takeLast(3).take(2)

        recyclerViewUpdater("https://saurav.tech/NewsAPI/top-headlines/category/$selectedCategory/$selectedCountry.json")

        val categoryAdapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, Category)
        binding.category.adapter = categoryAdapter

            binding.category.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    selectedCategory  = Category[position].drop(1).dropLast(1)
                    recyclerViewUpdater("https://saurav.tech/NewsAPI/top-headlines/category/$selectedCategory/$selectedCountry.json")
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    recyclerViewUpdater("https://saurav.tech/NewsAPI/top-headlines/category/$selectedCategory/$selectedCountry.json")
                }
            }

        val countryAdapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, Country)
        binding.country.adapter = countryAdapter


        binding.country.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                selectedCountry  = Country[position].takeLast(3).take(2)
                recyclerViewUpdater("https://saurav.tech/NewsAPI/top-headlines/category/$selectedCategory/$selectedCountry.json")
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                recyclerViewUpdater("https://saurav.tech/NewsAPI/top-headlines/category/$selectedCategory/$selectedCountry.json")
            }
        }
    }

    fun recyclerViewUpdater (url: String){
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData(url)
        nAdapter = NewsAdapter(this,this)
        binding.recyclerView.adapter = nAdapter
    }

    private fun fetchData (url : String){
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null, {
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for(i in 0 until newsJsonArray.length()){
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                nAdapter.updateNews(newsArray)
        }, {

            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News){
         val customTabsIntent = CustomTabsIntent.Builder().build()
         customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }

    override fun onLongClicked(item: News) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, item.url)
            type = "text/plain"
        }
        startActivity(sendIntent)
    }

}