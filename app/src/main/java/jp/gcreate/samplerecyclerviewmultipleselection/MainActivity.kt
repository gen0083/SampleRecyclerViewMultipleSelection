package jp.gcreate.samplerecyclerviewmultipleselection

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import jp.gcreate.samplerecyclerviewmultipleselection.databinding.ActivityMainBinding
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val random = Random(System.currentTimeMillis())
    private val charSource = ('0'..'z').toList()
    private var sampleList: MutableList<SampleData> = mutableListOf()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        
        binding.fab.setOnClickListener { view ->
            initializeSampleData()
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        
        initializeSampleData()
    }
    
    private fun initializeSampleData() {
        sampleList = mutableListOf()
        repeat(30) {
            val data = SampleData(UUID.randomUUID().toString(), generateRandomString())
            sampleList.add(data)
        }
        Log.d("test", sampleList.joinToString("\n"))
    }
    
    private fun generateRandomString(maxLength: Int = 12): String =
        (0..random.nextInt(1, maxLength))
            .map { random.nextInt(0, charSource.size) }
            .map(charSource::get)
            .joinToString("")
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else                 -> super.onOptionsItemSelected(item)
        }
    }
}
