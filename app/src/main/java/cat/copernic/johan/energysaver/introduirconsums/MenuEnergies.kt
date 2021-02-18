package cat.copernic.johan.energysaver.introduirconsums

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentMenuEnergiesBinding



class MenuEnergies : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentMenuEnergiesBinding>(inflater,
            R.layout.fragment_menu_energies,
            container, false)
        binding.imgbAigua .setOnClickListener { view:View ->
            view.findNavController().navigate(R.id.action_menuEnergies_to_consumAigua)
        }
        binding.imgbLlum.setOnClickListener { view:View ->
            view.findNavController().navigate(R.id.action_menuEnergies_to_consumLlum)
        }
        binding.imgbGas.setOnClickListener { view:View ->
            view.findNavController().navigate(R.id.action_menuEnergies_to_consumGas)
        }
        binding.imgbGasoil .setOnClickListener { view:View ->
            view.findNavController().navigate(R.id.action_menuEnergies_to_consumGasoil)
        }

        return binding.root
    }


}