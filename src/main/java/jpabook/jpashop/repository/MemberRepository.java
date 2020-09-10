package jpabook.jpashop.repository;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    //저장
    public void save(Member member) {
        em.persist(member);
    }
    
    //찾기
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    //모두 찾기
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    //이름으로 찾기
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    //이름 중복인 회원 찾기
    public Long findByNameCount(String name){
        return em.createQuery("select count(m) from Member  m where m.name = :name", Long.class)
                .setParameter("name", name)
                .getSingleResult();
    }
}
