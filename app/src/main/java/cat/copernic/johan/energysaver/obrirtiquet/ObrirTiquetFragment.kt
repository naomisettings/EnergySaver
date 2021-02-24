package cat.copernic.johan.energysaver.obrirtiquet

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentObrirBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ObrirTiquetFragment : Fragment() {

    private lateinit var binding: FragmentObrirBinding
    val db = FirebaseFirestore.getInstance()
    var titol: String = ""
    var descripcio: String = ""
    private val GALLERY_REQUEST_CODE: Int = 101

    init {
        Singleton.nameImg
        Singleton.rutaImg
    }

    //nom arxiu de la imatge a pujar al storage
    var fileName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_obrir, container, false)

        //Pujar imatge al storage
        binding.imgBttnCarregaImatge.setOnClickListener {
            Singleton.nameImg = ""
            selectImageFromGallery()
            Toast.makeText(context, R.string.imatgeCarrgada, Toast.LENGTH_SHORT).show()
        }

        binding.imgBttnCamera.setOnClickListener {
            val intent = Intent(activity, ObrirTiquetActivity::class.java).apply {}
            startActivity(intent)

        }

        //Snackbar carregar imatge carregada
        if (Singleton.nameImg != ""){
            view?.let { Snackbar.make(it, R.string.imatgeCarrgada, Snackbar.LENGTH_SHORT).show() }
        }

        //Botó confirmar que truca a la funció per inserir dades al firestore
        binding.bttnConfirmarTiquet.setOnClickListener {
            val nomFoto = Singleton.nameImg
            val ruta = Singleton.rutaImg
            if (nomFoto != "") {
                if (ruta != null) {
                    uploadImageToFirebase(ruta)
                }
            }
            rebreDades(it)

            view?.findNavController()
                ?.navigate(R.id.action_obrirFragment_to_veureFragment)
        }

        return binding.root
    }

    fun rebreDades(view: View) {

        //Agafar dades del editText titol i descripció
        binding.apply {
            titol = editTextTemaTiquet.text.toString()
            descripcio = editTxtDescripcioTiquet.text.toString()
        }
        //Comprova que els camps esitguin emplenats
        if (titol.isEmpty() || descripcio.isEmpty()) {
            Log.w("ObrirTiquetFragment", "Entra fun rebre dades")
            Snackbar.make(view, R.string.campsBuitsToastObrirTiquet, Snackbar.LENGTH_LONG)
                .show()
        } else {

            //Guarda les dades del usuari connectat a la constant user
            val user = Firebase.auth.currentUser

            //Guarda el mail del usuari que ha fet login
            val mail = user?.email.toString()

            //Consulta per extreure el nickname per guardar-lo al document tiquet
            val usuaris = db.collection("usuaris")
            val query = usuaris.whereEqualTo("mail", mail).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val usuari = document.toObjects(Usuari::class.java)
                        val nickname = usuari[0].nickname

                        guardarDadesFirestore(nickname, mail)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun guardarDadesFirestore(nickname: String, mail: String) {
        //Extreu la data i hora del sistema per guardar al document tiquet
        val id = UUID.randomUUID().toString()


        val data = Calendar.getInstance().time
        val formatterdt = SimpleDateFormat("yyyy.MM.dd")
        val formatterhr = SimpleDateFormat("HH:mm:ss")
        val formatedDate = formatterdt.format(data)
        val formatedHour = formatterhr.format(data)

        //Accedir al nom de la foto feta amb la camera
        val nameFoto = Singleton.nameImg
        var tiquet: HashMap<String, String>

        //En el cas que s'hagi fet una foto amb la camera entrarà al else
        if (nameFoto == "") {
            //Map per fer l'insert
            tiquet = hashMapOf(
                "id" to id,
                "mail" to mail,
                "nickname" to nickname,
                "data" to formatedDate,
                "hora" to formatedHour,
                "titol" to titol,
                "descripcio" to descripcio,
                "imatge" to fileName,
                "resposta" to ""
            )
        } else {
            tiquet = hashMapOf(
                "id" to id,
                "mail" to mail,
                "nickname" to nickname,
                "data" to formatedDate,
                "hora" to formatedHour,
                "titol" to titol,
                "descripcio" to descripcio,
                "imatge" to nameFoto,
                "resposta" to ""
            )
        }

        Log.i("nomarxiu", fileName)

        //Neteja dels camps tema i descripció
        binding.apply {
            editTextTemaTiquet.text.clear()
            editTxtDescripcioTiquet.text.clear()
        }

        //Incerció a la col·lecció tiquet
        db.collection("tiquet")
            .add(tiquet)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    private fun selectImageFromGallery() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Please select..."
            ),
            GALLERY_REQUEST_CODE
        )
    }

    //Generar uri per a la imatge
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (requestCode == GALLERY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
            && data != null
            && data.data != null
        ) {

            // Get the Uri of data
            val file_uri = data.data!!

            if (file_uri != null) {
                uploadImageToFirebase(file_uri)
            }
        }
    }

    //Pujar la imatge al storage
    private fun uploadImageToFirebase(fileUri: Uri) {
        //Generar un nom per a la imatge
        val nameFoto = Singleton.nameImg

        Log.d("imguri", fileUri.toString())
        if (nameFoto == "") {
            fileName = SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        } else {
            fileName = nameFoto
        }


        //val database = FirebaseDatabase.getInstance()
        //Crear una referencia per pujar la imatge
        val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")

        refStorage.putFile(fileUri)
            .addOnSuccessListener(
                OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                        val imageUrl = it.toString()
                    }
                })

            ?.addOnFailureListener(OnFailureListener { e ->
                print(e.message)
            })
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}


//Classe que correspon als camps de la col·lecció usuaris
data class Usuari(
    var adreca: String = "", var cognoms: String = "", var contrasenya: String = "",
    var mail: String = "", var nickname: String = "", var nom: String = "",
    var poblacio: String = "", var telefon: String = ""
)
