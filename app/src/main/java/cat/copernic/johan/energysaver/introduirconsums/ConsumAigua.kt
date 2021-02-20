package cat.copernic.johan.energysaver.introduirconsums

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
import com.google.android.material.snackbar.Snackbar

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase


class ConsumAigua : Fragment() {
    private lateinit var binding: FragmentConsumAiguaBinding
    private lateinit var auth: FirebaseAuth

    //instancia a firebase
    val db = FirebaseFirestore.getInstance()
    var consumAigua: String = ""
    var importAigua: String = ""
    var dataAigua: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_consum_aigua, container, false)

        binding.btnConfirmarConsumAigua.setOnClickListener { view: View ->
            //retorn a menu energies i guardar dades
            guardarConsum()
            // view.findNavController().navigate(R.id.action_consumAigua_to_menuEnergies)
        }

        return binding.root
    }

    fun guardarConsum() {

        //guardem les dades de l'usari identificat
        val user = Firebase.auth.currentUser
        //agafem el mail com a identificador unic de l'usuari
        val mail = user?.email.toString()
        //actualitza per establir un camp de col·lecció despesaConsum
        val actualitza = db.collection("despesaConsum").addSnapshotListener { snapshot, error ->
            //guardem els documents dels usuaris
            val doc = snapshot?.documents

            //iterem pels documents dels usuaris
            doc?.forEach {
                //guardem els usuaris que hem trovat a l'objecte Usuari (Data Class)
                val usuariConsulta = it.toObject(DadesTotals::class.java)


                //si el mail de l'usuari identificat coincideix amb un dels guardarts
                if (usuariConsulta?.mail == mail) {
                    //guardem el id del document d'usuari identificat
                    val usuariId = it.id
                    Log.d("id document usuari", usuariId)
                    db.collection("despesaConsum").whereEqualTo("mail", mail).get()
                        .addOnSuccessListener { doc ->
                            var consumsGuardats = doc.toObjects(DadesTotals::class.java)
                            var consumAiguaMap: HashMap<String, String>
                            var importAiguaMap: HashMap<String, String>
                            consumAiguaMap = consumsGuardats[0].aiguaConsum
                            importAiguaMap = consumsGuardats[0].aiguaDiners

                            var consumAiguaEntrada = binding.editTextConsumAigua.text.toString()
                            var dataAiguaEntrada = binding.editTextDataAigua.text.toString()
                            var importAiguaEntrada = binding.editTextImportAigua.text.toString()

                            consumAiguaMap.put(dataAiguaEntrada, consumAiguaEntrada)
                            importAiguaMap.put(dataAiguaEntrada, importAiguaEntrada)
                            val despesaConsum = hashMapOf(
                                "aiguaConsum" to consumAiguaMap,
                                "aiguaDiners" to importAiguaMap,
                                "mail" to mail
                            )
                            Log.d("despesaconsum", despesaConsum.toString())
                            val sfDocRef = db.collection("despesaConsum").document(usuariId)
                            Log.d("sfDocRef", sfDocRef.toString())
                            //afegim un nou registre al document del usuari identificat
                            db.runTransaction { transaction ->

                                Log.d("despesaconsum", despesaConsum.toString())

                                Log.d("sfDocRef", sfDocRef.id)

                                val snapshot = transaction.get(sfDocRef)
                                Log.d("id document", snapshot.id)
                                transaction.set(sfDocRef, despesaConsum)
                                
                                Log.d(" dades afegir document", sfDocRef.toString())
                            }
                        }
                } else {
                    var consumAiguaMap: HashMap<String, String> = hashMapOf()
                    var importAiguaMap: HashMap<String, String> = hashMapOf()
                    Log.d("Map Aigua", consumAiguaMap.toString())

                    var consumAiguaEntrada = binding.editTextConsumAigua.text.toString()
                    var dataAiguaEntrada = binding.editTextDataAigua.text.toString()
                    var importAiguaEntrada = binding.editTextImportAigua.text.toString()

                    consumAiguaMap.put(dataAiguaEntrada, consumAiguaEntrada)
                    importAiguaMap.put(dataAiguaEntrada, importAiguaEntrada)
                    Log.d("Map Aigua", consumAiguaMap.toString())
                    Log.d("Map Aigua", importAiguaMap.toString())


                    //si no trova l'usuari identificat afegeix un nou document a la colleccio
                    val despesaConsum = hashMapOf(
                        "aiguaConsum" to consumAiguaMap,
                        "aiguaDiners" to importAiguaMap,
                        "mail" to mail
                    )

                    Log.d("Map", despesaConsum.toString())
                    db.collection("despesaConsum").add(despesaConsum)
                    /*  .addOnSuccessListener { documentReference ->
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

                      }*/

                }
            }
        }
    }
}

data class DadesTotals(
    var aiguaConsum: HashMap<String, String> = hashMapOf(),
    var aiguaDiners: HashMap<String, String> = hashMapOf(),
    var mail: String = ""
)

