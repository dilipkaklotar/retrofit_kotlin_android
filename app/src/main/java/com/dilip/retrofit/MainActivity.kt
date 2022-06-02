package com.dilip.retrofit

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.dilip.retrofit.databinding.ActivityMainBinding
import com.dilip.retrofit.model.AlbumsItem
import com.dilip.retrofit.webservice.response.Albums
import com.dilip.retrofit.webservice.RetrofitInstance
import com.dilip.retrofit.webservice.services.AlbumService
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var retrofitService: AlbumService

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: AlbumAdapter

    val entrees: MutableList<AlbumsItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)


        retrofitService = RetrofitInstance
            .getRetrofitInstance()
            .create(AlbumService::class.java)

        getRequestWithQueryParameters()
    }

    private fun getRequestWithQueryParameters() {
        val responseLiveData: LiveData<Response<Albums>> = liveData {
            val response = retrofitService.getSortedAlbums(7)
            emit(response)
        }
        responseLiveData.observe(this, Observer {
            val albumsList = it.body()?.listIterator()
            if (albumsList != null) {


                while (albumsList.hasNext()) {
                    val albumsItem = albumsList.next()
                    entrees.add(albumsItem)
                    /*albumsItem.id
                    albumsItem.title
                    albumsItem.userId*/
                    initRecyclerView(entrees)

                }
            }
        })
    }
    private fun initRecyclerView(albumsItems: List<AlbumsItem>){
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AlbumAdapter({ selectedItem: AlbumsItem ->listItemClicked(selectedItem)})
        binding.recyclerView.adapter = adapter
        adapter.setList(albumsItems)
        adapter.notifyDataSetChanged()
    }

    private fun listItemClicked(item: AlbumsItem) {
        Toast.makeText(this, item.title + " Clicked", Toast.LENGTH_LONG).show()
    }


    private fun getRequestWithPathParameters() {
        val pathResponse: LiveData<Response<AlbumsItem>> = liveData {
            val response = retrofitService.getAlbum(7)
            emit(response)

        }

        pathResponse.observe(this, Observer {
            val title = it.body()?.title
            Toast.makeText(applicationContext, title, Toast.LENGTH_LONG).show()
        })
    }

    private fun uploadAlbum() {
        val album = AlbumsItem(0, "My title", 7)
        val postResponse: LiveData<Response<AlbumsItem>> = liveData {
            val response = retrofitService.uploadAlbum(album)
            emit(response)
        }
        postResponse.observe(this, Observer {
            val receivedAlbumsItem = it.body()
            val result = " " + "Album Title : ${receivedAlbumsItem?.title}" + "\n" +
                    " " + "Album id : ${receivedAlbumsItem?.id}" + "\n" +
                    " " + "User id : ${receivedAlbumsItem?.userId}" + "\n\n\n"

        })

    }
}