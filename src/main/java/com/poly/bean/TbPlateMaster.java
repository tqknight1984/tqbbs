package com.poly.bean;
import java.util.*;

public class  TbPlateMaster  implements java.io.Serializable{

	public String KEY = "master_id";
	public boolean[] COLUMN_FLAG = {false,false,false,false,false};
	private int master_id;//版主id
	private int user_id;//用户id
	private int plate_id;//板块id
	private Date add_time;//添加时间
	private int is_delete;//是否被删除


	public void setMaster_id(int master_id)
	{
		this.master_id=master_id;
		COLUMN_FLAG[0] = true;
	}
	public int getMaster_id()
	{
		return master_id;
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
	public void setPlate_id(int plate_id)
	{
		this.plate_id=plate_id;
		COLUMN_FLAG[2] = true;
	}
	public int getPlate_id()
	{
		return plate_id;
	}
	public void setAdd_time(Date add_time)
	{
		this.add_time=add_time;
		COLUMN_FLAG[3] = true;
	}
	public Date getAdd_time()
	{
		return add_time;
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
