package frgp.utn.edu.ar.tp3.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "parkings",
    indices = [ Index("user") ],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = User::class,
            parentColumns = ["username"],
            childColumns = ["user"],
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class Parking (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val user: String,
    val mat: String,
    val duration: Int
)