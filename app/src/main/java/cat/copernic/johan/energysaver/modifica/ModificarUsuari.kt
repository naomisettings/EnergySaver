package cat.copernic.johan.energysaver.modifica

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.autentificacio.AuthActivity
import cat.copernic.johan.energysaver.databinding.FragmentModificarUsuariBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.oAuthCredential
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase


class ModificarUsuari : Fragment() {
    private lateinit var binding: FragmentModificarUsuariBinding
    var usuari: MutableList<Usuari>? = null

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_modificar_usuari, container, false)
        recollirDadesModificar()
        binding.btnConfirmarModificar.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_modificarUsuari_to_menuPrincipalFragment)
            modificarUsuari()
        }

        binding.btnEnergiesModificar.setOnClickListener {

                view: View ->
            view.findNavController()
                .navigate(R.id.action_modificarUsuari_to_seleccionarEnergiaFragment)

        }

        binding.btnTancarModificar.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_modificarUsuari_to_authActivity)
        }
        binding.btnBaixaModificar.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_modificarUsuari_to_authActivity)
        }

        return binding.root
    }

    //funcio per recuperar les dades a modificar del usuari identificat
    fun recollirDadesModificar() {
        //guardem les dades de l'usari identificat
        val user = Firebase.auth.currentUser
        //agafem el mail com a identificador unic de l'usuari
        val mail = user?.email.toString()
        val usuaris = db.collection("usuaris")
        val query = usuaris.whereEqualTo("mail", mail).get().addOnSuccessListener { document ->
            if (document != null) {
                usuari = document.toObjects(Usuari::class.java)
                binding.editTextNomModificar.setText(usuari!![0].nom)
                binding.editTextCognomsModificar.setText(usuari!![0].cognoms)
                binding.editTextAdrecaModificar.setText(usuari!![0].adreca)
                binding.editTextPoblacioModificar.setText(usuari!![0].poblacio)
                binding.editTextTelefonModificar.setText(usuari!![0].telefon)
                binding.editTextTextEmailAddressModificar.setText((usuari!![0].mail))
                binding.editTextContrasenyaModificar.setText(usuari!![0].contrasenya)


            } else {
                Snackbar.make(
                    requireView(),
                    "Error al carregar las teves dades",
                    Snackbar.LENGTH_LONG
                ).show()

            }
        }
    }

    //funcio per guardar les dades del modificades
    fun modificarUsuari() {
        binding.apply {
            nom = editTextNomModificar.text.toString()
            cognoms = editTextCognomsModificar.text.toString()
            mail = editTextTextEmailAddressModificar.text.toString()
            adreca = editTextAdrecaModificar.text.toString()
            poblacio = editTextPoblacioModificar.text.toString()
            telefon = editTextTelefonModificar.text.toString()
            contrasenya = editTextContrasenyaModificar.text.toString()
        }
        //validar camps
        /*  if(nom.isEmpty()||cognoms.isEmpty()|| mail.isEmpty() || nickname.isEmpty() || adreca.isEmpty()
            || poblacio.isEmpty() || telefon.isEmpty()|| contrasenya.isEmpty()){
            Log.d("Dades recollides", nom)
            view?.let { Snackbar.make(it, "Has d'omplir tots els camps", Snackbar.LENGTH_LONG).show() }

        }else{ *///guardem a un hashMap
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
        Log.d("Dades hash", nom) //arriba be mirar com actualitzar el registre.
        //guardem el hashMap a un colleccio del Firebase
        db.collection("usuaris").add(usuari).addOnSuccessListener { documentReference ->
            view?.let {
                Snackbar.make(it, "Registre creat correctament", Snackbar.LENGTH_LONG).show()
            }
        }.addOnFailureListener { e ->
            view?.let {
                Snackbar.make(it, "Error al crear el registre", Snackbar.LENGTH_LONG).show()
            }

        }

    }
//}

    //data class per les dades d l'usuari
    data class Usuari( var nom: String = "", var cognoms:String ="", var mail:String ="",
                       var adreca:String ="", var poblacio:String ="", var telefon:String ="",
                       var nickname:String ="", var contrasenya:String ="")


}


