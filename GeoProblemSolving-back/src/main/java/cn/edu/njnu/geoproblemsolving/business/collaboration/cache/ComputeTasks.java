package cn.edu.njnu.geoproblemsolving.business.collaboration.cache;

import cn.edu.njnu.geoproblemsolving.business.collaboration.entity.ComputeMsg;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author mzy
 * @Date 2021/4
 * @Description 用于构建操作队列，避免冲突
 */
@Service
public class ComputeTasks {
    private static ConcurrentHashMap<String, ArrayList<ComputeMsg>> cacheMap = new ConcurrentHashMap<>();

    /**
     * 获取缓存的对象
     * @param cacheKey
     * @return
     */
    public ArrayList<ComputeMsg> getCache(String cacheKey) {
        return cacheMap.get(cacheKey);
    }

    /**
     * 既调用方法，又更新缓存数据，一般用于更新操作
     * @param records
     * @return
     */
    public void putCache(String key, ArrayList<ComputeMsg> records) {
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
