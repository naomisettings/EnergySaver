package cat.copernic.johan.energysaver.medalles

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentMedallesBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MedallesFragment : Fragment() {

    val db = FirebaseFirestore.getInstance()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentMedallesBinding>(
            inflater,
            R.layout.fragment_medalles,
            container, false
        )

        medallesDeTemps()
        binding.imgBttnMedalla1.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_medallesFragment_to_medalla1Fragment)
        }
        binding.imgBttnGranEstalviador.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_medallesFragment_to_medalla2Fragment)
        }
        binding.imgBttnMigAny.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_medallesFragment_to_medalla3Fragment)
        }
        binding.imgBttnUnAny.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_medallesFragment_to_medalla4Fragment)
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun medallesDeTemps() {
        val user = Firebase.auth.currentUser
        //Guarda el mail del usuari que ha fet login
        val mail = user?.email.toString()

        //Consulta per extreure el nickname per guardar-lo al document tiquet
        val despesaConsumFirestore = db.collection("despesaConsum")
        val query = despesaConsumFirestore.whereEqualTo("mail", mail).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val despesaConsumDC = document.toObjects(DespesaConsumDC::class.java)
                    val dates = arrayListOf<String>()
                    for ((key, value) in despesaConsumDC[0].aiguaConsum) {
                        dates.add(key)
                    }
                    for ((key, value) in despesaConsumDC[0].llumConsum) {
                        dates.add(key)
                    }
                    for ((key, value) in despesaConsumDC[0].gasConsum) {
                        dates.add(key)
                    }
                    for ((key, value) in despesaConsumDC[0].gasoilConsum) {
                        dates.add(key)
                    }

                    for ((i, x) in dates.withIndex()) {
                        if (i == 0) {
                            val date = LocalDate.parse(x, DateTimeFormatter.ISO_DATE)
                        } else {
                            val dataMenor = LocalDate.parse(x, DateTimeFormatter.ISO_DATE)

                        }

                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
}

data class DespesaConsumDC(
    var aiguaConsum: Map<String, String> = mapOf(),
    var llumConsum: Map<String, String> = mapOf(),
    var gasConsum: Map<String, String> = mapOf(),
    var gasoilConsum: Map<String, String> = mapOf(),
    var aiguaDiners: Map<String, String> = mapOf(),
    var llumDiners: Map<String, String> = mapOf(),
    var gasDiners: Map<String, String> = mapOf(),
    var gasoilDiners: Map<String, String> = mapOf(),
    var mail: String = ""
)