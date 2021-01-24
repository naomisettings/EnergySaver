package cat.copernic.johan.energysaver

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
import androidx.databinding.DataBindingUtil
import cat.copernic.johan.energysaver.databinding.ActivityAuthBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser


class AuthActivity : AppCompatActivity(), View.OnClickListener {




    //declarem una instància de FirebaseAuth
    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityAuthBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //capturem els botons
        binding.btnAccedir.setOnClickListener(this)
        binding.btnRegistre.setOnClickListener (this)
        binding.btnSortir.setOnClickListener (this)


       //inicialitzem la variable auth
        auth = Firebase.auth
    }

    //funcio que verifica que l'usuari ja accedit
    public override fun onStart() {
        super.onStart()
        //  Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser!=null){
            //reload()
            return
        }

    }
    //metode que rep per parameter el mail i la contrasenya per crear l'usuari
    private fun createAccount(email: String, password: String){
        if(!validateFormat()){
            return
        }
      //  showProgressBar()

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){
            task ->
            if(task.isSuccessful){
                //si accedeix correctament
                val user = auth.currentUser
                updateUI(user)
            }else{
                //si falla la validacio
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage("No s'ha pogut crear l'usuari")
                builder.setPositiveButton("Acceptar", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()

                updateUI(null)

            }
        }

    }
    //metode d'acces
    private fun signIn(email: String,password: String){
        Log.d("EmailPassword", "Acces amb el:$email")
        if(!validateFormat()){
            return
        }

       // showProgressBar()

        //validacio amb mail i contrasenya
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){
            task ->
            if (task.isSuccessful){
                //acces correcte, actalitzacio de la informacio de l'usuari
                val user = auth.currentUser
                updateUI(user)

            }else{
                //si falla l'acces
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage("Error amb l'autentificació ")
                builder.setPositiveButton("Acceptar", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()

                updateUI(null)

            }
        }

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
        updateUI(null)
    }
    //falta verificar funcionament correcte
    private fun updateUI(user: FirebaseUser?){
        if(user != null){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Acció correcte")
            builder.setMessage("S'ha creat l'usuari correctament")
            builder.setPositiveButton("Acceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
            //..

        }else{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Error")
            builder.setMessage("L'usuari no existeix, primer l'has de registrar ")
            builder.setPositiveButton("Acceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()

        }
    }
    //funcio per recarregar l'usuari
  /*  private fun reload() {
        auth.currentUser!!.reload().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                updateUI(auth.currentUser)
                Snackbar.make(findViewById(R.id.ConstraintLayout), "Recarrega correcte", Snackbar.LENGTH_SHORT).show()
            }
        }
    }*/
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
    //

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnRegistre -> {
                createAccount(binding.editTextCorreu.text.toString(), binding.editTextContrasenya.text.toString())

            }
            R.id.btnAccedir -> signIn(binding.editTextCorreu.text.toString(), binding.editTextContrasenya.text.toString())
            R.id.btnSortir -> signOUt()

        }

    }


}




