package com.poly.service.hotImg;

import com.poly.bean.HotImg;
import com.poly.dao.HotImgDao;
import com.poly.redis.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xiyuan_fengyu on 2017/1/6.
 */
@Service
public class HotImgService {

    @Autowired
    private HotImgDao hotImgDao;

    private static final String kIds = "tb_hot_img:ids";

    private static final String kIdNext = "tb_hot_img:id:next";

    private static final String kInsertIds = "tb_hot_img:insert:ids";

    private static final String kUpdateIds = "tb_hot_img:update:ids";

    private static String kId(String id) {
        return "tb_hot_img:id:" + id;
    }

    public void redisToMysql() {
        Jedis jedis = RedisManager.getJedisObject();
        try {
            Set<String> insertIds = jedis.zrevrange(kInsertIds, 0, -1);
            for (String insertId : insertIds) {
                HotImg hotImg = new HotImg(jedis.hgetAll(kId(insertId)));
                hotImgDao.add(hotImg);
                jedis.zrem(kInsertIds, insertId);
            }

            Set<String> updateIds = jedis.zrevrange(kUpdateIds, 0, -1);
            for (String updateId : updateIds) {
                HotImg hotImg = new HotImg(jedis.hgetAll(kId(updateId)));
                hotImgDao.update(hotImg);
                jedis.zrem(kUpdateIds, updateId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisManager.recycleJedisOjbect(jedis);
        }
    }

    public void mysqlToRedis() {
        Jedis jedis = RedisManager.getJedisObject();
        try {
            List<HotImg> hotImgs = hotImgDao.all();
            long nextId = 0;
            for (HotImg hotImg : hotImgs) {
                nextId = Math.max(nextId, hotImg.getId());
                jedis.zadd(kIds, hotImg.getId(), "" + hotImg.getId());
                jedis.hmset(kId("" + hotImg.getId()), hotImg.toMap());
            }
            jedis.set(kIdNext, "" + (nextId + 1));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisManager.recycleJedisOjbect(jedis);
        }
    }

    public List<HotImg> all() {
        return list(false);
    }

    public List<HotImg> visibles() {
        return list(true);
    }

    private List<HotImg> list(boolean justVisible) {
        List<HotImg> hotImgs = new ArrayList<>();
        Jedis jedis = RedisManager.getJedisObject();
        try {
            Set<String> ids = jedis.zrevrange(kIds, 0, -1);
            for (String id : ids) {
                Map<String, String> map = jedis.hgetAll(kId(id));
                if (justVisible == false || "1".equals(map.get("visible"))) {
                    HotImg hotImg = new HotImg(map);
                    hotImgs.add(hotImg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisManager.recycleJedisOjbect(jedis);
        }
        return hotImgs;
    }

    public HotImg get(Long id) {
        Jedis jedis = RedisManager.getJedisObject();
        try {
            return new HotImg(jedis.hgetAll(kId("" + id)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisManager.recycleJedisOjbect(jedis);
        }
        return null;
    }

    public void add(HotImg hotImg) {
        Jedis jedis = RedisManager.getJedisObject();
        try {
            long id = jedis.incr(kIdNext);
            hotImg.setId(id);
            jedis.zadd(kInsertIds, id, "" + id);
            jedis.zadd(kIds, hotImg.getId(), "" + hotImg.getId());
            jedis.hmset(kId("" + id), hotImg.toMap());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisManager.recycleJedisOjbect(jedis);
        }
    }

    public void update(HotImg hotImg) {
        Jedis jedis = RedisManager.getJedisObject();
        try {
            jedis.zadd(kUpdateIds, hotImg.getId(), "" + hotImg.getId());
            jedis.hmset(kId("" + hotImg.getId()), hotImg.toMap());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisManager.recycleJedisOjbect(jedis);
        }
    }

}
