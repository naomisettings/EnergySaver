package cat.copernic.johan.energysaver.medalles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentMedallesBinding

class MedallesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentMedallesBinding>(inflater,
            R.layout.fragment_medalles,
            container, false)

        binding.imgBttnMedalla1.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_medallesFragment_to_medalla1Fragment)
        }
        binding.imgBttnGranEstalviador.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_medallesFragment_to_medalla2Fragment)
        }
        binding.imgBttnMigAny.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_medallesFragment_to_medalla3Fragment)
        }
        binding.imgBttnUnAny.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_medallesFragment_to_medalla4Fragment)
        }
        return binding.root
    }

}