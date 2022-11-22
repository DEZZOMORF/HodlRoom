package com.dezzomorf.financulator.api.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlin.reflect.full.memberProperties

data class CurrencyEntity(

    @SerializedName("aed")
    @Expose
    val AED: Float = 0f,

    @SerializedName("ars")
    @Expose
    val ARS: Float = 0f,

    @SerializedName("aud")
    @Expose
    val AUD: Float = 0f,

    @SerializedName("bch")
    @Expose
    val BCH: Float = 0f,

    @SerializedName("bdt")
    @Expose
    val BDT: Float = 0f,

    @SerializedName("bhd")
    @Expose
    val BHD: Float = 0f,

    @SerializedName("bmd")
    @Expose
    val BMD: Float = 0f,

    @SerializedName("bnb")
    @Expose
    val BNB: Float = 0f,

    @SerializedName("brl")
    @Expose
    val BRL: Float = 0f,

    @SerializedName("btc")
    @Expose
    val BTC: Float = 0f,

    @SerializedName("cad")
    @Expose
    val CAD: Float = 0f,

    @SerializedName("chf")
    @Expose
    val CHF: Float = 0f,

    @SerializedName("clp")
    @Expose
    val CLP: Float = 0f,

    @SerializedName("cny")
    @Expose
    val CNY: Float = 0f,

    @SerializedName("czk")
    @Expose
    val CZK: Float = 0f,

    @SerializedName("dkk")
    @Expose
    val DKK: Float = 0f,

    @SerializedName("dot")
    @Expose
    val DOT: Float = 0f,

    @SerializedName("eos")
    @Expose
    val EOS: Float = 0f,

    @SerializedName("eth")
    @Expose
    val ETH: Float = 0f,

    @SerializedName("eur")
    @Expose
    val EUR: Float = 0f,

    @SerializedName("gbp")
    @Expose
    val GBP: Float = 0f,

    @SerializedName("hkd")
    @Expose
    val HKD: Float = 0f,

    @SerializedName("huf")
    @Expose
    val HUF: Float = 0f,

    @SerializedName("idr")
    @Expose
    val IDR: Float = 0f,

    @SerializedName("ils")
    @Expose
    val ILS: Float = 0f,

    @SerializedName("inr")
    @Expose
    val INR: Float = 0f,

    @SerializedName("jpy")
    @Expose
    val JPY: Float = 0f,

    @SerializedName("krw")
    @Expose
    val KRW: Float = 0f,

    @SerializedName("kwd")
    @Expose
    val KWD: Float = 0f,

    @SerializedName("lkr")
    @Expose
    val LKR: Float = 0f,

    @SerializedName("ltc")
    @Expose
    val LTC: Float = 0f,

    @SerializedName("mmk")
    @Expose
    val MMK: Float = 0f,

    @SerializedName("mxn")
    @Expose
    val MXN: Float = 0f,

    @SerializedName("myr")
    @Expose
    val MYR: Float = 0f,

    @SerializedName("ngn")
    @Expose
    val NGN: Float = 0f,

    @SerializedName("nok")
    @Expose
    val NOK: Float = 0f,

    @SerializedName("nzd")
    @Expose
    val NZD: Float = 0f,

    @SerializedName("php")
    @Expose
    val PHP: Float = 0f,

    @SerializedName("pkr")
    @Expose
    val PKR: Float = 0f,

    @SerializedName("pln")
    @Expose
    val PLN: Float = 0f,

    @SerializedName("rub")
    @Expose
    val RUB: Float = 0f,

    @SerializedName("sar")
    @Expose
    val SAR: Float = 0f,

    @SerializedName("sek")
    @Expose
    val SEK: Float = 0f,

    @SerializedName("sgd")
    @Expose
    val SGD: Float = 0f,

    @SerializedName("thb")
    @Expose
    val THB: Float = 0f,

    @SerializedName("try")
    @Expose
    val TRY: Float = 0f,

    @SerializedName("twd")
    @Expose
    val TWD: Float = 0f,

    @SerializedName("uah")
    @Expose
    val UAH: Float = 0f,

    @SerializedName("usd")
    @Expose
    val USD: Float = 0f,

    @SerializedName("vef")
    @Expose
    val VER: Float = 0f,

    @SerializedName("vnd")
    @Expose
    val VND: Float = 0f,

    @SerializedName("xag")
    @Expose
    val XAG: Float = 0f,

    @SerializedName("xau")
    @Expose
    val XAU: Float = 0f,

    @SerializedName("xdr")
    @Expose
    val XDR: Float = 0f,

    @SerializedName("xlm")
    @Expose
    val XLM: Float = 0f,

    @SerializedName("xrp")
    @Expose
    val XRP: Float = 0f,

    @SerializedName("yfi")
    @Expose
    val YFI: Float = 0f,

    @SerializedName("zar")
    @Expose
    val ZAR: Float = 0f,

    @SerializedName("bits")
    @Expose
    val BITS: Float = 0f,

    @SerializedName("link")
    @Expose
    val LINK: Float = 0f,

    @SerializedName("sats")
    @Expose
    val SATS: Float = 0f,
) {

    fun getPriceByPosition(position: Int): Float {
        return CurrencyEntity::class.memberProperties.map { member ->
            member.get(this)
        }[position] as Float? ?: 0f
    }
}