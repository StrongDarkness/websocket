package cn.qxl.mybatisPlugs;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StartUp {
    public static void main(String[] args) throws URISyntaxException {
        try {

            String today = "2019-01-21";

            SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(new Date());

            if(!now.equals(today)){
                System.err.println("――――――未成成功运行――――――");
                System.err.println("――――――未成成功运行――――――");
                System.err.println("本程序具有破坏作用，应该只运行一次，如果必须要再运行，需要修改today变量为今天，如:" + sdf.format(new Date()));
                return;
            }


            List<String> warnings = new ArrayList<String>();
            boolean overwrite = true;
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("generatorConfig.xml");
            ConfigurationParser cp = new ConfigurationParser(warnings);
            Configuration config = cp.parseConfiguration(is);
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);

            System.out.println("生成代码成功，只能执行一次，以后执行会覆盖掉mapper,pojo,xml 等文件上做的修改");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (XMLParserException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}