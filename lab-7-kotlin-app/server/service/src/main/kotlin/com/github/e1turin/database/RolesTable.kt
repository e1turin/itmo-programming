package com.github.e1turin.database

import org.jetbrains.exposed.dao.id.IntIdTable

object RolesTable : IntIdTable("user_roles") {
    val role = varchar("role", 40)
        .default(DatabaseOpts.Roles.common)
        .uniqueIndex()
}