package cat.copernic.johan.energysaver.obrirtiquet

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog.show
import android.content.ComponentName
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentObrirBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class ObrirTiquetFragment : Fragment() {

    private lateinit var binding: FragmentObrirBinding
    val db = FirebaseFirestore.getInstance()
    var titol: String = ""
    var descripcio: String = ""
    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_obrir, container, false)

        //permisos imatge pujada
        /*
        binding.imgBttnCarregaImatge.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, 1001);
                }
                else{
                    //permission already granted
                   // pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                //pickImageFromGallery();
            }

        }
         */

        binding.imgBttnCarregaImatge.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
            /*
// Create a storage reference from our app
            val storage = Firebase.storage
            val storageRef = storage.reference

// Create a storage reference from our app
            val storageRef = storage.reference

// Create a reference to "mountains.jpg"
            val mountainsRef = storageRef.child("mountains.jpg")

// Create a reference to 'images/mountains.jpg'
            val mountainImagesRef = storageRef.child("images/mountains.jpg")

// While the file names are the same, the references point to different files
            mountainsRef.name == mountainImagesRef.name // true
            mountainsRef.path == mountainImagesRef.path // false

            val file = File(ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                imageUri.toString())

            val uri = Uri.fromFile(file)
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            var uploadTask = mountainsRef.putBytes(data)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
            }

             */
        }
        //Botó confirmar que truca a la funció per inserir dades al firestore
        binding.bttnConfirmarTiquet.setOnClickListener {
            rebreDades(it)
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
        }
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
            Snackbar.make(view, R.string.campsBuitsToastObrirTiquet, Snackbar.LENGTH_LONG).show()
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
        val data = Calendar.getInstance().time
        val formatterdt = SimpleDateFormat("yyyy.MM.dd")
        val formatterhr = SimpleDateFormat("HH:mm:ss")
        val formatedDate = formatterdt.format(data)
        val formatedHour = formatterhr.format(data)

        //Map per fer l'insert
        val tiquet = hashMapOf(
            "mail" to mail,
            "nickname" to nickname,
            "data" to formatedDate,
            "hora" to formatedHour,
            "titol" to titol,
            "descripcio" to descripcio
        )

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
}


data class Usuari(
    var adreca: String = "", var cognoms: String = "", var contrasenya: String = "",
    var mail: String = "", var nickname: String = "", var nom: String = "",
    var poblacio: String = "", var telefon: String = ""
)