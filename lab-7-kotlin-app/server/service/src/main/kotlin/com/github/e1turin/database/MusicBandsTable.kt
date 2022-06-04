package com.github.e1turin.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.jodatime.CurrentDateTime
import org.jetbrains.exposed.sql.jodatime.date
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime
import java.time.Instant

object MusicBandsTable : IntIdTable("musicbands") {
    val name = varchar("name", 50)
    val coord_x = double("coord_x")
    val coord_y = double("coord_y")
    val albums_count = long("albums_count")
    val establishment = date("date_est").nullable().default(null)
    val genre = varchar("genre", 15)
    val label = long("label")
    val owner = reference(
        "owner",
        UsersTable,
        onDelete = ReferenceOption.SET_NULL,
        onUpdate = ReferenceOption.CASCADE
    ).nullable()
}