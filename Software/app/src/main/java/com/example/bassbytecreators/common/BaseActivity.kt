import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.example.bassbytecreators.R
import com.google.android.material.navigation.NavigationView

abstract class BaseActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var userRole: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setupNavigationDrawer(navigationView: NavigationView) {
        val navController = findNavController(R.id.fragment_container)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                /*R.id.nav_my_profile -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }*/

                R.id.nav_addgigs -> {
                    navController.navigate(R.id.addGigsFragment)
                    true
                }

                else -> false
            }
        }
    }
}

    /*fun setupNavigationDrawer(navigationView: NavigationView) {
        val userId = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            .getInt("logged_in_user_id", -1)

        userRole = intent.getStringExtra("USER_ROLE") ?: ""

        // Postavi vidljivost menija ovisno o userId i userRole
        val menu = navigationView.menu
        val isLoggedIn = userId != -1

        menu.findItem(R.id.nav_login)?.isVisible = !isLoggedIn
        menu.findItem(R.id.nav_registration)?.isVisible = !isLoggedIn
        menu.findItem(R.id.nav_my_profile)?.isVisible = isLoggedIn
        menu.findItem(R.id.nav_search)?.isVisible = isLoggedIn

        if (userRole == "Korisnik") {
            menu.findItem(R.id.nav_addgigs)?.isVisible = false
            menu.findItem(R.id.nav_djstatistics)?.isVisible = false
        } else if (isLoggedIn) {
            menu.findItem(R.id.nav_addgigs)?.isVisible = true
            menu.findItem(R.id.nav_djstatistics)?.isVisible = true
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_my_profile -> {
                    when (userRole) {
                        "DJ" -> {
                            val intent = Intent(this, DJMyProfileActivity::class.java)
                            intent.putExtra("user_id", userId)
                            intent.putExtra("USER_ROLE", userRole)
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                            startActivity(intent)
                        }
                        "Korisnik" -> {
                            val intent = Intent(this, UserMyProfileActivity::class.java)
                            intent.putExtra("user_id", userId)
                            intent.putExtra("USER_ROLE", userRole)
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                            startActivity(intent)
                        }
                    }
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_djstatistics -> {
                    val intent = Intent(this, DJStatisticsActivity::class.java)
                    intent.putExtra("user_id", userId)
                    intent.putExtra("USER_ROLE", userRole)
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_addgigs -> {
                    val intent = Intent(this, AddGigsActivity::class.java)
                    intent.putExtra("user_id", userId)
                    intent.putExtra("USER_ROLE", userRole)
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_main -> {
                    when (userRole) {
                    "DJ" -> {
                        val intent = Intent(this, DJDetailActivity::class.java)
                        intent.putExtra("user_id", userId)
                        intent.putExtra("USER_ROLE", userRole)
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                        startActivity(intent)
                    }
                    "Korisnik" -> {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("user_id", userId)
                        intent.putExtra("USER_ROLE", userRole)
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                        startActivity(intent)
                    }
                }
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_search -> {
                    val intent = Intent(this, SearchActivity::class.java)
                    intent.putExtra("user_id", userId)
                    intent.putExtra("USER_ROLE", userRole)
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_login -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_registration -> {
                    val intent = Intent(this, RegistrationActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }
    }
}*/
