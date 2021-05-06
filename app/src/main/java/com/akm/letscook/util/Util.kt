package com.akm.letscook.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("SimpleDateFormat")
fun currDate(): String = SimpleDateFormat("yyyyMMdd").format(Date())
