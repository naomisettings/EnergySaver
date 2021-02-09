package cat.copernic.johan.energysaver.obrirtiquet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentObrirBinding

class ObrirTiquetFragment : Fragment() {

    private lateinit var binding : FragmentObrirBinding
    var titol: String = ""
    var descripcio: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_obrir, container, false)

        binding.bttnConfirmarTiquet.setOnClickListener{
            rebreDades()
        }

        return binding.root
    }

    fun rebreDades(){
        binding.apply{
            titol = editTextTemaTiquet.text.toString()
            descripcio = editTxtDescripcioTiquet.text.toString()
        }

        if (titol.isEmpty() || descripcio.isEmpty()){
            Toast.makeText(context, R.string.campsBuitsToastObrirTiquet, Toast.LENGTH_SHORT).show()
        }else{

            print("firebase")

        }

    }
}