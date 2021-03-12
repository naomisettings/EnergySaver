package cat.copernic.johan.energysaver.modifica

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentModificarUsuariBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import androidx.appcompat.app.AlertDialog
import com.google.android.material.transition.MaterialSharedAxis


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

        //TRANSACTION
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 0.01.toLong()
        }

        binding.btnConfirmarModificar.setOnClickListener { view: View ->
            modificarUsuari()
            view.findNavController().navigate(R.id.action_modificarUsuari_to_menuPrincipalFragment)

        }
        binding.btnEnergiesModificar.setOnClickListener {view: View ->
            view.findNavController()
                .navigate(R.id.action_modificarUsuari_to_seleccionarEnergiaFragment)

        }
        binding.btnTancarModificar.setOnClickListener { view: View ->
            logOut()
            view.findNavController().navigate(R.id.action_modificarUsuari_to_authActivity)
        }
        binding.btnBaixaModificar.setOnClickListener { view: View ->
           //alerta de confirmacio per esborrar usuari
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setTitle("Vols esborrar el teu usuari?")
                builder.setPositiveButton("Acceptar",
                    DialogInterface.OnClickListener { dialog, id ->
                        //si l'usuari accepta esborrar cridem a la funcio d'esborrar usuari
                        esborrarUsuari()
                        view.findNavController().navigate(R.id.action_modificarUsuari_to_authActivity)

                    })
                builder.setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->
                        //si l'usuari cancela l'accio tornem al menu principal
                        view.findNavController().navigate(R.id.menuPrincipalFragment)
                    })
                val dialog: AlertDialog = builder.create()
                dialog.show()
                // Create the AlertDialog
                builder.create()
            }

        }

        return binding.root
    }
    //funcio per tancar sessio
    fun logOut(){
        //guardem les dades de l'usari identificat
        val user = Firebase.auth.currentUser
        FirebaseAuth.getInstance().signOut()

        view?.let {
            Snackbar.make(
                it,
                "Has tancat sessió",
                Snackbar.LENGTH_LONG
            ).show()
        }


    }

    //funcio per esborrar usuari
    fun esborrarUsuari() {

        //guardem les dades de l'usari identificat
        val user = Firebase.auth.currentUser
        //agafem el mail com a identificador unic de l'usuari
        val mail = user?.email.toString()
        val db = FirebaseFirestore.getInstance()

        //actualitza per establir un camp de col·lecció d 'usuaris
        val actualitza = db.collection("usuaris").addSnapshotListener { snapshot, e ->
            //guardem els documents dels usuaris
            val doc = snapshot?.documents

            //iterem pels documents dels usuaris
            doc?.forEach {
                //guardem els usuaris que hem trovat a l'objecte Usuari (Data Class)
                val usuariConsulta = it.toObject(Usuari::class.java)
                //si el mail de l'usuari identificat coincideix amb un dels guardarts
                if (usuariConsulta?.mail == mail) {
                    //guardem el id del document d'usuari identificat
                    val usuariId = it.id
                    Log.d("id document usuari", usuariId)
                    //agafem l'usuari de la collecio amb el seu ID
                    val sfDocRef = db.collection("usuaris").document(usuariId)


                    //Actualitzem
                    db.runTransaction { transaction ->
                        //agafem el ID
                        val snapshot = transaction.get(sfDocRef)
                        //actualitzem el id amb el mail del usuari identificat i guardem els camps
                        val newUsuari = snapshot.getString("mail")!!
                        Log.d("nou usuari", newUsuari)
                        //esborrem usuari del Firestore
                        transaction.delete(sfDocRef)
                        //esborrem usuari del Authentification
                        user?.delete()

                    }
                        .addOnSuccessListener {
                            Log.d(
                                "error",
                                "DocumentSnapshot successfully deleted!"
                            )
                        }
                        .addOnFailureListener { e -> Log.w("error", "Error deleting document", e) }


                }
            }
        }

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
                binding.editTextNicknameModificar.setText((usuari!![0].nickname))
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
        val db = FirebaseFirestore.getInstance()


        //actualitza per establir un camp de col·lecció d 'usuaris
        val actualitza = db.collection("usuaris").addSnapshotListener { snapshot, e ->
            //guardem els documents dels usuaris
            val doc = snapshot?.documents

            //iterem pels documents dels usuaris
            doc?.forEach {
                //guardem els usuaris que hem trovat a l'objecte Usuari (Data Class)
                val usuariConsulta = it.toObject(Usuari::class.java)
                //si el mail de l'usuari identificat coincideix amb un dels guardarts
                if (usuariConsulta?.mail == mail) {
                    //guardem el id del document d'usuari identificat
                    val usuariId = it.id
                    Log.d("id document usuari", usuariId)
                    //agafem l'usuari de la collecio amb el seu ID
                    val sfDocRef = db.collection("usuaris").document(usuariId)

                    //Actualitzem
                    db.runTransaction { transaction ->
                        //agafem el ID
                        val snapshot = transaction.get(sfDocRef)
                        //actualitzem el id amb el mail del usuari identificat i guardem els camps
                        val newUsuari = snapshot.getString("mail")!!
                        Log.d("nou usuari", newUsuari)
                        transaction.update(
                            sfDocRef,
                            "nom",
                            binding.editTextNomModificar.text.toString()
                        )
                        transaction.update(
                            sfDocRef,
                            "cognoms",
                            binding.editTextCognomsModificar.text.toString()
                        )
                        transaction.update(
                            sfDocRef,
                            "nickname",
                            binding.editTextNicknameModificar.text.toString()
                        )
                        transaction.update(
                            sfDocRef,
                            "adreca",
                            binding.editTextAdrecaModificar.text.toString()
                        )
                        transaction.update(
                            sfDocRef,
                            "poblacio",
                            binding.editTextPoblacioModificar.text.toString()
                        )
                        transaction.update(
                            sfDocRef,
                            "telefon",
                            binding.editTextTelefonModificar.text.toString()
                        )
                        transaction.update(
                            sfDocRef,
                            "contrasenya",
                            binding.editTextContrasenyaModificar.text.toString()
                        )



                        null
                    }.addOnSuccessListener {Log.d("TAG", "Transaction success!")
                        view?.let {
                            Snackbar.make(
                                it,
                                "Registre creat correctament",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }}
                        .addOnFailureListener {e -> Log.w("TAG2", "Transaction failure.", e)
                            view?.let {
                                Snackbar.make(it, "Error al crear el registre", Snackbar.LENGTH_LONG).show()
                            }}
                }

            }
        }



    }


    //data class per les dades d l'usuari
    data class Usuari(
        var nom: String = "", var cognoms: String = "", var mail: String = "",
        var adreca: String = "", var poblacio: String = "", var telefon: String = "",
        var nickname: String = "", var contrasenya: String = "", var admin: Boolean = false
    )


}






