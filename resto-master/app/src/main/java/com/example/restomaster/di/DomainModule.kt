package com.example.restomaster.di

import com.example.restomaster.domain.ai.AllergenSuggestionEngine
import com.example.restomaster.domain.ai.RuleBasedAllergenSuggestionEngine
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
    fun provideAllergenSuggestionEngine(): AllergenSuggestionEngine {
        return RuleBasedAllergenSuggestionEngine()
    }
}
