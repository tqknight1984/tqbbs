package com.poly.bean;
import java.util.*;

public class  TbArticleElite  implements java.io.Serializable{

	public String KEY = "elite_id";
	public boolean[] COLUMN_FLAG = {false,false,false,false,false};
	private int elite_id;//精华帖id
	private int article_id;//文章id
	private Date add_time;//添加时间
	private int add_user;//操作人id
	private int is_delete;//是否被删除


	public void setElite_id(int elite_id)
	{
		this.elite_id=elite_id;
		COLUMN_FLAG[0] = true;
	}
	public int getElite_id()
	{
		return elite_id;
	}
	public void setArticle_id(int article_id)
	{
		this.article_id=article_id;
		COLUMN_FLAG[1] = true;
	}
	public int getArticle_id()
	{
		return article_id;
	}
	public void setAdd_time(Date add_time)
	{
		this.add_time=add_time;
		COLUMN_FLAG[2] = true;
	}
	public Date getAdd_time()
	{
		return add_time;
	}
	public void setAdd_user(int add_user)
	{
		this.add_user=add_user;
		COLUMN_FLAG[3] = true;
	}
	public int getAdd_user()
	{
		return add_user;
	}
	public void setIs_delete(int is_delete)
	{
		this.is_delete=is_delete;
		COLUMN_FLAG[4] = true;
	}
	public int getIs_delete()
	{
		return is_delete;
	}
}
