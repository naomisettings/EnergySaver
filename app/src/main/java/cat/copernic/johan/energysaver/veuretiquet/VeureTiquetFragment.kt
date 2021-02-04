package cat.copernic.johan.energysaver.veuretiquet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentVeureBinding


class VeureTiquetFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentVeureBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_veure, container, false)

        binding.bttnNouTiquet.setOnClickListener {
                view : View ->
            view.findNavController().navigate(R.id.action_veureFragment_to_obrirFragment)
        }

        return binding.root
    }
}