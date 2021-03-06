package cat.copernic.johan.energysaver.informes

import junit.framework.TestCase

import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class InformesFragmentTest : TestCase() {

    @Before
    public override fun setUp() {

    }

    @After
    public override fun tearDown() {
    }

    @Test
    fun estalviadorTotalIndiv() {
        val inf = InformesFragment()
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
    }

    @Test
    fun getFurthestDate() {
    }
}