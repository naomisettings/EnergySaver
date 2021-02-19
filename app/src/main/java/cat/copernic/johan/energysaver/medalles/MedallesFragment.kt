package cat.copernic.johan.energysaver.medalles

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentMedallesBinding
import cat.copernic.johan.energysaver.modifica.ModificarUsuari
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

        binding.imgBttnMedalla1.visibility = View.INVISIBLE
        binding.imgBttnGranEstalviador.visibility = View.INVISIBLE

        binding.txtViewMedEstalviador.visibility = View.INVISIBLE
        binding.txtViewGranEstalviador.visibility = View.INVISIBLE

        val despesaConsumFirestore = db.collection("despesaConsum")

        val query = despesaConsumFirestore.whereEqualTo("mail", mail).get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    val despesaConsumDC = document.toObjects(DespesaConsumDC::class.java)

                    if (despesaConsumDC[0].medallaMigAny && !despesaConsumDC[0].medallaUnAny) {
                        binding.imgBttnMigAny.visibility = View.VISIBLE
                        binding.txtViewMigAny.visibility = View.VISIBLE
                        binding.txtViewUnAny.visibility = View.INVISIBLE
                        binding.medallMigAnyGris.visibility = View.INVISIBLE
                        binding.imgBttnUnAny.visibility = View.INVISIBLE
                    } else if (despesaConsumDC[0].medallaUnAny) {
                        binding.imgBttnMigAny.visibility = View.VISIBLE
                        binding.imgBttnUnAny.visibility = View.VISIBLE
                        binding.txtViewMigAny.visibility = View.VISIBLE
                        binding.txtViewUnAny.visibility = View.VISIBLE
                        binding.medallMigAnyGris.visibility = View.INVISIBLE
                        binding.medallaUnAnyGris.visibility = View.INVISIBLE
                    } else {
                        medallesDeTemps()
                    }
                }
            }

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

    //Funció per a mostra o no les medalles de temps
    @RequiresApi(Build.VERSION_CODES.O)
    fun medallesDeTemps() {

        //Consulta a la col·lecció despesaConsum
        val despesaConsumFirestore = db.collection("despesaConsum")
        //Obtenim el document de consum del usuari a partir del mail
        val query = despesaConsumFirestore.whereEqualTo("mail", mail).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    //Guardem el consum en un objecte
                    val despesaConsumDC = document.toObjects(DespesaConsumDC::class.java)
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

                    afegirMedallesDataBD(dateMenor1, dataMajor1)
                }

            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun afegirMedallesDataBD(dataMenor: LocalDate, dataMajor: LocalDate) {

        val diferencia = dataMajor.toEpochDay() - dataMenor.toEpochDay()

        if (diferencia in 183..365) {
            val actualitza = db.collection("despesaConsum").addSnapshotListener { snapshot, e ->
                val doc = snapshot?.documents
                doc?.forEach {
                    val despesaConsumConsulta = it.toObject(DespesaConsumDC::class.java)
                    if (despesaConsumConsulta?.mail == mail) {
                        val usuariId = it.id
                        val sfDocRef = db.collection("despesaConsum").document(usuariId)

                        db.runTransaction { transaction ->
                            transaction.update(
                                sfDocRef,
                                "medallaMigAny",
                                true
                            )
                            null
                        }
                    }

                }
            }
        } else if (diferencia >= 365) {
            val actualitza = db.collection("despesaConsum").addSnapshotListener { snapshot, e ->
                val doc = snapshot?.documents
                doc?.forEach {
                    val despesaConsumConsulta = it.toObject(DespesaConsumDC::class.java)
                    if (despesaConsumConsulta?.mail == mail) {
                        val usuariId = it.id
                        val sfDocRef = db.collection("despesaConsum").document(usuariId)

                        db.runTransaction { transaction ->
                            transaction.update(
                                sfDocRef,
                                "medallaUnAny",
                                true
                            )
                            null
                        }
                    }

                }
            }
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
    var mail: String = "",
    var medallaMigAny: Boolean = false,
    var medallaUnAny: Boolean = false,
    var medallaEstalviador: Boolean = false,
    var medallaGranEstalviador: Boolean = false
)