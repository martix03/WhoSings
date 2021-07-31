package it.marta.whosings.data.vo

import com.google.gson.annotations.SerializedName

data class TrackList(
    @SerializedName("track_list")
    val trackList: List<Track>?
)

data class Track(
    @SerializedName("track")
    val track: TrackDetail?
)

data class TrackDetail(
    @SerializedName("album_id")
    val albumId: Int?,
    @SerializedName("album_name")
    val albumName: String?,
    @SerializedName("artist_id")
    val artistId: Int?,
    @SerializedName("artist_name")
    val artistName: String?,
    @SerializedName("commontrack_id")
    val commontrackId: Int?,
    @SerializedName("explicit")
    val explicit: Int?,
    @SerializedName("has_lyrics")
    val hasLyrics: Int?,
    @SerializedName("has_richsync")
    val hasRichsync: Int?,
    @SerializedName("has_subtitles")
    val hasSubtitles: Int?,
    @SerializedName("instrumental")
    val instrumental: Int?,
    @SerializedName("num_favourite")
    val numFavourite: Int?,
    @SerializedName("primary_genres")
    val primaryGenres: PrimaryGenres?,
    @SerializedName("restricted")
    val restricted: Int?,
    @SerializedName("track_edit_url")
    val trackEditUrl: String?,
    @SerializedName("track_id")
    val trackId: Int?,
    @SerializedName("track_name")
    val trackName: String?,
    @SerializedName("track_name_translation_list")
    val trackNameTranslationList: List<Any>?,
    @SerializedName("track_rating")
    val trackRating: Int?,
    @SerializedName("track_share_url")
    val trackShareUrl: String?,
    @SerializedName("updated_time")
    val updatedTime: String?
)

data class PrimaryGenres(
    @SerializedName("music_genre_list")
    val musicGenreList: List<MusicGenre>?
)

data class MusicGenre(
    @SerializedName("music_genre")
    val musicGenre: MusicGenreX?
)

data class MusicGenreX(
    @SerializedName("music_genre_id")
    val musicGenreId: Int?,
    @SerializedName("music_genre_name")
    val musicGenreName: String?,
    @SerializedName("music_genre_name_extended")
    val musicGenreNameExtended: String?,
    @SerializedName("music_genre_parent_id")
    val musicGenreParentId: Int?,
    @SerializedName("music_genre_vanity")
    val musicGenreVanity: String?
)

data class SimpleDetailTrack(
    val trackId: Int?,
    val artistName: String?,
    var stringTrack: String? = null
)
data class Lyrics(
    @SerializedName("lyrics")
    val lyrics: Lyric
)

data class Lyric(
    @SerializedName("explicit")
    val explicit: Int?,
    @SerializedName("lyrics_body")
    val lyricsBody: String?,
    @SerializedName("lyrics_copyright")
    val lyricsCopyright: String?,
    @SerializedName("lyrics_id")
    val lyricsId: Int?,
    @SerializedName("pixel_tracking_url")
    val pixelTrackingUrl: String?,
    @SerializedName("script_tracking_url")
    val scriptTrackingUrl: String?,
    @SerializedName("updated_time")
    val updatedTime: String?
)