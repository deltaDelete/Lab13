package ru.deltadelete.lab13

import org.junit.Test

import org.junit.Assert.*
import ru.deltadelete.lab13.api.WaifuPicsApi
import ru.deltadelete.lab13.api.WaifuPicsApiImpl

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun api_isAccessible() {
        val api: WaifuPicsApi = WaifuPicsApiImpl()
        val result = api.getRandom()
        val matches = Regex("https://i\\.waifu\\.pics/[\\w\\d-~]+\\.(png|jpg|gif)").matches(result)
        assertTrue(matches)
    }

    @Test
    fun regex_checkUrl() {
        val result = "https://i.waifu.pics/cKe~_-Z.jpg"
        val matches = Regex("https://i\\.waifu\\.pics/[\\w\\d-~]+\\.(png|jpg|gif)").matches(result)
        assertTrue(matches)
    }

    @Test
    fun api_isManyAccessible() {
        val api: WaifuPicsApi = WaifuPicsApiImpl()
        val result = api.getManyRandom()
        assertEquals(30, result.size)
    }
}