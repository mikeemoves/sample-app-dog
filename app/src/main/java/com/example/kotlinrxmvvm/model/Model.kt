package com.example.kotlinrxmvvm.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class DogBreed(
    @ColumnInfo(name = "breed_id")
    @SerializedName("id")
    val breedID: String?,
    @ColumnInfo(name = "dog_name")
    @SerializedName("name")
    val dogBreed: String?,
    @ColumnInfo(name = "life_span")
    @SerializedName("life_span")
    val lifespan: String?,
    @ColumnInfo(name = "breed_group")
    @SerializedName("breed_group")
    val breedGroup: String?,
    @ColumnInfo(name = "bred_for")
    @SerializedName("bred_for")
    val breedFor: String?,

    @SerializedName("temperament")
    val temperament: String,
    @ColumnInfo(name = "dog_url")
    @SerializedName("url")
    val imageURL: String
){
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
    constructor() : this("", "", "", "", "", "", "")
}

data class DogPalette(var color : Int)