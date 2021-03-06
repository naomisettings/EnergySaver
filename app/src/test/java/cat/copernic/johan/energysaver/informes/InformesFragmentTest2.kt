package cat.copernic.johan.energysaver.informes

import junit.framework.TestCase
import java.time.LocalDate

class InformesFragmentTest2 : TestCase() {
    private val inf = InformesFragment()

    fun testEstalviadorTotalIndiv() {
        val map: Map<String, Double> = mapOf("2020.01.08" to 100.0,
            "2020.06.06" to 50.0,
            "2021.01.06" to 25.0)

        val estalviat = inf.estalviadorTotalIndiv(map)
        assertEquals(estalviat, 75.0)
    }

    fun testEstalviadorPeriode() {
        val map: Map<String, Double> = mapOf("2020.01.08" to 100.0,
            "2020.06.06" to 50.0,
            "2021.01.06" to 25.0)
        val estPeriode = inf.estalviadorPeriode(map)
        assertEquals(estPeriode, 25.0)
    }

    fun testTempsEstalviat() {
        val map: Map<String, Double> = mapOf("2020.01.08" to 100.0,
            "2020.06.06" to 50.0,
            "2021.01.06" to 25.0)
        val tempsEst = inf.tempsEstalviat(map)
        assertEquals(tempsEst, 423)
    }

    fun testGetNearestDate() {
        val ld1: LocalDate = LocalDate.of(2020,1,1)
        val ld2: LocalDate = LocalDate.of(2020,7,6)
        val ld3: LocalDate = LocalDate.of(2021,2,2)
        val ldFound: LocalDate = LocalDate.of(2021,1,1)

        val date: ArrayList<LocalDate> = arrayListOf(ld1, ld2, ld3)
        val nearest = inf.getNearestDate(date, ldFound)
        TestCase.assertEquals(nearest, ld3)
    }

    fun testGetFurthestDate() {
        val ld1: LocalDate = LocalDate.of(2020,1,1)
        val ld2: LocalDate = LocalDate.of(2020,7,6)
        val ld3: LocalDate = LocalDate.of(2021,2,2)

        val date: ArrayList<LocalDate> = arrayListOf(ld1, ld2, ld3)

        val furthest = inf.getFurthestDate(date, LocalDate.now())
        assertEquals(furthest, ld1)
    }
}