package uk.co.conjure.viewmodelscope

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.createViewModelScope(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> = viewModels<VM>(extrasProducer, factoryProducer).also {
    ViewModelStoreOwnerRegistry.instance.put(VM::class, this)
    VM::class.java.interfaces.forEach {
        ViewModelStoreOwnerRegistry.instance.registerInterface(it.kotlin, VM::class)
    }
}