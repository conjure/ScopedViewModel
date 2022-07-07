package uk.co.conjure.vmscopedemo.ui.main.screens.first

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

interface ChildViewModel {
    val timer: LiveData<Int>
    fun increaseTimer(value: Int)
}

class ParentViewModel : ViewModel(), ChildViewModel {
    override val timer: MutableLiveData<Int> = MutableLiveData(120)

    override fun increaseTimer(value: Int) {
        timer.value = timer.value!! + value
    }

    init {
        // Start a naive timer
        viewModelScope.launch {
            while (true) {
                delay(1000)
                timer.postValue((timer.value!! - 1).coerceAtLeast(0))
            }
        }
    }

}