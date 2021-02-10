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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class ObrirTiquetFragment : Fragment() {

    private lateinit var binding: FragmentObrirBinding
    val db = Firebase.firestore
    var titol: String = ""
    var descripcio: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_obrir, container, false)

        binding.bttnConfirmarTiquet.setOnClickListener {
            rebreDades(it)
        }

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    fun rebreDades(view: View) {
        binding.apply {
            titol = editTextTemaTiquet.text.toString()
            descripcio = editTxtDescripcioTiquet.text.toString()
        }

        if (titol.isEmpty() || descripcio.isEmpty()) {
            Log.w("ObrirTiquetFragment", "Entra fun rebre dades")
            Snackbar.make(view, R.string.campsBuitsToastObrirTiquet, Snackbar.LENGTH_LONG).show()
        } else {

            val user = Firebase.auth.currentUser
            Log.i("usuari", user.toString())

            val usuaris = db.collection("usuaris")
            val query = usuaris.whereEqualTo("nickname", user?.uid).get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.d(TAG, "${document.id} => ${document.data}")
                    }

                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }

           // val nickname = query.result.toString()

            val data = Calendar.getInstance().time
            val formatterdt = SimpleDateFormat("yyyy.MM.dd")
            val formatterhr = SimpleDateFormat("HH:mm:ss")
            val formatedDate = formatterdt.format(data)
            val formatedHour = formatterhr.format(data)

            val tiquet = hashMapOf(
                "usuari" to "prova",
                "data" to formatedDate,
                "hora" to formatedHour,
                "titol" to titol,
                "descripcio" to descripcio
            )

            binding.apply {
                editTextTemaTiquet.text.clear()
                editTxtDescripcioTiquet.text.clear()
            }
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