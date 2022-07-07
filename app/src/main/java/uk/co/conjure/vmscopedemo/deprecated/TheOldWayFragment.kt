package uk.co.conjure.vmscopedemo.deprecated

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import uk.co.conjure.vmscopedemo.R
import java.lang.IllegalStateException

/**
 * This class was created for a presentation. You can ignore it.
 *
 * It demonstrates how we used to share data between Fragments before we had ViewModels.
 */
class TheOldWayFragment : Fragment() {


    private lateinit var viewModel: TheOldWayViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TheOldWayProvider) {
            this.viewModel = context.getViewModel();
        } else {
            throw IllegalStateException("This Fragment can only be attached to a class implementing TheOldWayProvider!")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btn_click_me).setOnClickListener { viewModel.doSomething() }
    }

    interface TheOldWayProvider {
        fun getViewModel(): TheOldWayViewModel
    }

    interface TheOldWayViewModel {
        fun doSomething()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_the_old_way, container, false)
    }

}