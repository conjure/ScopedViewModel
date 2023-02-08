package uk.co.conjure.vmscopedemo.ui.main.screens.multifragment

import androidx.lifecycle.ViewModel
import kotlin.random.Random

interface ChildViewModel {
    val number: Int
}

class ChildViewModelImpl : ViewModel(), ChildViewModel {
    override val number: Int by lazy { Random(System.nanoTime()).nextInt(100000) }
}