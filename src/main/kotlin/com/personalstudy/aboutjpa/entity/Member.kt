package com.personalstudy.aboutjpa

import com.personalstudy.aboutjpa.entity.Address
import com.personalstudy.aboutjpa.entity.Order
import javax.persistence.*

@Entity
class Member {
    @Id @GeneratedValue
    var id: Long = 0
    var username: String = ""
    @Embedded
    var address: Address = Address()

    @OneToMany(mappedBy = "member")
    var orders: MutableList<Order> = arrayListOf()


}
