package com.spring.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/*

	# AOP( Aspect-Oriented Programming ) 관점 지향 프로그래밍
	
	- 프로젝트 개발 과정에서 핵심 기능 외에 추가적이고, 다양한 부가(공통) 기능이 필요하다. (로깅,보안,트랜젝션,테스트)
	
	- 이 부가(공통)기능들은 프로젝트에 굉장히 중요한 역할을 하며 이 부가(공통)기능이 코드마다 반복적(중복)으로 나타나는 부분이 존재한다. 
	
	- 코드에서 비즈니스 핵심 로직과 부가기능을 분리하여 부가 로직을 따로 관리(모듈화)한다.
	
	- 종단(비즈니스 로직) 기능 , 횡단(관심,Aspect) 기능 
	
	- 부가 기능이 비즈니스 로직(핵심 기능)을 담은 클래스의 코드에 전혀 영향을 주지 않으면서 부가기능의 구현을 용이하게 할 수 있는 구조를 제공한다.
	
	- AOP는 OOP를 대체하는 새로운 개념이 아니라 OOP를 돕는 보조적 기술 중에 하나 이다.
	
	- 스프링 DI  : 의존성(new)주입 , 스프링 AOP  : 로직(code) 주입
	
	
	[ 용어 정리 ]
	
		1) Aspect	   : 관점
		2) Advice	   : 핵심기능에 부여되는 부가기능 ( 타겟 메서드에 적용될 부가 기능 )
		
			2-1) Around (Advice)		 : 대상 객체의 메서드 실행 전,후 및 예외 발생 모두 실행한다.
			2-2) Before (Advice)	     : 대상 객체의 메서드 메서드 호출전에 수행한다.
			2-3) After (Advice)			 : 대상 객체의 메서드 실행도중 예외 발생 여부와 상관없이 메서드 실행 후 실행한다.
			2-4) AfterReturning (Advice) : 대상 객체의 메서드가 실행 도중 예외없이 실행 성공한 경우에 실행한다.
			2-5) AfterThrowing (Advice)  : 대상 객체의 메서드가 실행 도중 예외가 발생한 경우에 실행한다.
		
		3) Pointcut   : Aspect 적용 위치 지정자      ( Advice를 어디에 적용할지를 결정  )
		4) Advisor    : Advice + Pointcut
		5) Joinpoint  : Aspect가 적용한 지점
	
	
	
	[ 구현 실습 예시 ]
	
	1) pom.xml 파일에 AOP 관련 dependency 추가
	
		<!-- AspectJ -->
		<dependency>
			<groupId>org.aspectj</groupId>
			<artifactId>aspectjrt</artifactId>
			<version>${org.aspectj-version}</version>
		</dependency>	
		
		<dependency>
		   <groupId>org.aspectj</groupId>
		   <artifactId>aspectjweaver</artifactId>
		   <version>1.6.11</version>
		 </dependency>
	
	    <dependency>
		  <groupId>org.springframework</groupId>
		  <artifactId>spring-aop</artifactId>
		  <version>${org.springframework-version}</version>
	    </dependency>
	
	2) AOP설정 xml 파일에 aop autoproxy 지시
	
		xmlns:aop="http://www.springframework.org/schema/aop"							
		http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.1.xsd
	
		<aop:aspectj-autoproxy />
	
	
	3) Advice 클래스에 @Aspect 지정
		
		@Component
		@Aspect
	
	4) Advice 적용

*/
@Component
@Aspect		// Aspect사용 어노테이션
public class Advice {

	/*
	 *  # execution 명시자 
	 *  
	 *  - execution(수식어패턴 리턴타입패턴 클래스이름패턴?메서드이름패턴(파라미터패턴)) 
	 *  - 각 패턴은 *을 이용하여 모든값을 표현할 수 있다.
	 *  
	 *  
	 *  [패키지]
	 *  com.spring.aop	  > com.spring.aop패키지를 타겟
	 *  com.spring.aop..  > com.spring.aop로 시작하는 하위의 모든 패키지를 타겟
	 *  
	 *  
	 *  [리턴타입]
	 *  *		> 모든 리턴 타입 타겟
	 *  void	> 리턴 타입이 void인 메서드만 타겟
	 *  !void	> 리턴 타입이 void가 아닌 메서드만 타겟 
	 *  
	 *  
	 *  [매개 변수 지정]
	 *  (..)		>  0개 이상의 모든 파라미터 타겟
	 *  (*)			> 1개의 파라미터만 타겟
	 *  (*,*)		> 2개의 파라미터만 타겟
	 *  (String,*)	> 2개의 파라미터중 첫번째 파라미터가 String타입만 타겟
	 *  
	 *  
	 *  [샘플 예시]
	 *  
	 *  execution(* set*(..)) 							>> 리턴 타입이 void이고 메서드 이름이 set으로 시작하고 파라미터가 0개 이상인 메서드 타겟
	 *  execution(* abc.*.*()) 								>> 모든 리턴타입 매칭 ,  abc패키지에 속한 파라미터가 없는 모든 메서드 타겟
	 *  execution(* abc..*.*(..)) 							>> 모든 리턴타입 매칭 , abc패키지 및 하위 패키지에 있는 파라미터가 0개 이상인 메서드 타겟
	 *  execution(Long com.spring.aop.Boss.work(..))   		>> 리턴 타입인 Long인 com.spring.aop 패키지 안의 Boss클래스의 work 메서드 타겟
	 *  execution (* get*(*)) 								>> 모든 리턴타입 매칭 , 이름이 get으로 시작하고 파라미터가 한 개인 메서드 타겟
	 *  execution(* get*(*,*)) 								>> 모든 리턴타입 매칭 , 이름이 get으로 시작하고 파라미터가 2개인 메서드 타겟
	 *  execution(* read*(int,..)) 							>> 메서드 이름이 read 로시작하고, 첫번째 파라미터 타입이 int이며 한개 이상의 파라미터를 갖는 메서드 타겟
	 *  
	 * */
	
	// 메서드 호출 전
	@Before("execution(* work())")
	public void before() {
		System.out.println("출근한다.");
	}
	
	// 메서드 호출 후
	@After("execution(void com.spring.aop.*.work())") 
	public void after() {
		System.out.println("퇴근한다.");
	}
	
	// 메서드 호출 전 후
	@Around("execution(* com.spring.aop.*.getWorkingTime())")
	public void around(ProceedingJoinPoint pjp) throws Throwable {
		
		// 메서드 호출 전
		System.out.println("=================");
		long startTime = System.currentTimeMillis();
		
		// ProceedingJoinPoint의  proceed() 메서를 사용하여 타겟팅 메서드를 실행한다.
		pjp.proceed();
		
		// 메서드 호출 후
		long endTime = System.currentTimeMillis();
		System.out.println("메서드 동작 시간 : " + (endTime - startTime) + "초");
		
		
	}
	
	@After("execution(* getInfo(..))")
	public void getInfo(JoinPoint jp) { // JoinPoint를 통해서 타겟 메서드의 파라메타를 전달 받을 수 있다.
		
		System.out.println("1 : " + Arrays.toString(jp.getArgs())); // 메서드의 파라메타를 확인
		System.out.println("2 : " + jp.getKind());					// 메서드의 종류를 확인
		System.out.println("3 : " + jp.getSignature().getName());	// 메서드의 이름을 확인
		System.out.println("4 : " + jp.getTarget());				// 타겟 객체를 확인
		System.out.println();
		
	}
	
	
	
	
	
	
	
}
