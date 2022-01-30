package com.red.code005.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.red.code005.ui.common.Event
import com.red.code005.databinding.FragmentAccountBinding
import com.red.code005.navigateTo
import com.red.code005.toast
import com.red.code005.ui.account.AccountViewModel.AccountNavigation
import com.red.code005.ui.account.AccountViewModel.AccountNavigation.ShowError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {

    // region Fields

    private val viewModel: AccountViewModel by viewModels()

    // endregion

    // region Override Methods & Callbacks

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?) =
        FragmentAccountBinding.inflate(i, c, false).apply {
            accountViewModel = viewModel
            lifecycleOwner = this@AccountFragment
            viewModel.events.observe(
                viewLifecycleOwner,
                Observer(this@AccountFragment::validateEvents)
            )
            topBar.setNavigationOnClickListener {
                navigateTo(AccountFragmentDirections.actionAccountToHome())
            }
        }.root

    // endregion

    // region Private Methods

    private fun validateEvents(event: Event<AccountNavigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is ShowError -> navigation.run {
                    this@AccountFragment.toast("Error -> ${error.message}")
                }
                AccountNavigation.SignOut -> {
                    navigateTo(AccountFragmentDirections.actionAccountToLogin())
                }
            }
        }
    }

    // endregion
}