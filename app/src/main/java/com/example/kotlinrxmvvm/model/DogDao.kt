package com.example.kotlinrxmvvm.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DogDao {
    @Insert
    suspend fun insertAll(vararg dogs: DogBreed): List<Long>

    @Query ("select * from dogbreed")
    suspend fun getAllDogs(): List<DogBreed>

    @Query("select * from dogbreed where breed_id = :dogID")
    suspend fun  getDogInfo(dogID : Int) : DogBreed

    @Query("DELETE FROM dogbreed")
    suspend fun deleteAllDogs()
}