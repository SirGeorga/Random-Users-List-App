package com.example.randomnameslist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UserViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)) {

    var userImage: ImageView = itemView.findViewById(R.id.ivUserImage)
    var userFirstName: TextView = itemView.findViewById(R.id.tvUserFirstName)
    var userLastName: TextView = itemView.findViewById(R.id.tvUserLastName)
    var userAdress: TextView = itemView.findViewById(R.id.tvUserAdress)
    var userPhoneNumber: TextView = itemView.findViewById(R.id.tvUserPhoneNumber)


    fun bind(user: User) {
        userFirstName.text = user.name.first
        userLastName.text = user.name.last
        userAdress.text = "${user.location.country} ${user.location.city} ${user.location.street}"
        userPhoneNumber.text = user.phone
        Glide.with(itemView)
            .load(user.picture.medium)
            .into(userImage)
        }
}