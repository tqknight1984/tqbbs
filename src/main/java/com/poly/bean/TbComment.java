package com.poly.bean;
import java.util.*;

public class  TbComment  implements java.io.Serializable{

	public String KEY = "com_id";
	public boolean[] COLUMN_FLAG = {false,false,false,false,false,false,false,false,false};
	private int com_id;//回复id
	private int article_userid;//帖子作者id
	private int com_userid;//回复的为评论时，评论人id
	private int article_id;//帖子id
	private int comment_id;//评论id（当id为0时，回复的为帖子）
	private int user_id;//回复人id
	private String com_content;//回复内容
	private Date add_time;//添加时间
	private int is_delete;//是否被删除


	public void setCom_id(int com_id)
	{
		this.com_id=com_id;
		COLUMN_FLAG[0] = true;
	}
	public int getCom_id()
	{
		return com_id;
	}
	public void setArticle_userid(int article_userid)
	{
		this.article_userid=article_userid;
		COLUMN_FLAG[1] = true;
	}
	public int getArticle_userid()
	{
		return article_userid;
	}
	public void setCom_userid(int com_userid)
	{
		this.com_userid=com_userid;
		COLUMN_FLAG[2] = true;
	}
	public int getCom_userid()
	{
		return com_userid;
	}
	public void setArticle_id(int article_id)
	{
		this.article_id=article_id;
		COLUMN_FLAG[3] = true;
	}
	public int getArticle_id()
	{
		return article_id;
	}
	public void setComment_id(int comment_id)
	{
		this.comment_id=comment_id;
		COLUMN_FLAG[4] = true;
	}
	public int getComment_id()
	{
		return comment_id;
	}
	public void setUser_id(int user_id)
	{
		this.user_id=user_id;
		COLUMN_FLAG[5] = true;
	}
	public int getUser_id()
	{
		return user_id;
	}
	public void setCom_content(String com_content)
	{
		this.com_content=com_content;
		COLUMN_FLAG[6] = true;
	}
	public String getCom_content()
	{
		return com_content;
	}
	public void setAdd_time(Date add_time)
	{
		this.add_time=add_time;
		COLUMN_FLAG[7] = true;
	}
	public Date getAdd_time()
	{
		return add_time;
	}
	public void setIs_delete(int is_delete)
	{
		this.is_delete=is_delete;
		COLUMN_FLAG[8] = true;
	}
	public int getIs_delete()
	{
		return is_delete;
	}
}
