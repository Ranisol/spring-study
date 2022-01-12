package com.personalstudy.aboutjpa.service

import com.personalstudy.aboutjpa.Member
import com.personalstudy.aboutjpa.repository.MemberRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest // JUnit5에서는 @SpringBootTest에 @RunWith(SpringRunner.class)가 포함
@Transactional
class MemberServiceTest (
    @Autowired private val memberService: MemberService,
    @Autowired private val memberRepository: MemberRepository
        ) {
    @Test
    fun `회원가입`() {
        // given
        val member = Member().apply {
            username = "username"
        }
        // when
        val id:Long = memberService.join(member)
        // then
        Assertions.assertThat(member).isEqualTo(memberRepository.findOne(id))
    }
    @Test
    fun `중복_회원_예외`() {
        // given
        memberService.join(Member().apply { username = "username" })
        // when
        val thrown: IllegalStateException =
            assertThrows(IllegalStateException::class.java) { memberService.join(Member().apply { username = "username" }) }
        // then
        assertEquals("이미 존재하는 회원입니다.", thrown.message)
    }
}