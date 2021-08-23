package cn.edu.njnu.geoproblemsolving.business.collaboration.cache;

import cn.edu.njnu.geoproblemsolving.business.collaboration.entity.ComputeMsg;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author mzy
 * @Date 2021/4
 * @Description 用于构建操作队列，避免冲突
 */
@Service
public class ComputeTasks {
    // private static ConcurrentHashMap<String, ArrayList<ComputeMsg>> cacheMap = new ConcurrentHashMap<>();
    // 计算队列 key: taskId, value: ComputeMsg
    private static  ConcurrentHashMap<String, HashMap<String, ComputeMsg>> cacheMap = new ConcurrentHashMap<>();

    /**
     * 获取缓存的对象
     * @param cacheKey
     * @return
     */
    public HashMap<String, ComputeMsg> getCache(String cacheKey) {
        return cacheMap.get(cacheKey);
    }

    /**
     * 既调用方法，又更新缓存数据，一般用于更新操作
     * 每个 websocket 对应一个线程只有一个cache, groupKey 是在协同工具层面上的东西，有人进入工具则
     * cacheKey 是 groupKey，一个 groupKey 对应的是一个协同工具的
     * @param records
     * @return
     */
    public void putCache(String key, HashMap<String, ComputeMsg> records) {
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
