import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.randomnameslist.R
import com.example.randomnameslist.User
import com.example.randomnameslist.UserDetailsActivity
import com.google.gson.Gson

class UserAdapter(private val users: List<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            itemView.name.text = user.name
            // Glide.with(itemView.context).load(user.picture).into(itemView.picture)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, UserDetailsActivity::class.java)
                intent.putExtra("user", Gson().toJson(user))
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() = users.size
}