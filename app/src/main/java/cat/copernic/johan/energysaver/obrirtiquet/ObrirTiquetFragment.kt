package cat.copernic.johan.energysaver.obrirtiquet

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentObrirBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class ObrirTiquetFragment : Fragment() {

    private lateinit var binding: FragmentObrirBinding
    val db = FirebaseFirestore.getInstance()
    var titol: String = ""
    var descripcio: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_obrir, container, false)

        //Botó confirmar que truca a la funció per inserir dades al firestore
        binding.bttnConfirmarTiquet.setOnClickListener {
            rebreDades(it)
        }

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    fun rebreDades(view: View) {

        //Agafar dades del editText titol i descripció
        binding.apply {
            titol = editTextTemaTiquet.text.toString()
            descripcio = editTxtDescripcioTiquet.text.toString()
        }
        //Comprova que els camps esitguin emplenats
        if (titol.isEmpty() || descripcio.isEmpty()) {
            Log.w("ObrirTiquetFragment", "Entra fun rebre dades")
            Snackbar.make(view, R.string.campsBuitsToastObrirTiquet, Snackbar.LENGTH_LONG).show()
        } else {

            //Guarda les dades del usuari connectat a la constant user
            val user = Firebase.auth.currentUser

            //Guarda el mail del usuari que ha fet login
            val mail = user?.email.toString()

            //Consulta per extreure el nickname per guardar-lo al document tiquet
            /*
            val usuaris = db.collection("usuaris")
            val query = usuaris.whereEqualTo("mail", mail).get()
                .addOnSuccessListener {
                        document ->
                    if (document != null){
                      // document.getString("nickname")
                    }

                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }

           Log.i("consulta",query.toString())

             */

            //Extreu la data i hora del sistema per guardar al document tiquet
            val data = Calendar.getInstance().time
            val formatterdt = SimpleDateFormat("yyyy.MM.dd")
            val formatterhr = SimpleDateFormat("HH:mm:ss")
            val formatedDate = formatterdt.format(data)
            val formatedHour = formatterhr.format(data)

            //Map per fer l'insert
            val tiquet = hashMapOf(
                "mail" to mail,
                "nickname" to " ",
                "data" to formatedDate,
                "hora" to formatedHour,
                "titol" to titol,
                "descripcio" to descripcio
            )

            //Neteja dels camps tema i descripció
            binding.apply {
                editTextTemaTiquet.text.clear()
                editTxtDescripcioTiquet.text.clear()
            }

            //Incerció a la col·lecció tiquet
            db.collection("tiquet")
                .add(tiquet)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }
}