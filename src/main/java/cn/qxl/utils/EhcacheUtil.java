package cn.qxl.utils;


import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * ehcache缓存工具类
 * Created by qiu on 2019/1/16.
 */
public class EhcacheUtil {
    /**
     * 默认数据集
     */
    private static final String DEFAULT_CACHE = "userToken";
    /**
     * 默认保存时间
     */
    private static final long DEFAULT_LiVE_TIME = 300;
    /**
     * 缓存管理器
     */
    private CacheManager manager;

    private static EhcacheUtil ehCache;

    private EhcacheUtil() {
        manager = CacheManager.create();
    }

    /**
     * 单例
     *
     * @return
     */
    public static EhcacheUtil getInstance() {
        if (ehCache == null) {
            synchronized (EhcacheUtil.class) {
                if (ehCache == null) {
                    ehCache = new EhcacheUtil();
                }
            }
        }
        return ehCache;
    }

    public CacheManager getManager() {
        return manager;
    }

    /**
     * 保存数据
     *
     * @param cacheName 缓存名
     * @param key
     * @param value
     * @param liveTime  保存时间
     */
    public void put(String cacheName, Object key, Object value, long liveTime) {
//        Cache cache = manager.getCache(cacheName);
        Cache cache = getOrCreateCache(cacheName);
        CacheConfiguration config = cache.getCacheConfiguration();
        config.setTimeToLiveSeconds(liveTime);
        Element element = new Element(key, value);
        cache.put(element);
    }

    /**
     * 保存数据
     *
     * @param cacheName
     * @param key
     * @param value
     */
    public void put(String cacheName, Object key, Object value) {
        put(cacheName, key, value, DEFAULT_LiVE_TIME);
    }

    /**
     * 保存数据
     *
     * @param key
     * @param value
     */
    public void put(Object key, Object value) {
        put(DEFAULT_CACHE, key, value);
    }

    /**
     * 获取保存的数据
     *
     * @param cacheName
     * @param key
     * @return
     */
    public Object get(String cacheName, Object key) {
//        Cache cache = manager.getCache(cacheName);
        Cache cache = getOrCreateCache(cacheName);
        Element element = cache.get(key);
        return element == null ? null : element.getObjectValue();
    }

    /**
     * 获取保存的数据
     *
     * @param key
     * @return
     */
    public Object get(Object key) {
        return get(DEFAULT_CACHE, key);
    }

    /**
     * 获取缓存对象
     *
     * @param cacheName
     * @return
     */
    public Cache getCache(String cacheName) {
//        return manager.getCache(cacheName);
        return getOrCreateCache(cacheName);
    }

    /**
     * 移除保存的数据
     *
     * @param cacheName
     * @param key
     */
    public void remove(String cacheName, Object key) {
//        Cache cache = manager.getCache(cacheName);
        Cache cache = getOrCreateCache(cacheName);
        cache.remove(key);
    }

    /**
     * 移除保存的数据
     *
     * @param key
     */
    public void remove(Object key) {
        remove(DEFAULT_CACHE, key);
    }

    /**
     * 更新保存的数据
     *
     * @param cacheName
     * @param key
     * @param value
     */
    public void update(String cacheName, Object key, Object value) {
        remove(cacheName, key);
        put(cacheName, key, value);
    }

    /**
     * 更新保存的数据
     *
     * @param key
     * @param value
     */
    public void update(Object key, Object value) {
        update(DEFAULT_CACHE, key, value);
    }

    /**
     * 获取或创建缓存
     *
     * @param cacheName
     * @return
     */
    public Cache getOrCreateCache(String cacheName) {
        return getOrCreateCache(cacheName, DEFAULT_LiVE_TIME);
    }

    /**
     * 获取或创建缓存
     *
     * @param cacheName
     * @param liveTime
     * @return
     */
    public Cache getOrCreateCache(String cacheName, long liveTime) {
        Cache cache = manager.getCache(cacheName);
        if (cache == null) {
            synchronized (EhcacheUtil.class) {
                if (cache == null) {
                    manager.addCacheIfAbsent(cacheName);
                    cache = manager.getCache(cacheName);
                    CacheConfiguration config = cache.getCacheConfiguration();
                    config.setTimeToLiveSeconds(liveTime);
                    config.setEternal(false);
                }
            }
        }
        return cache;
    }

    public static void main(String[] args) {
        EhcacheUtil ehcacheUtil = new EhcacheUtil();
        List<String> list = new ArrayList<>();
        list.add("张三");
        list.add("李四");
        list.add("王五");
        list.add("陈麻子");
//        ehcacheUtil.put("commonData","name","张三");
//        ehcacheUtil.put("commonData","name","李四");
//        ehcacheUtil.put("commonData","name","王五");
        ehcacheUtil.put("commonData", "a", list);
        Object a = ehcacheUtil.get("commonData", "a");
        System.out.println(a.getClass().isArray());
        System.out.println(ehcacheUtil.get("commonData", "a"));
    }
}
