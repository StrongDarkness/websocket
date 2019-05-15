package cn.qxl.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import cn.qxl.interceptor.ResourceInterceptor;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    public final static String MARKETING = "/marketing/";
    public final static String MARKETINGDIR = "file:F:/marketing/";


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 如果我们将/xxxx/** 修改为 /** 与默认的相同时，则会覆盖系统的配置，可以多次使用 addResourceLocations 添加目录，
         * 优先级先添加的高于后添加的。
         *
         * 如果是/xxxx/** 引用静态资源 加不加/xxxx/ 均可，因为系统默认配置（/**）也会作用
         * 如果是/** 会覆盖默认配置，应用addResourceLocations添加所有会用到的静态资源地址，系统默认不会再起作用
         */
        registry.addResourceHandler("/**").addResourceLocations("classpath:/templates/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/");

        registry.addResourceHandler("/upload/images/**").addResourceLocations("file:D:\\upload\\images\\");

        registry.addResourceHandler(MARKETING + "**").addResourceLocations(MARKETINGDIR);

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ResourceInterceptor()).excludePathPatterns("/static/**");
    }


    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("classpath:/templates/");
//        resolver.setPrefix("/WEB-INF/");//web-inf配置jsp
        resolver.setSuffix(".html");
//        resolver.setSuffix(".jsp");
        resolver.setViewNames("/*");
//        resolver.setViewNames("/jsp/*");
        resolver.setOrder(2);
        return resolver;
    }

    @Bean
    public ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setPrefix("classpath:/templates/");//注意，要加classpath，否则无法读取html
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("utf-8");
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        Set<IDialect> additionalDialects = new HashSet<IDialect>();
        additionalDialects.add(new ShiroDialect());//设置shiro-thymeleaf模板
        templateEngine.setAdditionalDialects(additionalDialects);
        return templateEngine;
    }


    @Bean
    public ThymeleafViewResolver viewResolverThymeLeaf() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("utf-8");
        viewResolver.setViewNames(new String[]{"html/*"});
        viewResolver.setOrder(1);
        viewResolver.setCache(false);
        return viewResolver;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(
                Charset.forName("UTF-8"));//设置返回的中文编码
        return converter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        configureMessageConverters(converters);

        converters.add(responseBodyConverter());//在前
        converters.add(fastJsonConverter());
    }

    @Bean
    public FastJsonHttpMessageConverter fastJsonConverter() {
        //创建fastJson消息转换器
        FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
        //创建配置类
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        //过滤并修改配置返回内容
        fastJsonConfig.setSerializerFeatures(
                //List字段如果为null,输出为[],而非null
                SerializerFeature.WriteNullListAsEmpty,
                //字符类型字段如果为null,输出为"",而非null
                SerializerFeature.WriteNullStringAsEmpty,
                //Boolean字段如果为null,输出为falseJ,而非null
                //SerializerFeature.WriteNullBooleanAsFalse,
                //消除对同一对象循环引用的问题，默认为false（如果不配置有可能会进入死循环）
                SerializerFeature.DisableCircularReferenceDetect,
                //是否输出值为空map的字段,默认为false。
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.DisableCircularReferenceDetect,
                //输出日期为格式化
                SerializerFeature.WriteDateUseDateFormat

        );

        //处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<MediaType>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastMediaTypes.add(MediaType.parseMediaType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"));
        fastJsonConverter.setSupportedMediaTypes(fastMediaTypes);
        fastJsonConverter.setFastJsonConfig(fastJsonConfig);
        return fastJsonConverter;
    }

    @Override
    public void configureContentNegotiation(
            ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }
}


