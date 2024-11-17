package com.kolown.app_test_camera.di;

import android.content.Context;
import com.kolown.data.datasource.AppDataSource;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class AppDataModule_ProvidesAppDataSourceFactory implements Factory<AppDataSource> {
  private final AppDataModule module;

  private final Provider<Context> contextProvider;

  public AppDataModule_ProvidesAppDataSourceFactory(AppDataModule module,
      Provider<Context> contextProvider) {
    this.module = module;
    this.contextProvider = contextProvider;
  }

  @Override
  public AppDataSource get() {
    return providesAppDataSource(module, contextProvider.get());
  }

  public static AppDataModule_ProvidesAppDataSourceFactory create(AppDataModule module,
      Provider<Context> contextProvider) {
    return new AppDataModule_ProvidesAppDataSourceFactory(module, contextProvider);
  }

  public static AppDataSource providesAppDataSource(AppDataModule instance, Context context) {
    return Preconditions.checkNotNullFromProvides(instance.providesAppDataSource(context));
  }
}
