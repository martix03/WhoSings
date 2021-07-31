package it.marta.whosings.data.repository

import it.marta.whosings.data.api.WhoSingsService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WhoSingsRepository(private val dispatcher: CoroutineDispatcher) : KoinComponent {

    private val service: WhoSingsService by inject()

    suspend fun getListTrack() = withContext(dispatcher) {
        service.getListTrack().message
    }

    suspend fun getLyric(track_id: Int) = withContext(dispatcher) {
        service.getLyric(track_id).message
    }


}