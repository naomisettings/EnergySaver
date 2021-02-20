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
import cat.copernic.johan.energysaver.medalles.DespesaConsumDC
import cat.copernic.johan.energysaver.seleccio.Energies
import cat.copernic.johan.energysaver.veuretiquet.Tiquet
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class InformesFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentInformesBinding>(inflater, R.layout.fragment_informes,
        container, false)
        return binding.root
    }

    fun omplirDades(){
        val user = Firebase.auth.currentUser
        val mail = user?.email.toString()

        val energies = db.collection("despesaConsum")
        val query = energies.whereEqualTo("mail", mail).get()
            .addOnSuccessListener { document ->
                if (!document.isEmpty) {
                    var consumAigua =  mapOf<String, Double>()
                    var aiguaDiners = mapOf<String, Double>()
                    var consumLlum =  mapOf<String, Double>()
                    var llumDiners = mapOf<String, Double>()
                    var consumGas =  mapOf<String, Double>()
                    var gasDiners = mapOf<String, Double>()
                    var consumGasoil =  mapOf<String, Double>()
                    var gasoilDiners = mapOf<String, Double>()

                    val dadesEnergia = document.toObjects(DespesaConsumDC::class.java)
                    consumAigua = dadesEnergia[0].aiguaConsum
                    aiguaDiners = dadesEnergia[0].aiguaDiners

                    var estalviPeriode = 0
                    var estalviTotal = 0
                    var percentatgeTotal = 0

                    var aiguaEstalviPeriode = 0
                    var aiguaEstalviTotal = 0
                    var aiguaPercentatgeTotal = 0

                    var llumEstalviPeriode = 0
                    var llumEstalviTotal = 0
                    var llumPercentatgeTotal = 0

                    var gasEstalviPeriode = 0
                    var gasEstalviTotal = 0
                    var gasPercentatgeTotal = 0

                    var gasoilEstalviPeriode = 0
                    var gasoilEstalviTotal = 0
                    var gasoilPercentatgeTotal = 0
                }
            }
    }
    fun estalviadorTotal(map: Map<String, Double>): Double{
        var aux: Double? = null
        var estalviat: Double = 0.0
        for (item in map){
            if(aux != null){
                estalviat += (aux - item.value)
            }
            aux = item.value
        }
        return estalviat
    }

    fun estalviadorPeriode(map: Map<String, Double>, periode: Int): Double{

    }
}