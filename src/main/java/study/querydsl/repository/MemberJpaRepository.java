package study.querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static study.querydsl.entity.QMember.member;

/**
 * 순수 JPA 레포지토리와 Querydsl
 */
@Repository
public class MemberJpaRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

//    public MemberJpaRepository(EntityManager em) {
//        this.em = em;
//        this.queryFactory = new JPAQueryFactory(em); //querydsl
//    }

    //시작 메소드에서 스프링 Bean으로 등록해두기기
    //RequiredArgsConstructor로 대체 가능 (final 필드들로 생성자 만들어줌)
   public MemberJpaRepository(EntityManager em, JPAQueryFactory queryFactory) {
        this.em = em;
        this.queryFactory = queryFactory; //querydsl
    }

    public void save(Member member) {
        em.persist(member);
    }
    public Optional<Member> findById(Long id) {
        Member findMember = em.find(Member.class, id);
        return Optional.ofNullable(findMember);
    }
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
    public List<Member> findByUsername(String username) {
        return em.createQuery("select m from Member m where m.username" +
                "= :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    //    Query dsl로 바꿔보기

    public List<Member> findAll_Querydsl(){
        return queryFactory
                .selectFrom(member) //Qmember.member
                .fetch();
    }

    public List<Member> findByUsername_Querydsl(String username) {
        return queryFactory
                .selectFrom(member)
                .where(member.username.eq(username))
                .fetch();
    }

}
