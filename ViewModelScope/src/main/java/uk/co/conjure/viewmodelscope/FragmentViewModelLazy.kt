package uk.co.conjure.viewmodelscope

import android.os.Bundle
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras

/**
 * Adds a view model key to the arguments of the fragment. You can use it like this:
 * childFragmentManager.beginTransaction()
 *     .add(
 *         fragmentContainerId,
 *         ChildFragment::class.java,
 *         Bundle().addViewModelKey(childViewModelKey),
 *         CHILD_FRAGMENT_TAG
 *     )
)
 */
fun Bundle.addViewModelKey(key: String): Bundle = this.apply {
    putString(KEY_EXTRA_KEY, key)
}

/**
 * Creates a ViewModel with the given owner (default is this@Fragment) and registers it.
 *
 * Any ChildFragment can use [scopedViewModel] or [scopedInterface] to retrieve it.
 */
@MainThread
inline fun <reified VM : ViewModel> Fragment.createViewModelScope(
    extraKey: String? = null,
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val key = extraKey ?: DEFAULT_KEY

    val fragmentProducer = {
        val fragment = if (extraKey == null) {
            this
        } else {
            childFragmentManager.findFragmentByTag(key) ?: (Fragment()
                .also { keyHolderFragment ->
                    childFragmentManager.beginTransaction()
                        .add(keyHolderFragment, key)
                        .commitNow()
                })
        }
        putOwnerInRegistry<VM>(key, fragment)
        fragment
    }

    return viewModels<VM>(
        ownerProducer = fragmentProducer,
        extrasProducer = extrasProducer,
        factoryProducer = factoryProducer
    ).also { vmLazy -> vmLazy.registerWith(this) }
}

/**
 * Provides a [ViewModel] that has benn registered via [createViewModelScope] by any parent of this Fragment.
 */
@MainThread
inline fun <reified VM : ViewModel> Fragment.scopedViewModel(): Lazy<VM> = viewModels({
    val extraKey = getViewModelKey()
    ViewModelStoreOwnerRegistry.instance.get(VM::class, extraKey)!!
})


/**
 * Provides an interface that has been registered via [createViewModelScope] by any parent of this Fragment.
 * This will fail if no [ViewModel] implementing the Interface was registered.
 */
@MainThread
inline fun <reified VM> Fragment.scopedInterface(): Lazy<VM> = lazy {
    if (!VM::class.java.isInterface) throw IllegalArgumentException("${VM::class} is not an Interface")
    ViewModelStoreOwnerRegistry.instance.let {
        val extraKey = getViewModelKey()
        val vmClazz = it.getViewModel(VM::class, extraKey)
            ?: throw IllegalStateException("No ViewModel registered implementing ${VM::class}")
        val result = it.get(vmClazz, extraKey)
            ?: throw IllegalStateException("No ViewModelStoreOwner registered for $vmClazz (implementing ${VM::class}")
        createViewModelLazy(vmClazz, { result.viewModelStore }).value as VM
    }
}


fun <T> Lazy<T>.registerWith(lifecycleOwner: LifecycleOwner) {
    lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
            this@registerWith.value
            owner.lifecycle.removeObserver(this)
        }
    })
}