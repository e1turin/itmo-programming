package com.github.e1turin.application

import kotlinx.datetime.Clock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.serializers.LocalDateComponentSerializer
import kotlinx.datetime.todayAt
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import java.text.DateFormat
import java.text.SimpleDateFormat
//import java.time.Clock
//import java.time.LocalDate
//import java.util.*
import kotlinx.serialization.encoding.*
//import java.util.Date

//import java.time.LocalDate


@Serializable
data class MusicBand(
    private val name: String,
    private val coordinates: Coordinates,
    private val numberOfParticipants: Int,
    private val albumsCount: Long,
//    @Serializable(with = DateSerializer::class) private val establishmentDate: Date?,
    private val establishmentDate: LocalDate?,
    private val genre: MusicGenre,
    private val label: Label
) : Comparable<MusicBand> {

//    @JsonSetter("establishmentDate")
//    fun setEstablishmentDate(value: String){
//        this.establishmentDate = Date.
//    }
//    @Serializable(with = LocalDateSerializer::class)
    private var creationDate: LocalDate = Clock.System.todayAt(TimeZone.currentSystemDefault())
//    private var creationDate = LocalDate.now(Clock.systemUTC())
    var id: Int = -1
        private set

    fun getNumberOfParticipants() = numberOfParticipants
    fun getAlbumsCount(): Long = albumsCount

    fun setId(id: Int) {
        this.id = id
    }

    override fun compareTo(other: MusicBand): Int {
        var result: Int = name.compareTo(other.name)
        if (result==0) result = genre.compareTo(other.genre)
        if (result==0) result = other.establishmentDate?.let { establishmentDate?.compareTo(it) } ?: 0
        if (result==0) result = albumsCount.compareTo(other.albumsCount)
        if (result==0) result = numberOfParticipants.compareTo(other.numberOfParticipants)
        return result
    }

    override fun toString(): String {
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

/*
object DateSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    // Consider wrapping in ThreadLocal if serialization may happen in multiple threads
    private val df: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS").apply {
        timeZone = TimeZone.getTimeZone("GMT+2")
    }

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(df.format(value))
    }

    override fun deserialize(decoder: Decoder): Date {
        return df.parse(decoder.decodeString())
    }
}

 */

/*
object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind
        .STRING)

    // Consider wrapping in ThreadLocal if serialization may happen in multiple threads
    private val df: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS").apply {
        timeZone = TimeZone.getTimeZone("GMT+2")
    }

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(df.format(value))
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return df.parse(decoder.decodeString())
    }
}

 */
