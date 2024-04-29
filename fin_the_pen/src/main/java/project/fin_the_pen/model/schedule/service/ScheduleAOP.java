package project.fin_the_pen.model.schedule.service;


import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.repository.CrudScheduleRepository;
import project.fin_the_pen.model.schedule.type.RegularType;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Aspect
public class ScheduleAOP {
    private final CrudScheduleRepository crudScheduleRepository;

    @Before("execution(public * project.fin_the_pen.model.schedule.service.register.*.*(..))")
    public void isDuplicatedRegularSchedule(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();

        String userId = null;
        String eventName = null;
        String category = null;

        for (Object arg : args) {
            if (arg instanceof String) {
                if (arg.equals("userId")) {
                    userId = (String) arg;
                } else if (arg.equals("eventName")) {
                    eventName = (String) arg;
                } else if (arg.equals("category")) {
                    category = (String) arg;
                }

                Optional<Schedule> optionalSchedule =
                        crudScheduleRepository
                                .findByUserIdAndEventNameAndCategoryAndRegularType(userId, eventName, category, RegularType.REGULAR);

                if (optionalSchedule.isPresent()) {
                    throw new RuntimeException();
                }
            }
        }
    }

}
