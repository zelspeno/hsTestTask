package com.zelspeno.hstesttask.ui.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.zelspeno.hstesttask.R

class BonusListRecyclerAdapter(private var bonusList: List<BonusCard>):
    RecyclerView.Adapter<BonusListRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val image: ImageView = itemView.findViewById(R.id.bonusRVImage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.bonus_recyclerview_item, parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.image.setImageResource(bonusList[position].image)
    }

    override fun getItemCount(): Int = bonusList.size

    fun updateList(list: List<BonusCard>) {
        this.bonusList = list
        notifyDataSetChanged()
    }

    fun getList(): List<BonusCard> = this.bonusList

}