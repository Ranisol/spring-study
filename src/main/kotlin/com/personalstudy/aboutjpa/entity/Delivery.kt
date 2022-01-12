package com.personalstudy.aboutjpa.entity

import javax.persistence.*

enum class DeliveryStatus {
    READY, COMP
}

@Entity
class Delivery {
    @Id @GeneratedValue
    var id:Long = 0
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    var order:Order? = null
    @Embedded
    var address: Address = Address()
    @Enumerated(EnumType.STRING) // EnumType.ORDINARY 쓰면 x, 숫자로 들어가는데, 순서가 바뀌면 망함
    var status:DeliveryStatus = DeliveryStatus.READY
}