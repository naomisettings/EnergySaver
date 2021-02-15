package cat.copernic.johan.energysaver.veuretiquet

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.johan.energysaver.Communicator
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentVeureBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class VeureTiquetFragment : Fragment(){
    var tiquets: ArrayList<Tiquet> = arrayListOf()
    val db = FirebaseFirestore.getInstance()
    lateinit var comm: Communicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val binding : FragmentVeureBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_veure, container, false)

        //tiquets = Tiquet.createTiquetList("titol","descripcio",20)
        val rvTiquets = binding.rcvTiquets
        omplirRecycleView(rvTiquets)

        comm = activity as Communicator
        //comm.passDataCom(binding..text.toString())

        binding.bttnNouTiquet.setOnClickListener {
                view : View ->
            view.findNavController().navigate(R.id.action_veureFragment_to_obrirFragment)
        }


        return binding.root
    }


    fun omplirRecycleView(rvTiquets: RecyclerView){

        //Guarda les dades del usuari connectat a la constant user
        val user = Firebase.auth.currentUser

        //Guarda el mail del usuari que ha fet login
        val mail = user?.email.toString()

        //Consulta per extreure el nickname per guardar-lo al document tiquet
        val tiquetsFirestore = db.collection("tiquet")
        val query = tiquetsFirestore.whereEqualTo("mail", mail).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val tiquetsDC = document.toObjects(TiquetDC::class.java)

                    for (i in 0 until tiquetsDC.size) {
                        val tq = Tiquet(tiquetsDC[i].id, tiquetsDC[i].titol,  tiquetsDC[i].descripcio, false)
                        tiquets.add(tq)
                    }
                   /* val adapter = TiquetsAdapter(tiquets, this)
                    rvTiquets.adapter = adapter
                    rvTiquets.layoutManager = LinearLayoutManager(this.context)

                    */

                    val adapter = TiquetsAdapter(tiquets, CellClickListener { tiquetId ->
                        comm.passDataCom(tiquetId)
                        view?.findNavController()?.navigate(R.id.action_veureFragment_to_tiquetObertFragment)
                        Log.i("entraFijo", tiquetId)

                    })
                    rvTiquets.adapter = adapter
                    rvTiquets.layoutManager = LinearLayoutManager(this.context)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
}

//Classe que correspon als camps de la col·lecció usuaris
data class TiquetDC(
    var id: String = "", var data: String = "", var descripcio: String = "", var hora: String = "",
    var mail: String = "", var nickname: String = "", var titol: String = ""
)