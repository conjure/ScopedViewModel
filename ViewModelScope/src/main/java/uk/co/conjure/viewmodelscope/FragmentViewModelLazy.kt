package uk.co.conjure.viewmodelscope

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras


/**
 * Creates a ViewModel with the given owner (default is this@Fragment) and registers it.
 *
 * Any ChildFragment can use [scopedViewModel] or [scopedInterface] to retrieve it.
 */
@MainThread
inline fun <reified VM : ViewModel> Fragment.createViewModelScope(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> = viewModels<VM>(ownerProducer, extrasProducer, factoryProducer).also {
    ViewModelStoreOwnerRegistry.instance.put(VM::class, ownerProducer.invoke())
    VM::class.java.interfaces.forEach {
        ViewModelStoreOwnerRegistry.instance.registerInterface(it.kotlin, VM::class)
    }
}


/**
 * Provides a [ViewModel] that has benn registered via [createViewModelScope] by any parent of this Fragment.
 */
@MainThread
inline fun <reified VM : ViewModel> Fragment.scopedViewModel(): Lazy<VM> =
    viewModels({ ViewModelStoreOwnerRegistry.instance.get(VM::class)!! })


/**
 * Provides an interface that has been registered via [createViewModelScope] by any parent of this Fragment.
 * This will fail if no [ViewModel] implementing the Interface was registered.
 */
@MainThread
inline fun <reified VM> Fragment.scopedInterface(): Lazy<VM> = lazy {
    if (!VM::class.java.isInterface) throw IllegalArgumentException("${VM::class} is not an Interface")
    ViewModelStoreOwnerRegistry.instance.let {
        val vmClazz = it.getViewModel(VM::class)
            ?: throw IllegalStateException("No ViewModel registered implementing ${VM::class}")
        val result = it.get(vmClazz)
            ?: throw IllegalStateException("No ViewModelStoreOwner registered for $vmClazz (implementing ${VM::class}")
        createViewModelLazy(vmClazz, { result.viewModelStore }).value as VM
    }
}