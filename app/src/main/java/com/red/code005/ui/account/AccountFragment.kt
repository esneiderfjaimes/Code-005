package com.red.code005.ui.account

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.red.code005.databinding.FragmentAccountBinding
import com.red.code005.ui.account.AccountViewModel.AccountEvent
import com.red.code005.ui.account.AccountViewModel.AccountEvent.ShowError
import com.red.code005.ui.account.AccountViewModel.AccountEvent.SignOut
import com.red.code005.utils.navigateBack
import com.red.code005.utils.navigateTo
import com.red.code005.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {

    // region Fields

    private val viewModel: AccountViewModel by viewModels()

    // endregion

    // region Override Methods & Callbacks

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG, "onAttach: $this")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e(TAG, "onDetach: $this")
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?) =
        FragmentAccountBinding.inflate(i, c, false).apply {
            accountViewModel = viewModel
            lifecycleOwner = this@AccountFragment
            viewModel.event.observe(
                viewLifecycleOwner,
                Observer(this@AccountFragment::validateEvent)
            )
            topBar.setNavigationOnClickListener {
                navigateBack()
            }
        }.root

    // endregion

    // region Private Methods

    private fun validateEvent(event: AccountEvent) {
        when (event) {
            is ShowError -> toast("Error -> ${event.error.message}")
            SignOut -> navigateTo(AccountFragmentDirections.actionAccountToLogin())
        }
    }

    // endregion

    companion object {
        const val TAG = "LOG:Account"
    }
}