package cat.copernic.johan.energysaver.veuretiquet

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentVeureBinding
import cat.copernic.johan.energysaver.tiquetobert.TiquetDC
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class VeureTiquetFragment : Fragment() {
    var tiquets = arrayListOf<Tiquet>()
    val db = FirebaseFirestore.getInstance()
    lateinit var adapter: TiquetsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentVeureBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_veure, container, false)

        setHasOptionsMenu(true)

        var rvTiquets = binding.rcvTiquets

        veureRecyclerView(rvTiquets)
        var sb = StringBuilder()
        binding.bttnBorrarTIquet.setOnClickListener {

            if (adapter.checkedTiquets.size > 0) {
                for (x in adapter.checkedTiquets) {
                    val tiquetsFirestore = db.collection("tiquet")
                    val query = tiquetsFirestore.whereEqualTo("id", x.idTiquet).get()
                        .addOnSuccessListener { document ->
                            for (doc in document) {

                                Log.d("prova33",doc.get("hora").toString())

                                db.collection("tiquet").document(doc.id)
                                    .delete()
                                    .addOnSuccessListener {
                                        Log.d(
                                            TAG,
                                            "DocumentSnapshot successfully deleted!"
                                        )
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w(
                                            TAG,
                                            "Error deleting document",
                                            e
                                        )
                                    }
                            }
                        }
                }
                rvTiquets.removeAllViews()
                veureRecyclerView(rvTiquets)
            } else {
                view?.let { it1 ->
                    Snackbar.make(it1, R.string.borrarTiquet, Snackbar.LENGTH_LONG)
                        .show()
                }
            }

        }
        binding.bttnNouTiquet.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_veureFragment_to_obrirFragment)
        }


        return binding.root
    }

    fun veureRecyclerView(rvTiquets: RecyclerView) {
        //Guarda les dades del usuari connectat a la constant user
        val user = Firebase.auth.currentUser

        //Guarda el mail del usuari que ha fet login
        val mail = user?.email.toString()

        //Consulta per extreure el nickname per guardar-lo al document tiquet
        val tiquetsFirestore = db.collection("usuaris")
        val query = tiquetsFirestore.whereEqualTo("mail", mail).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val usuari = document.toObjects(TiquetDC::class.java)
                    if (usuari[0].admin) {
                        adminTure(rvTiquets)
                    } else {
                        adminFalse(rvTiquets)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    fun adminFalse(rvTiquets: RecyclerView) {

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
                    if (tiquets != null) {
                        tiquets.clear()
                    }
                    for (i in 0 until tiquetsDC.size) {
                        val tq = Tiquet(
                            tiquetsDC[i].id,
                            tiquetsDC[i].titol,
                            tiquetsDC[i].descripcio,
                            tiquetsDC[i].imatge,
                            false
                        )
                        tiquets.add(tq)
                    }
                    val adapter = TiquetsAdapter(
                        tiquets,
                        CellClickListener { tiquetId, titol, descripcio, imatge ->
                            view?.findNavController()
                                ?.navigate(
                                    VeureTiquetFragmentDirections
                                        .actionVeureFragmentToTiquetObertFragment(
                                            tiquetId,
                                            titol,
                                            descripcio,
                                            imatge
                                        )
                                )
                        })

                    rvTiquets.adapter = adapter
                    rvTiquets.layoutManager = LinearLayoutManager(this.context)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    fun adminTure(rvTiquets: RecyclerView) {
        //Guarda les dades del usuari connectat a la constant user
        val user = Firebase.auth.currentUser

        //Guarda el mail del usuari que ha fet login
        val mail = user?.email.toString()

        //Consulta per extreure el nickname per guardar-lo al document tiquet
        val tiquetsFirestore = db.collection("tiquet")
        val query = tiquetsFirestore.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val tiquetsDC = document.toObjects(TiquetDC::class.java)
                    if (tiquets != null) {
                        tiquets.clear()
                    }
                    for (i in 0 until tiquetsDC.size) {
                        val tq = Tiquet(
                            tiquetsDC[i].id,
                            tiquetsDC[i].titol,
                            tiquetsDC[i].descripcio,
                            tiquetsDC[i].imatge,
                            false
                        )
                        tiquets.add(tq)
                    }
                    adapter = TiquetsAdapter(
                        tiquets,
                        CellClickListener { tiquetId, titol, descripcio, imatge ->
                            view?.findNavController()
                                ?.navigate(
                                    VeureTiquetFragmentDirections
                                        .actionVeureFragmentToTiquetObertFragment(
                                            tiquetId,
                                            titol,
                                            descripcio,
                                            imatge
                                        )
                                )
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