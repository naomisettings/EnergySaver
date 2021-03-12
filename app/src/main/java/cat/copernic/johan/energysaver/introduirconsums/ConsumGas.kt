package cat.copernic.johan.energysaver.introduirconsums

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentConsumGasBinding
import cat.copernic.johan.energysaver.utils.DatePickerFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase


class ConsumGas : Fragment() {
    private lateinit var binding: FragmentConsumGasBinding

    //instancia a firebase
    val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_consum_gas, container, false)

        //TRANSACTION
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 0.01.toLong()
        }

        binding.btnConfirmarConsumGas.setOnClickListener { view: View ->
            //retorn a menu energies i guardar dades
            if(binding.editTextConsumGas.text.isEmpty() || binding.editTextDataGas.text.isEmpty()
                || binding.editTextImportGas.text.isEmpty()){
                Snackbar.make(view, "Has d'omplir tots els camps", Snackbar.LENGTH_LONG).show()

            }else {
                guardarConsum()
                view.findNavController().navigate(R.id.action_consumGas_to_menuEnergies)
            }

        }
        binding.editTextDataGas.setOnClickListener { showDatePickerDialog() }

        return binding.root
    }

    //funcio per capturar el DataPicker en un dialog
    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            // +1 perque Gener es zero
            val selectedDate = year.toString() + "." + twoDigits(month + 1) + "." + twoDigits(day)
            binding.editTextDataGas.setText(selectedDate)

        })

        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }

    //funcio per passar a dos digits el mes i dia quan no ho son
    private fun twoDigits(n: Int): String? {
        return if (n <= 9) "0$n" else n.toString()
    }

    fun guardarConsum() {

        //guardem les dades de l'usari identificat
        val user = Firebase.auth.currentUser
        //agafem el mail com a identificador unic de l'usuari
        val mail = user?.email.toString()


        db.collection("despesaConsum").whereEqualTo("mail", mail).get()
            .addOnSuccessListener { doc ->
                //guardem els usuaris que hem trovat a l'objecte Usuari (Data Class)
                val usuariConsulta = doc.toObjects(DadesTotalsGas::class.java)
                //si el mail de l'usuari identificat coincideix amb un dels guardarts

                if (usuariConsulta.isNullOrEmpty()) {

                    var consumGasMap: HashMap<String, Double> = hashMapOf()
                    var importGasMap: HashMap<String, Double> = hashMapOf()

                    var consumGasEntrada = binding.editTextConsumGas.text.toString().toDouble()
                    var dataGasEntrada = binding.editTextDataGas.text.toString()
                    var importGasEntrada = binding.editTextImportGas.text.toString().toDouble()

                    consumGasMap.put(dataGasEntrada, consumGasEntrada)
                    importGasMap.put(dataGasEntrada, importGasEntrada)

                    //si no trova l'usuari identificat afegeix un nou document a la colleccio
                    val despesaConsum = hashMapOf(
                        "gasConsum" to consumGasMap,
                        "gasDiners" to importGasMap,
                        "mail" to mail
                    )

                    db.collection("despesaConsum").add(despesaConsum)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        /*    view?.let {
                                Snackbar.make(
                                    it,
                                    "Registre creat correctament",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                         */
                        }.addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error adding document", e)
                           /* view?.let {
                                Snackbar.make(
                                    it,
                                    "Error al crear el registre",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }

                            */

                        }

                } else {

                    var consumsGuardats = doc.toObjects(DadesTotalsGas::class.java)
                    var consumGasMap: HashMap<String, Double>
                    var importGasMap: HashMap<String, Double>
                    consumGasMap = consumsGuardats[0].gasConsum
                    importGasMap = consumsGuardats[0].gasDiners


                    var consumGasEntrada = binding.editTextConsumGas.text.toString().toDouble()
                    var dataGasEntrada = binding.editTextDataGas.text.toString()
                    var importGasEntrada = binding.editTextImportGas.text.toString().toDouble()

                    consumGasMap.put(dataGasEntrada, consumGasEntrada)
                    importGasMap.put(dataGasEntrada, importGasEntrada)
                    val despesaConsum = hashMapOf(
                        "gasConsum" to consumGasMap,
                        "gasDiners" to importGasMap,
                        "mail" to mail
                    )


                    doc?.forEach {

                        //guardem el id del document d'usuari identificat
                        val usuariId = it.id
                        val sfDocRef = db.collection("despesaConsum").document(usuariId)

                        //afegim un nou registre al document del usuari identificat
                        db.runTransaction { transaction ->
                            val snapshot = transaction.get(sfDocRef)
                            transaction.set(sfDocRef, despesaConsum, SetOptions.merge())

                        }

                    }

                }
            }
    }
}


data class DadesTotalsGas(
    var gasConsum: HashMap<String, Double> = hashMapOf(),
    var gasDiners: HashMap<String, Double> = hashMapOf(),
    var mail: String = ""
)