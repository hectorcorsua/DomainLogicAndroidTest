package com.example.domainlogicandroidtest.presentation.features.searchusers

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domainlogicandroidtest.R
import com.example.domainlogicandroidtest.databinding.FragmentSearchUsersBinding
import com.example.domainlogicandroidtest.domain.model.GitHubUser
import com.example.domainlogicandroidtest.presentation.extensions.alert
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchUsersFragment : Fragment(R.layout.fragment_search_users) {
    private var _binding: FragmentSearchUsersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchUsersViewModel by viewModels()
    private lateinit var adapter: SearchUsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.searchUsersErrorState.collect { renderSearchUsersErrorState(it) }
            }
            launch {
                viewModel.searchUsersInfoState.collect { renderSearchUsersInfoState(it) }
            }
        }

        setUpView()
    }

    override fun onStop() {
        viewModel.stopJobs()
        super.onStop()
    }

    private fun setUpView() {

        binding.tilSearch.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(editable: Editable) {
                viewModel.checkSearchRequirements(editable.toString())
            }
        })

        binding.btnSearch.setOnClickListener {
            viewModel.searchGitHubUsers(binding.tilSearch.editText?.text.toString())
        }
        createAdapter()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun createAdapter() {
        binding.rvGithubUsers.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.rvGithubUsers.setHasFixedSize(true)
        adapter = SearchUsersAdapter(this::showGithubUserInfo)
        binding.rvGithubUsers.adapter = adapter
    }

    private fun showGithubUserInfo(gitHubUser: GitHubUser) {
        //TODO navigate To Show Github User Info
        //viewModel.saveProductInCart(gitHubUser)
        val bundle = Bundle().apply {
            putInt("userId", gitHubUser.id)
        }
        findNavController().navigate(R.id.action_searchUsersFragment_to_userDetailFragment, bundle)
        //findNavController().navigate(SearchUsersFragmentDirections.actionSearchUsersFragmentToUserDetailFragment())
    }


    private fun renderSearchUsersInfoState(state: SearchUsersInfoUIState) {
        when (state) {
            SearchUsersInfoUIState.Idle -> {}
            SearchUsersInfoUIState.EmptySearch -> {
                binding.btnSearch.isEnabled = false
                binding.tilSearch.error = null
                viewModel.processSearchUsersInfoState()
            }

            SearchUsersInfoUIState.InvalidMaxLengthCharacters -> {
                binding.btnSearch.isEnabled = false
                binding.tilSearch.error = getString(R.string.max_characters_error)
                viewModel.processSearchUsersInfoState()
            }

            SearchUsersInfoUIState.InvalidMinLengthCharacters -> {
                binding.btnSearch.isEnabled = false
                binding.tilSearch.error = getString(R.string.min_characters_error)
                viewModel.processSearchUsersInfoState()
            }

            SearchUsersInfoUIState.EnableSearchButton -> {
                binding.btnSearch.isEnabled = true
                binding.tilSearch.error = null
                viewModel.processSearchUsersInfoState()
            }

            SearchUsersInfoUIState.Loading -> {
                showLoading(true)
                viewModel.processSearchUsersInfoState()
            }

            is SearchUsersInfoUIState.ShowUserList -> {
                showLoading(false)
                binding.rvGithubUsers.visibility = View.VISIBLE
                binding.lyEmptyList.visibility = View.GONE
                adapter.setGithubUserList(state.gitHubUserList)
                viewModel.processSearchUsersInfoState()
            }

            SearchUsersInfoUIState.UserListEmpty -> {
                showLoading(false)
                binding.rvGithubUsers.visibility = View.GONE
                binding.lyEmptyList.visibility = View.VISIBLE
                viewModel.processSearchUsersInfoState()
            }
        }
    }

    private fun renderSearchUsersErrorState(state: SearchUsersErrorUIState) {
        when (state) {
            SearchUsersErrorUIState.Idle -> {}
            is SearchUsersErrorUIState.ShowSearchUsersError -> {
                when (state.errorState) {
                    is ErrorState.SearchUsersError -> state.errorState.showSearchUserError()
                    is ErrorState.SearchUsersUnknownError -> state.errorState.showSearchUserUnknownError()
                    is ErrorState.UnauthorizedError -> state.errorState.showUnauthorizedError()
                }
                viewModel.processSearchUsersErrorState()
            }
        }
    }

    private fun ErrorState.showSearchUserError() {
        this.errorType.show(null, null)
    }

    private fun ErrorState.showSearchUserUnknownError() {
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
            if(this@show.descriptionArguments == null) {
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