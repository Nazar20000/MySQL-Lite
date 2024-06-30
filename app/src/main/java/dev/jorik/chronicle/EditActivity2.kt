package dev.jorik.chronicle

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.urok.db.MyDbManager
import com.example.urok.db.MyIntentConstans
import dev.jorik.chronicle.databinding.EditActivityBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditActivity2 : AppCompatActivity() {
    var id = 0
    var isEditState = false
    val myDbManager = MyDbManager(this)
    val imageRequestCode = 10
    var tempImageUri = "Empty"
    lateinit var binding: EditActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = EditActivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getMyIntents()

        binding.bitn5.setOnClickListener {
            binding.editTextTitle.isEnabled = true
            binding.editTextText2.isEnabled = true
            binding.editTextData.isEnabled = true
            binding.bitn5.visibility = View.GONE
            binding.bitnImage.visibility = View.VISIBLE
            prov()
        }

        binding.bitnRedactor.setOnClickListener {
            binding.imageAvatar.visibility = View.VISIBLE
            image()
        }

        binding.bitnImage.setOnClickListener {
            binding.bitnImage.visibility = View.INVISIBLE
            binding.ImageLayout.visibility = View.VISIBLE
            binding.imageAvatar.setImageResource(R.drawable.baseline_image_24)
        }

        binding.bitnDelete.setOnClickListener {
            binding.ImageLayout.visibility = View.GONE
            binding.bitnImage.visibility = View.VISIBLE
            tempImageUri = "Empty"
        }

        binding.bitnSave.setOnClickListener {
            val myTitle = binding.editTextTitle.text.toString()
            val myDeck = binding.editTextText2.text.toString()
            val myData = binding.editTextData.text.toString()
            if (myTitle != "") {

                if (isEditState) {
                    myDbManager.updateItem(myTitle, myDeck, tempImageUri, id, getCurrentTime(), myData)
                } else {
                    myDbManager.insertToDb(myTitle, myDeck, tempImageUri, getCurrentTime(), myData)
                }
            }
            finish()
        }
    }

    fun prov() {
        if (tempImageUri == "Empty")return
        binding.bitnDelete.visibility = View.VISIBLE
        binding.bitnRedactor.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == imageRequestCode){
            binding.imageAvatar.setImageURI(data?.data)
            tempImageUri = data?.data.toString()
            contentResolver.takePersistableUriPermission(data?.data!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    fun image(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, imageRequestCode)
    }

    fun getMyIntents() {
        val i = intent
        if (i != null) {

            if (i.getStringExtra(MyIntentConstans.I_TITLE_KEY) != null) {

                binding.bitnImage.visibility = View.GONE
                binding.editTextTitle.setText(i.getStringExtra(MyIntentConstans.I_TITLE_KEY))
                isEditState = true
                binding.editTextTitle.isEnabled = false
                binding.editTextText2.isEnabled = false
                binding.editTextData.isEnabled = false
                binding.ImageLayout.visibility = View.GONE
                binding.editTextText2.setText(i.getStringExtra(MyIntentConstans.I_DECK_KEY))
                id = i.getIntExtra(MyIntentConstans.I_ID_KEY, 0)
                binding.bitn5.visibility = View.VISIBLE

                if (i.getStringExtra(MyIntentConstans.I_URI_KEY) != "Empty") {
                    binding.ImageLayout.visibility = View.VISIBLE
                    tempImageUri = i.getStringExtra(MyIntentConstans.I_URI_KEY)!!
                    binding.imageAvatar.setImageURI(Uri.parse(tempImageUri))
                    binding.bitnRedactor.visibility = View.GONE
                    binding.bitnDelete.visibility = View.GONE


                }
            }
        }
    }

    private var previousDate: String? = null

    private fun getCurrentTime(): String {
        val time = Calendar.getInstance().time
        val currentDateString = SimpleDateFormat("dd-MMMM", Locale.getDefault()).format(time)

        if (currentDateString != previousDate) {
            previousDate = currentDateString
            return currentDateString
        }

        return ""
    }
}
