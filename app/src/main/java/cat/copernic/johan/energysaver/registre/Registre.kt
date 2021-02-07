package cat.copernic.johan.energysaver.registre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import cat.copernic.johan.energysaver.MainActivity
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.ActivityRegistreBinding

private lateinit var binding: ActivityRegistreBinding

class Registre : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registre)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_registre)

        binding.btnConfirmarRegistre.setOnClickListener { val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }
    }

}