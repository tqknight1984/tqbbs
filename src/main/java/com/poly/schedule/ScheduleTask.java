package com.poly.schedule;

import javax.annotation.Resource;

import com.poly.service.hotImg.HotImgService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.poly.service.article.ArticleService;
import com.poly.service.comment.CommentService;
import com.poly.service.plate.PlateMasterService;
import com.poly.service.plate.PlateService;
import com.poly.service.user.UserService;

@Component
public class ScheduleTask {
	Logger log = Logger.getLogger(ScheduleTask.class);
	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="articleService")
	private ArticleService articleService;
	
	@Resource(name="commentService")
	private CommentService commentService;
	
	@Resource(name="plateMasterService")
	private PlateMasterService plateMasterService;
	
	@Resource(name="plateService")
	private PlateService plateService;

	@Autowired
	private HotImgService hotImgService;
	
	public void carinfoTimeTask() throws Exception{
//		//定时任务，每10分钟运行一次,将redis中的数据持久化到数据库中
		log.info("将增量持久化到数据库中");
		userService.persistentUser();
		//持久化article
		log.info("将增量持久化到数据库中--article");
		articleService.persistentArticle();
//		articleService.updateArticle();
		//持久化comment
		log.info("将增量持久化到数据库中--comment");
		commentService.persistComment();
		//持久化plate
		log.info("将增量持久化到数据库中--plate");
		plateService.persistPlate();
		//持久化plate_master
		log.info("将增量持久化到数据库中--master");
		plateMasterService.persistPlateMaster();
		//持久化hot_img
		log.info("将增量持久化到数据库中--hot_img");
		hotImgService.redisToMysql();
	}
	
}
