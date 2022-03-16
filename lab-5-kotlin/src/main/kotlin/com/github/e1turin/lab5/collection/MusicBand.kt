package com.github.e1turin.lab5.collection

import java.time.Clock
import java.time.LocalDate
import java.util.*

data class MusicBand(
    private var name: String,
    private var coordinates: Coordinates,
    private var numberOfParticipants: Int,
    private var albumsCount: Long,
    private var establishmentDate: Date?,
    private var genre: MusicGenre,
    private var label: Label
) : Comparable<MusicBand> {

//    @JsonSetter("establishmentDate")
//    fun setEstablishmentDate(value: String){
//        this.establishmentDate = Date.
//    }

    private var creationDate = LocalDate.now(Clock.systemUTC())
    var id: Int = -1
        private set

    fun getNumberOfParticipants() = numberOfParticipants
    fun getAlbumsCount(): Long = albumsCount

    fun setId(id: Int) {
        this.id = id
    }

    override fun compareTo(other: MusicBand): Int {
        var result: Int = name.compareTo(other.name)
        if (result==0) result = establishmentDate?.compareTo(other.establishmentDate) ?: 0
        if (result==0) result = genre.compareTo(other.genre)
        if (result==0) result = albumsCount.compareTo(other.albumsCount)
        if (result==0) result = numberOfParticipants.compareTo(other.numberOfParticipants)
        return result
    }

    override fun toString(): String {
        "sdfsd".compareTo("sDFF")
        return """
            MusicBand(id=$id,
                name='$name', 
                coordinates=$coordinates, 
                numberOfParticipants=$numberOfParticipants, 
                albumsCount=$albumsCount, 
                establishmentDate=$establishmentDate, 
                genre=$genre, 
                label=$label, 
                creationDate=$creationDate, 
            )""".trimMargin()
    }


}
