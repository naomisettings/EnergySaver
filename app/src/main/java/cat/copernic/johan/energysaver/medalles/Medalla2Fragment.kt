package cat.copernic.johan.energysaver.medalles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentMedalla2Binding

class Medalla2Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentMedalla2Binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_medalla2, container, false)

        return binding.root
    }
}