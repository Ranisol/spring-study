package com.personalstudy.aboutjpa.entity.item

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("B")
class Book : Item() {
    var artist: String = ""
    var isbn:String = ""
}