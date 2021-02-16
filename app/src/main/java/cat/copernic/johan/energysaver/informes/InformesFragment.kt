package cat.copernic.johan.energysaver.informes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentInformesBinding
import cat.copernic.johan.energysaver.seleccio.Energies
import cat.copernic.johan.energysaver.veuretiquet.Tiquet
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class InformesFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()

    var aiguaGastada = ArrayList<Int>()
    var llumGastada = ArrayList<Int>()
    var gasGastat = ArrayList<Int>()
    var gasoilGastat = ArrayList<Int>()
    var data = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentInformesBinding>(inflater, R.layout.fragment_informes,
        container, false)
        return binding.root
    }

    fun omplirDades(){
        val user = Firebase.auth.currentUser
        val mail = user?.email.toString()

        val energies = db.collection("entrarEnergia")
        val query = energies.whereEqualTo("mail", mail).get()
            .addOnSuccessListener { document ->
                if (!document.isEmpty) {
                    val dadesEnergia = document.toObjects(DadesEnergia::class.java)
                    for (i in 0 until dadesEnergia.size) {
                        aiguaGastada.add(dadesEnergia[i].aiguaGastats)
                        llumGastada.add(dadesEnergia[i].llumGastats)
                        gasGastat.add(dadesEnergia[i].gasGastats)
                        gasoilGastat.add(dadesEnergia[i].gasoilGastats)
                    }
                }
            }
    }
    //Implementar llibreria i métodes per realitzar els informes.
}

data class DadesEnergia(
    var aiguaGastats: Int = 0, var data: String = "", var gasGastats: Int = 0, var gasoilGastats: Int = 0,
    var llumGastats: Int = 0, var mail: String = ""
)