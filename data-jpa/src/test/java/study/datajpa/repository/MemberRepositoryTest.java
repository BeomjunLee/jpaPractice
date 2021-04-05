package study.datajpa.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;
import study.datajpa.entity.QMember;
import study.datajpa.entity.QTeam;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static study.datajpa.entity.QMember.member;
import static study.datajpa.entity.QTeam.team;

@SpringBootTest
@Transactional
class MemberRepositoryTest {


    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void basicCRUD() throws Exception {

        //단건 조회
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertEquals(findMember1, member1);
        assertEquals(findMember2, member2);

        findMember1.setUsername("member!!!!!!!!!");

        //리스트 조회
        List<Member> members = memberRepository.findAll();
        assertEquals(members.size(), 2);

        //카운트 검증
        long count = memberRepository.count();
        assertEquals(count, 2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertEquals(deletedCount, 0);

    }

    @Test
    public void findMemberDTO() throws Exception {
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member = new Member("userA", 10, team);
        memberRepository.save(member);

        //when
        List<MemberDTO> memberDTO = memberRepository.findMemberDTO();
        for (MemberDTO dto : memberDTO) {
            System.out.println("dto = " + dto);
        }

        //then
        assertEquals(member.getUsername(), memberDTO.get(0).getUsername());
        assertEquals(member.getId(), memberDTO.get(0).getId());
        assertEquals(member.getTeam().getName(), memberDTO.get(0).getTeamName());
    }

    @Test
    public void paging() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 10, null));
        memberRepository.save(new Member("member3", 10, null));
        memberRepository.save(new Member("member4", 10, null));
        memberRepository.save(new Member("member5", 10, null));

        int age = 10;
        //0페이지에서 3개 가져오기
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when

        //페이징
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        Page<MemberDTO> dto = page.map(member -> new MemberDTO(member.getId(), member.getUsername(), member.getTeam().getName()));
        //then
        List<MemberDTO> content = dto.getContent(); //0번째 페이지 3개 꺼내오기

        assertEquals(content.size(), 3); //한페이지당 개수
        assertEquals(page.getTotalElements(), 5); //total count
        assertEquals(page.getNumber(), 0); //페이지 번호
        assertEquals(page.getTotalPages(), 2); //전체 페이지 개수
        assertEquals(page.isFirst(), true); //이게 첫번째 페이지인가?
        assertEquals(page.hasNext(), true); //다음 페이지가 있는가?
    }

    @Test
    public void bulkUpdate() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 19, null));
        memberRepository.save(new Member("member3", 20, null));
        memberRepository.save(new Member("member4", 21, null));
        memberRepository.save(new Member("member5", 40, null));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);

        List<Member> findMember = memberRepository.findByUsername("member5");
        Member member = findMember.get(0);
        System.out.println(member);

        //then
        assertEquals(resultCount, 3); //update가 해당되는 member가 3명
    }

    @Test
    public void findMemberLazy() throws Exception {
        //given

        //member1 -> teamA
        //member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        Member member3 = new Member("member3", 10, null);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        em.flush();
        em.clear();

        //when
        List<Member> members = memberRepository.findMemberFetchJoin();
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("->member.team = " + member.getTeam());
        }

        //then
    }

    @Test
    public void queryHint() throws Exception {
        //given
        Member member1 = new Member("member1", 10, null);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");
        em.flush(); //변경감지 DB update


        //then
    }

    @Test
    public void lock() throws Exception {
        //given
        Member member1 = new Member("member1", 10, null);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        List<Member> findMember = memberRepository.findLockByUsername("member1");

        //then
    }

    @Test
    public void callCustom() throws Exception {
        //given
        Member member1 = new Member("member1", 10, null);
        Member member2 = new Member("member2", 10, null);
        Member member3 = new Member("member3", 10, null);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<Member> result = memberRepository.findMemberCustom();
        for (Member member : result) {
            System.out.println("member = " + member);
        }
        //when

        //then
    }

    @Test
    public void JpaEventBaseEntity() throws Exception {
        //given
        Member member = new Member("member1", 10, null);
        memberRepository.save(member); //prePersist

        Thread.sleep(100);
        member.setUsername("member2"); //preUpdate

        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findById(member.getId()).get();

        //then
        System.out.println("findMember.created = " + findMember.getCreatedDate());
        System.out.println("findMember.updated = " + findMember.getLastModifiedDate());
        System.out.println("findMember.createdBy = " + findMember.getCreatedBy());
        System.out.println("findMember.updatedBy = " + findMember.getLastModifiedBy());
    }

    @Autowired
    JPAQueryFactory queryFactory;

    @Test
    void fetchJoin() {
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()
                .fetch();
        for (Member m : fetch) {
            System.out.println("m = " + m);
            System.out.println("m.getTeam() = " + m.getTeam());
        }
    }

}