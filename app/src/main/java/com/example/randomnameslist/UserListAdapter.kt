import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.randomnameslist.User
import com.example.randomnameslist.UserDetailsActivity
import com.example.randomnameslist.UserViewHolder
import kotlin.collections.ArrayList

class UserListAdapter(var users: ArrayList<User>) : RecyclerView.Adapter<UserViewHolder>() {
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(parent)

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                val intent = Intent(holder.itemView.context, UserDetailsActivity::class.java)
                intent.putExtra(
                    "Full Name",
                    "${users[position].name.first} ${users[position].name.last}"
                )
                intent.putExtra(
                    "Adress",
                    "${users[position].location.country} ${users[position].location.city} ${users[position].location.street.number} ${users[position].location.street.name}"
                )
                intent.putExtra(
                    "Coordinates",
                    "${users[position].location.coordinates.latitude},${users[position].location.coordinates.longitude}"
                )
                intent.putExtra("Phone", users[position].phone)
                intent.putExtra("E-mail", users[position].email)
                intent.putExtra(
                    "DOB",
                    "${(users[position].dob.date).take(10)} (${users[position].dob.age} y.o.)"
                )
                intent.putExtra("Image", users[position].picture.large)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = users.size
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
} 