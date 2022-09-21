package uk.co.conjure.vmscopedemo.ui.main.screens.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import uk.co.conjure.viewmodelscope.scopedInterface
import uk.co.conjure.vmscopedemo.R
import uk.co.conjure.vmscopedemo.databinding.FragmentOtherBinding
import uk.co.conjure.vmscopedemo.ui.main.NavigationViewModel
import uk.co.conjure.vmscopedemo.ui.main.screens.first.ActivityChildViewModel

class OtherFragment : Fragment() {

    private val navigationViewModel: NavigationViewModel by activityViewModels()
    private val activityChildViewModel: ActivityChildViewModel by scopedInterface()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentOtherBinding.inflate(inflater, container, false).also {
            it.btnOpenFirstScreen.setOnClickListener {
                navigationViewModel.showFirstScreen()
            }
            it.tvActivityMessage.text = activityChildViewModel.activityMessage
        }.root
    }

}