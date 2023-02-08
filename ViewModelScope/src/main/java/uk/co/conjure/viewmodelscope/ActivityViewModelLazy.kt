package uk.co.conjure.viewmodelscope

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras


@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.createViewModelScope(
    extraKey: String? = null,
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> = viewModels<VM>(extrasProducer, factoryProducer)
    .also { putOwnerInRegistry<VM>(extraKey ?: DEFAULT_KEY, this) }