package com.example.randomnameslist

import API
import UserListAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
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

    private lateinit var usersList: RecyclerView
    private var users = ArrayList<User>()
    private val adapter = UserListAdapter(users)
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var emptyListMessage: TextView
    private lateinit var btClearUsersList: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        users = createUsersListFromJson(sharedPreferences.getString(USERS_PREF_KEY, null))

        initViews()

        adapter.users = users
        usersList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        usersList.adapter = adapter
        swipeToRefresh = findViewById(R.id.strSwiper)
        if (users.isEmpty()) {
            getAllUsers()
        }
        messageVisibilityCheck()

        swipeToRefresh.setOnRefreshListener {
            getAllUsers()
        }

        btClearUsersList.setOnClickListener {
            users.clear()
            adapter.notifyDataSetChanged()
            sharedPreferences.edit().clear().apply()
            messageVisibilityCheck()
        }

        btClearUsersList.setOnLongClickListener {
            Toast.makeText(applicationContext, "Очистить список", Toast.LENGTH_SHORT)
                .show()
            true
        }

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

    fun initViews() {
        usersList = findViewById(R.id.rvUsers)
        emptyListMessage = findViewById(R.id.tvNoItemsInList)
        btClearUsersList = findViewById(R.id.btClearUsersList)
    }

    private fun createUsersListFromJson(json: String?): ArrayList<User> {
        if (json == null) {
            return ArrayList()
        }
        val listType = object : TypeToken<ArrayList<User>>() {}.type
        return Gson().fromJson(json, listType)
    }

    private fun createJsonFromFactsList(facts: ArrayList<User>): String {
        return Gson().toJson(facts)
    }

    private fun getAllUsers() {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API::class.java)

        api.getUsers(100).enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.code() == 200) {
                    users.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        users.addAll(response.body()?.results!!)
                        adapter.notifyDataSetChanged()
                        users.forEach { user ->
                            Log.i(TAG, "onResponse: ${user.name.first} ${user.name.last}")
                            swipeToRefresh.isRefreshing = false
                            val sharedPreferences =
                                getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
                            sharedPreferences.edit()
                                .putString(USERS_PREF_KEY, createJsonFromFactsList(adapter.users))
                                .apply()
                            messageVisibilityCheck()
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Ничего не нашлось",
                            Toast.LENGTH_LONG
                        ).show()

                        swipeToRefresh.isRefreshing = false
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Проблемы со связью. Проверьте подключение к интернету",
                        Toast.LENGTH_LONG
                    ).show()
                    messageVisibilityCheck()

                    swipeToRefresh.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.i(TAG, "onFailure: ${t.message}")
                Toast.makeText(
                    applicationContext,
                    "Проблемы со связью. Проверьте подключение к интернету",
                    Toast.LENGTH_LONG
                ).show()
                swipeToRefresh.isRefreshing = false
            }

        })
    }


    private fun messageVisibilityCheck() {
        if (users.isEmpty()) {
            emptyListMessage.visibility = View.VISIBLE
            btClearUsersList.visibility = View.GONE
        } else {
            emptyListMessage.visibility = View.GONE
            btClearUsersList.visibility = View.VISIBLE
        }
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