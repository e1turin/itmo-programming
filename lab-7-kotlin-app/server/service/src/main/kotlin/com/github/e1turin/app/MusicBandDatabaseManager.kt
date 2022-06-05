package com.github.e1turin.app

import com.github.e1turin.database.DatabaseOpts
import com.github.e1turin.database.MusicBandsTable
import com.github.e1turin.database.RolesTable
import com.github.e1turin.database.UsersTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.io.IOException
import java.sql.SQLException
import java.time.Instant

internal class MusicBandDatabaseManager(musicBandStorage: MusicBandStorage) :
    MusicBandStorageManager(musicBandStorage) {
    protected val db = Database.connect(
        url = "jdbc:postgresql://pg:5432/studs",
        driver = "org.postgresql.Driver",
        user = System.getenv("username"),
        password = System.getenv("password")
    )
    private val users = UsersTable
    private val musicBands = MusicBandsTable
    private val roles = RolesTable

    init {
        transaction(db) {
            SchemaUtils.createMissingTablesAndColumns(users, musicBands, roles)
        }
    }

    @kotlin.jvm.Throws(SQLException::class, ExposedSQLException::class)
    override fun clearStore() {
        val ex = transaction(db) {
            try {
                musicBands.deleteAll()
                return@transaction null
            } catch (e: Exception) {
                return@transaction e
            }
        }
        if (ex == null) super.clearStore()
        else throw ex
    }

    @kotlin.jvm.Throws(SQLException::class, ExposedSQLException::class)
    override fun addElement(obj: MusicBand) {
        val (ex, id) = transaction(db) {
            try {
                val id = musicBands.insert {
                    it[name] = obj.name
                    it[coord_x] = obj.coordinates.x
                    it[coord_y] = obj.coordinates.y
                    it[albums_count] = obj.albumsCount
                    it[establishment] = DateTime(obj.establishmentDate ?: Instant.now())
                    it[genre] = obj.genre.name
                    it[label] = obj.label.bands
                } get musicBands.id
                return@transaction null to id
            } catch (e: Exception) {
                return@transaction e to null
            }
        }
        if (ex == null) super.addElement(obj.apply { setId(id!!.value) })
        else throw ex
    }

    @kotlin.jvm.Throws(SQLException::class, ExposedSQLException::class)
    fun addElementWithOwner(obj: MusicBand, creator: String) {
        val (ex, id) = transaction(db) {
            try {
                val id = musicBands.insert {
                    it[name] = obj.name
                    it[coord_x] = obj.coordinates.x
                    it[coord_y] = obj.coordinates.y
                    it[albums_count] = obj.albumsCount
//                    it[establishment] = DateTime(obj.establishmentDate ?: Instant.now())
                    it[genre] = obj.genre.name
                    it[label] = obj.label.bands
                    it[owner] = (users.select { users.name eq creator }.first()[users.id])
                } get musicBands.id
                return@transaction null to id
            } catch (e: Exception) {
                return@transaction e to null
            }
        }
        if (ex == null) super.addElement(obj.apply { setId(id!!.value) })
        else throw ex
    }

    @kotlin.jvm.Throws(SQLException::class, ExposedSQLException::class)
    fun removeWithId(id: Int) {
        val ex = transaction(db) {
            try {
                musicBands.deleteWhere {
                    musicBands.id eq id
                }
                return@transaction null
            } catch (e: Exception) {
                return@transaction e
            }
        }
        if (ex == null) super.removeIf { musicband ->
            musicband.id == id
        }
        else throw ex
    }

    @kotlin.jvm.Throws(SQLException::class, ExposedSQLException::class)
    fun updateWithId(id: Int, obj: MusicBand) {
        val ex = transaction(db) {
            try {
                musicBands.update(where = { musicBands.id eq id }, body = {
                    it[name] = obj.name
                    it[coord_x] = obj.coordinates.x
                    it[coord_y] = obj.coordinates.y
                    it[albums_count] = obj.albumsCount
//                    it[establishment] = DateTime(obj.establishmentDate)
                    it[genre] = obj.genre.name
                    it[label] = obj.label.bands
                })
                return@transaction null
            } catch (e: Exception) {
                return@transaction e
            }
        }
        if (ex == null) {
            removeWithId(id)
            super.addElement(obj.apply { setId(id) })
        } else throw ex
    }

    @kotlin.jvm.Throws(SQLException::class, ExposedSQLException::class)
    fun removeEquals(obj: MusicBand) {
        val id = findId { it == obj } ?: throw IOException("Object was not found")
        removeWithId(id)
    }

    @kotlin.jvm.Throws(SQLException::class, ExposedSQLException::class)
    fun getRoleByUserid(id: Int): String {
        val (ex, role) = transaction(db) {
            try {
                val role = users.join(
                    roles,
                    JoinType.INNER,
                    additionalConstraint = { users.role eq roles.id }
                ).select {
                    users.id eq id
                }.first()[roles.role]
                return@transaction null to role
            } catch (e: Exception) {
                return@transaction e to null
            }
        }
        if (ex != null) throw ex
        return role!!
    }

    @kotlin.jvm.Throws(
        SQLException::class,
        ExposedSQLException::class,
        NoSuchElementException::class
    )
    fun checkPassword(login: String, checking_password: String): Boolean {
        val (ex, password) = transaction(db) {
            try {
                val passwd = users.select {
                    users.name eq login
                }.first()[users.password]
                return@transaction null to passwd
            } catch (e: Exception) {
                return@transaction e to null
            }
        }
        if (ex != null) throw ex
        return password == checking_password
    }

    @kotlin.jvm.Throws(
        SQLException::class,
        ExposedSQLException::class,
        NoSuchElementException::class
    )
    fun getOwnerLoginByObjId(id: Int): String {
        val (ex, login) = transaction(db) {
            try {
                val login = musicBands.join(
                    users,
                    JoinType.INNER,
                    additionalConstraint = { musicBands.owner eq users.id }
                ).select {
                    musicBands.id eq id
                }.first()[users.name]
                return@transaction null to login
            } catch (e: Exception) {
                return@transaction e to null
            }
        }
        if (ex != null) throw ex
        return login!!
    }

    fun hasPermissionOn(objId: Int, login: String): Boolean {
        return try {
            getRoleByUserid(objId) in listOf(
                login, DatabaseOpts.Roles.admin, DatabaseOpts.Roles.editor
            )
        } catch (_: Exception) {
            false
        }
    }

    fun authoriseNewUser(login: String, encryptedPasswd: String) {
        val ex = transaction(db) {
            try {
                val roleid = roles.select { roles.role eq DatabaseOpts.Roles.common }.first()[roles
                    .id]
                users.insert {
                    it[name] = login
                    it[password] = encryptedPasswd
                    it[role] = roleid
                }
                return@transaction null
            } catch (e: Exception) {
                return@transaction e
            }
        }
        if (ex != null) throw ex
    }

    fun loadDataFromDatabase() {
        val (ex, list) = transaction(db) {
            try {
                val data = musicBands.selectAll().limit(100).map { it ->
                    MusicBand(
                        it[MusicBandsTable.name],
                        Coordinates(
                            it[MusicBandsTable.coord_x],
                            it[MusicBandsTable.coord_y]
                        ),
                        4,
                        it[MusicBandsTable.albums_count],
                        null,
                        MusicGenre.valueOf(it[MusicBandsTable.genre]),
                        Label(it[MusicBandsTable.label])
                    )
                }
                return@transaction null to data
            } catch (e: Exception) {
                return@transaction e to null
            }
        }
        store.appendData(list ?: listOf())

    }

}
