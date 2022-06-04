package com.github.e1turin.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object UsersTable : IntIdTable("users") {
    val name = varchar("name", 100).uniqueIndex()
    val password = varchar("pass", 100)
    val role = reference(
        "role",
        RolesTable,
        onDelete = ReferenceOption.SET_NULL,
        onUpdate = ReferenceOption.CASCADE
    ).nullable()
}