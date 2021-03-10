package cat.copernic.johan.energysaver.principal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentMenuPrincipalBinding
import com.google.android.material.transition.MaterialSharedAxis


class MenuPrincipalFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentMenuPrincipalBinding>(inflater,
            R.layout.fragment_menu_principal,
            container, false)

        //TRANSACTION
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 0.01.toLong()
        }

        binding.imgbModificar.setOnClickListener { view:View ->
        view.findNavController().navigate(R.id.action_menuPrincipalFragment_to_modificarUsuari)
        }
        binding.imgbGoInformes.setOnClickListener { view:View ->
            view.findNavController().navigate(R.id.action_menuPrincipalFragment_to_informesFragment)
        }
        binding.imgbMedalles.setOnClickListener { view:View ->
            view.findNavController().navigate(R.id.action_menuPrincipalFragment_to_medallesFragment)
        }
        binding.imgBttnSuport.setOnClickListener { view:View ->
            view.findNavController().navigate(R.id.action_menuPrincipalFragment_to_veureFragment)
        }
        binding.imgBtnConsums.setOnClickListener{ view:View ->
            view.findNavController().navigate(R.id.action_menuPrincipalFragment_to_menuEnergies)
        }
        binding.imgBtnEnergies.setOnClickListener { view:View ->
            view.findNavController().navigate(R.id.action_menuPrincipalFragment_to_seleccionarEnergiaFragment)
        }


        return binding.root
    }
}