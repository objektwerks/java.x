package aop;

import org.apache.log4j.Logger;

public class AopSchemaTarget {
    private static final Logger logger = Logger.getLogger(AopSchemaTarget.class);

    public String trace(String line) {
        logger.trace(line);
        return line;
    }

    public String profile(String line) {
        logger.trace(line);
        return line;
    }
}