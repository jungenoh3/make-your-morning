package com.nochunsam.makeyourmorning.common.data


data class BlockTime (
    val id: String = "MAIN_${System.currentTimeMillis()}",
    val minute: Int,
)