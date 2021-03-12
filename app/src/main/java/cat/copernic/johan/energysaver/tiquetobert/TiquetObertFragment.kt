package cat.copernic.johan.energysaver.tiquetobert

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentTiquetObertBinding
import coil.api.load
import com.google.android.material.transition.MaterialSharedAxis
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class TiquetObertFragment : Fragment() { //

    val db = FirebaseFirestore.getInstance()
    lateinit var binding: FragmentTiquetObertBinding
    lateinit var args: TiquetObertFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tiquet_obert, container, false
        )

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large_img).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 0.01.toLong()
        }

        //Rebre dades d'altres fragments
        args = TiquetObertFragmentArgs.fromBundle(requireArguments())

        Log.d("tiquetid", args.tiquetId)
        //Assignar al titol i motiu les variables
        binding.txtViewObrirTiquetMotiu.text = args.titol
        binding.txtViewTiquetObertDesc.text = args.descripcio

        //Pujar una imatge amb picasso
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("images/${args.imatge}")

        //Monstrar la imatge
        imageRef.downloadUrl.addOnSuccessListener { url ->
            binding.imgViewCarregarBBDD.load(url)
        }.addOnFailureListener {

        }
        //Funció per al botó respondre  (Només el pot veure l'admin)
        adminVeureBttn()

        //Obrir el fragent per introduir la resposta
        binding.bttnResposta.setOnClickListener() {
            view?.findNavController()
                ?.navigate(
                    TiquetObertFragmentDirections
                        .actionTiquetObertFragmentToRespostaTiquetFragment(
                            args.tiquetId,
                            args.titol,
                            args.descripcio,
                            args.imatge
                        )
                )
        }

        //Funció per a que l'usuari pugui veure la resposta
        veureResposata(args.tiquetId)

        return binding.root
    }

    fun adminVeureBttn() {
        //Guarda les dades del usuari connectat a la constant user
        val user = Firebase.auth.currentUser

        //Guarda el mail del usuari que ha fet login
        val mail = user?.email.toString()

        //Accedim a la col·leccció usuaris
        val tiquetsFirestore = db.collection("usuaris")
        //Realitzes la consulta en el qual el mail sigui igual
        val query = tiquetsFirestore.whereEqualTo("mail", mail).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    //Guardem a la val usuari la llista de documents amb usuaris amb aquest mail
                    val usuari =
                        document.toObjects(cat.copernic.johan.energysaver.veuretiquet.TiquetDC::class.java)
                    //Com que el mail es clau única agafem la [0] (I mirem que l'adimin sigui true)
                    if (usuari[0].admin) {
                        //Fem visible el botó resposta
                        binding.bttnResposta.visibility = View.VISIBLE
                    } else {
                        binding.bttnResposta.visibility = View.INVISIBLE
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }

    }

    fun veureResposata(tiquetId: String) {
        val user = Firebase.auth.currentUser

        //Guarda el mail del usuari que ha fet login
        val mail = user?.email.toString()

        //Consulta per extreure la resposta del tiquet
        val tiquetsFirestore = db.collection("tiquet")
        val query = tiquetsFirestore.whereEqualTo("id", tiquetId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val tiquetsDC = document.toObjects(TiquetDC::class.java)
                    //En el cas que la resposta estigui buida fa invisible el txt view de mostrar resposta
                    //I fa visible el text view sense resposta
                    Log.d("resposta2", tiquetsDC[0].resposta)
                    if (tiquetsDC[0].resposta == "") {
                        binding.txtViewMostraResposta.visibility = View.INVISIBLE
                        binding.txtViewSenseResposta.visibility = View.VISIBLE
                    } else {
                        //En cas contrari mostra la resposta
                        binding.txtViewMostraResposta.visibility = View.VISIBLE
                        binding.txtViewSenseResposta.visibility = View.INVISIBLE
                        binding.txtViewMostraResposta.text = tiquetsDC[0].resposta
                    }
                    if(tiquetsDC[0].resposta != ""){
                        binding.bttnResposta.visibility = View.INVISIBLE
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
}




