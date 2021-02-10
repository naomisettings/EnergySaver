package cat.copernic.johan.energysaver.seleccio

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentSeleccionarEnergiaBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SeleccionarEnergiaFragment : Fragment(), AdapterView.OnItemSelectedListener{
    private val db = Firebase.firestore

    private var periodeAigua = ""
    private var periodeLlum = ""
    private var periodeGas = ""
    private var periodeGasoil = ""

    private lateinit var binding : FragmentSeleccionarEnergiaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<FragmentSeleccionarEnergiaBinding>(inflater,
            R.layout.fragment_seleccionar_energia,
            container, false)
        val spinnerAigua : Spinner = binding.spnAigua
        val spinnerLlum : Spinner = binding.spnLlum
        val spinnerGas : Spinner = binding.spnGas
        val spinnerGasoil : Spinner = binding.spnGasoil

        Log.i("Ayuda: ", spinnerAigua.toString())

        this.context?.let {
            ArrayAdapter.createFromResource(it, R.array.periode_array, android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerAigua.adapter = adapter
                spinnerLlum.adapter = adapter
                spinnerGas.adapter = adapter
                spinnerGasoil.adapter = adapter
            }
        }

        binding.btnConfirmar.setOnClickListener { view: View ->
            rebreDades()
            view.findNavController()
                .navigate(R.id.action_seleccionarEnergiaFragment_to_menuPrincipalFragment)
        }
        return binding.root
    }

    fun rebreDades(){
        val energies_usuari = hashMapOf(
            "usuari" to "pedro" ,
            "llum" to binding.chbxLlum.isChecked,
            "periode_llum" to periodeLlum,
            "aigua" to binding.chbxAigua.isChecked,
            "periode_aigua" to periodeAigua,
            "gas" to binding.chbxGas.isChecked,
            "periode_gas" to periodeGas,
            "gasoil" to binding.chbxGasoil.isChecked,
            "periode_gasoil" to periodeGasoil,
        )

        db.collection("energies")
            .add(energies_usuari)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.i("Ayuda: ", parent.toString())
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.i("Ayuda: ", parent.toString())
    }
}