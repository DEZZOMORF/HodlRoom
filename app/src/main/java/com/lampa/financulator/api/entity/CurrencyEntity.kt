package com.lampa.financulator.api.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CurrencyEntity(

    @SerializedName("aed")
    @Expose
    val AED: Float?,

    @SerializedName("ars")
    @Expose
    val ARS: Float?,

    @SerializedName("aud")
    @Expose
    val AUD: Float?,

    @SerializedName("bch")
    @Expose
    val BCH: Float?,

    @SerializedName("bdt")
    @Expose
    val BDT: Float?,

    @SerializedName("bhd")
    @Expose
    val BHD: Float?,

    @SerializedName("bmd")
    @Expose
    val BMD: Float?,

    @SerializedName("bnb")
    @Expose
    val BNB: Float?,

    @SerializedName("brl")
    @Expose
    val BRL: Float?,

    @SerializedName("btc")
    @Expose
    val BTC: Float?,

    @SerializedName("cad")
    @Expose
    val CAD: Float?,

    @SerializedName("chf")
    @Expose
    val CHF: Float?,

    @SerializedName("clp")
    @Expose
    val CLP: Float?,

    @SerializedName("cny")
    @Expose
    val CNY: Float?,

    @SerializedName("czk")
    @Expose
    val CZK: Float?,

    @SerializedName("dkk")
    @Expose
    val DKK: Float?,

    @SerializedName("dot")
    @Expose
    val DOT: Float?,

    @SerializedName("eos")
    @Expose
    val EOS: Float?,

    @SerializedName("eth")
    @Expose
    val ETH: Float?,

    @SerializedName("eur")
    @Expose
    val EUR: Float?,

    @SerializedName("gbp")
    @Expose
    val GBP: Float?,

    @SerializedName("hkd")
    @Expose
    val HKD: Float?,

    @SerializedName("huf")
    @Expose
    val HUF: Float?,

    @SerializedName("idr")
    @Expose
    val IDR: Float?,

    @SerializedName("ils")
    @Expose
    val ILS: Float?,

    @SerializedName("inr")
    @Expose
    val INR: Float?,

    @SerializedName("jpy")
    @Expose
    val JPY: Float?,

    @SerializedName("krw")
    @Expose
    val KRW: Float?,

    @SerializedName("kwd")
    @Expose
    val KWD: Float?,

    @SerializedName("lkr")
    @Expose
    val LKR: Float?,

    @SerializedName("ltc")
    @Expose
    val LTC: Float?,

    @SerializedName("mmk")
    @Expose
    val MMK: Float?,

    @SerializedName("mxn")
    @Expose
    val MXN: Float?,

    @SerializedName("myr")
    @Expose
    val MYR: Float?,

    @SerializedName("ngn")
    @Expose
    val NGN: Float?,

    @SerializedName("nok")
    @Expose
    val NOK: Float?,

    @SerializedName("nzd")
    @Expose
    val NZD: Float?,

    @SerializedName("php")
    @Expose
    val PHP: Float?,

    @SerializedName("pkr")
    @Expose
    val PKR: Float?,

    @SerializedName("pln")
    @Expose
    val PLN: Float?,

    @SerializedName("rub")
    @Expose
    val RUB: Float?,

    @SerializedName("sar")
    @Expose
    val SAR: Float?,

    @SerializedName("sek")
    @Expose
    val SEK: Float?,

    @SerializedName("sgd")
    @Expose
    val SGD: Float?,

    @SerializedName("thb")
    @Expose
    val THB: Float?,

    @SerializedName("try")
    @Expose
    val TRY: Float?,

    @SerializedName("twd")
    @Expose
    val TWD: Float?,

    @SerializedName("uah")
    @Expose
    val UAH: Float?,

    @SerializedName("usd")
    @Expose
    val USD: Float?,

    @SerializedName("vef")
    @Expose
    val VER: Float?,

    @SerializedName("vnd")
    @Expose
    val VND: Float?,

    @SerializedName("xag")
    @Expose
    val XAG: Float?,

    @SerializedName("xau")
    @Expose
    val XAU: Float?,

    @SerializedName("xdr")
    @Expose
    val XDR: Float?,

    @SerializedName("xlm")
    @Expose
    val XLM: Float?,

    @SerializedName("xrp")
    @Expose
    val XRP: Float?,

    @SerializedName("yfi")
    @Expose
    val YFI: Float?,

    @SerializedName("zar")
    @Expose
    val ZAR: Float?,

    @SerializedName("bits")
    @Expose
    val BITS: Float?,

    @SerializedName("link")
    @Expose
    val LINK: Float?,

    @SerializedName("sats")
    @Expose
    val SATS: Float?,
)