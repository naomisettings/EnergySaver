package cat.copernic.johan.energysaver.tiquetobert

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentRespostaTiquetBinding
import cat.copernic.johan.energysaver.databinding.FragmentVeureBinding
import cat.copernic.johan.energysaver.modifica.ModificarUsuari
import cat.copernic.johan.energysaver.tiquetobert.RespostaTiquetFragmentArgs.fromBundle
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



        binding.bttnEnivarResposta.setOnClickListener {
            val resposta = binding.editTxtDescrResposta.text.toString()
            afegirRespostaBBDD(args.idTiquet, resposta)
            binding.editTxtDescrResposta.text.clear()
        }

        return binding.root
    }

    fun afegirRespostaBBDD(idTiquet: String, resposta: String) {
        val actualitza = db.collection("tiquet").addSnapshotListener { snapshot, e ->
            val doc = snapshot?.documents

            doc?.forEach {
                val tiquetConsulta = it.toObject(TiquetDC::class.java)
                if (tiquetConsulta?.id == idTiquet) {

                    val TiquetId = it.id
                    val sfDocRef = db.collection("tiquet").document(TiquetId)

                    //Actualitzem
                    db.runTransaction { transaction ->
                        //agafem el ID
                        val snapshot = transaction.get(sfDocRef)
                        //actualitzem el id amb el mail del usuari identificat i guardem els camps

                        db.runTransaction { transaction ->
                            val snapshot = transaction.get(sfDocRef)
                            //actualitzem el id amb el mail del usuari identificat i guardem els camps
                            //val newUsuari = snapshot.getString("mail")!!
                            //Log.d("nou usuari", newUsuari)
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
                            .addOnFailureListener { e ->
                                Log.w(
                                    "error",
                                    "Error deleting document",
                                    e
                                )
                            }


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