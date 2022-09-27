# ScopedViewModel
[![](https://jitpack.io/v/uk.co.conjure/ScopedViewModel.svg)](https://jitpack.io/#uk.co.conjure/ScopedViewModel)

```gradle
dependencies {
  implementation 'uk.co.conjure:ScopedViewModel:TAG'
}
```

Android library to provide ViewModels based on a scope extending the functionality of Fragment KTX.


## Code Sample

Consider the following ViewModel which you want to share between a Fragment and it's child Fragment

```kotlin
interface ChildViewModel {
    val sharedData: LiveData<Int>
}

class ParentViewModel : ViewModel(), ChildViewModel {
    override val sharedData: MutableLiveData<Int> = MutableLiveData(120)
}
```

### ParentFragment
Create a scoped ViewModel

```kotlin
class ParentFragment : Fragment() {

  private val parentViewModel: ParentViewModel by createViewModelScope()
  
  //...
}
```

### ChildFragment
Retrieve the ViewModel from the parent Fragment

```kotlin
class ChildFragment : Fragment() {

  private val childViewModel: ChildViewModel by scopedInterface()
  
  //...
}
```
