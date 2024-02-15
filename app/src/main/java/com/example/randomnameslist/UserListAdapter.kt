import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.randomnameslist.Name
import com.example.randomnameslist.User
import com.example.randomnameslist.UserResponse
import com.example.randomnameslist.UserViewHolder

class UserListAdapter() : RecyclerView.Adapter<UserViewHolder>() {
 
    var usersNames = ArrayList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder = UserViewHolder(parent)

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(usersNames[position])
    }

    override fun getItemCount(): Int = usersNames.size
} 