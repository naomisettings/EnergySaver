package cat.copernic.johan.energysaver.veuretiquet

class Tiquet (val titol: String, val descripcio: String, val seleccionat: Boolean) {

    companion object {

        fun createTiquetList(titol: String, descripcio: String, numTiquets: Int): ArrayList<Tiquet> {
            val tiquets = ArrayList<Tiquet>()
            for (i in 1..numTiquets) {
                tiquets.add(Tiquet( titol,  descripcio, false))
            }
            return tiquets
        }

    }
}