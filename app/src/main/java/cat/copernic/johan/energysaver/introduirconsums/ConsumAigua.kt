package cat.copernic.johan.energysaver.introduirconsums

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentConsumAiguaBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class ConsumAigua : Fragment() {
    private lateinit var binding: FragmentConsumAiguaBinding
    private lateinit var auth: FirebaseAuth
    //instancia a firebase
    val db = FirebaseFirestore.getInstance()
    var consumAigua: String = ""
    var importAigua: String = ""
    var dataAigua: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_consum_aigua, container, false)

        binding.btnConfirmarConsumAigua.setOnClickListener { view:View ->
            //retorn a menu energies i guardar dades
            guardarConsum()
            view.findNavController().navigate(R.id.action_consumAigua_to_menuEnergies)
        }

        return binding.root
    }

    fun guardarConsum(){
        //guardem les dades de l'usari identificat
        val user = Firebase.auth.currentUser
        //agafem el mail com a identificador unic de l'usuari
        val mail = user?.email.toString()
        binding.apply {
            consumAigua = editTextConsumAigua.text.toString()
            importAigua = editTextImportAigua.text.toString()
            dataAigua = editTextDataAigua.text.toString()
        }

        //validar camps
        if(consumAigua.isEmpty() || importAigua.isEmpty() || dataAigua.isEmpty()){
            view?.let { Snackbar.make(it, "Has d'omplir tots els camps", Snackbar.LENGTH_LONG).show() }

        }else{
            val despesaConsum = hashMapOf(
                "aiguaConsum" to consumAigua,
                "aiguaDiners" to importAigua,
                "aiguaConsum" to dataAigua,
                "aiguaDiners" to dataAigua,



            )
            db.collection("despesaConsum").add(despesaConsum).addOnSuccessListener { documentReference ->
                view?.let { Snackbar.make(it, "Registre creat correctament", Snackbar.LENGTH_LONG).show() }
            }.addOnFailureListener{ e->
                view?.let { Snackbar.make(it, "Error al crear el registre", Snackbar.LENGTH_LONG).show() }

            }
        }

    }


}