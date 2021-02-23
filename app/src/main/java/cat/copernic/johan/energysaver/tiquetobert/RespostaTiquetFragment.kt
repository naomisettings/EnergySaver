package cat.copernic.johan.energysaver.tiquetobert

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentRespostaTiquetBinding
import cat.copernic.johan.energysaver.databinding.FragmentVeureBinding
import cat.copernic.johan.energysaver.modifica.ModificarUsuari
import cat.copernic.johan.energysaver.tiquetobert.RespostaTiquetFragmentArgs.fromBundle
import cat.copernic.johan.energysaver.veuretiquet.TiquetDC
import com.google.firebase.firestore.FirebaseFirestore


class RespostaTiquetFragment : Fragment() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentRespostaTiquetBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_resposta_tiquet, container, false)

        val args = fromBundle(requireArguments())



        binding.bttnEnivarResposta.setOnClickListener { view: View ->
            val resposta = binding.editTxtDescrResposta.text.toString()
            afegirRespostaBBDD(args.idTiquet, resposta)
            binding.editTxtDescrResposta.text.clear()

            view.findNavController()
                .navigate(
                    RespostaTiquetFragmentDirections
                        .actionRespostaTiquetFragmentToTiquetObertFragment(
                            args.idTiquet,
                            args.titol,
                            args.descripcio,
                            args.imatge
                        )
                )

        }

        return binding.root
    }

    //Fragment només per a l'administrador per a constestar la resposta
    fun afegirRespostaBBDD(idTiquet: String, resposta: String) {
        val actualitza = db.collection("tiquet").addSnapshotListener { snapshot, e ->
            val doc = snapshot?.documents

            doc?.forEach {
                //Assignem la id del tíquet al objecte tiquetConsulta
                val tiquetConsulta = it.toObject(TiquetDC::class.java)
                if (tiquetConsulta?.id == idTiquet) {
                    //agafem la ide del tiquet
                    val TiquetId = it.id
                    val sfDocRef = db.collection("tiquet").document(TiquetId)

                    //Actualitzem
                    db.runTransaction { transaction ->
                        val snapshot = transaction.get(sfDocRef)
                        //actualitzem la resposta a la bbdd
                        transaction.update(
                            sfDocRef,
                            "resposta",
                            resposta
                        )
                    }
                        .addOnSuccessListener {
                            Log.d(
                                "error",
                                "DocumentSnapshot successfully deleted!"
                            )
                        }
                }
            }
        }

    }
}

//Classe que correspon als camps de la col·lecció usuaris
data class TiquetDC(
    var admin: Boolean = false,
    var id: String = "",
    var data: String = "",
    var descripcio: String = "",
    var hora: String = "",
    var mail: String = "",
    var nickname: String = "",
    var resposta: String = "",
    var titol: String = "",
    var imatge: String = ""
)