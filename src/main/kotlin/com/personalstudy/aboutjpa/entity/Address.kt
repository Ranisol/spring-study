package com.personalstudy.aboutjpa.entity

import javax.persistence.Embeddable

@Embeddable
class Address (city:String? = "", street:String? = "", zipcode:String? = "") {
    var city = ""
    var street = ""
    var zipcode = ""
}