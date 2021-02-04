package com.edipasquale.bitrise.viewmodel.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.edipasquale.bitrise.repository.main.MainRepository

class MainViewModel(
    _repository: MainRepository,
    app: Application
) : AndroidViewModel(app) {

    val latestAdditions = _repository.fetchLatestAdditions(true)
}
