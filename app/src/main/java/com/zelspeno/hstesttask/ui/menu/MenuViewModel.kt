package com.zelspeno.hstesttask.ui.menu


import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zelspeno.hstesttask.R
import com.zelspeno.hstesttask.room.menu.DishCache
import com.zelspeno.hstesttask.room.menu.MenuCacheDBEntity
import com.zelspeno.hstesttask.room.menu.MenuCacheRepository
import com.zelspeno.hstesttask.source.Dish
import com.zelspeno.hstesttask.source.DishesJSONObject
import com.zelspeno.hstesttask.source.ProductRepository
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val cacheRepository: MenuCacheRepository
) : ViewModel() {

    private val _dishesList = MutableSharedFlow<List<Dish>?>()
    val dishesList = _dishesList.asSharedFlow()

    private val _bonusList = MutableSharedFlow<List<BonusCard>?>()
    val bonusList = _bonusList.asSharedFlow()

    /** Emit values to [dishesList] */
    fun getDishesList() {

        val response: Call<DishesJSONObject> = repository.getDishesJson()

        response.enqueue(object: Callback<DishesJSONObject> {
            override fun onResponse(call: Call<DishesJSONObject>, response: Response<DishesJSONObject>) {
                viewModelScope.launch {
                    val res = response.body()!!.dishes
                    cacheRepository.updateDishesList(setDishesToCache(res))
                    _dishesList.emit(res)
                }
            }

            override fun onFailure(call: Call<DishesJSONObject>, t: Throwable) {
                viewModelScope.launch {
                    cacheRepository.getDishesList().collect {
                        if (it.isNotEmpty()) {
                            _dishesList.emit(getDishesFromCache(it))
                        } else _dishesList.emit(null)

                    }
                }
            }
        })
    }

    /** Check [dishes] on null-objects */
    private fun checkDishesOnErrors(dishes: List<Dish>): List<Dish> {
        val res = dishes.toMutableList()
        for (i in dishes.indices) {
            if (dishes[i].id == null || dishes[i].name == null || dishes[i].price == null ||
                dishes[i].tags == null) {
                res.removeAt(i)
            }
        }
        return res
    }

    /** Get [List] of unique [DishUI.tags] */
    fun getTagsList(dishes: List<DishUI>): List<Tag> {
        val res = mutableListOf<Tag>()
        for (i in dishes) {
            val tag = i.tags
            for (j in tag!!) {
                if (j !in res.map { it.name }) {
                    if (j == "Все меню") {
                        res.add(Tag(j, true))
                    } else {
                        res.add(Tag(j, false))
                    }
                }
            }
        }
        return res
    }

    /** Emit values to [bonusList] */
    fun getBonusList() {
        val bonuses = repository.getBonusesList()
        viewModelScope.launch {
            _bonusList.emit(bonuses)
        }
    }

    /** Get List<[Dish]> by [tag] */
    private fun getDishesByTag(dishes: List<DishUI>, tag: Tag): List<DishUI> {
        val res = mutableListOf<DishUI>()
        for (i in dishes) {
            for (j in i.tags!!) {
                if (tag.name == j) {
                    if (i !in res) {
                        res.add(i)
                    }
                }
            }
        }
        return res
    }

    /** Make double-type object to string,
     * if number after the decimal > 0 - return it in '0.0' format
     * else return it in '0' - format w/o dot */
    private fun Double.toUIText(): String {
        return if (this.toInt() - this > 0.0) {
            this.toString()
        } else this.toInt().toString()
    }

    /** Get [list] and convert it to List<[DishUI]> to send to RV */
    fun getDishUI(list: List<Dish>): List<DishUI> {
        val nList = checkDishesOnErrors(list)
        val res = mutableListOf<DishUI>()
        for (i in nList) {
            var ingredients: String? = getIngredientsToString(i.ingredients)
            res.add(
                DishUI(
                    id = i.id,
                    name = i.name,
                    price = i.price?.toUIText(),
                    ingredients = ingredients,
                    imageUrl = i.imageUrl,
                    tags = i.tags
                )
            )
        }
        return res
    }

    /** Get [list] from DB and convert it to List<[Dish]> to app-use */
    fun getDishesFromCache(list: List<DishCache>): List<Dish> {
        val res = mutableListOf<Dish>()
        for (i in list) {
            res.add(
                Dish(
                    id = i.id,
                    name = i.name,
                    price = i.price,
                    weight = i.weight,
                    ingredients = i.ingredients?.split(","),
                    imageUrl = i.imageUrl,
                    tags = i.tags?.split(",")
                )
            )
        }
        return res
    }

    /** Get [list] from app-use and convert it to DB format-list */
    fun setDishesToCache(list: List<Dish>): List<MenuCacheDBEntity> {
        val res = mutableListOf<MenuCacheDBEntity>()
        for (i in list) {
            res.add(
                MenuCacheDBEntity(
                    id = i.id,
                    name = i.name,
                    price = i.price,
                    weight = i.weight,
                    ingredients = getIngredientsToString(i.ingredients),
                    imageUrl = i.imageUrl,
                    tags = getTagsToString(i.tags)
                )
            )
        }
        return res
    }

    /** Convert List of ingredients to String? and return it */
    private fun getIngredientsToString(ingredientsList: List<String>?): String? {
        var ingredients: String? = ""
        if (ingredientsList != null) {
            for (j in ingredientsList.indices) {
                if (ingredientsList[j] != "null") {
                    if (j != ingredientsList.lastIndex) {
                        ingredients += "${ingredientsList[j]}, "
                    } else ingredients += "${ingredientsList[j]}."
                } else ingredients = null
            }
        }
        return ingredients
    }

    /** Convert List of tags to String? and return it */
    private fun getTagsToString(tagsList: List<String>?): String? {
        var tags: String? = ""
        if (tagsList != null) {
            for (j in tagsList.indices) {
                if (tagsList[j] != "null") {
                    if (j != tagsList.lastIndex) {
                        tags += "${tagsList[j]},"
                    } else tags += tagsList[j]
                }
            }
        } else tags = null
        return tags
    }


    /** On [tag] clicked logic */
    fun onTagClick(
        tag: Tag,
        allDishesList: List<DishUI>,
        adapterTags: TagsListRecyclerAdapter,
        adapterDishes: DishesListRecyclerAdapter,
    ) {
        val list = adapterTags.getList()
        val newList = mutableListOf<Tag>()
        for (i in list) {
            if (i !in newList) {
                if (i == tag) {
                    newList.add(Tag(i.name, true))
                } else {
                    newList.add(Tag(i.name, false))
                }
            }
        }
        adapterTags.updateList(newList)
        val dishes = getDishesByTag(allDishesList, tag)
        adapterDishes.updateList(dishes)
    }

}