package cat.copernic.johan.energysaver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    //navigation drawer
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this,navController)

        //navigation drawer
        toolbar = findViewById(R.id.toolbar)
         //  setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toogle = ActionBarDrawerToggle(this, drawerLayout, toolbar,0,0)
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()
        navView.setNavigationItemSelectedListener (this)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.Energies -> {
                Toast.makeText(this,"Energies clicked", Toast.LENGTH_SHORT).show()

            }
            R.id.Informes -> {
                Toast.makeText(this,"Informes clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.Modificar ->{
                Toast.makeText(this,"Modificar clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.Baixa ->{
                Toast.makeText(this,"Baixa clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.Suport ->{
                Toast.makeText(this,"Suport clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.Sortir ->{
                Toast.makeText(this,"Sing Out clicked", Toast.LENGTH_SHORT).show()

            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}