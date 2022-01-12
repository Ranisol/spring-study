package com.personalstudy.aboutjpa

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class MemberRepositoryTest (
   // @Autowired val memberRepository: MemberRepository
        ) {
//    @Test
//    @Transactional // transactional 이 테스트케이스에 있으면, 테스트 끝난 후 롤백함
//    @Rollback(false) // 해당 어노테이션이 있으면, 롤백 막음
//    fun testMember() {
//        var member = Member().apply {
//            username = "memberA"
//        }
//        var savedId = memberRepository.save(member)
//        var findMember = memberRepository.find(savedId)
//
//        Assertions.assertThat(findMember.id).isEqualTo(member.id)
//        Assertions.assertThat(findMember.username).isEqualTo(member.username)
//        Assertions.assertThat(findMember).isEqualTo(member) // == 비교, 객체의 참조값 비교. 같은 영속성 컨텍스트에서는 아이디가 같으면 같은 엔티티로 판별
}
