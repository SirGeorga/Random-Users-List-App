package com.example.randomnameslist
import API
import UserListAdapter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val BASE_URL = "https://randomuser.me/"
    private val TAG: String = "CHECK_RESPONSE"


    private lateinit var usersList: RecyclerView
    private var users = ArrayList<User>()
    private val adapter = UserListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usersList = findViewById(R.id.rvUsers)
        adapter.usersNames = users
        usersList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        usersList.adapter = adapter

        getAllComments()


/*
        apiService.getUsers(1).enqueue(object : Callback<UsersResponse> {
            override fun onResponse(call: Call<UsersResponse>,
                                    response: Response<UsersResponse>) {
                if (response.code() == 200) {
                    users.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        users.addAll(response.body()?.results!!)
                        adapter.notifyDataSetChanged()
                    }
                    if (users.isEmpty()) {
                        showMessage(getString(R.string.nothing_found), "")
                    } else {
                        showMessage("", "")
                    }
                } else {
                    showMessage(getString(R.string.something_went_wrong) + response.code(), response.code().toString())
                }
            }
            override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                showMessage(getString(R.string.something_went_wrong_harder), t.message.toString())
            }

        })*/
    }

    private fun getAllComments() {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API::class.java)

        api.getUsers(100).enqueue(object : Callback<UserResponse>{
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.code() == 200){
                    users.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        users.addAll(response.body()?.results!!)
                        adapter.notifyDataSetChanged()
                        users?.forEach { user ->
                            Log.i(TAG, "onResponse: ${user.name.first} ${user.name.last}")
                           /* Toast.makeText(
                                applicationContext,
                                 "Нашёлся ${user.name.first} ${user.name.last}",
                                Toast.LENGTH_LONG
                            ).show()*/
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Ничего не нашлось",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Проблемы со связью. Чел, чекни тырнет свой",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.i(TAG,"onFailure: ${t.message}")
                Toast.makeText(applicationContext, "Проблемы со связью. Чел, чекни тырнет свой", Toast.LENGTH_LONG).show()
            }

        })
    }



    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            //placeholderMessage.visibility = View.VISIBLE
            users.clear()
            adapter.notifyDataSetChanged()
            //placeholderMessage.text = text
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG)
                .show()
            /*if (additionalMessage.isNotEmpty()) {

            }
        } else {
            //placeholderMessage.visibility = View.GONE
        }*/
        }
    }

}