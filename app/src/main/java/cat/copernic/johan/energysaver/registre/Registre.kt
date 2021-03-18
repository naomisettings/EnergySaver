package cat.copernic.johan.energysaver.registre

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import cat.copernic.johan.energysaver.MainActivity
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.ActivityRegistreBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class Registre : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegistreBinding

    //instancia a firebase
    val db = FirebaseFirestore.getInstance()

    var nom: String = ""
    var cognoms: String = ""
    var mail: String = ""
    var nickname: String = ""
    var adreca: String = ""
    var poblacio: String = ""
    var telefon: String = ""
    var contrasenya: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registre)

        //inicialitzem la variable auth
        auth = Firebase.auth

        binding = DataBindingUtil.setContentView(this, R.layout.activity_registre)

        binding.btnConfirmarRegistre.setOnClickListener {
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
            if (nom.isEmpty() || cognoms.isEmpty() || mail.isEmpty() || nickname.isEmpty() || adreca.isEmpty()
                || poblacio.isEmpty() || telefon.isEmpty() || contrasenya.isEmpty()
            ) {
                Snackbar.make(it, "Has d'omplir tots els camps", Snackbar.LENGTH_LONG).show()

            }else {
                //guardar usuari al autentification
                createAccount(
                    binding.editTextTextEmailAddress.text.toString(),
                    binding.editTextContrasenyaRegistre.text.toString(), it
                )
            }

        }

    }

    //funcio per recollir les dades del formulari de registre
    fun recollirUsuari(view: View) {

            val usuari = hashMapOf(
                "nom" to nom,
                "cognoms" to cognoms,
                "mail" to mail,
                "nickname" to nickname,
                "adreca" to adreca,
                "poblacio" to poblacio,
                "telefon" to telefon,
                "contrasenya" to contrasenya,
                "admin" to false

            )

            //guardem el hashMap a un colleccio del Firebase
            db.collection("usuaris").add(usuari).addOnSuccessListener { documentReference ->
                // Snackbar.make(view, "Registre creat correctament", Snackbar.LENGTH_LONG).show()
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }.addOnFailureListener { e ->
                //Snackbar.make(view, "Error al crear el registre", Snackbar.LENGTH_LONG).show()
                Log.w(ContentValues.TAG, "Error adding document", e)

            }

      //  }
    }

    //funcio per crear el compte d'usuari al Authentification amb les dades del registre
    private fun createAccount(email: String, password: String, view: View) {
        if (validateFormat()) {
            recollirUsuari(view)
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //si accedeix correctament
                        val user = auth.currentUser
                        //guardem les dades de la pantalla registre

                        /*Snackbar.make(view, "Registre creat correctament", Snackbar.LENGTH_LONG)
                            .show()*/
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        //si falla la validacio
                        Snackbar.make(view, "Usuari incorrecte o ja existeix", Snackbar.LENGTH_LONG)
                            .show()
                    }
                }
        }
    }

    //metode que valida que els camps no estiguin buits
    private fun validateFormat(): Boolean {
        var valid = true
        val email: String = binding.editTextTextEmailAddress.text.toString()
        val mailPattern = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,18}")

        if (!mailPattern.matches(email)) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Error")
            builder.setMessage("Has de posar un correu electrònic vàlid")
            builder.setPositiveButton("Acceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()

            valid = false
        } else {
            binding.editTextTextEmailAddress.error = null
        }
        val password: String = binding.editTextContrasenyaRegistre.text.toString()
        if (TextUtils.isEmpty(password)) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Error")
            builder.setMessage("Has de posar una contrasenya vàlida")
            builder.setPositiveButton("Acceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()

            valid = false

        } else {
            binding.editTextContrasenyaRegistre.error = null
        }
        return valid
    }


}