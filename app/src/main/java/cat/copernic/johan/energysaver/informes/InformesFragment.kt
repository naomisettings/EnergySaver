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
import com.google.android.material.transition.MaterialSharedAxis
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class InformesFragment : Fragment() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentInformesBinding>(inflater, R.layout.fragment_informes,
            container, false)

        //TRANSACTION
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 0.01.toLong()
        }

        activacioButtons(binding)
        omplirDades(binding)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    fun activacioButtons(binding: FragmentInformesBinding){
        val user = Firebase.auth.currentUser
        val mail_usuari = user?.email.toString()
        Log.d("mail", mail_usuari)


        val db = FirebaseFirestore.getInstance()

        val energiesSeleccionades = db.collection("energies")
        val query = energiesSeleccionades.whereEqualTo("mail_usuari", mail_usuari).get()
            .addOnSuccessListener { document ->
                if (!document.isEmpty){
                    val energia = document.toObjects(Contractades::class.java)
                    if(!energia[0].aigua){
                        binding.btnAigua.setBackgroundColor(R.color.greyColor)
                        binding.btnAigua.alpha = 0.5f
                        binding.btnAigua.isClickable = false
                    }

                    if(!energia[0].llum){
                        binding.btnLlum.setBackgroundColor(R.color.greyColor)
                        binding.btnLlum.alpha = 0.5f
                        binding.btnLlum.isClickable = false
                    }

                    if(!energia[0].gas){
                        binding.btnGas.setBackgroundColor(R.color.greyColor)
                        binding.btnGas.alpha = 0.5f
                        binding.btnGas.isClickable = false
                    }

                    if(!energia[0].gasoil){
                        binding.btnGasoil.setBackgroundColor(R.color.greyColor)
                        binding.btnGasoil.alpha = 0.5f
                        binding.btnGasoil.isClickable = false
                    }
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun omplirDades(binding: FragmentInformesBinding){

        val db = FirebaseFirestore.getInstance()



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

                    var estalviatTotal: Double

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

                    estalviatTotal = estalviadorTotalIndiv(aiguaDiners) +
                                     estalviadorTotalIndiv(llumDiners) +
                                     estalviadorTotalIndiv(gasDiners) +
                                     estalviadorTotalIndiv(gasoilDiners)

                        binding.txvTotal.setText(estalviatTotal.toString())

                    binding.btnTotal.setOnClickListener{view: View ->
                        resetFields(binding)
                        binding.txvTotal.setText(estalviatTotal.toString())
                    }

                    if(!aiguaDiners.isEmpty()){
                        binding.btnAigua.setOnClickListener{view: View ->
                            resetFields(binding)
                            binding.txvTotal.setText(estalviadorTotalIndiv(aiguaDiners).toString())
                            binding.txvPeriodeInfo.setText(estalviadorPeriode(aiguaDiners).toString())
                            binding.txvTemps.setText(tempsEstalviat(aiguaDiners).toString())
                            binding.txvTotalConsum.setText(estalviadorTotalIndiv(consumAigua).toString())
                            binding.txvPeriodeConsum.setText(estalviadorPeriode(consumAigua).toString())
                            binding.txvTempsConsum.setText(tempsEstalviat(consumAigua).toString())
                        }
                    }

                    if(!llumDiners.isEmpty()){
                        binding.btnLlum.setOnClickListener{view: View ->
                            resetFields(binding)
                            binding.txvTotal.setText(estalviadorTotalIndiv(llumDiners).toString())
                            binding.txvPeriodeInfo.setText(estalviadorPeriode(llumDiners).toString())
                            binding.txvTemps.setText(tempsEstalviat(llumDiners).toString())
                            binding.txvTotalConsum.setText(estalviadorTotalIndiv(consumLlum).toString())
                            binding.txvPeriodeConsum.setText(estalviadorPeriode(consumLlum).toString())
                            binding.txvTempsConsum.setText(tempsEstalviat(consumLlum).toString())
                        }
                    }

                    if(!gasDiners.isEmpty()){
                        binding.btnGas.setOnClickListener{view: View ->
                            resetFields(binding)
                            binding.txvTotal.setText(estalviadorTotalIndiv(gasDiners).toString())
                            binding.txvPeriodeInfo.setText(estalviadorPeriode(gasDiners).toString())
                            binding.txvTemps.setText(tempsEstalviat(gasDiners).toString())
                            binding.txvTotalConsum.setText(estalviadorTotalIndiv(consumGas).toString())
                            binding.txvPeriodeConsum.setText(estalviadorPeriode(consumGas).toString())
                            binding.txvTempsConsum.setText(tempsEstalviat(consumGas).toString())
                        }
                    }

                    if(!gasoilDiners.isEmpty()){
                        binding.btnGasoil.setOnClickListener{view: View ->
                            resetFields(binding)
                            binding.txvTotal.setText(estalviadorTotalIndiv(gasoilDiners).toString())
                            binding.txvPeriodeInfo.setText(estalviadorPeriode(gasoilDiners).toString())
                            binding.txvTemps.setText(tempsEstalviat(gasoilDiners).toString())
                            binding.txvTotalConsum.setText(estalviadorTotalIndiv(consumGasoil).toString())
                            binding.txvPeriodeConsum.setText(estalviadorPeriode(consumGasoil).toString())
                            binding.txvTempsConsum.setText(tempsEstalviat(consumGasoil).toString())
                        }
                    }
                }
            }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun estalviadorTotalIndiv(map: Map<String, Double>): Double{
        var estalviat: Double = 0.0
        var avui = LocalDate.now()
        var valorPrimera = 0.0
        var valorSegona = 0.0

        for(x in map) {
            if(x.key == stringParser(getFurthestDate(getLlistaDates(extreureDatesMap(map)), avui))){
                valorPrimera = x.value
            }
        }

        for(x in map){
            if(x.key == stringParser(getNearestDate(getLlistaDates(extreureDatesMap(map)), avui))){
                valorSegona = x.value
            }
        }
        estalviat = valorPrimera - valorSegona

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

        llistaDates = getLlistaDates(extreureDatesMap(map))
       // Log.i("llistaDates", llistaDates.toString())

        for(x in map){
            if(x.key == stringParser(getNearestDate(llistaDates, LocalDate.now()))){
                dinersPrimera = x.value
                datePrimera = dataParser(x.key)
              //  Log.i("comparar", x.key + " -> " + x.value.toString())
              //  Log.i("comparar", stringParser(getNearestDate(llistaDates, LocalDate.now())))
            }
        }

        for (x in map){

           // Log.i("comparar", x.key + " -> " + x.value.toString() + " 2")
           // Log.i("comparar", stringParser(getNearestDate(llistaDates, datePrimera)) + " 2")
            if(x.key == stringParser(getNearestDate(llistaDates, datePrimera))){
                dinersSegona = x.value
            }
        }

        return dinersSegona - dinersPrimera
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun tempsEstalviat(map: Map<String, Double>): Int{
        var dateComparar = LocalDate.now()
        var tempsEstalviat = 0
        var llistaDates = getLlistaDates(extreureDatesMap(map))

        tempsEstalviat = ChronoUnit.DAYS.between(getFurthestDate(llistaDates, dateComparar), dateComparar).toInt()

       // Log.i("temps", getFurthestDate(llistaDates, dateComparar).toString())
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

                if(diff == -1 && x != targetDate){
                    nearestDate = x
                    diff = diffAux
                }

                if(diffAux < diff && x != targetDate){
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

                if(diff == -1 && x != targetDate){
                    furthestDate = x
                    diff = diffAux
                }

                if(diffAux > diff && x != targetDate){
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