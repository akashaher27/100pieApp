package com.example.a100pieapp.data.model

import com.google.gson.annotations.SerializedName

data class Currencies(

    @SerializedName("Currency")
    var currency: String,
    @SerializedName("CurrencyLong")
    var currencyLong: String,
    @SerializedName("TxFee")
    var txFee: String
) {
}