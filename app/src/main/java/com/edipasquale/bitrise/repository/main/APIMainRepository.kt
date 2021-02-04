package com.edipasquale.bitrise.repository.main

import androidx.lifecycle.liveData
import com.edipasquale.bitrise.source.main.MainLocalSource
import com.edipasquale.bitrise.source.main.MainNetworkSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.io.IOException

class APIMainRepository(
    private val _source: MainNetworkSource,
    private val _localSource: MainLocalSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MainRepository {

    override fun fetchLatestAdditions(forceRefresh: Boolean) = liveData(dispatcher) {
        emitSource(_localSource.fetchLatestAdditions())

        if (forceRefresh) {
            try {
                val response = _source.fetchLatestAdditions().execute()

                if (response.isSuccessful) {
                    val list = response.body()?.let {
                        it.data ?: emptyList()
                    } ?: emptyList()

                    if (list.isNotEmpty())
                        _localSource.insertRecents(list)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}