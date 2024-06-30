package dev.jorik.chronicle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.jorik.chronicle.db.MyAdapter
import com.example.urok.db.MyDbManager
import dev.jorik.chronicle.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var b = 1
    val myDbManager = MyDbManager(this)
    val myAdapter = MyAdapter(ArrayList(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()

        binding.bitn.setOnClickListener {
            perehod()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter()
    }

    fun perehod(){
        val intent = Intent(this, EditActivity2::class.java)
        startActivity(intent)
    }
    fun init(){
        binding.rcView.layoutManager = LinearLayoutManager(this)
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(binding.rcView)
        binding.rcView.adapter = myAdapter
    }

    fun fillAdapter(){
        val list = myDbManager.readDbData()
        myAdapter.updateAdapter(list)
        if (list.size > 0){
            binding.tvText.visibility = View.GONE
        }
        else{
            binding.tvText.visibility = View.VISIBLE
        }
    }

    private fun getSwapMg() : ItemTouchHelper{
        return ItemTouchHelper(object : ItemTouchHelper.
        SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.removeItem(viewHolder.adapterPosition, myDbManager)
            }
        })
    }
}