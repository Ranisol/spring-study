package com.personalstudy.aboutjpa.entity

import com.personalstudy.aboutjpa.Member
import java.time.LocalDateTime
import javax.persistence.*

enum class OrderStatus {
    ORDER, CANCEL
}

@Entity
@Table(name = "orders")
open class Order {
    protected constructor()
    @Id @GeneratedValue
    @Column(name = "order_id")
    var id:Long = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member:Member = Member()
    set(member) { // 연관관계 편의 메서드
        field = member
        member.orders.add(this)
    }

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL]) // cascade: persist시 같이 적용되는지 여부. persist를 전파하는 역할을 함
    var orderItems: MutableList<OrderItem> = arrayListOf()

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "delivery_id")
    var delivery:Delivery? = null  // oneToOne은 어디에다 외래키를 둬도 되지만, 많이 접근하는 쪽에서 두는게 좋음
    set(delivery) {
        field = delivery
        delivery?.order = this
    }

    var orderDate: LocalDateTime = LocalDateTime.now()

    @Enumerated(EnumType.STRING)
    var status: OrderStatus = OrderStatus.ORDER

    // 연관관계 편의 메서드
    open fun addOrderItems(orderItem: OrderItem) {
        this.orderItems.add(orderItem)
        orderItem.order = this
    }


    // 생성 메서드
    companion object {
        fun createOrder(member:Member, delivery: Delivery?, orderItems : List<OrderItem>):Order {
            return Order().also { order ->
                order.member = member
                order.delivery = delivery
                orderItems.map { order.addOrderItems(it) }
            }
        }
    }
    // 주문 취소
    fun cancel() {
        if(delivery == null) return;
        if(delivery?.status == DeliveryStatus.COMP) throw IllegalStateException("이미 배송 완료됨")
        status = OrderStatus.CANCEL
        orderItems.map { it.cancel() }
    }

    // 조회 로직
    fun getTotalPrice():Long = orderItems.sumOf { it.orderPrice * it.count }
}
