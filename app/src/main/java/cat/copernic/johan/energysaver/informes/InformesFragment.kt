package cat.copernic.johan.energysaver.informes

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentInformesBinding
import cat.copernic.johan.energysaver.medalles.DespesaConsumDC
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class InformesFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: FragmentInformesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<FragmentInformesBinding>(inflater, R.layout.fragment_informes,
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
                    var dinersTotal = arrayListOf<Double>()

                    val dadesEnergia = document.toObjects(DespesaConsumDC::class.java)
                    val de = dadesEnergia[0]
                    consumAigua = de.aiguaConsum
                    aiguaDiners = de.aiguaDiners
                    consumLlum = de.llumConsum
                    llumDiners = de.llumDiners
                    consumGas = de.gasConsum
                    gasDiners = de.gasDiners
                    consumGasoil = de.gasoilConsum
                    gasoilDiners = de.gasoilDiners

                    arrayListDeValors(dinersTotal, aiguaDiners)
                    arrayListDeValors(dinersTotal, llumDiners)
                    arrayListDeValors(dinersTotal, gasDiners)
                    arrayListDeValors(dinersTotal, gasoilDiners)

                    binding.txvTotal.setText(estalviadorTotal(dinersTotal).toString())

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
    fun estalviadorTotal(list: ArrayList<Double>): Double{
        var aux: Double? = null
        var estalviat: Double = 0.0
        for (item in list){
            if(aux != null){
                estalviat += (aux - item)
            }
            aux = item
        }
        return estalviat
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun estalviadorPeriode(map: Map<String, Double>): Double{
        val llistaString = arrayListOf<String>()
        var llistaDates = arrayListOf<LocalDate>()
        var datePrimera: LocalDate? = null
        var dinersPrimera: Double = 0.0
        var dinersSegona: Double = 0.0
        var estalviData: Double

        for (x in map){
            llistaString.add(x.key)
        }

        llistaDates = getLlistaDates(llistaString)

        for(x in map){
            if(x.key == stringParser(getNearestDate(llistaDates, LocalDate.now()))){
                dinersPrimera = x.value
                datePrimera = dataParser(x.key)
            }

            if(x.key == stringParser(getNearestDate(llistaDates, datePrimera)) && datePrimera != null){
                dinersSegona = x.value
            }
        }

        return dinersPrimera - dinersSegona
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dataParser(data: String): LocalDate{
        val formatCorrecte = data.replace(".", "-")
        return LocalDate.parse(formatCorrecte, DateTimeFormatter.ISO_DATE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun stringParser(data: LocalDate): String{
        return data.toString().replace("-", ".")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getLlistaDates(llista: ArrayList<String>): ArrayList<LocalDate>{
        val llistaCorrecte = arrayListOf<LocalDate>()

        for (x in llista){
            var data = dataParser(x)
            llistaCorrecte.add(data)
        }
        return llistaCorrecte
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNearestDate(dates: ArrayList<LocalDate>, targetDate: LocalDate?): LocalDate{
        var nearestDate = LocalDate.now()
        if(targetDate != null){
            var diff = 999999

            for (x in dates) {
                var diffAux: Int
                diffAux = x.compareTo(targetDate)

                if(diff == 999999){
                    nearestDate = x
                }

                if(diffAux > diff){
                    nearestDate = x
                }
            }
        }
        return nearestDate
    }

    fun arrayListDeValors(list: ArrayList<Double>, map: Map<String, Double>){
        for(x in map){
            list.add(x.value)
        }
    }
}