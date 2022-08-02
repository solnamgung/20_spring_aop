package com.spring.aop.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.spring.aop.HomeController;

@Component
@Aspect
public class LoggerAdvice {

	/* 
	
		# 스프링 로그파일 설정 방법 
	
		1) pom.xml에서 log4j의존성 추가 (기본값으로 셋팅되어 있음)
		
		2) log4j.xml 설정 파일에서 appender 설정
		
		<!-- DailyFile Appenders -->
		<appender name="dailyFileAppender" class="org.apache.log4j.DailyRollingFileAppender">
			<param name="File" value="C:\\web_log\\logTest.log" />
			<layout class="org.apache.log4j.PatternLayout">
			  <param name="DatePattern"   value="'.'yyyy-MM-dd-HH-mm"/>
			  <param name="ConversionPattern"   value="[%d{HH:mm:ss}][%-5p](%F:%L)-%m%n"/>
			</layout>
		</appender>
		
		
		3) log4j.xml 설정 파일에서 appender 추가
		<!-- Root Logger -->
		<root>
			<priority value="warn" />
			<appender-ref ref="console" />
			<appender-ref ref="dailyFileAppender" />  
		</root>
	
	 */
	
	/* 
	
	 	Logger객체 생성 (private) static Logger logger = LoggerFactory.getLogger(클래스이름.class);
	
	 	- log4j 로깅방식을 통해서 여러개발자가 협업을 하는 경우 로깅방법을 통일할 수 있다.
	 	- log4j 를 이용하여 로그의 레벨별로 체계적인 로그관리가 가능하다.
	 	- 로깅 레벨을 설정하면 그 이상 레벨을 로깅한다.
	 	
	 	1) FATAL : 가장 심각한 에러 발생시 사용
	 	2) ERROR : 일반 에러 발생시 사용
	 	3) WARN  : 에러는 아니지만 주의할 필요가 있을경우 사용
	 	4) INFO  : 일반 정보를 나타낼 경우 사용
	 	5) DEBUG : 일반 정보를 상세히 나타낼 경우 사용
		6) TRACE : 디버그 레벨이 너무 광범위한것을 해결하기 위해서 좀 더 상세한 이벤트를 나타내기 위해 사용
	  
	*/
	
	private static final Logger logger = LoggerFactory.getLogger(LoggerAdvice.class);
	
	@Around("execution(void com.spring.aop.*.*(..))")
	public void around(ProceedingJoinPoint pjp) throws Throwable {
		
		long startTime = System.currentTimeMillis();
		
		pjp.proceed();
		
		long endTime = System.currentTimeMillis();
		
		logger.warn(pjp.getSignature().getName() + " 메서드 동작 시간 " + 	(endTime - startTime) + "초");
		
		//System.out.println(pjp.getSignature().getName() + " 메서드 동작 시간 " + 	(endTime - startTime) + "초");
		
	}
	
	
}
