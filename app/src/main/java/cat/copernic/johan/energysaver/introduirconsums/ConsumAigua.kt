package cat.copernic.johan.energysaver.introduirconsums

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentConsumAiguaBinding
import cat.copernic.johan.energysaver.utils.DatePickerFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase


class ConsumAigua : Fragment() {
    private lateinit var binding: FragmentConsumAiguaBinding

    //instancia a firebase
    val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_consum_aigua, container, false)

        binding.btnConfirmarConsumAigua.setOnClickListener { view: View ->
            //retorn a menu energies i guardar dades
            if(binding.editTextConsumAigua.text.isEmpty() || binding.editTextDataAigua.text.isEmpty()
                || binding.editTextImportAigua.text.isEmpty()){
                Snackbar.make(view, "Has d'omplir tots els camps", Snackbar.LENGTH_LONG).show()

            }else {
                guardarConsum()
                view.findNavController().navigate(R.id.action_consumAigua_to_menuEnergies)
            }

        }
        binding.editTextDataAigua.setOnClickListener { showDatePickerDialog() }

        return binding.root
    }

    //funcio per capturar el DataPicker
    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            Log.d("mes", month.toString())
            // +1 perque Gener es zero
            if(month <=9){
                val selectedDate = year.toString() + "." + "0" + (month + 1) + "." + day
                binding.editTextDataAigua.setText(selectedDate)

            }else if(month >=10){
                val selectedDate = year.toString() + "."  + (month + 1) + "." + day
                binding.editTextDataAigua.setText(selectedDate)

            }

        })

        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
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

                    var consumAiguaMap: HashMap<String, Double> = hashMapOf()
                    var importAiguaMap: HashMap<String, Double> = hashMapOf()

                    var consumAiguaEntrada = binding.editTextConsumAigua.text.toString().toDouble()
                    var dataAiguaEntrada = binding.editTextDataAigua.text.toString()
                    var importAiguaEntrada = binding.editTextImportAigua.text.toString().toDouble()

                    consumAiguaMap.put(dataAiguaEntrada, consumAiguaEntrada)
                    importAiguaMap.put(dataAiguaEntrada, importAiguaEntrada)

                    //si no trova l'usuari identificat afegeix un nou document a la colleccio
                    val despesaConsum = hashMapOf(
                        "aiguaConsum" to consumAiguaMap,
                        "aiguaDiners" to importAiguaMap,
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

                    var consumsGuardats = doc.toObjects(DadesTotals::class.java)
                    var consumAiguaMap: HashMap<String, Double>
                    var importAiguaMap: HashMap<String, Double>
                    consumAiguaMap = consumsGuardats[0].aiguaConsum
                    importAiguaMap = consumsGuardats[0].aiguaDiners


                    var consumAiguaEntrada = binding.editTextConsumAigua.text.toString().toDouble()
                    var dataAiguaEntrada = binding.editTextDataAigua.text.toString()
                    var importAiguaEntrada = binding.editTextImportAigua.text.toString().toDouble()

                    consumAiguaMap.put(dataAiguaEntrada, consumAiguaEntrada)
                    importAiguaMap.put(dataAiguaEntrada, importAiguaEntrada)
                    val despesaConsum = hashMapOf(
                        "aiguaConsum" to consumAiguaMap,
                        "aiguaDiners" to importAiguaMap,
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


data class DadesTotals(
    var aiguaConsum: HashMap<String, Double> = hashMapOf(),
    var aiguaDiners: HashMap<String, Double> = hashMapOf(),
    var mail: String = ""
)

