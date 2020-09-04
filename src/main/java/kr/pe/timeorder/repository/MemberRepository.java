package kr.pe.timeorder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.pe.timeorder.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	public Optional<Member> findMemberByPhone(String phone);
	public Optional<Member> findMemberByPhoneAndPw(String phone, String pw);
}
