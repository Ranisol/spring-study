package com.personalstudy.aboutjpa.repository

import com.personalstudy.aboutjpa.Member
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class MemberRepository (
       private val em: EntityManager // 스프링이 주입해줌
        ) {

    fun save(member: Member) = em.persist(member) // persist로 영속성 컨텍스트에 넣을때 id 값도 채워줌
    fun findOne(id:Long):Member? = em.find(Member::class.java, id)
    fun findAll():List<Member?> = em.createQuery("select m from Member m", Member::class.java).resultList // jpql 은 엔티티를 대상으로 쿼리를 날린다고 생각
    fun findByName(username:String): List<Member?> = em
        .createQuery("select m from Member m where m.username = :username")
        .setParameter("username", username)
        .resultList as List<Member?>


}