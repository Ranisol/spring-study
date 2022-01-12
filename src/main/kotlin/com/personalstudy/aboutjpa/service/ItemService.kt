package com.personalstudy.aboutjpa.service

import com.personalstudy.aboutjpa.entity.item.Item
import com.personalstudy.aboutjpa.repository.ItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ItemService (
    private val itemRepository: ItemRepository
        ) {
    @Transactional
    fun saveItem(item: Item) = itemRepository.save(item)
    fun getItems() = itemRepository.findAll()
    fun getOne(itemId: Long) = itemRepository.findOne(itemId)
}