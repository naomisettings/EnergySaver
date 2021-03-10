package cat.copernic.johan.energysaver.introduirconsums

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentMenuEnergiesBinding
import com.google.android.material.transition.MaterialSharedAxis
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class MenuEnergies : Fragment() {

    private lateinit var binding: FragmentMenuEnergiesBinding

    //instancia a firebase
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentMenuEnergiesBinding>(
            inflater,
            R.layout.fragment_menu_energies,
            container, false
        )
        //TRANSACTION
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 0.01.toLong()
        }

        comprovarEnergies()
        binding.imgbAigua.setOnClickListener { view: View ->
             view.findNavController().navigate(R.id.action_menuEnergies_to_consumAigua)
            }
        binding.imgbLlum.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_menuEnergies_to_consumLlum)
        }
        binding.imgbGas.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_menuEnergies_to_consumGas)
        }
        binding.imgbGasoil.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_menuEnergies_to_consumGasoil)
        }

        return binding.root
    }

    //funcio per habilitar nom de les energies seleccionades al menu entrar consum energies
    fun comprovarEnergies() {

        //guardem les dades de l'usari identificat
        val user = Firebase.auth.currentUser
        //agafem el mail com a identificador unic de l'usuari
        val mail_usuari = user?.email.toString()
        Log.d("mail", mail_usuari)
        val energiesContractades = db.collection("energies")
        val query =
            energiesContractades.whereEqualTo("mail_usuari", mail_usuari).get().addOnSuccessListener { document ->

                if (document != null) {
                    val energia = document.toObjects(Contractades::class.java)
                    Log.d("energies contractades", energia.toString())
                   if (energia[0].aigua == false) {
                        binding.imgbAiguaDisable.visibility = View.VISIBLE
                        binding.imgbAiguaDisable.isClickable = false

                       binding.imgbAigua.visibility = View.INVISIBLE
                    }else{
                       binding.imgbAiguaDisable.visibility = View.INVISIBLE
                       binding.imgbAiguaDisable.alpha = 0.5f
                       binding.imgbAigua.visibility = View.VISIBLE

                   }
                    if(energia[0].gas == false){
                        binding.imgbGasDisable.visibility = View.VISIBLE
                        binding.imgbGasDisable.isClickable = false

                        binding.imgbGas.visibility = View.INVISIBLE
                    }else{
                        binding.imgbGasDisable.visibility = View.INVISIBLE
                        binding.imgbGasDisable.alpha = 0.5f
                        binding.imgbGas.visibility = View.VISIBLE

                    }
                    if(energia[0].llum == false){
                        binding.imgbLlumDisable.visibility = View.VISIBLE
                        binding.imgbLlumDisable.isClickable = false
                        binding.imgbLlum.visibility = View.INVISIBLE
                    }else{
                        binding.imgbLlumDisable.visibility = View.INVISIBLE
                        binding.imgbLlumDisable.alpha = 0.5f
                        binding.imgbLlum.visibility = View.VISIBLE

                    }

                    if(energia[0].gasoil == false){
                        binding.imgGasoilDisable.visibility = View.VISIBLE
                        binding.imgGasoilDisable.isClickable = false
                        binding.imgbGasoil.visibility = View.INVISIBLE
                    }else{
                        binding.imgGasoilDisable.visibility = View.INVISIBLE
                        binding.imgbGasoil.visibility = View.VISIBLE

                    }

                }

            }
    }
}
//Classe que correspon als camps de la col·lecció energies
data class Contractades(
    var aigua: Boolean = false,
    var gas: Boolean = false,
    var llum: Boolean = false,
    var gasoil: Boolean = false,
    var mail_usuari: String ="",
    var periode_aigua: Int = 0,
    var periode_gas:Int = 0,
    var periode_gasoil: Int = 0,
    var periode_llum:Int = 0

)