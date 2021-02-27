package cat.copernic.johan.energysaver.introduirconsums

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentConsumLlumBinding
import cat.copernic.johan.energysaver.utils.DatePickerFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase


class ConsumLlum : Fragment() {
    private lateinit var binding: FragmentConsumLlumBinding

    //instancia a firebase
    val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_consum_llum, container, false)

        //TRANSACTION
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }

        binding.btnConfirmarConsumLlum.setOnClickListener { view: View ->
            //retorn a menu energies i guardar dades
            if(binding.editTextConsumLlum.text.isEmpty() || binding.editTextDataLLum.text.isEmpty()
                || binding.editTextImportLlum.text.isEmpty()){
                Snackbar.make(view, "Has d'omplir tots els camps", Snackbar.LENGTH_LONG).show()

            }else {
                guardarConsum()
                view.findNavController().navigate(R.id.action_consumLlum_to_menuEnergies)
            }

        }
        binding.editTextDataLLum.setOnClickListener { showDatePickerDialog() }

        return binding.root
    }

    //funcio per capturar el DataPicker en un dialog
    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            // +1 perque Gener es zero
            val selectedDate = year.toString() + "." + twoDigits(month + 1) + "." + twoDigits(day)
            binding.editTextDataLLum.setText(selectedDate)

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
                val usuariConsulta = doc.toObjects(DadesTotalsLlum::class.java)
                //si el mail de l'usuari identificat coincideix amb un dels guardarts

                if (usuariConsulta.isNullOrEmpty()) {

                    var consumLlumMap: HashMap<String, Double> = hashMapOf()
                    var importLlumMap: HashMap<String, Double> = hashMapOf()

                    var consumLlumEntrada = binding.editTextConsumLlum.text.toString().toDouble()
                    var dataLlumEntrada = binding.editTextDataLLum.text.toString()
                    var importLlumEntrada = binding.editTextImportLlum.text.toString().toDouble()

                    consumLlumMap.put(dataLlumEntrada, consumLlumEntrada)
                    importLlumMap.put(dataLlumEntrada, importLlumEntrada)

                    //si no trova l'usuari identificat afegeix un nou document a la colleccio
                    val despesaConsum = hashMapOf(
                        "llumConsum" to consumLlumMap,
                        "llumDiners" to importLlumMap,
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

                    var consumsGuardats = doc.toObjects(DadesTotalsLlum::class.java)
                    var consumLlumMap: HashMap<String, Double>
                    var importLlumMap: HashMap<String, Double>
                    consumLlumMap = consumsGuardats[0].llumConsum
                    importLlumMap = consumsGuardats[0].llumDiners


                    var consumLlumEntrada = binding.editTextConsumLlum.text.toString().toDouble()
                    var dataLlumEntrada = binding.editTextDataLLum.text.toString()
                    var importLlumEntrada = binding.editTextImportLlum.text.toString().toDouble()

                    consumLlumMap.put(dataLlumEntrada, consumLlumEntrada)
                    importLlumMap.put(dataLlumEntrada, importLlumEntrada)
                    val despesaConsum = hashMapOf(
                        "llumConsum" to consumLlumMap,
                        "llumDiners" to importLlumMap,
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


data class DadesTotalsLlum(
    var llumConsum: HashMap<String, Double> = hashMapOf(),
    var llumDiners: HashMap<String, Double> = hashMapOf(),
    var mail: String = ""
)