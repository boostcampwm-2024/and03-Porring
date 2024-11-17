package com.kolown.app_test_camera;

import com.kolown.data.repository.AppDataRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<AppDataRepository> appDataRepositoryProvider;

  public MainViewModel_Factory(Provider<AppDataRepository> appDataRepositoryProvider) {
    this.appDataRepositoryProvider = appDataRepositoryProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(appDataRepositoryProvider.get());
  }

  public static MainViewModel_Factory create(
      Provider<AppDataRepository> appDataRepositoryProvider) {
    return new MainViewModel_Factory(appDataRepositoryProvider);
  }

  public static MainViewModel newInstance(AppDataRepository appDataRepository) {
    return new MainViewModel(appDataRepository);
  }
}
