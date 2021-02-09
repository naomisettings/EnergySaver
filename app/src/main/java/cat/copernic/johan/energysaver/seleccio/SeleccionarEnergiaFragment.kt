package cat.copernic.johan.energysaver.seleccio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentSeleccionarEnergiaBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SeleccionarEnergiaFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentSeleccionarEnergiaBinding>(inflater,
            R.layout.fragment_seleccionar_energia,
            container, false)
        binding.btnConfirmar.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_seleccionarEnergiaFragment_to_menuPrincipalFragment)
        }
        return binding.root
    }
}