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

    private val map: MutableMap<String, WeakReference<ViewModelStoreOwner>> = hashMapOf()

    private val interfaceMap: MutableMap<KClass<out Any>, KClass<out ViewModel>> = mutableMapOf()

    fun <VM : ViewModel> put(viewModelClass: KClass<VM>, owner: ViewModelStoreOwner) {
        map[viewModelClass.qualifiedName!!] = WeakReference(owner)
    }

    fun <VM : ViewModel> get(viewModelClass: KClass<VM>): ViewModelStoreOwner? {
        return map[viewModelClass.qualifiedName]?.get()
    }

    fun <VM : ViewModel> registerInterface(register: KClass<out Any>, with: KClass<VM>) {
        val registered = interfaceMap[register]
        if (registered != null && registered != with) throw IllegalStateException("Can not register more than one ViewModel with the Interface $register.")
        interfaceMap[register] = with
    }

    fun getViewModel(myInterface: KClassifier): KClass<out ViewModel>? {
        return interfaceMap[myInterface]
    }
}