//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.utils

import android.os.Bundle
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import kotlin.reflect.KClass

@MainThread
inline fun <reified VM : ViewModel> Fragment.parentViewModels() = createLazyViewModel(
    viewModelClass = VM::class,
    savedStateRegistryOwnerProducer = { requireParentFragment() },
    viewModelStoreOwnerProducer = { requireParentFragment() },
    viewModelProvider = {
        throw IllegalStateException(
            "${VM::class.java.name} should already exist scoped to the parent fragment."
        )
    }
)

fun <VM : ViewModel> createLazyViewModel(
    viewModelClass: KClass<VM>,
    savedStateRegistryOwnerProducer: () -> SavedStateRegistryOwner,
    viewModelStoreOwnerProducer: () -> ViewModelStoreOwner,
    viewModelProvider: (SavedStateHandle) -> VM
) = ViewModelLazy(viewModelClass, { viewModelStoreOwnerProducer().viewModelStore }) {
    object : AbstractSavedStateViewModelFactory(savedStateRegistryOwnerProducer(), Bundle()) {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            return viewModelProvider(handle) as T
        }
    }
}
