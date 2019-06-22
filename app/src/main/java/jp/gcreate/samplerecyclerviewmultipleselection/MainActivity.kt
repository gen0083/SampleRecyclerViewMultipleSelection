package jp.gcreate.samplerecyclerviewmultipleselection

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import jp.gcreate.samplerecyclerviewmultipleselection.databinding.ActivityMainBinding
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SampleListAdapter
    private lateinit var tracker: SelectionTracker<Long>
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
        
        setupRecyclerView()
        initializeSampleData()
        savedInstanceState?.let {
            tracker.onRestoreInstanceState(it)
        }
    }
    
    private fun initializeSampleData() {
        sampleList = mutableListOf()
        repeat(30) {
            val data = SampleData(UUID.randomUUID().mostSignificantBits, generateRandomString())
            sampleList.add(data)
        }
        adapter.submitList(sampleList)
    }
    
    private fun setupRecyclerView() {
        binding.recyclerView.also { view ->
            adapter = SampleListAdapter()
            view.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            view.adapter = adapter
        }
        tracker = SelectionTracker.Builder("sample-selection",
            binding.recyclerView,
            StableIdKeyProvider(binding.recyclerView),
            SampleItemDetailsLookup(binding.recyclerView),
            StorageStrategy.createLongStorage()
        )
            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()
        adapter.tracker = tracker
    }
    
    private fun generateRandomString(maxLength: Int = 12): String =
        (0..random.nextInt(1, maxLength))
            .map { random.nextInt(0, charSource.size) }
            .map(charSource::get)
            .joinToString("")
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.shuffle -> true.apply {
                sampleList.shuffle()
                adapter.notifyDataSetChanged()
            }
            else         -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracker.onSaveInstanceState(outState)
    }
}
