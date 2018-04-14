package com.poly.bean;

import java.util.Date;

public class  TbArticle  implements java.io.Serializable{

	public String KEY = "article_id";
	public boolean[] COLUMN_FLAG = {false,false,false,false,false,false,false,false,false,false,false,false,false};
	private int article_id;//帖子id
	private int user_id;//用户id
	private String article_title;//帖子标题
	private String article_markdown;//文章的markdown
	private String article_content;//帖子内容
	private int plate_id;//板块id
	private Date add_time;//添加时间
	private Date update_time;//更新时间
	private int article_state;//帖子状态：1-正常，2-冻结
	private int is_delete;//是否被删除
	private int article_lookcount;//查看数
	private int is_elite;//是否是精华帖(0-普通帖，1-精华帖)
	private long article_order;//置顶排序（不置顶为0，置顶按照该值排序）


	public void setArticle_id(int article_id)
	{
		this.article_id=article_id;
		COLUMN_FLAG[0] = true;
	}
	public int getArticle_id()
	{
		return article_id;
	}
	public void setUser_id(int user_id)
	{
		this.user_id=user_id;
		COLUMN_FLAG[1] = true;
	}
	public int getUser_id()
	{
		return user_id;
	}
	public void setArticle_title(String article_title)
	{
		this.article_title=article_title;
		COLUMN_FLAG[2] = true;
	}
	public String getArticle_title()
	{
		return article_title;
	}
	public void setArticle_markdown(String article_markdown)
	{
		this.article_markdown=article_markdown;
		COLUMN_FLAG[3] = true;
	}
	public String getArticle_markdown()
	{
		return article_markdown;
	}
	public void setArticle_content(String article_content)
	{
		this.article_content=article_content;
		COLUMN_FLAG[4] = true;
	}
	public String getArticle_content()
	{
		return article_content;
	}
	public void setPlate_id(int plate_id)
	{
		this.plate_id=plate_id;
		COLUMN_FLAG[5] = true;
	}
	public int getPlate_id()
	{
		return plate_id;
	}
	public void setAdd_time(Date add_time)
	{
		this.add_time=add_time;
		COLUMN_FLAG[6] = true;
	}
	public Date getAdd_time()
	{
		return add_time;
	}
	public void setUpdate_time(Date update_time)
	{
		this.update_time=update_time;
		COLUMN_FLAG[7] = true;
	}
	public Date getUpdate_time()
	{
		return update_time;
	}
	public void setArticle_state(int article_state)
	{
		this.article_state=article_state;
		COLUMN_FLAG[8] = true;
	}
	public int getArticle_state()
	{
		return article_state;
	}
	public void setIs_delete(int is_delete)
	{
		this.is_delete=is_delete;
		COLUMN_FLAG[9] = true;
	}
	public int getIs_delete()
	{
		return is_delete;
	}
	public void setArticle_lookcount(int article_lookcount)
	{
		this.article_lookcount=article_lookcount;
		COLUMN_FLAG[10] = true;
	}
	public int getArticle_lookcount()
	{
		return article_lookcount;
	}
	public void setIs_elite(int is_elite)
	{
		this.is_elite=is_elite;
		COLUMN_FLAG[11] = true;
	}
	public int getIs_elite()
	{
		return is_elite;
	}
	public void setArticle_order(long article_order)
	{
		this.article_order=article_order;
		COLUMN_FLAG[12] = true;
	}
	public long getArticle_order()
	{
		return article_order;
	}
}
