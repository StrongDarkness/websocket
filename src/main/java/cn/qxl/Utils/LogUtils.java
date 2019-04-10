package cn.qxl.Utils;

import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

/**
 * Created by qiu on 2018/10/30.
 */
public class LogUtils {
    private static Class aClass;
    private static Logger logger= null;
    public static Logger getLogger(Class aClass){
        aClass=aClass;
        logger=Logger.getLogger(aClass.getName());
        return Logger.getLogger(aClass.getName());
    }

    public void Info(String msg){
        logger.info(msg);
    }
}
