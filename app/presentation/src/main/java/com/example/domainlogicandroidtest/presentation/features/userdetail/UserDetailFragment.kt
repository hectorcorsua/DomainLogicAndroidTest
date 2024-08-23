package com.example.domainlogicandroidtest.presentation.features.userdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.domainlogicandroidtest.R
import com.example.domainlogicandroidtest.databinding.FragmentUserDetailBinding
import com.example.domainlogicandroidtest.domain.model.GitHubUserDetail
import com.example.domainlogicandroidtest.presentation.extensions.alert
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailFragment : Fragment(R.layout.fragment_user_detail) {
    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserDetailViewModel by viewModels()
    private val args: UserDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.userDetailsErrorState.collect { renderUserDetailsErrorState(it) }
            }
            launch {
                viewModel.userDetailsInfoState.collect { renderUserDetailsInfoState(it) }
            }
        }

        viewModel.getGitHubUserDetail(args.userId)
    }

    override fun onStop() {
        viewModel.stopJobs()
        super.onStop()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun renderUserDetailsInfoState(uiState: UserDetailsInfoUIState) {
        when (uiState) {
            UserDetailsInfoUIState.Idle -> {}
            UserDetailsInfoUIState.Loading -> {
                showLoading(true)
                viewModel.processUserDetailsInfoState()
            }

            is UserDetailsInfoUIState.ShowUseInfo -> {
                showLoading(false)
                fillUserInfo(uiState.gitHubUser)
                viewModel.processUserDetailsInfoState()
            }
        }
    }

    private fun renderUserDetailsErrorState(uiState: UserDetailsErrorUIState) {
        when (uiState) {
            UserDetailsErrorUIState.Idle -> {}
            is UserDetailsErrorUIState.ShowUserDetailsError -> {
                when (uiState.errorState) {
                    is ErrorState.GetUserError -> uiState.errorState.showGetUserError()
                    is ErrorState.GetUserUnknownError -> uiState.errorState.showGetUserUnknownError()
                    is ErrorState.UnauthorizedError -> uiState.errorState.showUnauthorizedError()
                }
                viewModel.processUserDetailsErrorState()
            }
        }
    }

    private fun fillUserInfo(user: GitHubUserDetail) {
        with(binding) {
            tvUserLogin.text = user.login
            Glide.with(this@UserDetailFragment).load(user.avatarUrl).into(ivUserImage)
            tvUserCompany.text = getString(R.string.user_company, user.company)
            tvUserEmail.text = getString(R.string.user_email, user.email)
            tvUserName.text = getString(R.string.user_name, user.name)
            tvUserTwitter.text = getString(R.string.user_twitter, user.twitterUsername)
        }
    }

    private fun ErrorState.showGetUserError() {
        this.errorType.show(null, null)
    }

    private fun ErrorState.showGetUserUnknownError() {
        this.errorType.show(null, null)
    }

    private fun ErrorState.showUnauthorizedError() {
        this.errorType.show(null, null)
    }

    private fun ErrorType.show(
        callbackPositive: (() -> Unit)? = null,
        callbackNegative: (() -> Unit)? = null
    ) {
        alert {
            setTitle(this@show.title)
            if (this@show.descriptionArguments == null) {
                setMessage(this@show.description)
            } else {
                setMessage(getString(this@show.description, *this@show.descriptionArguments))
            }
            setCancelable(false)
            setPositiveButton(
                when (this@show) {
                    is ErrorType.Cancel -> R.string.common_ok
                    is ErrorType.Retry -> R.string.common_retry
                }
            ) { _, _ ->
                callbackPositive?.invoke()
            }
            when (this@show) {
                is ErrorType.Retry -> {
                    setNegativeButton(R.string.common_cancel) { _, _ ->
                        callbackNegative?.invoke()
                    }
                }

                else -> {}
            }

        }.show()
    }
}