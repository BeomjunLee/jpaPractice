package jpabook.jpashop.service;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest //스프링 컨테이너 안에서 TEST
@Transactional // 테스트에선 테스트 끝나면 Rollback 한다
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    @Rollback(value = false)
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("이범준");

        //when
        Long memberId = memberService.join(member);

        //then
        Assertions.assertEquals(memberRepository.findOne(memberId), member);
    }
    
    @Test()
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("이범준");

        Member member2 = new Member();
        member2.setName("이범준");
        //when
        memberService.join(member1);

        //then
        IllegalStateException illegalStateException = Assertions.assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        Assertions.assertEquals("이미 존재하는 회원입니다.", illegalStateException.getMessage());

    }
}