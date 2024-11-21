package com.kolown.detail.navigation

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import com.kolown.detail.DetailRoute
import com.kolown.model.PostContentModel
import com.kolown.model.PostModel
import com.kolown.navigation.AppRoute
import com.kolown.navigation.MainMenuRoute
import com.kolown.navigation.Route
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

val PostType = object : NavType<PostContentModel>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): PostContentModel? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): PostContentModel {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: PostContentModel) {
        bundle.putString(key, Json.encodeToString(PostContentModel.serializer(), value))
    }

    override fun serializeAsValue(value: PostContentModel): String  = Uri.encode(Json.encodeToString<PostContentModel>(value))
}

fun NavController.navigateToDetail(postContentModel: PostContentModel) {
    navigate(AppRoute.Detail(postContentModel = postContentModel))
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.detailNavGraph(
    padding: PaddingValues
) {
    composable<AppRoute.Detail>(
        typeMap = mapOf(typeOf<PostContentModel>() to PostType),
    ) {
        DetailRoute(padding = padding)
    }
}