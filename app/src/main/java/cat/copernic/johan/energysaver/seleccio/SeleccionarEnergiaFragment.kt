package cat.copernic.johan.energysaver.seleccio

import android.content.ContentValues
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentSeleccionarEnergiaBinding
import cat.copernic.johan.energysaver.obrirtiquet.Usuari
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SeleccionarEnergiaFragment() : Fragment(), AdapterView.OnItemSelectedListener{
    private val db = FirebaseFirestore.getInstance()

    private var periodeAigua = 0
    private var periodeLlum = 0
    private var periodeGas = 0
    private var periodeGasoil = 0

    private lateinit var binding : FragmentSeleccionarEnergiaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<FragmentSeleccionarEnergiaBinding>(inflater,
            R.layout.fragment_seleccionar_energia,
            container, false)
        omplirDades()
        val checkBoxAigua : CheckBox = binding.chbxAigua
        val checkBoxLlum : CheckBox = binding.chbxLlum
        val checkBoxGas : CheckBox = binding.chbxGas
        val checkBoxGasoil : CheckBox = binding.chbxGasoil

        val spinnerAigua : Spinner = binding.spnAigua
        val spinnerLlum : Spinner = binding.spnLlum
        val spinnerGas : Spinner = binding.spnGas
        val spinnerCombustible : Spinner = binding.spnCombustible

        spinnerAigua.setEnabled(checkBoxAigua.isChecked)
        spinnerLlum.setEnabled(checkBoxLlum.isChecked)
        spinnerGas.setEnabled(checkBoxGas.isChecked)
        spinnerCombustible.setEnabled(checkBoxGasoil.isChecked)

        checkBoxAigua.setOnCheckedChangeListener { view, isChecked ->
            spinnerAigua.setEnabled(!spinnerAigua.isEnabled)
        }
        checkBoxLlum.setOnCheckedChangeListener { view, isChecked ->
            spinnerLlum.setEnabled(!spinnerLlum.isEnabled)
        }
        checkBoxGas.setOnCheckedChangeListener { view, isChecked ->
            spinnerGas.setEnabled(!spinnerGas.isEnabled)
        }
        checkBoxGasoil.setOnCheckedChangeListener { view, isChecked ->
            spinnerCombustible.setEnabled(!spinnerCombustible.isEnabled)
        }

        spinnerAigua.onItemSelectedListener = this
        spinnerLlum.onItemSelectedListener = this
        spinnerGas.onItemSelectedListener = this
        spinnerCombustible.onItemSelectedListener = this

        this.context?.let {
            ArrayAdapter.createFromResource(it, R.array.periode_array, android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerAigua.adapter = adapter
                spinnerLlum.adapter = adapter
                spinnerGas.adapter = adapter
                spinnerCombustible.adapter = adapter
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
        //Guarda les dades del usuari connectat a la constant user
        val user = Firebase.auth.currentUser

        //Guarda el mail del usuari que ha fet login
        val mail = user?.email.toString()

        val energies_usuari = hashMapOf(
            "mail_usuari" to mail ,
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

    private fun omplirDades(){
        Log.i("Proba", "Proba1")
        var aigua = false
        var llum = false
        var gas = false
        var gasoil = false

        Log.i("Proba", "Proba2")
        val user = Firebase.auth.currentUser
        val mail = user?.email.toString()
        Log.i("proba", mail)
        val energies = db.collection("energies")
        val query = energies.whereEqualTo("mail_usuari", mail).get()
            .addOnSuccessListener { document ->
                if (!document.isEmpty) {
                    val energia = document.toObjects(Energies::class.java)
                    Log.i("Proba", energia[0].toString())
                    val aiguaDoc = energia[0].aigua
                    var aigua = aiguaDoc
                    llum = energia!![0].llum
                    gas = energia!![0].gas
                    gasoil = energia!![0].gasoil

                    periodeAigua = energia!![0].periode_aigua
                    periodeLlum = energia!![0].periode_llum
                    periodeGas = energia!![0].periode_gas
                    periodeGasoil = energia!![0].periode_gasoil

                    binding.chbxAigua.isChecked = aigua
                    binding.chbxLlum.isChecked = llum
                    binding.chbxGas.isChecked = gas
                    binding.chbxGasoil.isChecked = gasoil
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }

        Log.i("Proba", "Proba3")

        Log.i("Proba", "Proba4")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            Log.i("Ayuda: ", parent.getItemAtPosition(position).toString())
        }
        if(parent.toString().contains("Aigua")){
            periodeAigua = position
        }
        if(parent.toString().contains("Llum")){
            periodeLlum = position
        }
        if(parent.toString().contains("Gas")){
            periodeGas = position
        }
        if(parent.toString().contains("Combustible")){
            periodeGasoil = position
        }
    }
}

data class Energies(
    var aigua: Boolean = false, var gas: Boolean = false,var gasoil: Boolean = false, var llum: Boolean = false,
    var mail_usuari: String = "", var periode_aigua: Int = 0, var periode_gas: Int = 0,
    var periode_gasoil: Int = 0, var periode_llum: Int = 0
)