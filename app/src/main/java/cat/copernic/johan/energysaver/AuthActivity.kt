package cat.copernic.johan.energysaver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import androidx.databinding.DataBindingUtil
import cat.copernic.johan.energysaver.databinding.ActivityAuthBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

private const val TAG = "GoogleActivity"
private const val RC_SIGN_IN = 100

class AuthActivity : AppCompatActivity(), View.OnClickListener {

    //declarem una instància de FirebaseAuth
    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityAuthBinding
    private lateinit var googleSignInClient: GoogleSignInClient




    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.Theme_EnergySaver)
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //capturem els botons
        binding.btnAccedir.setOnClickListener(this)
        binding.btnRegistre.setOnClickListener (this)
        binding.btnSortir.setOnClickListener (this)
        binding.btnGoogle.setOnClickListener (this)



       //inicialitzem la variable auth
        auth = Firebase.auth
    }

    //funcio que verifica que l'usuari ja accedit
    public override fun onStart() {
        super.onStart()
        //  comprovem si l'usuari esta loginat
        val currentUser = auth.currentUser

        if(currentUser!=null){
            reload()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Resultat que retorna de L'Intent de GoogleSignInApi.getSignInIntent(...)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // L'access amb google ha sigut un exit
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Success")
                builder.setMessage("S'ha connectat a google correctament")
                builder.setPositiveButton("Acceptar", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // L'acces amb google ha fallat
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage("L'acces amb google ha fallat")
                builder.setPositiveButton("Acceptar", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()

            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Si l'usuari es logejat correctament, mostra un log amb les dades de l'usuari
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser

                } else {
                    // Si falla, mostra Log de l'error
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    // [START_EXCLUDE]
                    val view = binding.root
                    // [END_EXCLUDE]
                    Snackbar.make(view, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()

                }
            }
    }
    //metode que rep per parametre el mail i la contrasenya per crear l'usuari
    private fun createAccount(email: String, password: String){
        if(!validateFormat()){
            return
        }


        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){
            task ->
            if(task.isSuccessful){
                //si accedeix correctament
                val user = auth.currentUser
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Success")
                builder.setMessage("Usuari creat correctament")
                builder.setPositiveButton("Acceptar", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()

            }else {
                //si falla la validacio
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage("Usuari incorrecte o ja existeix")
                builder.setPositiveButton("Acceptar", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()


            }
        }

    }
    //metode d'acces
    private fun signIn(email: String,password: String){
        Log.d("EmailPassword", "Acces amb el:$email")
        if(!validateFormat()){
            return
        }

        //validacio amb mail i contrasenya
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){
            task ->
            if (task.isSuccessful){
                //acces correcte, actalitzacio de la informacio de l'usuari
                val user = auth.currentUser
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Success")
                builder.setMessage("S'ha loginat correctament")
                builder.setPositiveButton("Acceptar", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()


            }else{
                //si falla l'acces
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage("Error amb l'autentificació, usuario o contrasenya no vàlids ")
                builder.setPositiveButton("Acceptar", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()


            }
        }

    }
    //Inicia sessió amb google
    private fun signInWithGoogle() {
        //Configurem el google sign in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    //metode per tanca sessió
    private fun signOUt(){
        auth.signOut()
        binding.editTextCorreu.setText("")
        binding.editTextContrasenya.setText("")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Tancar sessió")
        builder.setMessage("Has tancat sessió")
        builder.setPositiveButton("Acceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

        googleSignInClient.signOut().addOnCompleteListener(this) {

        }
    }

    //funcio per recarregar l'usuari
    private fun reload() {
        auth.currentUser!!.reload().addOnCompleteListener { task ->
            if (task.isSuccessful) {

                Snackbar.make(findViewById(R.id.ConstraintLayout), "Recarrega correcte", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    //metode que valida que els camps no estiguin buits
    private fun validateFormat(): Boolean{
        var valid = true
        val email: String = binding.editTextCorreu.text.toString()
        if(TextUtils.isEmpty(email)){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Error")
            builder.setMessage("Has de posar un correu electrònic vàlid")
            builder.setPositiveButton("Acceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()

            valid = false
        }else{
            binding.editTextCorreu.error = null
        }
        val password: String = binding.editTextContrasenya.text.toString()
        if (TextUtils.isEmpty(password)){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Error")
            builder.setMessage("Has de posar una contrasenya vàlida")
            builder.setPositiveButton("Acceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()

            valid = false

                }else{
            binding.editTextContrasenya.error = null
        }
        return valid
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnRegistre -> {
                createAccount(binding.editTextCorreu.text.toString(), binding.editTextContrasenya.text.toString())

            }
            R.id.btnAccedir -> signIn(binding.editTextCorreu.text.toString(), binding.editTextContrasenya.text.toString())
            R.id.btnSortir -> signOUt()
            R.id.btnGoogle -> signInWithGoogle()
        }

    }


}




