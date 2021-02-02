package cat.copernic.johan.energysaver.modifica

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentModificarUsuariBinding





class ModificarUsuari : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding: FragmentModificarUsuariBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_modificar_usuari,container, false)
        return binding.root
    }


}