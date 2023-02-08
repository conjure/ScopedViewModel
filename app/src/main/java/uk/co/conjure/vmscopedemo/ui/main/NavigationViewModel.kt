package uk.co.conjure.vmscopedemo.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

interface AppNavigation {
    fun showFirstScreen()
    fun showSecondScreen()
    fun showMultifragmentScreen()

    val currentScreen: LiveData<Screen>
}

enum class Screen {
    FIRST,
    SECOND,
    MULTIFRAGMENT
}

class NavigationViewModel : ViewModel(), AppNavigation {

    override val currentScreen: MutableLiveData<Screen> = MutableLiveData(Screen.FIRST)

    override fun showFirstScreen() {
        currentScreen.value = Screen.FIRST
    }

    override fun showSecondScreen() {
        currentScreen.value = Screen.SECOND
    }

    override fun showMultifragmentScreen() {
        currentScreen.value = Screen.MULTIFRAGMENT
    }
}