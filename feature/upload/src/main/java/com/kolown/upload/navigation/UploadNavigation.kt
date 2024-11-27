package com.kolown.upload.navigation

import android.net.Uri
import android.os.Bundle
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kolown.model.UploadModel
import com.kolown.navigation.Route
import com.kolown.upload.UploadRoute
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

val UploadType = object : NavType<UploadModel>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): UploadModel? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): UploadModel {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: UploadModel) {
        bundle.putString(key, Json.encodeToString(UploadModel.serializer(), value))
    }

    override fun serializeAsValue(value: UploadModel): String =
        Uri.encode(Json.encodeToString<UploadModel>(value))
}

fun NavController.navigateUpload(
    imgUri: String,
    uploadModel: UploadModel,
    navOptions: NavOptions? = null
) {
    navigate(Route.Upload(imgUri, uploadModel), navOptions)
}

fun NavGraphBuilder.uploadNavGraph(
    navigateToHome: () -> Unit,
    uploadPost: (String, String, List<String>) -> Unit,
    padding: PaddingValues
) {
    composable<Route.Upload>(
        typeMap = mapOf(typeOf<UploadModel>() to UploadType)
    ) { navBackStackEntry ->
        val imgUri = navBackStackEntry.toRoute<Route.Upload>().imgUri

        UploadRoute(
            imgUri = imgUri,
            padding = padding,
            navigateToHome = navigateToHome,
            uploadPost = uploadPost
        )
    }
}