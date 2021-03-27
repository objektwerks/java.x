package aop;

import static org.junit.Assert.assertEquals;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import aop.profiler.Profiler;

public class Main implements Runnable {
    public Main() {
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:aop.context.xml");
        context.registerShutdownHook();
        AopTarget aopTarget = (AopTarget) context.getBean("aopTarget");
        aopTarget.profile("AopTarget.profile(..) invoked...");
        Profiler profiler = (Profiler) context.getBean("profiler");
        System.out.println(profiler);
        assertEquals(1, profiler.getSuccessCount());
        new Thread(new Main()).start();
    }

    public void run() {
        while (true) {            
        }
    }
}