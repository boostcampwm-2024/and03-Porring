package com.kolown.data.remote

import com.kolown.model.FollowerModel


data class FollowerDto(
    val followId : String = "",
    val followerId : String = "",
    val followerName : String = "",
    val userId : String = ""
)

fun FollowerDto.toFollowerModel() : FollowerModel {
    return FollowerModel(
        followerId = followerId,
        followerName = followerName
    )
}