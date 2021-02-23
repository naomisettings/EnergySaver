package cat.copernic.johan.energysaver.informes

import android.os.Build
import android.os.Bundle
import android.util.Log
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentInformesBinding>(inflater, R.layout.fragment_informes,
        container, false)
        omplirDades(binding)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun omplirDades(binding: FragmentInformesBinding){
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

                    Log.i("prova", dinersTotal.toString())

                    binding.txvTotal.setText(estalviadorTotal(dinersTotal).toString())

                    binding.btnAigua.setOnClickListener{view: View ->
                        binding.txvTotal.setText(estalviadorTotal(aiguaDiners).toString())
                        binding.txvPeriodeInfo.setText(estalviadorPeriode(aiguaDiners).toString())
                    }
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun estalviadorPeriode(map: Map<String, Double>): Double{
        val llistaString = arrayListOf<String>()
        var llistaDates = arrayListOf<LocalDate>()
        var datePrimera: LocalDate? = null
        var dinersPrimera: Double = 2.0
        var dinersSegona: Double = 5.0
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
                var diffAux = x.compareTo(targetDate)

                if(diff == 999999){
                    nearestDate = x
                    diff = diffAux
                }

                if(diffAux > diff){
                    nearestDate = x
                    diff = diffAux
                }
            }
        }
        return nearestDate
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFurthestDate(dates: ArrayList<LocalDate>, targetDate: LocalDate?): LocalDate{
        var furthestDate = LocalDate.now()
        if(targetDate != null){
            var diff = 999999

            for (x in dates) {
                var diffAux: Int
                diffAux = x.compareTo(targetDate)

                if(diff == 999999){
                    furthestDate = x
                    diff = diffAux
                }

                if(diffAux < diff){
                    furthestDate = x
                    diff = diffAux
                }
            }
        }
        return furthestDate
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun arrayListDeValors(list: ArrayList<Double>, map: Map<String, Double>){
        val llistaString = arrayListOf<String>()
        var llistaDates = arrayListOf<LocalDate>()

        for (x in map){
            llistaString.add(x.key)
        }

        llistaDates = getLlistaDates(llistaString)

        for(x in map){
            if(x.key == stringParser(getFurthestDate(llistaDates, LocalDate.now()))){
                list.add(x.value)
                Log.i("prova", "a" + x.value.toString())
                }
            }

        for(x in map){
            if(x.key != stringParser(getFurthestDate(llistaDates, LocalDate.now()))){
                list.add(x.value)
                Log.i("prova", "b" + x.value.toString())
            }
        }
    }
}