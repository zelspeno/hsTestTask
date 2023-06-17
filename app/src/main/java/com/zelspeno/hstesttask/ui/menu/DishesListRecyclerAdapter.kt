package com.zelspeno.hstesttask.ui.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zelspeno.hstesttask.R

class DishesListRecyclerAdapter(private var dishes: List<DishUI>):
    RecyclerView.Adapter<DishesListRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image: ImageView = itemView.findViewById(R.id.dishRVImage)
        val name: TextView = itemView.findViewById(R.id.dishRVName)
        val ingredients: TextView = itemView.findViewById(R.id.dishRVIngredients)
        val cost: Button = itemView.findViewById(R.id.dishRVCostButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.dishes_recyclerview_item, parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (dishes[position].imageUrl == "null" || dishes[position].imageUrl == null) {
            holder.image.setImageResource(R.drawable.empty_image)
        } else {
            Picasso.get().load(dishes[position].imageUrl).into(holder.image)
        }
        holder.name.text = dishes[position].name
        holder.cost.text = "от ${dishes[position].price} р"
        holder.ingredients.text = dishes[position].ingredients ?: ""
    }

    override fun getItemCount(): Int = dishes.size

    fun updateList(list: List<DishUI>) {
        this.dishes = list
        notifyDataSetChanged()
    }

    fun getList(): List<DishUI> = this.dishes

}