package cat.copernic.johan.energysaver.informes

import android.annotation.SuppressLint
import android.icu.util.TimeUnit
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentInformesBinding
import cat.copernic.johan.energysaver.introduirconsums.Contractades
import cat.copernic.johan.energysaver.medalles.DespesaConsumDC
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class InformesFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentInformesBinding>(inflater, R.layout.fragment_informes,
            container, false)
        activacioButtons(binding)
        omplirDades(binding)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    fun activacioButtons(binding: FragmentInformesBinding){
        val user = Firebase.auth.currentUser
        val mail_usuari = user?.email.toString()
        Log.d("mail", mail_usuari)

        val energiesSeleccionades = db.collection("energies")
        val query = energiesSeleccionades.whereEqualTo("mail_usuari", mail_usuari).get()
            .addOnSuccessListener { document ->
                if (!document.isEmpty){
                    val energia = document.toObjects(Contractades::class.java)
                    if(!energia[0].aigua){
                        binding.btnAigua.setBackgroundColor(R.color.totalColor)
                        binding.btnAigua.isClickable = false
                    }

                    if(!energia[0].llum){
                        binding.btnLlum.setBackgroundColor(R.color.totalColor)
                        binding.btnAigua.isClickable = false
                    }

                    if(!energia[0].gas){
                        binding.btnGas.setBackgroundColor(R.color.totalColor)
                        binding.btnAigua.isClickable = false
                    }

                    if(!energia[0].gasoil){
                        binding.btnGasoil.setBackgroundColor(R.color.totalColor)
                        binding.btnAigua.isClickable = false
                    }
                }
            }
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

                    if(dinersTotal != null){
                        binding.txvTotal.setText(estalviadorTotal(dinersTotal).toString())
                    }

                    binding.btnTotal.setOnClickListener{view: View ->
                        resetFields(binding)
                        binding.txvTotal.setText(estalviadorTotal(dinersTotal).toString())
                    }

                    if(aiguaDiners != null){
                        binding.btnAigua.setOnClickListener{view: View ->
                            resetFields(binding)
                            binding.txvTotal.setText(estalviadorTotal(aiguaDiners).toString())
                            binding.txvPeriodeInfo.setText(estalviadorPeriode(aiguaDiners).toString())
                            binding.txvTemps.setText(tempsEstalviat(aiguaDiners).toString())
                            binding.txvTotalConsum.setText(estalviadorTotal(consumAigua).toString())
                            binding.txvPeriodeConsum.setText(estalviadorPeriode(consumAigua).toString())
                            binding.txvTempsConsum.setText(tempsEstalviat(consumAigua).toString())
                        }
                    }

                    if(llumDiners != null){
                        binding.btnLlum.setOnClickListener{view: View ->
                            resetFields(binding)
                            binding.txvTotal.setText(estalviadorTotal(llumDiners).toString())
                            binding.txvPeriodeInfo.setText(estalviadorPeriode(llumDiners).toString())
                            binding.txvTemps.setText(tempsEstalviat(llumDiners).toString())
                            binding.txvTotalConsum.setText(estalviadorTotal(consumLlum).toString())
                            binding.txvPeriodeConsum.setText(estalviadorPeriode(consumLlum).toString())
                            binding.txvTempsConsum.setText(tempsEstalviat(consumLlum).toString())
                        }
                    }

                    if(gasDiners != null){
                        binding.btnGas.setOnClickListener{view: View ->
                            resetFields(binding)
                            binding.txvTotal.setText(estalviadorTotal(gasDiners).toString())
                            binding.txvPeriodeInfo.setText(estalviadorPeriode(gasDiners).toString())
                            binding.txvTemps.setText(tempsEstalviat(gasDiners).toString())
                            binding.txvTotalConsum.setText(estalviadorTotal(consumGas).toString())
                            binding.txvPeriodeConsum.setText(estalviadorPeriode(consumGas).toString())
                            binding.txvTempsConsum.setText(tempsEstalviat(consumGas).toString())
                        }
                    }

                    if(gasoilDiners != null){
                        binding.btnGasoil.setOnClickListener{view: View ->
                            resetFields(binding)
                            binding.txvTotal.setText(estalviadorTotal(gasoilDiners).toString())
                            binding.txvPeriodeInfo.setText(estalviadorPeriode(gasoilDiners).toString())
                            binding.txvTemps.setText(tempsEstalviat(gasoilDiners).toString())
                            binding.txvTotalConsum.setText(estalviadorTotal(consumGasoil).toString())
                            binding.txvPeriodeConsum.setText(estalviadorPeriode(consumGasoil).toString())
                            binding.txvTempsConsum.setText(tempsEstalviat(consumGasoil).toString())
                        }
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
        return -estalviat
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
        return -estalviat
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun estalviadorPeriode(map: Map<String, Double>): Double{
        val llistaString = arrayListOf<String>()
        var llistaDates = arrayListOf<LocalDate>()
        var datePrimera: LocalDate? = null
        var dinersPrimera: Double = 0.0
        var dinersSegona: Double = 0.0
        var estalviData: Double

        llistaDates = getLlistaDates(extreureDatesMap(map))

        for(x in map){
            if(x.key == stringParser(getNearestDate(llistaDates, LocalDate.now()))){
                dinersPrimera = x.value
                datePrimera = dataParser(x.key)
                Log.i("prova", x.key + " -> " + x.value.toString())
            }
        }

        for (x in map){
            if(datePrimera != null && x.key == stringParser(getNearestDate(llistaDates,
                    LocalDate.of(datePrimera.year, datePrimera.month, datePrimera.dayOfMonth - 1)))){
                dinersSegona = x.value
                Log.i("prova", x.key + " -> " + x.value.toString() + " 2")
            }
        }

        return dinersPrimera - dinersSegona
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun tempsEstalviat(map: Map<String, Double>): Int{
        var dateComparar = LocalDate.now()
        var tempsEstalviat = 0
        var llistaDates = getLlistaDates(extreureDatesMap(map))

        tempsEstalviat = ChronoUnit.DAYS.between(getFurthestDate(llistaDates, dateComparar), dateComparar).toInt()

        Log.i("temps", getFurthestDate(llistaDates, dateComparar).toString())
        return tempsEstalviat
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

    fun extreureDatesMap(map: Map<String, Double>): ArrayList<String>{
        val llistaString = arrayListOf<String>()
        for (x in map){
            llistaString.add(x.key)
        }
        return llistaString
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
            var diff = -1

            for (x in dates) {
                var diffAux = ChronoUnit.DAYS.between(x, targetDate).toInt()

                if(diff == -1){
                    nearestDate = x
                    diff = diffAux
                }

                if(diffAux < diff){
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
            var diff = -1

            for (x in dates) {
                var diffAux: Int
                diffAux = ChronoUnit.DAYS.between(x, targetDate).toInt()

                if(diff == -1){
                    furthestDate = x
                    diff = diffAux
                }

                if(diffAux > diff){
                    furthestDate = x
                    diff = diffAux
                    Log.i("temps", furthestDate.toString())
                }
            }
        }
        return furthestDate
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun arrayListDeValors(list: ArrayList<Double>, map: Map<String, Double>){
        val llistaString = arrayListOf<String>()
        var llistaDates = arrayListOf<LocalDate>()

        llistaDates = getLlistaDates(extreureDatesMap(map))

        for(x in map){
            if(x.key == stringParser(getFurthestDate(llistaDates, LocalDate.now()))){
                list.add(x.value)
            }
        }

        for(x in map){
            if(x.key != stringParser(getFurthestDate(llistaDates, LocalDate.now()))){
                list.add(x.value)
            }
        }
    }

    fun resetFields(binding: FragmentInformesBinding){
        binding.txvTemps.setText("")
        binding.txvPeriodeInfo.setText("")
        binding.txvTotal.setText("")
        binding.txvPeriodeConsum.setText("")
        binding.txvTotalConsum.setText("")
        binding.txvTempsConsum.setText("")
    }
}