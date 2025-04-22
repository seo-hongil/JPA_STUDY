package hellojpa;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        //1. JPA 기초/기본 실행
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello"); // 하나만 생성 후 애플리케이션 전체에서 공유
        EntityManager em = emf.createEntityManager(); //쓰레드간의 공유는 X 사용하고 버려지는, 실질적으로 동작하는 매니저
        EntityTransaction tx = em.getTransaction(); // JPA의 모든 데이터 변경은 트랜잭션 안에서 실행된다.
        tx.begin();

        try{
            // insert
            Member member = new Member();
            member.setId(1L);
            member.setName("HelloA");
            em.persist(member);

            // select1
            Member findMember = em.find(Member.class, 1L);
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.name = " + findMember.getName());

            // select2
            /*구체적인 조건하에 찾는 조건은 JPQL로 진행*/
            List<Member> findMembers = em.createQuery("select m from Member", Member.class)
                    .setFirstResult(5) // 페이징 limit (mysql)
                    .setMaxResults(10) // 페이징 offset (mysql)
                    .getResultList();

            // update
            findMember.setName("HelloJPA");
            /*트랜잭션 커밋 시 업데이트 쿼리를 만들어서 실행 후 커밋*/

            // remove
            em.remove(findMember);

            tx.commit(); // 커밋을 해야 반영
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close(); // 자원 사용 후 반드시 닫아줄 것
        }

        emf.close(); // 앱 닫을 때 WAS가 내려갈 때 닫아줘 소스나 다른게 릴리즈 된다.
    }
}

// JPA를 사용하면 엔티티 객체를 중심으로 개발

// JPQL
// 검색 쿼리는 JPQL로 해결
// 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색
// 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능
// 애플리케이션이 필요한 데이터만 DB에서 불러오려면 결국 검색 조건이 포함된 SQL이 필요
// JPA는 SQL을 추상화한 JPQL이라는 객체지향쿼리어 제공
// SQL과 문법 유사 (selet, from, where, group by, having, join 지원)
// JPQL은 엔티티 객체를 대상으로 쿼리
// SQL은 데이터베이스 테이블을 대상으로 쿼리
// 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리
// SQL을 추상화해서 특정 데이터베이스 SQL에 의존 X
// JPQL을 한마디로 정의하면 객체 지향 SQL
// JPQL은 뒤에서 아주 자세히 다룰 예정정