package com.personalstudy.aboutjpa.service

import com.personalstudy.aboutjpa.entity.Delivery
import com.personalstudy.aboutjpa.entity.Order
import com.personalstudy.aboutjpa.entity.OrderItem
import com.personalstudy.aboutjpa.repository.ItemRepository
import com.personalstudy.aboutjpa.repository.MemberRepository
import com.personalstudy.aboutjpa.repository.OrderRepository
import com.personalstudy.aboutjpa.repository.OrderSearchOption
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class OrderReq (
    val itemId:Long,
    val count: Long
        )

@Service
@Transactional(readOnly = true)
class OrderService (
    private val orderRepository: OrderRepository,
    private val memberRepository: MemberRepository,
    private val itemRepository: ItemRepository
        ) {
    // 주문
    @Transactional
    fun order(memberId:Long, orderReqs: List<OrderReq>): Long {
        val member = memberRepository.findOne(memberId) ?: throw ChangeSetPersister.NotFoundException()
        // 주문 샹품 생성
        val orderItems = orderReqs.map { orderReq ->
            val item = itemRepository.findOne(orderReq.itemId)
            OrderItem.createOrderItem(item, item.price, orderReq.count)
        }
        // 배송 정보 생성
        val delivery = Delivery().also {
            it.address = member.address
        }

        // 주문 생성
        val order = Order.createOrder(member, delivery, orderItems)
        orderRepository.save(order) // 하나만 하는 이유? order에 있는 cascade 옵션 때문
        return order.id
    }

    // 취소
    @Transactional
    fun cancelOrder(orderId: Long) {
        val order = orderRepository.findOne(orderId) ?: throw ChangeSetPersister.NotFoundException()
        order.cancel()
    }

    fun findAllByOptions(orderSearchOption: OrderSearchOption) = orderRepository.findAll(orderSearchOption)
}