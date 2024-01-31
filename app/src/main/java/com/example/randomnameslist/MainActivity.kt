import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.randomnameslist.API
import com.example.randomnameslist.R
import com.example.randomnameslist.User
import com.example.randomnameslist.UserListAdapter
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
        private lateinit var apiService: API
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiService = Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API::class.java)

        sharedPreferences = getSharedPreferences("app", Context.MODE_PRIVATE)

        getUsers()
    }

    private fun getUsers() {
        apiService.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    val editor = sharedPreferences.edit()
                    editor.putString("users", Gson().toJson(users))
                    editor.apply()

                    recyclerView.adapter = UserListAdapter(users)
                } else {
                    Toast.makeText(this@MainActivity, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show()
            }
        })
    }
}