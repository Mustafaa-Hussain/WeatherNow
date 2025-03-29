package com.mustafa.weathernow.map.data.pojo

import com.google.gson.annotations.SerializedName

data class SearchItem(
	@SerializedName("place_id")
	val placeId: Int,
	val lon: String,
	val lat: String,
	@SerializedName("display_name")
	val displayName: String,
	val name: String
)

