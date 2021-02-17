package cat.copernic.johan.energysaver.tiquetobert

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.FragmentTiquetObertBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.StorageReference
import java.io.InputStream

@GlideModule
class TiquetObertFragment : Fragment() { //

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTiquetObertBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tiquet_obert, container, false
        )

        val args = TiquetObertFragmentArgs.fromBundle(requireArguments())

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

        binding.bttnResposta.setOnClickListener() {
            view?.findNavController()
                ?.navigate(
                    TiquetObertFragmentDirections
                        .actionTiquetObertFragmentToRespostaTiquetFragment(
                            args.tiquetId
                        )
                )
        }
        return binding.root
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


