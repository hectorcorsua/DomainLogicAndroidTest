package com.example.domainlogicandroidtest.presentation.features.searchusers

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.domainlogicandroidtest.databinding.ItemGithubUserBinding
import com.example.domainlogicandroidtest.domain.model.GitHubUser

class SearchUsersAdapter(
    private val showGitHubUserInfo: (GitHubUser) -> Unit
) : androidx.recyclerview.widget.RecyclerView.Adapter<SearchUsersAdapter.ViewHolder>() {

    private var gitHubUsers = arrayListOf<GitHubUser>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemGithubUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(gitHubUsers[position])
    }

    override fun getItemCount(): Int {
        return gitHubUsers.size
    }

    inner class ViewHolder(private val itemGithubUserBinding: ItemGithubUserBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemGithubUserBinding.root) {

        fun bind(gitHubUser: GitHubUser) {
            itemGithubUserBinding.tvUserName.text = gitHubUser.login
            itemGithubUserBinding.tvUserId.text = gitHubUser.id.toString()
            itemGithubUserBinding.parent.setOnClickListener { showGitHubUserInfo(gitHubUser) }
        }
    }

    fun setGithubUserList(gitHubUserList: List<GitHubUser>) {
        gitHubUsers.clear()
        gitHubUsers.addAll(gitHubUserList)
        notifyDataSetChanged()
    }
}