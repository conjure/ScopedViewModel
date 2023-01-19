package uk.co.conjure.viewmodelscope

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
    private val map = hashMapOf<String, MutableMap<String, WeakReference<ViewModelStoreOwner>>>()

    /**
     * A map from the interface KClass to a second map of the extra key to the ViewModel KClass.
     */
    private val interfaceMap =
        hashMapOf<KClass<out Any>, MutableMap<String, KClass<out ViewModel>>>()

    fun <VM : ViewModel> put(
        viewModelClass: KClass<VM>,
        extraKey: String,
        owner: ViewModelStoreOwner
    ) {
        viewModelClass.qualifiedName?.let { className ->
            if (map.containsKey(className)) map[className]?.put(extraKey, WeakReference(owner))
            else map[className] = hashMapOf(extraKey to WeakReference(owner))
        }
    }

    fun <VM : ViewModel> get(
        viewModelClass: KClass<VM>,
        extraKey: String
    ): ViewModelStoreOwner? {
        return map[viewModelClass.qualifiedName]?.get(extraKey)?.get()
    }

    fun <VM : ViewModel> registerInterface(
        register: KClass<out Any>,
        extraKey: String,
        with: KClass<VM>
    ) {
        val registered = interfaceMap[register]?.get(extraKey)
        if (registered != null && registered != with) throw IllegalStateException("Can not register more than one ViewModel with the Interface $register.")
        if (interfaceMap.containsKey(register)) interfaceMap[register]?.put(extraKey, with)
        else interfaceMap[register] = hashMapOf(extraKey to with)
    }

    fun getViewModel(myInterface: KClassifier, extraKey: String?): KClass<out ViewModel>? {
        return interfaceMap[myInterface]?.get(extraKey)
    }
}