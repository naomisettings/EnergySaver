package cat.copernic.johan.energysaver.veuretiquet

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.databinding.ItemTiquetBinding
import com.squareup.okhttp.internal.Internal.logger
import java.util.*

class TiquetsAdapter(
    private val mTiquets: ArrayList<Tiquet>,
    private val cellClickListener: CellClickListener,
) : RecyclerView.Adapter<TiquetsAdapter.ViewHolder>() {

    private lateinit var binding: ItemTiquetBinding

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        private lateinit var binding: ItemTiquetBinding

        //inicialitzem les variables dels items
        val titolTextView = itemView.findViewById<TextView>(R.id.titolTiquet)
        val descripcioTextView = itemView.findViewById<TextView>(R.id.descripcioTiquet)
        val seleccioCheckBox = itemView.findViewById<CheckBox>(R.id.tiquetSeleccionat)
    }

    var checkedTiquets: ArrayList<Tiquet> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiquetsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // carreguem el layou del tiquet
        val tiquetView = inflater.inflate(R.layout.item_tiquet, parent, false)
        // retorna una instancia del viewholder
        return ViewHolder(tiquetView)
    }

    @SuppressLint("LongLogTag")
    override fun onBindViewHolder(holder: TiquetsAdapter.ViewHolder, position: Int) {
        val tiquet: Tiquet = mTiquets.get(position)
        val titolTextView = holder.titolTextView
        titolTextView.setText(tiquet.titol)
        val descTextView = holder.descripcioTextView
        descTextView.setText(tiquet.descripcio)
        val chbx = holder.seleccioCheckBox
        chbx.isChecked = tiquet.seleccionat

        val data = mTiquets[position]
        holder.titolTextView.text = data.titol
        holder.descripcioTextView.text = data.descripcio

        if (tiquet.respost) {
            holder.seleccioCheckBox.setBackgroundResource(R.drawable.borde_imatge)
        } else {
            holder.seleccioCheckBox.setBackgroundResource(R.drawable.borde_imatge_red)
        }


        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(data)
        }
        //seleccionar checkbox
        holder.seleccioCheckBox.setOnClickListener {
            if (chbx.isChecked) {
                tiquet.seleccionat = true
                checkedTiquets.add(tiquet)
            }
        }
    }

    override fun getItemCount(): Int {
        return mTiquets.size
    }
}

//Veure si s'ha clicat en un tiquet
open class CellClickListener(val clickListener: (idTiquet: String, titol: String, descrpicio: String, imatge: String, respost: Boolean) -> Unit) {
    fun onCellClickListener(data: Tiquet) {
        clickListener(data.idTiquet, data.titol, data.descripcio, data.imatge, data.respost)

    }
}

@BindingAdapter("tiquetQualityString")
fun TextView.setSleepQualityString(item: Tiquet?) {
    item?.let {
        text = item.idTiquet
    }
}



