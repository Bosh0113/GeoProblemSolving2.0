package cn.edu.njnu.geoproblemsolving.business.collaboration.cache;

import cn.edu.njnu.geoproblemsolving.business.collaboration.entity.ChatMsg;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class CommunicationCache {

    private static ConcurrentHashMap<String, ArrayList<ChatMsg>> cacheMap = new ConcurrentHashMap<>();

    /**
     * 获取缓存的对象
     * @param cacheKey
     * @return
     */
    public ArrayList<ChatMsg> getCache(String cacheKey) {
        return cacheMap.get(cacheKey);
    }

    /**
     * 既调用方法，又更新缓存数据，一般用于更新操作
     * @param records
     * @return
     */
    public void putCache(String key, ArrayList<ChatMsg> records) {
        cacheMap.put(key, records);
    }

    /**
     * 移除缓存信息
     * @param cacheKey
     */
    public void removeCache(String cacheKey) {
        cacheMap.remove(cacheKey);
    }
}
