package cat.copernic.johan.energysaver.registre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import cat.copernic.johan.energysaver.MainActivity
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.ActivityRegistreBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase






class Registre : AppCompatActivity() {
    private lateinit var binding: ActivityRegistreBinding

    val db = FirebaseFirestore.getInstance()

    var nom: String =""
    var cognoms: String =""
    var mail: String = ""
    var nickname : String =""
    var adreca: String = ""
    var telefon: String = ""
    var contrasenya: String = ""


    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registre)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_registre)

        binding.btnConfirmarRegistre.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }
    }

    fun recollirUsuari(view:View){
        binding.apply {
            nom = editTextNom.text.toString()
            cognoms = editTextCognoms.text.toString()
            mail = editTextTextEmailAddress.text.toString()
            nickname = editTextNickName.text.toString()
            adreca = editTextAdreca.text.toString()
            telefon = editTextTelefon.text.toString()
            contrasenya = editTextContrasenyaRegistre.text.toString()
        }
    }

}