package uk.co.conjure.vmscopedemo.ui.main.screens.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import uk.co.conjure.vmscopedemo.databinding.FragmentOtherBinding
import uk.co.conjure.vmscopedemo.ui.main.NavigationViewModel

class OtherFragment : Fragment() {

    private val navigationViewModel: NavigationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentOtherBinding.inflate(inflater, container, false).also {
            it.btnOpenFirstScreen.setOnClickListener {
                navigationViewModel.showFirstScreen()
            }
        }.root
    }

}