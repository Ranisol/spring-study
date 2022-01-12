package com.personalstudy.aboutjpa.entity

import com.personalstudy.aboutjpa.entity.item.Item
import javax.persistence.*

@Entity
open class OrderItem {

    protected constructor()

    @Id @GeneratedValue
    var id:Long = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    var item: Item = Item()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    var order:Order? = null

    var orderPrice:Long = 0;
    var count:Long = 0;

    fun cancel() {
        item.addStock(count)
    }


    companion object {
        fun createOrderItem(item:Item, orderPrice:Long, count:Long):OrderItem {
            return OrderItem().also { oi ->
                oi.item = item
                oi.orderPrice = orderPrice
                oi.count = count
                item.removeStock(count)
            }
        }
    }

    fun getTotalPrice() = orderPrice * count
}