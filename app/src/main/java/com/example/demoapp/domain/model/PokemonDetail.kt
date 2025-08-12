package com.example.demoapp.domain.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
    val height: Int,
    val weight: Int,
    val abilities: List<String>,
    val stats: List<Stat>
)

data class Stat(
    val name: String,
    val baseStat: Int
)
