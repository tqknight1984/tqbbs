package com.poly.dao;

import com.poly.bean.HotImg;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by xiyuan_fengyu on 2017/1/6.
 */
@Component
public class HotImgDao {

    @Resource(name="queryRunner")
    QueryRunner query;

    /**
     * @return
     * @throws SQLException
     */
    public List<HotImg> all() throws SQLException {
        String sql = "select * from tb_hot_img;";
        return query.query(sql, new BeanListHandler<>(HotImg.class));
    }

    /**
     * 所有要显示的焦点图
     * @return
     * @throws SQLException
     */
    public List<HotImg> visibles() throws SQLException {
        String sql = "select * from tb_hot_img where visible = 1;";
        return query.query(sql, new BeanListHandler<>(HotImg.class));
    }

    public void add(HotImg hotImg) throws SQLException {
        String sql = "insert into tb_hot_img(id, title, url, visible, create_time) values (?, ?, ?, ?, ?);";
        query.insert(sql, new BeanHandler<>(HotImg.class),
                hotImg.getId(),
                hotImg.getTitle(),
                hotImg.getUrl(),
                hotImg.getVisible(),
                hotImg.getCreate_time()
        );
    }

    public void update(HotImg hotImg) throws SQLException {
        String sql = "update tb_hot_img set title = ?, url = ?, visible = ?, create_time = ? where id = ?;";
        query.insert(sql, new BeanHandler<>(HotImg.class),
                hotImg.getTitle(),
                hotImg.getUrl(),
                hotImg.getVisible(),
                hotImg.getCreate_time(),
                hotImg.getId()
        );
    }

}
