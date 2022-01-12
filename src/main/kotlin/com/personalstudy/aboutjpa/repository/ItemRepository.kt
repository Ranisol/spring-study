package com.personalstudy.aboutjpa.repository

import com.personalstudy.aboutjpa.entity.item.Item
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class ItemRepository (
    private val em:EntityManager
        ) {
    fun save(item:Item) {
        if(item.id == null) {
            em.persist(item) // 객체가 없을때 사용
            return;
        }
        em.merge(item) // 일종의 update
    }
    fun findOne(id:Long):Item = em.find(Item::class.java, id)
    fun findAll():List<Item> = em.createQuery("select i from Item i", Item::class.java).resultList
}