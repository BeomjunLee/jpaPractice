package jpabook.jpashop.service;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true) // 조회 최적화(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //회원 가입
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
       Long findMembers = memberRepository.findByNameCount(member.getName());
        if(findMembers > 0){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
        //두 유저가 동시에 가입할 경우를 대비해서 DB에도 유니크 제약조건을 걸어줘야한다
    }

    //회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
    
    //단건 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name, String city, String street, String zipcode) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
        member.setAddress(new Address(city, street, zipcode));
    }
}
