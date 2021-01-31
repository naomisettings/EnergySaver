package cat.copernic.johan.energysaver.baixa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentBaixaUsuariBinding



class BaixaUsuari : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentBaixaUsuariBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_baixa_usuari, container, false)
        return binding.root
    }

}