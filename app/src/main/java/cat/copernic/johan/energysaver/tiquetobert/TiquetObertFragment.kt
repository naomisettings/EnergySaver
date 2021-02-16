
package cat.copernic.johan.energysaver.tiquetobert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentTiquetObertBinding
import com.google.firebase.storage.FirebaseStorage

class TiquetObertFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTiquetObertBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tiquet_obert, container, false
        )

        val args = TiquetObertFragmentArgs.fromBundle(requireArguments())

        binding.txtViewObrirTiquetMotiu.text = args.titol
        binding.txtViewTiquetObertDesc.text = args.descripcio

        val refStorage = FirebaseStorage.getInstance()
        var islandRef = refStorage.getReference("images/${args.imatge}")

        val ONE_MEGABYTE: Long = 1024 * 1024
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            // Data for "images/island.jpg" is returned, use this as needed

        }.addOnFailureListener {
            // Handle any errors
        }

        return binding.root
    }
}


