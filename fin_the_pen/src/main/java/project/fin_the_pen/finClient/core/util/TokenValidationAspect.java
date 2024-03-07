package project.fin_the_pen.finClient.core.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.fin_the_pen.finClient.core.error.customException.TokenNotFoundException;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class TokenValidationAspect {
    private final TokenManager tokenManager;

    @Autowired
    public TokenValidationAspect(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    /**
     * TODO
     *  token을 검증하는 모든 부분의 대상을 빼자.
     * AOP
     * aspect를 사용해서 token을 검사하는 부분에 대해 관점을 분리
     * excution 표현식을 사용
     * 0. @Before 지정된 대상에 대해서 실행 이전에 수행
     * 1. public을 지정하여 해당 경로의 메소드 또는 패키지에 대해서 public 접근지정자만 적용
     * 2. || 를 사용해서 여러 대상을 지정
     * 3. JoinPoint를 사용하여 현재 실행되는 메소드의 정보를 가져옴.
     * getArgs() 메소드를 사용하여 메소드에 전달된 모든 인자를 가져옴.
     * 메소드에 전달된 각 인자를 반복하면서 instanceof 연산자를 사용하여 HttpServletRequest 인스턴스를 찾고, HttpServletRequest 객체를 사용하여 Bearer 토큰을 추출하고, tokenManager를 사용하여 해당 토큰을 파싱.
     * 파싱된 토큰이 null이면, 즉 토큰이 존재하지 않으면 TokenNotFoundException을 throw하여 예외를 발생.
     * 이렇게 함으로써 메소드가 호출되기 전에 토큰의 유효성을 검사하고, 유효하지 않은 경우 예외를 던져서 메소드 호출을 중지할 수 있다.
     *
     * @param joinPoint
     */
    @Before("execution(public * project.fin_the_pen.model.home.service.HomeService.inquiryMonth(..))) || " +
            "execution(public * project.fin_the_pen.model.home.service.HomeService.inquiryWeek(..))) || " +
            "execution(public * project.fin_the_pen.model.home.service.HomeService.inquiryDay(..))) || " +
            "execution(public * project.fin_the_pen.model.assets.service.AssetsService.*.*(..))) || " +
            "excution(public * project.fin_the_pen.model.report.service.ReportService.*.*(..))")
    public void beforeMethodExecution(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                HttpServletRequest request = (HttpServletRequest) arg;
                String accessToken = tokenManager.parseBearerToken(request);

                if (accessToken == null) {
                    throw new TokenNotFoundException("Token not found");
                }
            }
        }
    }
}
