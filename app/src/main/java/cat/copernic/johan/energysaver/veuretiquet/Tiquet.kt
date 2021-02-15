package cat.copernic.johan.energysaver.veuretiquet

class Tiquet (val idTiquet: String, val titol: String, val descripcio: String, val seleccionat: Boolean) {

    companion object {

        fun createTiquetList(): ArrayList<Tiquet> {
            val tiquets = ArrayList<Tiquet>()

            return tiquets
        }

    }


}

