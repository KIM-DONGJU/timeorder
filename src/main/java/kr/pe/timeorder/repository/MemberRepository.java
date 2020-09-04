package kr.pe.timeorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.pe.timeorder.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	public Member findMemberByPhone(String phone);
	public Member findMemberByPhoneAndPw(String phone, String pw);
}
