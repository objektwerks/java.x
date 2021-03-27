package aop;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/aop.schema.context.xml"})
public class AopSchemaTest {
    @Autowired private AopSchemaTarget aopSchemaTarget;

    @Test
    public void profile() {
        String line = "*** profiling test...";
        assertEquals(line, aopSchemaTarget.profile(line));
    }

    @Test
    public void trace() {
        String line = "*** tracing test...";
        assertEquals(line, aopSchemaTarget.trace(line));
    }
}