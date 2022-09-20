package uk.co.conjure.vmscopedemo.ui.main.screens.first

import androidx.lifecycle.ViewModel

interface ActivityChildViewModel {
    val activityMessage: String
}

class ActivityViewModel : ViewModel(), ActivityChildViewModel {
    private var _message: String = ""
    override val activityMessage: String
        get() = _message

    fun setMessage(message: String) {
        _message = message
    }
}