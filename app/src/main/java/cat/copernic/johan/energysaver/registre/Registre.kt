package cat.copernic.johan.energysaver.registre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import cat.copernic.johan.energysaver.MainActivity
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.ActivityRegistreBinding
import com.google.firebase.ktx.Firebase





class Registre : AppCompatActivity() {
    private lateinit var binding: ActivityRegistreBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registre)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_registre)

        binding.btnConfirmarRegistre.setOnClickListener {
            //db.collection
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }
    }

}