package ru.deltadelete.lab13.api

interface WaifuPicsApi {
    fun getRandom(category: Categories = Categories.waifu): String

    fun getManyRandom(amount: Int = 30, category: Categories = Categories.waifu): List<String>

    enum class Categories {
        waifu,
        neko,
        shinobu,
        megumin,
        bully,
        cuddle,
        cry,
        hug,
        awoo,
        kiss,
        lick,
        pat,
        smug,
        bonk,
        yeet,
        blush,
        smile,
        wave,
        highfive,
        handhold,
        nom,
        bite,
        glomp,
        slap,
        kill,
        kick,
        happy,
        wink,
        poke,
        dance,
        cringe,
    }
}