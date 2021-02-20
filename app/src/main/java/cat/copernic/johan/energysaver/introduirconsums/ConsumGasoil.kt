package cat.copernic.johan.energysaver.introduirconsums

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentConsumGasoilBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class ConsumGasoil : Fragment() {
    private lateinit var binding: FragmentConsumGasoilBinding

    //instancia a firebase
    val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_consum_gasoil, container, false)

        binding.btnConfirmarConsumGasoil.setOnClickListener { view: View ->
            //retorn a menu energies i guardar dades
            if(binding.editTextConsumGasoil.text.isEmpty() || binding.editTextDataGasoil .text.isEmpty()
                || binding.editTextImportGasoil.text.isEmpty()){
                Snackbar.make(view, "Has d'omplir tots els camps", Snackbar.LENGTH_LONG).show()

            }else {
                guardarConsum()
                view.findNavController().navigate(R.id.action_consumGasoil_to_menuEnergies)
            }

        }

        return binding.root
    }

    fun guardarConsum() {

        //guardem les dades de l'usari identificat
        val user = Firebase.auth.currentUser
        //agafem el mail com a identificador unic de l'usuari
        val mail = user?.email.toString()


        db.collection("despesaConsum").whereEqualTo("mail", mail).get()
            .addOnSuccessListener { doc ->
                //guardem els usuaris que hem trovat a l'objecte Usuari (Data Class)
                val usuariConsulta = doc.toObjects(DadesTotalsGasoil::class.java)
                //si el mail de l'usuari identificat coincideix amb un dels guardarts

                if (usuariConsulta.isNullOrEmpty()) {

                    var consumGasoilMap: HashMap<String, Double> = hashMapOf()
                    var importGasoilMap: HashMap<String, Double> = hashMapOf()

                    var consumGasoilEntrada = binding.editTextConsumGasoil.text.toString().toDouble()
                    var dataGasoilEntrada = binding.editTextDataGasoil.text.toString()
                    var importGasoilEntrada = binding.editTextImportGasoil.text.toString().toDouble()

                    consumGasoilMap.put(dataGasoilEntrada, consumGasoilEntrada)
                    importGasoilMap.put(dataGasoilEntrada, importGasoilEntrada)

                    //si no trova l'usuari identificat afegeix un nou document a la colleccio
                    val despesaConsum = hashMapOf(
                        "gasoilConsum" to consumGasoilMap,
                        "gasoilDiners" to importGasoilMap,
                        "mail" to mail
                    )

                    db.collection("despesaConsum").add(despesaConsum)
                        .addOnSuccessListener { documentReference ->
                            view?.let {
                                Snackbar.make(
                                    it,
                                    "Registre creat correctament",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }.addOnFailureListener { e ->
                            view?.let {
                                Snackbar.make(
                                    it,
                                    "Error al crear el registre",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }

                        }

                } else {

                    var consumsGuardats = doc.toObjects(DadesTotalsGasoil::class.java)
                    var consumGasoilMap: HashMap<String, Double>
                    var importGasoilMap: HashMap<String, Double>
                    consumGasoilMap = consumsGuardats[0].gasoilConsum
                    importGasoilMap = consumsGuardats[0].gasoilDiners


                    var consumGasoilEntrada = binding.editTextConsumGasoil.text.toString().toDouble()
                    var dataGasoilEntrada = binding.editTextDataGasoil.text.toString()
                    var importGasoilEntrada = binding.editTextImportGasoil.text.toString().toDouble()

                    consumGasoilMap.put(dataGasoilEntrada, consumGasoilEntrada)
                    importGasoilMap.put(dataGasoilEntrada, importGasoilEntrada)
                    val despesaConsum = hashMapOf(
                        "gasoilConsum" to consumGasoilMap,
                        "gasoilDiners" to importGasoilMap,
                        "mail" to mail
                    )


                    doc?.forEach {

                        //guardem el id del document d'usuari identificat
                        val usuariId = it.id
                        val sfDocRef = db.collection("despesaConsum").document(usuariId)

                        //afegim un nou registre al document del usuari identificat
                        db.runTransaction { transaction ->
                            val snapshot = transaction.get(sfDocRef)
                            transaction.set(sfDocRef, despesaConsum)

                        }

                    }

                }
            }
    }
}


data class DadesTotalsGasoil(
    var gasoilConsum: HashMap<String, Double> = hashMapOf(),
    var gasoilDiners: HashMap<String, Double> = hashMapOf(),
    var mail: String = ""
)