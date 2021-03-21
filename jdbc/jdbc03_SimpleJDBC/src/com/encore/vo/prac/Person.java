package com.encore.vo.prac;
 
/*
 * db 테이블의 Record를 인스턴스화 시킨 클래스
 * DO(Domain object) ---> VO(Value Object) ---> DTO(Domain Transfer Object)
 * 테이블의 컬럼이 클래스의 필드와 연결된다.
 */
public class Person {
	private int ssn;
	private String name;
	private String adress;
	
	public Person() {
		
	}
	public Person(int ssn, String name, String adress) {
		super();
		this.ssn = ssn;
		this.name = name;
		this.adress = adress;
	}
	public int getSsn() {
		return ssn;
	}
	public void setSsn(int ssn) {
		this.ssn = ssn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	@Override
	public String toString() {
		return "Person [ssn=" + ssn + ", name=" + name + ", adress=" + adress + "]";
	}
	
	
	
}
