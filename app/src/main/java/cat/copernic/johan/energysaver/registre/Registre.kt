package cat.copernic.johan.energysaver.registre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import cat.copernic.johan.energysaver.MainActivity
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.ActivityRegistreBinding
import com.google.android.material.snackbar.Snackbar
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
    var poblacio: String =""
    var telefon: String = ""
    var contrasenya: String = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registre)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_registre)

        binding.btnConfirmarRegistre.setOnClickListener {
            recollirUsuari(it)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }
    }
    //funcio per recollir les dades del formulari de registre
    fun recollirUsuari(view:View){
        binding.apply {
            nom = editTextNom.text.toString()
            cognoms = editTextCognoms.text.toString()
            mail = editTextTextEmailAddress.text.toString()
            nickname = editTextNickName.text.toString()
            adreca = editTextAdreca.text.toString()
            poblacio = editTextPoblacio.text.toString()
            telefon = editTextTelefon.text.toString()
            contrasenya = editTextContrasenyaRegistre.text.toString()
        }
        //validar camps
        if(nom.isEmpty()||cognoms.isEmpty()|| mail.isEmpty() || nickname.isEmpty() || adreca.isEmpty()
            || poblacio.isEmpty() || telefon.isEmpty()|| contrasenya.isEmpty()){
            Snackbar.make(view, "Has d'omplir tots els camps", Snackbar.LENGTH_LONG).show()

        }else{ //guardem a un hashMap
            val usuari = hashMapOf(
                "nom" to nom,
                "cognoms" to cognoms,
                "mail" to mail,
                "nickname" to nickname,
                "adreca" to adreca,
                "poblacio" to poblacio,
                "telefon" to telefon,
                "contrasenya" to contrasenya

            )
            //netejem els camps
            binding.apply {
                editTextNom.text.clear()
                editTextCognoms.text.clear()
                editTextTextEmailAddress.text.clear()
                editTextNickName.text.clear()
                editTextAdreca.text.clear()
                editTextPoblacio.text.clear()
                editTextTelefon.text.clear()
                editTextContrasenyaRegistre.text.clear()

            }
            //guardem el hashMap a un colleccio dek Firebase
            db.collection("usuaris").add(usuari).addOnSuccessListener { documentReference ->
                Snackbar.make(view, "Registre creat correctament", Snackbar.LENGTH_LONG).show()
            }.addOnFailureListener{ e->
                Snackbar.make(view, "Error al crear el registre", Snackbar.LENGTH_LONG).show()

            }

        }
    }





}