package com.poly.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiyuan_fengyu on 2017/1/6.
 * 焦点图,对应表 tb_hot_img
 */
public class HotImg {

    private Long id;

    private String title;

    //相对路径，例如：001.jpg，实际存放地址：127.0.0.1:/data/app/nginx/tqbbs_html/images
    private String url;

    //是否显示，1：显示，0：不显示
    private Integer visible;

    private Date create_time;

    public HotImg() {
    }

    public HotImg(String title, String url) {
        this.title = title;
        this.url = url;
        this.visible = 1;
        this.create_time = new Date();
    }

    public HotImg(Map<String, String> map) {
        this.id = Long.parseLong(map.get("id"));
        this.title = map.get("title");
        this.url = map.get("url");
        this.visible = Integer.valueOf(map.get("visible"));
        this.create_time = new Date(Long.parseLong(map.get("create_time")));
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("id", "" + id);
        map.put("title", title);
        map.put("url", url);
        map.put("visible", "" + visible);
        map.put("create_time", "" + create_time.getTime());
        return map;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
