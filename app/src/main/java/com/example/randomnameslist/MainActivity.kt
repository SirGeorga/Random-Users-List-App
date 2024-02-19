package com.example.randomnameslist

import API
import UserListAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SHARED_PREFERENCES = "shared_preferences"
const val USERS_PREF_KEY = "shared_pref_key"

class MainActivity : AppCompatActivity() {
    private val BASE_URL = "https://randomuser.me/"
    private val TAG: String = "CHECK_RESPONSE"
    private lateinit var rvUsersList: RecyclerView
    private var users = ArrayList<User>()
    private val adapter = UserListAdapter(users)
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var emptyListMessage: TextView
    private lateinit var btClearUsersList: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.title = "Список пользователей"
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        initViews()
        progressBarVisible()
        users = createUsersListFromJson(sharedPreferences.getString(USERS_PREF_KEY, null))
        adapter.users = users
        rvUsersList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvUsersList.adapter = adapter
        swipeToRefresh = findViewById(R.id.strSwiper)
        if (users.isEmpty()) {
            getAllUsers()
        }
        messageVisibility()

        swipeToRefresh.setOnRefreshListener {
            swipeToRefresh.isRefreshing = false
            getAllUsers()
        }

        btClearUsersList.setOnClickListener {
            users.clear()
            adapter.notifyDataSetChanged()
            sharedPreferences.edit().clear().apply()
            messageVisibility()
        }

        btClearUsersList.setOnLongClickListener {
            Toast.makeText(
                applicationContext,
                "Полное очищение списка пользователей, в том числе из памяти устройства",
                Toast.LENGTH_SHORT
            ).show()
            true
        }
    }

    fun initViews() {
        rvUsersList = findViewById(R.id.rvUsers)
        emptyListMessage = findViewById(R.id.tvNoItemsInList)
        btClearUsersList = findViewById(R.id.btClearUsersList)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun createUsersListFromJson(json: String?): ArrayList<User> {
        if (json == null) {
            return ArrayList()
        }
        val listType = object : TypeToken<ArrayList<User>>() {}.type
        return Gson().fromJson(json, listType)
    }

    private fun createJsonFromUsersList(users: ArrayList<User>): String {
        return Gson().toJson(users)
    }

    private fun getAllUsers() {
        progressBarVisible()
        Thread {
            val api = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build().create(API::class.java)

            api.getUsers(100).enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>, response: Response<UserResponse>
                ) {
                    if (response.code() == 200) {
                        val newUsers = response.body()?.results ?: emptyList()
                        runOnUiThread {
                            users.clear()
                            users.addAll(newUsers)
                            adapter.notifyDataSetChanged()
                            val sharedPreferences =
                                getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
                            sharedPreferences.edit()
                                .putString(USERS_PREF_KEY, createJsonFromUsersList(adapter.users))
                                .apply()
                            messageVisibility()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.st_no_internet),
                                Toast.LENGTH_LONG
                            ).show()
                            messageVisibility()
                        }
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    runOnUiThread {
                        Log.i(TAG, "onFailure: ${t.message}")
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.st_something_wrong),
                            Toast.LENGTH_LONG
                        ).show()
                        messageVisibility()
                    }
                }
            })
        }.start()
    }

    private fun messageVisibility() {
        emptyListMessage.visibility = if (users.isEmpty()) View.VISIBLE else View.GONE
        btClearUsersList.visibility = if (users.isEmpty()) View.GONE else View.VISIBLE
        progressBar.visibility = View.GONE
        rvUsersList.visibility = View.VISIBLE
    }

    private fun progressBarVisible() {
        btClearUsersList.visibility = View.GONE
        emptyListMessage.visibility = View.GONE
        rvUsersList.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }
}