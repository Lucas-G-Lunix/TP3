package frgp.utn.edu.ar.tp3.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [
        Index(value = ["mail"], unique = true)
    ])
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "mail")
    val mail: String?,
    @ColumnInfo(name = "first_name")
    val firstName: String?,
    @ColumnInfo(name = "password")
    val password: String?
)