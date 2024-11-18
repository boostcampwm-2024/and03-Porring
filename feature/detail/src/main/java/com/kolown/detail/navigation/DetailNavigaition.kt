package com.kolown.detail.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kolown.detail.DetailRoute

const val DETAIL_ROUTE = "detail_route"

fun NavController.navigateToDetail(navOptions: NavOptions? = null) = navigate(DETAIL_ROUTE,navOptions)

fun NavGraphBuilder.detailNavGraph(
    padding: PaddingValues
){
    composable(route = DETAIL_ROUTE) {
        DetailRoute(padding=padding)
    }
}