package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

//    @Query(name = "Member.findByUsername")
    //List<Member> findByUsername(@Param("username") String username);

    //Entity 조회
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);


    //값 조회
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    //DTO 조회
    @Query("select new study.datajpa.dto.MemberDTO(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDTO> findMemberDTO();

    //파라미터 in
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);


    //List 반환타입
    List<Member> findListByUsername(String username);

    //Member 반환타입
    Member findMemberByUsername(String username);

    //Optional 반환타입
    Optional<Member> findOptionalByUsername(String username);

    //페이징 안하고 몇개만 받고싶을때는 반환타입을 List로 해주면 된다
    //카운트쿼리가 Join되어있으면 성능이 안나올수 있어서 따로 분리해서 적을수 있다
    @Query(value = "select m from Member m left join m.team",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    //슬라이스 : 전체 카운트를 가져오지 않고 다음 페이지가 있는지로만 판단
    //limit + 1로 조회한다(모바일에서 많이 사용한다 -> ex)개시물 더보기)
    //Slice<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true) // <- 벌크연산후 자동으로 영속성 컨텍스트 초기화
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    //JPQL 사용하지않고 left fetch join
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //JPQL과 EntityGraph같이 사용
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMembers();

    //회원이름으로 조회하는데 team도 필요할때
    @EntityGraph(attributePaths = {"team"})
    List<Member> findByUsername(@Param("username") String username);

    @EntityGraph("Member.all")
    @Query("select m from Member m")
    List<Member> findMembers2();


    //변경 최적화
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    //select for update
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
