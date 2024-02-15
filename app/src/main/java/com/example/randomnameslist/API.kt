import com.example.randomnameslist.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface API {
    @GET("/api/")
    fun getUsers(@Query("results") results: Int): Call<UserResponse>
}
