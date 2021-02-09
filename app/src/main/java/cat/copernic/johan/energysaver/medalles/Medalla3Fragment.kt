package cat.copernic.johan.energysaver.medalles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentMedalla3Binding

class Medalla3Fragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentMedalla3Binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_medalla3, container, false)

        return binding.root
    }
}