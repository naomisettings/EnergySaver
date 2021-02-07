package cat.copernic.johan.energysaver.modifica

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentModificarUsuariBinding





class ModificarUsuari : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding: FragmentModificarUsuariBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_modificar_usuari,container, false)

        //seleccionar Confirmar energia porta al fragment de menu principal
        binding.btnConfirmarModificar.setOnClickListener {
            view: View ->
            view.findNavController().navigate(R.id.action_modificarUsuari_to_menuPrincipalFragment)
        }
        //seleccionar Modificar Energia porta al fragment de seleccionar energia
        binding.btnEnergiesModificar.setOnClickListener {
            view: View ->
            view.findNavController().navigate(R.id.action_modificarUsuari_to_seleccionarEnergiaFragment)

        }
        //seleccionar Baixa elimina usuari
        binding.btnBaixaModificar.setOnClickListener {
            //codi per eliminar usuari
        }

        //seleccionar Tancar tanca sessio
        binding.btnTancarModificar.setOnClickListener {
            //codi per tancar sessio
        }

        return binding.root
    }


}