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
import com.google.firebase.firestore.QuerySnapshot
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
            modificarUsuari()
            view.findNavController().navigate(R.id.action_modificarUsuari_to_menuPrincipalFragment)

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
            //cridem a la funcio per esborrar usuari
            esborrarUsuari()
            view.findNavController().navigate(R.id.action_modificarUsuari_to_authActivity)
        }

        return binding.root
    }

    //funcio per esborrar usuari
    fun esborrarUsuari() {
        //guardem les dades de l'usari identificat
        val user = Firebase.auth.currentUser
        //agafem el mail com a identificador unic de l'usuari
        val mail = user?.email.toString()
        Log.d("usari1 esborrar", usuari.toString())
        val usuaris = db.collection("usuaris")
        var usuariID = usuaris.document().get()
        Log.d("usari id", usuariID.toString())
        val query = usuaris.whereEqualTo("mail", mail).get().addOnSuccessListener { document ->
            Log.d("usari2 esborrar", usuari.toString())
            usuari = document.toObjects(Usuari::class.java)
            Log.d("usari3 esborrar", usuari.toString())
            db.collection("usuaris").document(usuari.toString())
                .delete()
                .addOnSuccessListener { Log.d("error", "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w("error", "Error deleting document", e) }

        }
        //Neteja dels camps tema i descripció
        /* binding.apply {
             editTextNomModificar.text.clear()
             editTextAdrecaModificar.text.clear()
             editTextCognomsModificar.text.clear()
             editTextContrasenyaModificar.text.clear()
             editTextPoblacioModificar.text.clear()
             editTextTelefonModificar.text.clear()
             editTextTextEmailAddressModificar.text.clear()
         }*/

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
        //guardem les dades de l'usari identificat
        val user = Firebase.auth.currentUser
        //agafem el mail com a identificador unic de l'usuari
        val mail = user?.email.toString()
        val usuaris = db.collection("usuaris")
        val query =
            usuaris.whereEqualTo("mail", mail).get().addOnSuccessListener { document ->
                if (document != null) {
                    usuari = document.toObjects(Usuari::class.java)
                  /*  val usuari = hashMapOf(
                        "nom" to binding.editTextNomModificar.text.toString(),
                        "cognoms" to binding.editTextCognomsModificar.text.toString(),
                        "mail" to binding.editTextTextEmailAddressModificar.text.toString(),
                        // "nickname" to nickname,
                        "adreca" to binding.editTextAdrecaModificar.text.toString(),
                        "poblacio" to binding.editTextPoblacioModificar.text.toString(),
                        "telefon" to binding.editTextTelefonModificar.text.toString(),
                        "contrasenya" to binding.editTextContrasenyaModificar.text.toString()
                    )*/
                    val mailModificat = binding.editTextTextEmailAddressModificar.text.toString()
                    Log.d("nom hash", usuari.toString())
                    val actualitza = db.collection("usuaris").addSnapshotListener { snapshot, e ->

                        val doc = snapshot?.documents

                        doc?.forEach {
                            val usuariConsulta = it.toObject(Usuari::class.java)
                            if (usuariConsulta?.mail == mail) {
                                val usuariId = it.id
                                Log.d("id usuari", usuariId)
                                val sfDocRef = db.collection("usuaris").document(usuariId )
                                db.runTransaction { transaction ->
                                    val snapshot = transaction.get(sfDocRef)

                                    // Note: this could be done without a transaction
                                    //       by updating the population using FieldValue.increment()
                                    val newUsuari = snapshot.getString("mail")!!
                                    Log.d("nou usuari", mailModificat)
                                    transaction.update(sfDocRef, "mail", mailModificat)


                                    // Success
                                    null
                                }.addOnSuccessListener { Log.d("TAG", "Transaction success!") }
                                    .addOnFailureListener { e -> Log.w("TAG2", "Transaction failure.", e) }

                               /* db.collection(usuariId).document().update("mail",mail)
                                    ?.addOnSuccessListener { document ->
                                        Log.d("usuari db ", usuari.toString())
                                        view?.let {
                                            Snackbar.make(
                                                it,
                                                "Registre creat correctament",
                                                Snackbar.LENGTH_LONG
                                            ).show()
                                        }
                                    }?.addOnFailureListener { e ->
                                        view?.let {
                                            Snackbar.make(
                                                it,
                                                "Error al crear el registre",
                                                Snackbar.LENGTH_LONG
                                            ).show()
                                        }

                                    }*/

                            }


                        }


                    }


                }


            }

    }


    //}


    //data class per les dades d l'usuari
    data class Usuari(
        var nom: String = "", var cognoms: String = "", var mail: String = "",
        var adreca: String = "", var poblacio: String = "", var telefon: String = "",
        var nickname: String = "", var contrasenya: String = ""
    )


}



