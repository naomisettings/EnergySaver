package cat.copernic.johan.energysaver.informes

import junit.framework.TestCase

import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import java.time.LocalDate

class InformesFragmentTest : TestCase() {
    private val inf = InformesFragment()

    @Before
    public override fun setUp() {

    }

    @After
    public override fun tearDown() {
    }

    @Test
    fun estalviadorTotalIndiv() {

        val map: Map<String, Double> = mapOf("2020.01.08" to 100.0,
        "2020.06.06" to 50.0,
        "2021.01.06" to 25.0)

        val estalviat = inf.estalviadorTotalIndiv(map)
        assertEquals(estalviat, 75.0)
    }

    @Test
    fun estalviadorPeriode() {
    }

    @Test
    fun tempsEstalviat() {
    }

    @Test
    fun getNearestDate() {

        val ld1: LocalDate = LocalDate.of(2020,1,1)
        val ld2: LocalDate = LocalDate.of(2020,7,6)
        val ld3: LocalDate = LocalDate.of(2021,2,2)
        val ldFound: LocalDate = LocalDate.of(2021,1,1)

        val date: ArrayList<LocalDate> = arrayListOf(ld1, ld2, ld3)
        val nearest = inf.getNearestDate(date, ldFound)
        assertEquals(nearest, ld3)
    }

    @Test
    fun getFurthestDate() {
    }
}