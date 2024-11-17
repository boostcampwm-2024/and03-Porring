package com.kolown.app_test_camera.di;

import com.kolown.data.datasource.AppDataSource;
import com.kolown.data.repository.AppDataRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class AppDataModule_ProvideAppDataFactory implements Factory<AppDataRepository> {
  private final AppDataModule module;

  private final Provider<AppDataSource> appDataSourceProvider;

  public AppDataModule_ProvideAppDataFactory(AppDataModule module,
      Provider<AppDataSource> appDataSourceProvider) {
    this.module = module;
    this.appDataSourceProvider = appDataSourceProvider;
  }

  @Override
  public AppDataRepository get() {
    return provideAppData(module, appDataSourceProvider.get());
  }

  public static AppDataModule_ProvideAppDataFactory create(AppDataModule module,
      Provider<AppDataSource> appDataSourceProvider) {
    return new AppDataModule_ProvideAppDataFactory(module, appDataSourceProvider);
  }

  public static AppDataRepository provideAppData(AppDataModule instance,
      AppDataSource appDataSource) {
    return Preconditions.checkNotNullFromProvides(instance.provideAppData(appDataSource));
  }
}
