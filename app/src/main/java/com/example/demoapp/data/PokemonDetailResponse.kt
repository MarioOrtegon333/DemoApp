package com.example.demoapp.data

import com.google.gson.annotations.SerializedName
import com.example.demoapp.data.models.OtherSprites
import com.example.demoapp.data.models.StatsItem

data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val types: List<TypeSlot>,
    val height: Int,
    val weight: Int,
    val abilities: List<AbilitySlot>,
    @SerializedName("stats")
    val stats: List<StatsItem>


)

data class Sprites(
    val front_default: String?,
    val other: OtherSprites?
)



data class TypeSlot(
    val slot: Int,
    val type: TypeInfo
)

data class TypeInfo(
    val name: String,
    val url: String
)

data class AbilitySlot(
    val ability: AbilityInfo
)

data class AbilityInfo(
    val name: String,
    val url: String
)