package cat.copernic.johan.energysaver

import android.content.ClipData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import cat.copernic.johan.energysaver.tiquetobert.TiquetObertFragment
import cat.copernic.johan.energysaver.veuretiquet.Tiquet
import cat.copernic.johan.energysaver.veuretiquet.VeureTiquetFragment
import com.google.android.material.navigation.NavigationView
import java.lang.String

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    //, Communicator
    //navigation drawer
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        //navigation drawer
        toolbar = findViewById(R.id.toolbar)
        // setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toogle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()
        navView.setNavigationItemSelectedListener(this)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.seleccionarEnergiaFragment, R.id.informesFragment, R.id.modificarFragment,
                R.id.Baixa, R.id.obrirFragment, R.id.Sortir
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

/*
        val fragmentVeureTiquet = VeureTiquetFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragmentVeureTiquet).commit()
 */
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.seleccionarEnergiaFragment -> {

                Toast.makeText(this, "Energies clicked", Toast.LENGTH_SHORT).show()

            }
            R.id.informesFragment -> {

                Toast.makeText(this, "Informes clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.modificarFragment -> {
                Toast.makeText(this, "Modificar clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.Baixa -> {
                Toast.makeText(this, "Baixa clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.obrirFragment -> {
                Toast.makeText(this, "Suport clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.Sortir -> {
                Toast.makeText(this, "Sing Out clicked", Toast.LENGTH_SHORT).show()

            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}


