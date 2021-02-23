package cat.copernic.johan.energysaver.obrirtiquet

import android.net.Uri

object Singleton {

    init {
        println("Singleton class invoked.")
    }

    var nameImg = ""
        get() = field
        set(value) {
            field = value
        }

    var rutaImg: Uri? = null
        get() = field
        set(value) {
            field = value
        }

}

