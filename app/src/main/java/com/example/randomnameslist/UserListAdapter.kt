import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.randomnameslist.User
import com.example.randomnameslist.UserDetailsActivity
import com.example.randomnameslist.UserViewHolder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserListAdapter(var users: ArrayList<User>) : RecyclerView.Adapter<UserViewHolder>() {
 

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder = UserViewHolder(parent)

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, UserDetailsActivity::class.java)
            intent.putExtra("Full Name", "${users[position].name.first} ${users[position].name.last}")
            intent.putExtra(
                "Adress",
                "${users[position].location.country} ${users[position].location.city} ${users[position].location.street.number} ${users[position].location.street.name}"
            )
            intent.putExtra("Coordinates", "${users[position].location.coordinates.latitude},${users[position].location.coordinates.longitude}")
            intent.putExtra("Phone", users[position].phone)
            intent.putExtra("E-mail", users[position].email)
            intent.putExtra("DOB", "${(users[position].dob.date).take(10)} (${users[position].dob.age} y.o.)")
            intent.putExtra("Image", users[position].picture.large)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = users.size

} 