package com.poly.bean;
import java.util.*;

public class  TbPlate  implements java.io.Serializable{
	public String KEY = "plate_id";
	public boolean[] COLUMN_FLAG = {false,false,false,false,false};
	private int plate_id;//板块id
	private String plate_name;//板块名称
	private Date add_time;//添加时间
	private int is_in;//是否为内部板块
	private int is_delete;//是否被删除


	public void setPlate_id(int plate_id)
	{
		this.plate_id=plate_id;
		COLUMN_FLAG[0] = true;
	}
	public int getPlate_id()
	{
		return plate_id;
	}
	public void setPlate_name(String plate_name)
	{
		this.plate_name=plate_name;
		COLUMN_FLAG[1] = true;
	}
	public String getPlate_name()
	{
		return plate_name;
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
	public void setIs_in(int is_in)
	{
		this.is_in=is_in;
		COLUMN_FLAG[3] = true;
	}
	public int getIs_in()
	{
		return is_in;
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
