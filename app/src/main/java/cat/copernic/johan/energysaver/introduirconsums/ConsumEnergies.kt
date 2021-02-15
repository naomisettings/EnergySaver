package cat.copernic.johan.energysaver.introduirconsums

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentConsumEnergiesBinding
import cat.copernic.johan.energysaver.modifica.ModificarUsuari
import cat.copernic.johan.energysaver.seleccio.Energies
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class ConsumEnergies : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentConsumEnergiesBinding



    //instancia a firebase
    val db = FirebaseFirestore.getInstance()

    var consumAigua = ""
    var consumLlum = ""
    var consumGas = ""
    var consumGasoil = ""
    var dataConsum = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //inicialitzem la variable auth
        auth = Firebase.auth
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_consum_energies, container, false)

        binding.btnConfirmarConsums.setOnClickListener{ view: View ->
            recollirConsums()
            view.findNavController().navigate(R.id.action_consumEnergies_to_menuPrincipalFragment)
        }

        return binding.root


    }


    fun recollirConsums(){
        //guardem les dades de l'usari identificat
        val user = Firebase.auth.currentUser
        //agafem el mail com a identificador unic de l'usuari
        val mail = user?.email.toString()
        val consums = db.collection("entrarEnergia")
        binding.apply {
            consumAigua = editTextConsumAigua.text.toString()
            consumLlum = editTextConsumLlum.text.toString()
            consumGas = editTextConsumGas.text.toString()
            consumGasoil = editTextConsumGasoil.text.toString()
            dataConsum = editTextDataConsum.text.toString()

        }
        val consumsEnergies = hashMapOf(
            "aiguaGastats" to consumAigua,
            "gasGastats" to consumGas,
            "gasoilGastats" to consumGasoil,
            "llumGastats" to consumLlum,
            "data" to dataConsum,
            "mail" to mail
        )
        db.collection("entrarEnergia").add(consumsEnergies).addOnSuccessListener { documnetReference ->
            view?.let { Snackbar.make(it, "Registre creat correctament", Snackbar.LENGTH_LONG).show() }
        }.addOnFailureListener{ e->
            view?.let { Snackbar.make(it, "Error al crear el registre", Snackbar.LENGTH_LONG).show() }

        }



    }

    data class Consums(
        var aiguaGastats: String = "", var gasGastats: String = "", var gasoilGastats: String = "",
        var llumGastats: String = "", var data: String = "", var mail: String = ""
    )
    }





