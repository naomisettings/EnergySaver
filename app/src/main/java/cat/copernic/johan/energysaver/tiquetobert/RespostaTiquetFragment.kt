package cat.copernic.johan.energysaver.tiquetobert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentRespostaTiquetBinding
import cat.copernic.johan.energysaver.databinding.FragmentVeureBinding


class RespostaTiquetFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentRespostaTiquetBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_resposta_tiquet, container, false)


        return binding.root
    }

}