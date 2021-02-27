package cat.copernic.johan.energysaver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import cat.copernic.johan.energysaver.informes.InformesFragment
import cat.copernic.johan.energysaver.introduirconsums.ConsumAiguaDirections
import cat.copernic.johan.energysaver.introduirconsums.MenuEnergiesDirections
import cat.copernic.johan.energysaver.medalles.MedallesFragmentDirections
import cat.copernic.johan.energysaver.modifica.ModificarUsuariDirections
import cat.copernic.johan.energysaver.principal.MenuPrincipalFragmentDirections
import cat.copernic.johan.energysaver.tiquetobert.TiquetObertFragmentDirections
import cat.copernic.johan.energysaver.veuretiquet.VeureTiquetFragmentDirections
import com.google.android.material.navigation.NavigationView
import com.google.android.material.transition.MaterialSharedAxis


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    val currentNavigationFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            ?.childFragmentManager
            ?.fragments
            ?.first()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = this.findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.seleccionarEnergiaFragment, R.id.informesFragment, R.id.modificarFragment,
                R.id.menuPrincipalFragment, R.id.obrirFragment, R.id.registre,
                R.id.veureFragment, R.id.menuEnergies
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
    }

    private fun navigateToMedalles() {
        val directionsMedalles = MenuPrincipalFragmentDirections.actionMenuPrincipalFragmentToMedallesFragment()
        val directionsTiquetObert = VeureTiquetFragmentDirections.actionVeureFragmentToTiquetObertFragment(
            "",
            "",
            "",
            ""
        )
        val directionsVeureTiquet = MenuPrincipalFragmentDirections.actionMenuPrincipalFragmentToVeureFragment("","","","")
        val directionsNouTiquet = VeureTiquetFragmentDirections.actionVeureFragmentToObrirFragment()
        val directionsResposta = TiquetObertFragmentDirections.actionTiquetObertFragmentToRespostaTiquetFragment("","","","")
        val directionsMedalla1 = MedallesFragmentDirections.actionMedallesFragmentToMedalla1Fragment()
        val directionsMedalla2 = MedallesFragmentDirections.actionMedallesFragmentToMedalla2Fragment()
        val directionsMedalla3 = MedallesFragmentDirections.actionMedallesFragmentToMedalla3Fragment()
        val directionsMedalla4 = MedallesFragmentDirections.actionMedallesFragmentToMedalla4Fragment()
        val directionsMenuEnergiesAigua = MenuEnergiesDirections.actionMenuEnergiesToConsumAigua()
        val directionsMenuEnergiesLlum = MenuEnergiesDirections.actionMenuEnergiesToConsumLlum()
        val directionsMenuEnergiesGas = MenuEnergiesDirections.actionMenuEnergiesToConsumGas()
        val directionsMenuEnergiesGasoil = MenuEnergiesDirections.actionMenuEnergiesToConsumGasoil()
        val directionsModificar = MenuPrincipalFragmentDirections.actionMenuPrincipalFragmentToModificarUsuari()
        val directionsSeleccionar = ModificarUsuariDirections.actionModificarUsuariToSeleccionarEnergiaFragment()
        val directionsMenuEnergies = MenuPrincipalFragmentDirections.actionMenuPrincipalFragmentToMenuEnergies()
        val directionsInformes = MenuPrincipalFragmentDirections.actionMenuPrincipalFragmentToInformesFragment()

        currentNavigationFragment?.apply {
            exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
                duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            }
            reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
                duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            }
        }
        findNavController(R.id.nav_host_fragment).navigate(directionsMedalles)
        findNavController(R.id.nav_host_fragment).navigate(directionsTiquetObert)
        findNavController(R.id.nav_host_fragment).navigate(directionsVeureTiquet)
        findNavController(R.id.nav_host_fragment).navigate(directionsNouTiquet)
        findNavController(R.id.nav_host_fragment).navigate(directionsResposta)
        findNavController(R.id.nav_host_fragment).navigate(directionsMedalla1)
        findNavController(R.id.nav_host_fragment).navigate(directionsMedalla2)
        findNavController(R.id.nav_host_fragment).navigate(directionsMedalla3)
        findNavController(R.id.nav_host_fragment).navigate(directionsMedalla4)
        findNavController(R.id.nav_host_fragment).navigate(directionsMenuEnergiesAigua)
        findNavController(R.id.nav_host_fragment).navigate(directionsMenuEnergiesLlum)
        findNavController(R.id.nav_host_fragment).navigate(directionsMenuEnergiesGas)
        findNavController(R.id.nav_host_fragment).navigate(directionsMenuEnergiesGasoil)
        findNavController(R.id.nav_host_fragment).navigate(directionsModificar)
        findNavController(R.id.nav_host_fragment).navigate(directionsSeleccionar)
        findNavController(R.id.nav_host_fragment).navigate(directionsMenuEnergies)
        findNavController(R.id.nav_host_fragment).navigate(directionsInformes)
    }
}





