package com.kolown.app_test_camera.di;

import android.content.Context;
import com.kolown.data.repository.ImageCacheRepository;
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
public final class ImageCacheModule_ProvideImageCacheRepositoryFactory implements Factory<ImageCacheRepository> {
  private final ImageCacheModule module;

  private final Provider<Context> applicationContextProvider;

  public ImageCacheModule_ProvideImageCacheRepositoryFactory(ImageCacheModule module,
      Provider<Context> applicationContextProvider) {
    this.module = module;
    this.applicationContextProvider = applicationContextProvider;
  }

  @Override
  public ImageCacheRepository get() {
    return provideImageCacheRepository(module, applicationContextProvider.get());
  }

  public static ImageCacheModule_ProvideImageCacheRepositoryFactory create(ImageCacheModule module,
      Provider<Context> applicationContextProvider) {
    return new ImageCacheModule_ProvideImageCacheRepositoryFactory(module, applicationContextProvider);
  }

  public static ImageCacheRepository provideImageCacheRepository(ImageCacheModule instance,
      Context applicationContext) {
    return Preconditions.checkNotNullFromProvides(instance.provideImageCacheRepository(applicationContext));
  }
}
