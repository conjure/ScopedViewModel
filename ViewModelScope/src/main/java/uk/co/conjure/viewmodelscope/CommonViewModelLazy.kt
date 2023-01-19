package uk.co.conjure.viewmodelscope

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner

/**
 * You should not need to reference this directly.
 *
 * The key used as a tag in the childFragmentManager to store the ViewModelStoreOwner. And passed
 * in a Bundle to the child Fragment to retrieve the ViewModelStoreOwner.
 */
const val KEY_EXTRA_KEY = "uk.co.conjure.viewmodelscope.EXTRA_KEY"

/**
 * You should not need to reference this directly.
 *
 * The default key used as a tag in the childFragmentManager to store the ViewModelStoreOwner if no
 * extraKey is provided.
 */
const val DEFAULT_KEY = "uk.co.conjure.viewmodelscope.DEFAULT_KEY"

/**
 * You shouldn't need to call this directly.
 */
inline fun <reified VM : ViewModel> putOwnerInRegistry(extraKey: String, owner: ViewModelStoreOwner) {
    ViewModelStoreOwnerRegistry.instance.put(VM::class, extraKey, owner)
    VM::class.java.interfaces.forEach {
        ViewModelStoreOwnerRegistry.instance.registerInterface(it.kotlin, extraKey, VM::class)
    }
}

/**
 * You shouldn't need to call this directly.
 */
inline fun <reified VM : ViewModel> Fragment.registerFragmentOnCreate(
    crossinline fragmentFinder: () -> Fragment?,
    crossinline fragmentProducer: () -> Fragment,
    key: String
) {
    lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
            if (fragmentFinder() == null) {
                childFragmentManager.beginTransaction()
                    .add(fragmentProducer(), key)
                    .commitNow()
            }
            putOwnerInRegistry<VM>(key, fragmentProducer())
        }
    })
}

/**
 * You should not need to call this directly. It is called by [scopedInterface] and [scopedViewModel].
 * If you have added a key to the arguments of the fragment, it will use that key to retrieve the
 * ViewModelStoreOwner. Otherwise it will use the default key.
 */
fun Fragment.getViewModelKey(): String = arguments?.getString(KEY_EXTRA_KEY) ?: DEFAULT_KEY
