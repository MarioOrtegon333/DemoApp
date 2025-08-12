package com.example.demoapp.di

import com.example.demoapp.data.repository.PokedexRepositoryImpl
import com.example.demoapp.domain.repository.PokedexRepository
import com.example.demoapp.domain.usecase.GetPokemonListUseCase
import com.example.demoapp.domain.usecase.GetPokemonDetailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    @Singleton
    fun providePokedexRepository(): PokedexRepository = PokedexRepositoryImpl()

    @Provides
    @Singleton
    fun provideGetPokemonListUseCase(repository: PokedexRepository): GetPokemonListUseCase = GetPokemonListUseCase(repository)

    @Provides
    @Singleton
    fun provideGetPokemonDetailUseCase(repository: PokedexRepository): GetPokemonDetailUseCase = GetPokemonDetailUseCase(repository)
}
