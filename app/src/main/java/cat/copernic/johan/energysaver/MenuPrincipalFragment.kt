package cat.copernic.johan.energysaver

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.johan.energysaver.databinding.FragmentMenuPrincipalBinding


class MenuPrincipalFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentMenuPrincipalBinding>(inflater, R.layout.fragment_menu_principal,
                container, false)
        return binding.root
    }
}