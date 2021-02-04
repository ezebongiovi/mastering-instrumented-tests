package com.edipasquale.bitrise.view.activity.main

import android.os.Bundle
import android.view.View
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edipasquale.bitrise.databinding.ActivityMainBinding
import com.edipasquale.bitrise.db.entity.RecentAnimeEntity
import com.edipasquale.bitrise.dto.User
import com.edipasquale.bitrise.view.activity.auth.core.AuthActivity
import com.edipasquale.bitrise.view.adapter.RecentAnimeAdapter
import com.edipasquale.bitrise.viewmodel.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AuthActivity() {

    private lateinit var _binding: ActivityMainBinding
    private val _viewModel: MainViewModel by viewModel()
    private val _adapter = RecentAnimeAdapter()

    override fun onAuthenticated(savedInstanceState: Bundle?, user: User) {
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setSupportActionBar(_binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        _binding.latestAdditionsView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        _binding.latestAdditionsView.adapter = _adapter

        _viewModel.latestAdditions.observe(this, {
            render(it)
        })
    }

    private fun render(model: PagedList<RecentAnimeEntity>) {
        if (model.isEmpty()) {
            _binding.viewFlipper.visibility = View.GONE
        } else {
            _binding.viewFlipper.visibility = View.VISIBLE
            _binding.viewFlipper.displayedChild = 1
            _adapter.submitList(model)
        }
    }

    override fun shouldAuthenticate() = true
}