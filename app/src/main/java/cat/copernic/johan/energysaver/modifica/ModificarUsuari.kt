package cat.copernic.johan.energysaver.modifica

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.autentificacio.AuthActivity
import cat.copernic.johan.energysaver.databinding.FragmentModificarUsuariBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.oAuthCredential
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class ModificarUsuari : Fragment() {



    var nom: String = ""
    var cognoms: String =""
    var mail: String = ""
    var nickname : String =""
    var adreca: String = ""
    var poblacio: String =""
    var telefon: String = ""
    var contrasenya: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding: FragmentModificarUsuariBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_modificar_usuari,container, false)

        binding.btnConfirmarModificar.setOnClickListener {
            view:View ->
            view.findNavController().navigate(R.id.action_modificarUsuari_to_menuPrincipalFragment)
        }

        binding.btnEnergiesModificar.setOnClickListener {
            view:View ->
            view.findNavController().navigate(R.id.action_modificarUsuari_to_seleccionarEnergiaFragment)
        }

        binding.btnTancarModificar.setOnClickListener {
            view:View ->
            view.findNavController().navigate(R.id.action_modificarUsuari_to_authActivity)
        }
        binding.btnBaixaModificar.setOnClickListener {
            view:View->
            view.findNavController().navigate(R.id.action_modificarUsuari_to_authActivity)
        }
        //funcio per recuperar les dades a modificar del usuari identificat
        fun dadesModificar() {
            var mail: String =""



        }


                return binding.root
    }


}


