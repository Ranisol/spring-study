package com.personalstudy.aboutjpa.service

import com.personalstudy.aboutjpa.Member
import com.personalstudy.aboutjpa.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true) // 조회하는거에 readOnly를 주면, 최적화시킴. 따라서 기본적으로는 true 옵션을 주고, 데이터 변경이 일어나는 곳에 Transaction 어노테이션을 붙이는 방법도 있음
class MemberService (
        private val memberRepository: MemberRepository
    ) {
    // 회원 가입
    @Transactional
    fun join(member:Member):Long {
        val findMembers = memberRepository.findByName(member.username) // count로 따지는게 최적화, 그리고 멀티쓰레드 상황에서는 동시에 읽어올 수 있음. 이럴때는 검증 로직을 둘다 통과할 가능성 존재. 따라서 애초에 unique 제약조건을 통해 db 쪽에서 끊어주는게 바람직
        if(findMembers.isNotEmpty()) throw IllegalStateException("이미 존재하는 회원입니다.")
        memberRepository.save(member)
        return member.id
    }
    // 회원 전체 조회
    fun findMembers() = memberRepository.findAll()

    fun findMember(id:Long) = memberRepository.findOne(id) ?: throw IllegalStateException("값 없음")
}