package com.mustafa.weathernow.utils

import java.text.NumberFormat

fun Number?.format(): String = NumberFormat.getInstance().format(this?:0)
