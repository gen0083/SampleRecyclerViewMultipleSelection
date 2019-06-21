package jp.gcreate.samplerecyclerviewmultipleselection

import org.junit.Test

import org.junit.Assert.*
import kotlin.random.Random

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private val charSource = ('0'..'z').toList()
    private val random = Random(0)
    
    @Test
    fun generate_random_string() {
        repeat(30) {
            val str = (0..random.nextInt(1, 12))
                .map { random.nextInt(0, charSource.size) }
                .map(charSource::get)
                .joinToString("")
            println(str)
        }
    }
}
