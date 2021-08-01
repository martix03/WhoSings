package it.marta.whosings.data.api

import it.marta.whosings.data.vo.Lyric
import it.marta.whosings.data.vo.Lyrics
import it.marta.whosings.data.vo.ResponseWrapper
import it.marta.whosings.data.vo.TrackList
import retrofit2.http.GET
import retrofit2.http.Query

interface WhoSingsService {

    @GET("chart.tracks.get?page=1&page_size=3&country=IT&f_has_lyrics=1&apikey=$apikeyMarta")
    suspend fun getListTrack(): ResponseWrapper<TrackList>

    @GET("track.lyrics.get")
    suspend fun getLyric(@Query("track_id") track_id: Int, @Query("apikey") apikey: String = apikeyMarta): ResponseWrapper<Lyrics>

}