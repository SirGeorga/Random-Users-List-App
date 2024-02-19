package com.example.randomnameslist

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide

class UserDetailsActivity : AppCompatActivity() {
    private lateinit var userFullName: TextView
    private lateinit var userFullAdress: TextView
    private lateinit var userFullPhone: TextView
    private lateinit var userFullEmail: TextView
    private lateinit var userFullAge: TextView
    private lateinit var userImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
        supportActionBar?.apply {
            title = "Данные пользователя"
            setDisplayHomeAsUpEnabled(true)
        }

        initViews()
        initListeners()

        userFullName.text =
            intent.extras?.getString("Full Name") ?: "Имя пользователя"//getString(R.string.st_unknown_track)
        userFullAdress.text =
            intent.extras?.getString("Adress") ?: "Адрес пользователя"//getString(R.string.st_unknown_artist)
        userFullPhone.text = intent.extras?.getString("Phone") ?: "Телефон пользователя"//getString(R.string.st_00_00)
        userFullEmail.text = intent.extras?.getString("E-mail") ?: "Почта пользователя"// getString(R.string.st_unknown_album)
        userFullAge.text = intent.extras?.getString("DOB") ?: "Возраст пользователя"//getString(R.string.st_unknown_year)).take(4)
        val getImage = (intent.extras?.getString("Image") ?: "Unknown Image")
        if (getImage != "Unknown Image") {
            Glide.with(this).load(getImage).placeholder(R.drawable.ic_user_placeholder).into(userImage)
        }
    }

    private fun initViews() {
        userFullName = findViewById<TextView>(R.id.tvFullName)
        userFullAdress = findViewById<TextView>(R.id.tvFullAdress)
        userFullPhone = findViewById<TextView>(R.id.tvFullPhone)
        userFullEmail = findViewById<TextView>(R.id.tvFullEmail)
        userFullAge = findViewById<TextView>(R.id.tvFullAge)
        userImage = findViewById<ImageView>(R.id.tvFullImage)
    }

    private fun initListeners() {

        userFullEmail.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf( intent.extras?.getString("E-mail")))
            startActivity(supportIntent)
        }
        userFullPhone.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_DIAL)
            supportIntent.data = Uri.parse("tel:" + "${intent.extras?.getString("Phone")}")
            startActivity(supportIntent)
        }

        userFullAdress.setOnClickListener {
            val mapsIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("geo:${intent.extras?.getString("Coordinates")}"))
            startActivity(mapsIntent)
        }
        userFullAdress.setOnLongClickListener{
            Toast.makeText(applicationContext, intent.extras?.getString("Coordinates"), Toast.LENGTH_SHORT)
                .show()
            true
        }
    }

       override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}