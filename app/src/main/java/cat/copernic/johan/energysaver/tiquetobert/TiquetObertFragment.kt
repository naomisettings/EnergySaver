package cat.copernic.johan.energysaver.tiquetobert

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentTiquetObertBinding
import cat.copernic.johan.energysaver.veuretiquet.*
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import java.io.InputStream

@GlideModule
class TiquetObertFragment : Fragment() { //

    val db = FirebaseFirestore.getInstance()
    lateinit var binding: FragmentTiquetObertBinding
    lateinit var args: TiquetObertFragmentArgs
var admin: String = "hola"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tiquet_obert, container, false
        )

        args = TiquetObertFragmentArgs.fromBundle(requireArguments())

        binding.txtViewObrirTiquetMotiu.text = args.titol
        binding.txtViewTiquetObertDesc.text = args.descripcio

        //PujarImatge().pujar(binding, args.imatge)
/*
        if (args.imatge !== null) {
            Glide.with(this)
                .load(args.imatge).override(100, 280).placeholder(android.R.drawable.progress_indeterminate_horizontal).error(android.R.drawable.stat_notify_error)
                .into(binding.imgViewCarregarBBDD)
        } else {
            binding.imgViewCarregarBBDD.setImageResource(R.drawable.common_full_open_on_phone)
        }
 */
        GlideAppModule().setImageFromUrl(binding.imgViewCarregarBBDD, args.imatge)

        binding.bttnResposta.visibility = View.INVISIBLE
        adminVeureBttn()
        Log.d("porva", "${admin}blablabal")
        veureResposata(args.tiquetId)

        return binding.root
    }

    fun adminVeureBttn() {
        //Guarda les dades del usuari connectat a la constant user
        val user = Firebase.auth.currentUser

        //Guarda el mail del usuari que ha fet login
        val mail = user?.email.toString()

        //Consulta per extreure el nickname per guardar-lo al document tiquet
        val tiquetsFirestore = db.collection("usuaris")
        val query = tiquetsFirestore.whereEqualTo("mail", mail).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val usuari =
                        document.toObjects(cat.copernic.johan.energysaver.veuretiquet.TiquetDC::class.java)
                    admin = usuari[0].descripcio
                    if (usuari[0].admin) {
                        binding.bttnResposta.visibility = View.VISIBLE
                        binding.bttnResposta.setOnClickListener() {
                            view?.findNavController()
                                ?.navigate(
                                    TiquetObertFragmentDirections
                                        .actionTiquetObertFragmentToRespostaTiquetFragment(
                                            args.tiquetId
                                        )
                                )
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }

    }

    fun veureResposata(tiquetId: String) {
        val user = Firebase.auth.currentUser

        //Guarda el mail del usuari que ha fet login
        val mail = user?.email.toString()

        //Consulta per extreure el nickname per guardar-lo al document tiquet
        val tiquetsFirestore = db.collection("tiquet")
        val query = tiquetsFirestore.whereEqualTo("id", tiquetId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val tiquetsDC = document.toObjects(TiquetDC::class.java)
                    binding.txtViewMostraResposta.text = tiquetsDC[0].resposta
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
}

@GlideModule
class GlideAppModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        registry.append(
            StorageReference::class.java,
            InputStream::class.java,
            FirebaseImageLoader.Factory()
        )
    }

    @BindingAdapter("loadImage")
    fun setImageFromUrl(view: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            Glide
                .with(view)
                .load(url)
                .fitCenter()
                .apply(RequestOptions.bitmapTransform(RoundedCorners(4)))
                .into(view)
        } else {
            Log.d("prova", "pp")
        }
    }
}



