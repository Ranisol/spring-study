package com.personalstudy.aboutjpa.entity.item

import com.personalstudy.aboutjpa.entity.Category
import com.personalstudy.aboutjpa.entity.exception.NotEnoughStockException
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
open class Item {
    @Id @GeneratedValue
    var id:Long = 0
    var name:String = ""
    var price:Long = 0
    var stockQuantity:Long = 0
    @ManyToMany(mappedBy = "items")
    var categories:List<Category> = emptyList()

    fun addStock(quantity:Long) {
        this.stockQuantity += quantity
    }
    fun removeStock(quantity: Long) {
        val restStock = this.stockQuantity - quantity
        if(restStock < 0) throw NotEnoughStockException("not enough stock")
        this.stockQuantity = restStock
    }
}