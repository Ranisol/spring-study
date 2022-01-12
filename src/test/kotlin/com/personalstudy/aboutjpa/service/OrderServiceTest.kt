package com.personalstudy.aboutjpa.service

import com.personalstudy.aboutjpa.Member
import com.personalstudy.aboutjpa.entity.Address
import com.personalstudy.aboutjpa.entity.OrderStatus
import com.personalstudy.aboutjpa.entity.exception.NotEnoughStockException
import com.personalstudy.aboutjpa.entity.item.Book
import com.personalstudy.aboutjpa.repository.OrderRepository
import com.personalstudy.aboutjpa.repository.OrderSearchOption
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

import javax.persistence.EntityManager

@SpringBootTest
@Transactional
class OrderServiceTest (
    @Autowired private val em:EntityManager,
    @Autowired private val orderService: OrderService,
    @Autowired private val orderRepository: OrderRepository
        ) {
    companion object {
        val member = Member().apply {
            username = "user1"
            address = Address(city = "서울", street = "경기", zipcode = "123")
        }
        val book1 = Book().apply {
            name = "jpa"
            price = 10000
            stockQuantity = 10
        }
        val book2 = Book().apply {
            name = "aa"
            price = 20000
            stockQuantity = 10
        }
    }
    @Test
    fun `상품주문`() {

        // given
        em.persist(member)
        em.persist(book1)
        em.persist(book2)

        val orderReq = arrayListOf(OrderReq(itemId = book1.id, 3), OrderReq(book2.id, 5))

        // when
        val orderId = orderService.order(member.id, orderReq)

        // then
        val order = orderRepository.findOne(orderId)
        Assertions.assertThat(order?.status).isEqualTo(OrderStatus.ORDER) // 주문 상태
        Assertions.assertThat(order?.orderItems?.size).isEqualTo(2) // 아이템 갯수
        Assertions.assertThat(order?.getTotalPrice()).isEqualTo(10000 * 3 + 20000 * 5) // 주문 가격
        Assertions.assertThat(book1.stockQuantity).isEqualTo(7)
        Assertions.assertThat(book2.stockQuantity).isEqualTo(5)
    }
    @Test
    fun `상품 주문 수량 초과`() {
        em.persist(member)
        em.persist(book1)
        val orderReq = arrayListOf(OrderReq(itemId = book1.id, 12))

        // when
        val thrown: NotEnoughStockException =
            org.junit.jupiter.api.Assertions.assertThrows(NotEnoughStockException::class.java)  { orderService.order(member.id, orderReq) }

        // then
        org.junit.jupiter.api.Assertions.assertEquals("not enough stock", thrown.message)

    }

    @Test
    fun `주문 취소시 재고수량에 해당 사항 반영`() {
        em.persist(member)
        em.persist(book1)
        val orderReq = arrayListOf(OrderReq(itemId = book1.id, 9))

        val orderId = orderService.order(member.id, orderReq)

        Assertions.assertThat(book1.stockQuantity).isEqualTo(1)

        orderService.cancelOrder(orderId)
        val order = orderRepository.findOne(orderId)
        Assertions.assertThat(order?.status).isEqualTo(OrderStatus.CANCEL) // 취소로 상태 반영
        Assertions.assertThat(book1.stockQuantity).isEqualTo(10)    // 재고 원복
    }

    @Test
    fun `동적 쿼리`() {
        em.persist(member)
        em.persist(book1)
        em.persist(book2)
        val orderReqOne = arrayListOf(OrderReq(itemId = book1.id, 9))
        val orderReqTwo = arrayListOf(OrderReq(itemId = book2.id, 5))
        val orderIdOne = orderService.order(member.id, orderReqOne)
        val orderIdTwo = orderService.order(member.id, orderReqTwo)
        orderService.cancelOrder(orderIdTwo)
        val optionOne = OrderSearchOption("no-user")
        val optionTwo = OrderSearchOption("user%", OrderStatus.CANCEL)
        val optionThree = OrderSearchOption("user%", OrderStatus.ORDER)
        val searchOrderOne = orderRepository.findAll(optionOne)
        val searchOrderTwo = orderRepository.findAll(optionTwo)
        val searchOrderThree = orderRepository.findAll(optionThree)
        println(searchOrderOne)
        println(searchOrderTwo)
        println(searchOrderThree)
        println("optionThree $optionThree")
        Assertions.assertThat(searchOrderOne.size).isEqualTo(0)
        Assertions.assertThat(searchOrderTwo.size).isEqualTo(1)
        Assertions.assertThat(searchOrderThree.size).isEqualTo(1)
        Assertions.assertThat(searchOrderTwo.all { it?.status == OrderStatus.CANCEL }).isEqualTo(true)
        Assertions.assertThat(searchOrderThree.all { it?.status == OrderStatus.ORDER }).isEqualTo(true)
    }
}