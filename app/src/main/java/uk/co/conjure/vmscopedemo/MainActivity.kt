package uk.co.conjure.vmscopedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import uk.co.conjure.vmscopedemo.ui.main.NavigationViewModel
import uk.co.conjure.vmscopedemo.ui.main.Screen
import uk.co.conjure.vmscopedemo.ui.main.screens.first.ParentFragment
import uk.co.conjure.vmscopedemo.ui.main.screens.second.OtherFragment

private const val FIRST_FRAGMENT_TAG = "FIRST_FRAGMENT"
private const val SECOND_FRAGMENT_TAG = "SECOND_FRAGMENT"

/**
 * The MainActivity subscribes to the [NavigationViewModel.currentScreen] to switch between
 * [ParentFragment] and [OtherFragment].
 */
class MainActivity : AppCompatActivity() {

    private val navigationViewModel: NavigationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationViewModel.currentScreen.observe(this) { screen ->
            when (screen) {
                Screen.FIRST -> showFirstScreen()
                Screen.SECOND -> showSecondScreen()
                null -> {} // ignore
            }
        }
    }


    private fun showFirstScreen() {
        if (supportFragmentManager.findFragmentByTag(FIRST_FRAGMENT_TAG) == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ParentFragment(), FIRST_FRAGMENT_TAG)
                .commitNow()
        }
    }

    private fun showSecondScreen() {
        if (supportFragmentManager.findFragmentByTag(SECOND_FRAGMENT_TAG) == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, OtherFragment(), SECOND_FRAGMENT_TAG)
                .commitNow()
        }
    }

}