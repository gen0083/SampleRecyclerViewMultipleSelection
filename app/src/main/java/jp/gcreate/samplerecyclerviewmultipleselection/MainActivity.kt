package jp.gcreate.samplerecyclerviewmultipleselection

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
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
    private var actionMode: ActionMode? = null
    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            Log.d("ActionMode", "onCreateActionMode $mode / $menu")
            val inflater = mode.menuInflater
            inflater.inflate(R.menu.menu_main_in_action_mode, menu)
            return true
        }
        
        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            Log.d("ActionMode", "onPrepareActionMode $mode / $menu")
            Log.d("ActionMode", "recyclerView adapter = ${adapter.itemCount}")
            mode.setTitle(R.string.title_main_activity_in_action_mode)
            return false
        }
        
        override fun onDestroyActionMode(mode: ActionMode) {
            Log.d("ActionMode", "onDestroyActionMode $mode")
            actionMode = null
            tracker.clearSelection()
        }
        
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            Log.d("ActionMode", "onActionItemClicked $mode / $item")
            when (item.itemId) {
                R.id.action_mode_execute -> {
                    Log.d("ActionMode", "click execute")
                    // delete items with tracker
                    sampleList.removeAll { tracker.selection.contains(it.id) }
                    tracker.clearSelection()
                    adapter.notifyDataSetChanged()
                }
                
                R.id.action_mode_cancel  -> {
                    Log.d("ActionMode", "click cancel")
                }
                
                else                     -> return false
            }
            actionMode?.finish()
            actionMode = null
            tracker.clearSelection()
            return true
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        setTitle(R.string.title_main_activity)
        
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
        tracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionRefresh() {
                super.onSelectionRefresh()
                Log.d("tracker", "onSelectionRefresh")
            }
        
            override fun onSelectionRestored() {
                super.onSelectionRestored()
                Log.d("tracker", "onSelectionRestored")
            }
        
            override fun onItemStateChanged(key: Long, selected: Boolean) {
                super.onItemStateChanged(key, selected)
                Log.d("tracker", "onItemStateChanged $key/$selected")
            }
        
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                Log.d("tracker", "onSelectionChanged")
                when {
                    tracker.hasSelection() && actionMode == null  -> {
                        actionMode = startSupportActionMode(actionModeCallback)
                    }
                
                    !tracker.hasSelection() && actionMode != null -> {
                        actionMode?.finish()
                        actionMode = null
                    }
                
                    else                                          -> {
                    }
                }
            }
        })
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
            R.id.delete  -> true.apply {
                actionMode = startSupportActionMode(actionModeCallback)
            }
            else         -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracker.onSaveInstanceState(outState)
    }
}
