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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
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
            view.findNavController().navigate(R.id.action_consumAigua_to_menuEnergies)
        }

        return binding.root
    }

    fun guardarConsum() {

        //guardem les dades de l'usari identificat
        val user = Firebase.auth.currentUser
        //agafem el mail com a identificador unic de l'usuari
        val mail = user?.email.toString()

        val consums = db.collection("despesaConsum")
        val query = consums.whereEqualTo("mail", mail).get().addOnSuccessListener { document ->
            //guardem els documents dels usuaris


            val usuariConsulta = document.toObjects(dadesTotals::class.java)
            //si el mail de l'usuari identificat coincideix amb un dels guardarts
            if (document != null) {

                //agafem l'usuari de la collecio amb el seu ID
                val sfDocRef = db.collection("despesaConsum").document(mail)
                Log.d("Document", sfDocRef.get().toString())
                /*val despesaConsum = hashMapOf(
                    "aiguaConsum" to ,
                    "aiguaDiners" to dadesImportAigua(dataAigua, importAigua),
                    "mail" to mail
                )
                //afegim un nou registre al document del usuari identificat
                db.runTransaction { transaction ->
                    //agafem el ID
                    val document = transaction.get(sfDocRef)
                    val newConsum = document.getString("mail")!!
                    //  Log.d("nou usuari", newConsum)
                    transaction.set(sfDocRef, despesaConsum)
                    Log.d(" dades afegir document", sfDocRef.toString())
                }*/
            } else {
                var consumAiguaMap: HashMap<String,String> = hashMapOf()
                var importAiguaMap: HashMap<String,String> = hashMapOf()

                var consumAiguaEntrada = binding.editTextConsumAigua.text.toString()
                var dataAiguaEntrada = binding.editTextDataAigua.text.toString()
                var importAiguaEntrada = binding.editTextImportAigua.text.toString()

                consumAiguaMap.put(dataAiguaEntrada,consumAiguaEntrada)
                importAiguaMap.put(dataAiguaEntrada,importAiguaEntrada)


                //si no trova l'usuari identificat afegeix un nou document a la colleccio
                val despesaConsum = hashMapOf(
                    "aiguaConsum" to consumAiguaMap,
                    "aiguaDiners" to importAiguaMap,
                    "mail" to mail
                )
                Log.d("Map", despesaConsum.toString())
                db.collection("despesaConsum").add(despesaConsum)
                    /*.addOnSuccessListener { documentReference ->
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


    //validar camps
    /* if(consumAigua.isEmpty() || importAigua.isEmpty() || dataAigua.isEmpty()){
            view?.let { Snackbar.make(it, "Has d'omplir tots els camps", Snackbar.LENGTH_LONG).show() }

        }else{

            val despesaConsum = hashMapOf(
                "aiguaConsum" to dadesConsumAigua(dataAigua,consumAigua),
                "aiguaDiners" to dadesImportAigua(dataAigua,importAigua),
                "mail" to mail
            )

            db.collection("despesaConsum").add(despesaConsum).addOnSuccessListener { documentReference ->
                view?.let { Snackbar.make(it, "Registre creat correctament", Snackbar.LENGTH_LONG).show() }
            }.addOnFailureListener{ e->
                view?.let { Snackbar.make(it, "Error al crear el registre", Snackbar.LENGTH_LONG).show() }

            }
        }*/

}



    //data class pel cosum d l'usuari
 /*   data class dadesConsumAigua(
        var dataAigua: String = "", var consumAigua: String = ""
       
    )
    data class dadesImportAigua(
        var dataAigua: String = "", var importAigua: String = ""

    )*/
    data class dadesTotals(
        var aiguaConsum: Map<String, String> = mapOf(),
        var aiguaDiners: Map<String, String> = mapOf(),
        var mail: String = ""
    )

