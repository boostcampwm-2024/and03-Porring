package com.kolown.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Fake

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Real