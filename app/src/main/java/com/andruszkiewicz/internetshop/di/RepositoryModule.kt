package com.andruszkiewicz.internetshop.di

import com.andruszkiewicz.internetshop.domain.repository.ProductRepository
import com.andruszkiewicz.internetshop.network.repository.ProductRepositoryImpl
import com.andruszkiewicz.internetshop.network.service.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductRepository(service: ProductService): ProductRepository =
        ProductRepositoryImpl(service)

}