package uk.co.conjure.viewmodelscope

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import java.lang.ref.WeakReference
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier

/**
 * No need to access this directly.
 *
 * The ViewModelRegistry makes ViewModels and their interfaces available to child Fragments via [scopedInterface] and [scopedViewModel].
 *
 * It keeps a WeakReference to the ViewModelStoreOwner creating the ViewModel.
 * This ensures that ViewModels can be resolved as long as the component that created it is alive and it's automatically cleaned up if
 * no other reference to ViewModelStoreOwner is kept in memory.
 *
 */
class ViewModelStoreOwnerRegistry private constructor() {

    companion object {
        val instance: ViewModelStoreOwnerRegistry by lazy { ViewModelStoreOwnerRegistry() }
    }

    /**
     * A map from the view model class name to a second map of the extra key to the ViewModelStoreOwner.
     */
    private val map = hashMapOf<ViewModelClass, WeakReference<ViewModelStoreOwner>>()

    /**
     * A map from the interface KClass to a second map of the extra key to the ViewModel KClass.
     */
    private val interfaceMap = hashMapOf<Interface, KClass<out ViewModel>>()

    fun <VM : ViewModel> put(
        viewModelClass: KClass<VM>,
        extraKey: String,
        owner: ViewModelStoreOwner
    ) {
        viewModelClass.qualifiedName?.let { className ->
            map[ViewModelClass(className, extraKey)] = WeakReference(owner)
        }
    }

    fun <VM : ViewModel> get(
        viewModelClass: KClass<VM>,
        extraKey: String
    ): ViewModelStoreOwner? {
        return map[ViewModelClass(viewModelClass.qualifiedName!!, extraKey)]?.get()
    }

    fun <VM : ViewModel> registerInterface(
        interfaceClass: KClass<out Any>,
        key: String,
        viewModelClass: KClass<VM>
    ) {
        val interfaceKey = Interface(interfaceClass, key)
        val registered = interfaceMap[interfaceKey]
        if (registered != null && registered != viewModelClass) {
            Log.w("ViewModelStoreOwnerRegistry",
                "Warning more than one ViewModel with the Interface $interfaceClass and " +
                        "key $key was registered. Are you sure this was intentional? The " +
                        "default behaviour is to store only the last one registered."
            )
        }
        interfaceMap[interfaceKey] = viewModelClass
    }

    fun getViewModel(myInterface: KClassifier, extraKey: String): KClass<out ViewModel>? {
        return interfaceMap[Interface(myInterface, extraKey)]
    }

    data class Interface(val interfaceClass: KClassifier, val key: String)
    data class ViewModelClass(val className: String, val key: String)
}