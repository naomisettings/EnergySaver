package cat.copernic.johan.energysaver.obrirtiquet

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentObrirBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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

    fun rebreDades(view: View) {
        binding.apply {
            titol = editTextTemaTiquet.text.toString()
            descripcio = editTxtDescripcioTiquet.text.toString()
        }

        if (titol.isEmpty() || descripcio.isEmpty()) {
            Log.w("ObrirTiquetFragment", "Entra fun rebre dades")
            Snackbar.make(view, R.string.campsBuitsToastObrirTiquet, Snackbar.LENGTH_LONG).show()
        } else {
            print("firestore")

        }

    }
}