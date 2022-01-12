package com.personalstudy.aboutjpa.repository

import com.personalstudy.aboutjpa.QMember
import com.personalstudy.aboutjpa.entity.Order
import com.personalstudy.aboutjpa.entity.OrderStatus
import com.personalstudy.aboutjpa.entity.QOrder
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class OrderRepository (
    private val em: EntityManager,
    private val queryFactory: JPAQueryFactory
)  : QuerydslRepositorySupport(Order::class.java) {

    val order:QOrder = QOrder.order
    val member: QMember = QMember.member

    fun save(order:Order) {
        em.persist(order)
    }
    fun findOne(id: Long):Order? = em.find(Order::class.java, id)

    // 동적 쿼리
    fun findAll(options: OrderSearchOption):List<Order?> {
        return queryFactory
            .select(order)
            .from(order)
            .join(order.member, member)
            .where(
                isStatusEq(options.orderStatus),
               isNameLike(options.username)
            )
            .limit(1000)
            .fetch()
    }
    private fun isStatusEq(orderStatus: OrderStatus?): BooleanExpression? {
        if(orderStatus == null) return null
        return order.status.eq(orderStatus)
    }
    private fun isNameLike(name:String?):BooleanExpression? {
        if(name == null) return null
        return member.username.like(name)
    }

}


data class OrderSearchOption (
    var username:String? = null,
    var orderStatus:OrderStatus? = null
)