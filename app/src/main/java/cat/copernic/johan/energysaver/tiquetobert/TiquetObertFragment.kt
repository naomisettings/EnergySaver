
package cat.copernic.johan.energysaver.tiquetobert

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentTiquetObertBinding

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
        Toast.makeText(context, "NumCorrect: ${args.tiquetId}", Toast.LENGTH_LONG).show()
        binding.txtViewObrirTiquetMotiu.text = args.tiquetId

        return binding.root
    }
}