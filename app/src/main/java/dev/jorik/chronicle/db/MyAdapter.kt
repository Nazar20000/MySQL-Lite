package dev.jorik.chronicle.db

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.urok.db.ListItem
import com.example.urok.db.MyDbManager
import com.example.urok.db.MyIntentConstans
import dev.jorik.chronicle.EditActivity2
import dev.jorik.chronicle.R

class MyAdapter(listMain: ArrayList<ListItem>, contextM: Context): RecyclerView.Adapter<MyAdapter.MyHolder>() {
    var listArray = listMain
    var context = contextM
    class MyHolder(itemView: View, contextV: Context) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        var tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val context = contextV
        fun setData(item: ListItem){

            tvTitle.text = item.title
            tvTime.text = item.time

            itemView.setOnClickListener {

                val intent = Intent(context, EditActivity2::class.java).apply {
                    putExtra(MyIntentConstans.I_TITLE_KEY,item.title)
                    putExtra(MyIntentConstans.I_DECK_KEY,item.deck)
                    putExtra(MyIntentConstans.I_URI_KEY,item.uri)
                    putExtra(MyIntentConstans.I_ID_KEY,item.id)
                    putExtra(MyIntentConstans.I_DATA_KEY, item.data)
                }
                context.startActivity(intent)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.rc_item, parent, false), context)
    }

    override fun getItemCount(): Int {
        return listArray.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listArray.get(position))
    }

    fun updateAdapter(listItems: List<ListItem>){

        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }

    fun removeItem(pos:Int, dbManager: MyDbManager){
        dbManager.removeItemFromDb(listArray[pos].id.toString())
        listArray.removeAt(pos)
        notifyItemRangeChanged(0, listArray.size)
        notifyItemRemoved(pos)
    }
}
