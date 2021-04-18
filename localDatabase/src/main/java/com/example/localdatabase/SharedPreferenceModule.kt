package com.example.localdatabase

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class SharedPreferenceModule {

    @Singleton
    @Provides
    fun sharedPreference(@ApplicationContext context: Context) :SharedPreferences
    {
     return context.getSharedPreferences("chefling", Context.MODE_PRIVATE)
    }
}