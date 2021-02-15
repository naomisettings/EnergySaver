package cat.copernic.johan.energysaver.veuretiquet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.johan.energysaver.databinding.ItemTiquetBinding
import cat.copernic.johan.energysaver.R

class TiquetsAdapter(private val mTiquets: ArrayList<Tiquet>, private val cellClickListener: CellClickListener): RecyclerView.Adapter<TiquetsAdapter.ViewHolder>() {
    private lateinit var binding: ItemTiquetBinding




    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        private lateinit var binding: ItemTiquetBinding
        //inicialitzem les variables dels items
        val titolTextView = itemView.findViewById<TextView>(R.id.titolTiquet)
        val descripcioTextView = itemView.findViewById<TextView>(R.id.descripcioTiquet)
        val seleccioCheckBox = itemView.findViewById<CheckBox>(R.id.tiquetSeleccionat)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiquetsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // carreguem el layou del tiquet
        val tiquetView = inflater.inflate(R.layout.item_tiquet, parent, false)
        // retorna una instancia del viewholder
        return ViewHolder(tiquetView)


    }

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

        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(data)
        }
    }


    override fun getItemCount(): Int {
        return mTiquets.size
    }

}

interface CellClickListener {
    fun onCellClickListener(data: Tiquet)
}


