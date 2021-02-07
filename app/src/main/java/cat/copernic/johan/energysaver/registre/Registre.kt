package cat.copernic.johan.energysaver.registre

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.ActivityRegistreBinding

class Registre : AppCompatActivity() {

    private lateinit var binding: ActivityRegistreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registre)

        binding.btnRegistre.setOnClickListener { R.id.menuPrincipalFragment }


    }

}