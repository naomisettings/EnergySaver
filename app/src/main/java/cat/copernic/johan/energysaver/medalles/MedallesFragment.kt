package cat.copernic.johan.energysaver.medalles

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
import cat.copernic.johan.energysaver.databinding.FragmentInformesBinding
import cat.copernic.johan.energysaver.databinding.FragmentMedallesBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MedallesFragment : Fragment() {

    lateinit var binding: FragmentMedallesBinding
    val db = FirebaseFirestore.getInstance()
    val user = Firebase.auth.currentUser
    val mail = user?.email.toString()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<FragmentMedallesBinding>(
            inflater,
            R.layout.fragment_medalles,
            container, false
        )

        //Inicalitzar totes les medalles en gris
        medallesEnGris()

        //Mostre les dues medalles de temps (Mig any estalviant i un any)
        mostrarMedallesTemps()

        //Medalles diners estalviats
        omplirDades()

        //Accedir als fragments per a veure les medalles en gran
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

    fun medallesEnGris() {
        binding.apply {
            imgBttnMigAny.visibility = View.INVISIBLE
            imgBttnUnAny.visibility = View.INVISIBLE
            txtViewMigAny.visibility = View.INVISIBLE
            txtViewUnAny.visibility = View.INVISIBLE

            imgBttnMedalla1.visibility = View.INVISIBLE
            imgBttnGranEstalviador.visibility = View.INVISIBLE
            txtViewMedEstalviador.visibility = View.INVISIBLE
            txtViewGranEstalviador.visibility = View.INVISIBLE
        }
    }

    fun medallaMigAnyVisible() {
        binding.apply {
            imgBttnMigAny.visibility = View.VISIBLE
            txtViewMigAny.visibility = View.VISIBLE
            txtViewUnAny.visibility = View.INVISIBLE
            medallMigAnyGris.visibility = View.INVISIBLE
            imgBttnUnAny.visibility = View.INVISIBLE
        }
    }

    fun medallaUnAnyVisible() {
        binding.apply {
            imgBttnMigAny.visibility = View.VISIBLE
            imgBttnUnAny.visibility = View.VISIBLE
            txtViewMigAny.visibility = View.VISIBLE
            txtViewUnAny.visibility = View.VISIBLE
            medallMigAnyGris.visibility = View.INVISIBLE
            medallaUnAnyGris.visibility = View.INVISIBLE
        }
    }

    fun medallaEstalviadorVisible() {
        binding.apply {
            medallaEstalviadorGris.visibility = View.INVISIBLE
            imgBttnMedalla1.visibility = View.VISIBLE
            txtViewMedEstalviador.visibility = View.VISIBLE
        }
        view?.let { Snackbar.make(it, R.string.perdreMedalla, Snackbar.LENGTH_LONG).show() }


    }

    fun medallaGranEstalviadorVisible() {
        binding.apply {
            medallaGranEstalviadorGris.visibility = View.INVISIBLE
            imgBttnGranEstalviador.visibility = View.VISIBLE
            txtViewGranEstalviador.visibility = View.VISIBLE
        }
        view?.let { Snackbar.make(it, R.string.perdreMedalla, Snackbar.LENGTH_LONG).show() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mostrarMedallesTemps() {
        //consulta adepesa consum per a comprovar si s'han guañat les medalles
        val despesaConsumFirestore = db.collection("despesaConsum")

        val query = despesaConsumFirestore.whereEqualTo("mail", mail).get()
            .addOnSuccessListener { document ->
                val despesaConsumDC = document.toObjects(DespesaConsumDC::class.java)
                //Es comprova si el document despesa consum de l'usuari és null
                if (!despesaConsumDC.isNullOrEmpty()) {
                    //Si encara no s'han actualitzat els camps medalles entra a la funció per a fer la comprovació
                    if (!despesaConsumDC[0].medallaMigAny || !despesaConsumDC[0].medallaUnAny) {
                        medallesDeTemps()
                        //En cas que tingui com a true el camp mig any i false la medalla d'un any
                    } else if (despesaConsumDC[0].medallaUnAny) {
                        medallaUnAnyVisible()
                        //En el cas que estigui com a true la medalla d'un any
                    } else if (despesaConsumDC[0].medallaMigAny) {
                        medallaMigAnyVisible()
                        medallesDeTemps()
                    }
                }
            }
    }

    //Funció per a comprovar si s'han guanyat les medalles de temps
    @RequiresApi(Build.VERSION_CODES.O)
    fun medallesDeTemps() {

        //Consulta a la col·lecció despesaConsum
        val despesaConsumFirestore = db.collection("despesaConsum")
        //Obtenim el document de consum del usuari a partir del mail
        val query = despesaConsumFirestore.whereEqualTo("mail", mail).get()
            .addOnSuccessListener { document ->
                //Guardem el consum en un objecte
                val despesaConsumDC = document.toObjects(DespesaConsumDC::class.java)

                if (!despesaConsumDC.isNullOrEmpty()) {
                    //Guardem les dates de tots els consums en un arrayList
                    // per mostra les medalles (mig any estalviant i un any estalviant)
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
                    //Iterem a l'arrayList amb totes les dates per a extreure més petita
                    // i després poder-la comparar amb la més gran
                    var dateMenor1 = LocalDate.of(9999, 1, 1)
                    var dataMenor2 = LocalDate.of(9999, 1, 1)
                    var auxDataMenor: LocalDate = LocalDate.of(9999, 1, 1)

                    var dataMajor1 = LocalDate.of(1950, 1, 1)
                    var dataMajor2 = LocalDate.of(1950, 1, 1)
                    var auxDataMajor: LocalDate = LocalDate.of(1950, 1, 1)
                    for (x in dates) {
                        val formatCorrecte = x.replace(".", "-")
                        dataMenor2 = LocalDate.parse(formatCorrecte, DateTimeFormatter.ISO_DATE)
                        if (dateMenor1.isBefore(dataMenor2)) {
                            auxDataMenor = dateMenor1
                        } else {
                            auxDataMenor = dataMenor2
                        }
                        dateMenor1 = auxDataMenor
                        dataMajor2 = LocalDate.parse(formatCorrecte, DateTimeFormatter.ISO_DATE)
                        if (dataMajor1.isAfter(dataMajor2)) {
                            auxDataMajor = dataMajor1

                        } else {
                            auxDataMajor = dataMajor2
                        }

                        dataMajor1 = auxDataMajor
                    }
                    Log.d("dataMesPetita", dateMenor1.toString())
                    Log.d("dataMesGran", dataMajor1.toString())

                    //Comproven si l'usuari ha guanyat alguna medalla de temps (Mig any)
                    if (dataMajor1.toEpochDay() - dateMenor1.toEpochDay() >= 182) {
                        //En cas que hagi guanyat es truca a questa funció per a modificar el camp
                        //medalla (Boolean) a la BBDD
                        afegirMedallesDataBD(dateMenor1, dataMajor1)
                    }
                }

            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun afegirMedallesDataBD(dataMenor: LocalDate, dataMajor: LocalDate) {

        //Comprovem la difència per saber si ha guanyat alguna de les dues medalles
        val diferencia = dataMajor.toEpochDay() - dataMenor.toEpochDay()

        //Si ha guanyat la de mig any fa un update per a modifica el camp (Si el camp no hi és s'afegeix)
        if (diferencia.toInt() in 182..365) {
            medallaMigAnyVisible()
            val actualitza = db.collection("despesaConsum").addSnapshotListener { snapshot, e ->
                val doc = snapshot?.documents
                doc?.forEach {
                    val despesaConsumConsulta = it.toObject(DespesaConsumDC::class.java)
                    if (despesaConsumConsulta?.mail == mail) {
                        val usuariId = it.id
                        val sfDocRef = db.collection("despesaConsum").document(usuariId)

                        db.runTransaction { transaction ->
                            val despesaConsum = hashMapOf(
                                "medallaMigUnAny" to true,
                            )
                            transaction.set(sfDocRef, despesaConsum, SetOptions.merge())
                        }
                    }

                }
            }
            //Aquí es mira si s'ha guanyat la de un any estalviant i s'afageix a la BBDD
        } else if (diferencia >= 365) {
            medallaUnAnyVisible()
            val actualitza = db.collection("despesaConsum").addSnapshotListener { snapshot, e ->
                val doc = snapshot?.documents
                doc?.forEach {
                    val despesaConsumConsulta = it.toObject(DespesaConsumDC::class.java)
                    if (despesaConsumConsulta?.mail == mail) {
                        val usuariId = it.id
                        val sfDocRef = db.collection("despesaConsum").document(usuariId)

                        db.runTransaction { transaction ->
                            var despesaConsum: HashMap<String, Boolean>
                            if (!despesaConsumConsulta.medallaMigAny) {
                                despesaConsum = hashMapOf(
                                    "medallaMigAny" to true,
                                    "medallaUnAny" to true
                                )
                            } else {
                                despesaConsum = hashMapOf(
                                    "medallaUnAny" to true
                                )
                            }
                            transaction.set(sfDocRef, despesaConsum, SetOptions.merge())
                        }
                    }

                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun omplirDades() {
        val user = Firebase.auth.currentUser
        val mail = user?.email.toString()

        val energies = db.collection("despesaConsum")
        val query = energies.whereEqualTo("mail", mail).get()
            .addOnSuccessListener { document ->
                if (!document.isEmpty) {
                    var consumAigua = mapOf<String, Double>()
                    var aiguaDiners = mapOf<String, Double>()
                    var consumLlum = mapOf<String, Double>()
                    var llumDiners = mapOf<String, Double>()
                    var consumGas = mapOf<String, Double>()
                    var gasDiners = mapOf<String, Double>()
                    var consumGasoil = mapOf<String, Double>()
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

                    estalviadorTotal(dinersTotal)
                }
            }
    }

    fun estalviadorTotal(list: ArrayList<Double>): Double {
        var aux: Double? = null
        var estalviat: Double = 0.0
        for (item in list) {
            if (aux != null) {
                estalviat += (aux - item)
            }
            aux = item
        }

        Log.d("estalviat", estalviat.toString())
        if (estalviat >= -10) {
            medallaEstalviadorVisible()
        }
        if (estalviat >= -5) {
            medallaGranEstalviadorVisible()

        }
        return estalviat
    }

    fun arrayListDeValors(list: ArrayList<Double>, map: Map<String, Double>) {
        for (x in map) {
            list.add(x.value)
        }
    }
}

//Data class per a guardar el document despesaConsum
data class DespesaConsumDC(
    var aiguaConsum: Map<String, Double> = mapOf(),
    var llumConsum: Map<String, Double> = mapOf(),
    var gasConsum: Map<String, Double> = mapOf(),
    var gasoilConsum: Map<String, Double> = mapOf(),
    var aiguaDiners: Map<String, Double> = mapOf(),
    var llumDiners: Map<String, Double> = mapOf(),
    var gasDiners: Map<String, Double> = mapOf(),
    var gasoilDiners: Map<String, Double> = mapOf(),
    var mail: String = "",
    var medallaMigAny: Boolean = false,
    var medallaUnAny: Boolean = false,
    var medallaEstalviador: Boolean = false,
    var medallaGranEstalviador: Boolean = false
)