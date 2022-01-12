package com.personalstudy.aboutjpa.entity

import com.personalstudy.aboutjpa.entity.item.Item
import javax.persistence.*

@Entity
class Category {
    @Id @GeneratedValue
    var id:Long = 0
    var name:String = ""

    @ManyToMany
    @JoinTable(
        name = "category_item",
        joinColumns = [JoinColumn(name = "category_id")],
        inverseJoinColumns = [JoinColumn(name = "item_id")]
    )
    var items : MutableList<Item> = mutableListOf()

    @ManyToOne
    @JoinColumn(name = "parent_id")
    var parent:Category? = null
    @OneToMany(mappedBy = "parent")
    var child:MutableList<Category> = arrayListOf()

    fun addChildCategory(child:Category) {
        this.child.add(child)
        child.parent = this
    }
}