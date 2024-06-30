package com.example.save

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.asLiveData
import com.example.save.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = MainDb.getDb(this)
        db.getDao().getAllItem().asLiveData().observe(this){ list->
            binding.tvList.text = ""
            list.forEach {
                val text = "Id: ${it.id} Name: ${it.name} Price: ${it.price}\n"
                binding.tvList.append(text)
            }
        }
        binding.button2.setOnClickListener {
            val item = Item(null,
                binding.edName.text.toString(),
                binding.edPrice.text.toString()
            )
            Thread{
                db.getDao().insertItem(item)
            }.start()
        }
    }
}