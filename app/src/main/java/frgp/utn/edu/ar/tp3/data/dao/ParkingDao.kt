package frgp.utn.edu.ar.tp3.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import frgp.utn.edu.ar.tp3.data.entity.Parking
import kotlinx.coroutines.flow.Flow

@Dao
interface ParkingDao {
    @Insert
    fun insert(vararg parking: Parking)

    @Delete
    fun delete(parking: Parking)

    @Query("SELECT * FROM parkings WHERE parkings.user = :username")
    fun getByUsername(username: String): Flow<List<Parking>>


}