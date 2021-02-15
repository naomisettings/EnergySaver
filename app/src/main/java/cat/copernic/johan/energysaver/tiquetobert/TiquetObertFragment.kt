package cat.copernic.johan.energysaver.tiquetobert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentTiquetObertBinding

class TiquetObertFragment : Fragment() {

    var titol: String = "prova"
    var descripcio: String = "prova2"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTiquetObertBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tiquet_obert, container, false
        )

        return binding.root
    }

}