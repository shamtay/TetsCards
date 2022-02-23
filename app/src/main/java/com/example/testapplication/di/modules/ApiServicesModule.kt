package com.example.testapplication.di.modules

import com.example.testapplication.data.api.GithubApiService
import com.example.testapplication.di.scopes.ApplicationScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ApiServicesModule {

    @Provides
    @ApplicationScope
    fun getGithubApiService(retrofit: Retrofit): GithubApiService {
        return retrofit.create(GithubApiService::class.java)
    }
}