package kr.pe.timeorder.expression;

public class RegularExpression {
	//전화번호 정규식, 10~20 자의 숫자
	public final static String phoneNum = "^[0-9]{9,20}$";
	//비밀번호 정규식, 최소 8자, 최소 하나의 문자, 하나의 숫자, 하나의 특수 문자 포함
	public final static String pw = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$";
	//멤버이름 정규식, 2~12 자의 문자와 숫자, _
	public final static String memberName = "^[a-zA-Z가-힣0-9_]{2,12}$";
	//store, item 이름 정규식 2~16자의 문자, 공백, 숫자
	public final static String storeItemName = "^[a-zA-Z가-힣0-9][a-zA-Z가-힣0-9 ]{1,16}$";

}
