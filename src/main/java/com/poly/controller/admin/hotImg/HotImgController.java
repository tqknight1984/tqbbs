package com.poly.controller.admin.hotImg;

import com.poly.bean.HotImg;
import com.poly.service.hotImg.HotImgService;
import com.poly.util.MyConfig;
import com.poly.util.ResponseUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.Date;
import java.util.Map;

/**
 * Created by xiyuan_fengyu on 2017/1/6.
 */
@Controller
public class HotImgController {

    @Autowired
    private HotImgService hotImgService;

    @RequestMapping(value = "admin/hotImg")
    public String hotImgPage(Model model) {
        model.addAttribute("hotImgs", hotImgService.all());
        return "admin/hotImg/index.ftl";
    }

    @RequestMapping(value = "admin/hotImg/add")
    public String addPage() {
        return "admin/hotImg/add.ftl";
    }

    @RequestMapping(value = "admin/hotImg/insert")
    @ResponseBody
    public Map<String, Object> insert(String title, MultipartFile img) {
        if (StringUtils.isNotEmpty(title) && img != null && img.getSize() > 0) {
            String[] split = img.getOriginalFilename().split("\\.");
            String subfix = split.length > 1 ? split[split.length - 1] : ".jpg";
            String savePath = "" + new Date().getTime() / 1000 + "_" + (int) (Math.random() * 1000) + "." +  subfix;
            try {
                File saveDir = new File(MyConfig.hot_img_savePath);
                if (!saveDir.exists()) {
                    saveDir.mkdirs();
                }
                File saveFile = new File(MyConfig.hot_img_savePath + savePath);
                Files.copy(img.getInputStream(), saveFile.toPath());
            }
            catch (Exception e) {
                return ResponseUtil.fail("图片保存失败");
            }
            HotImg hotImg = new HotImg(title, savePath);
            hotImgService.add(hotImg);
            return ResponseUtil.success("焦点图添加成功");
        }
        else {
            return ResponseUtil.fail("缺少参数");
        }
    }

    @RequestMapping(value = "admin/hotImg/update")
    @ResponseBody
    public Map<String, Object> update(Long id, String title, MultipartFile img, int visible) {
        if (id != null) {
            HotImg hotImg = hotImgService.get(id);
            if (hotImg == null) {
                return ResponseUtil.fail("该焦点图片不存在");
            }

            hotImg.setTitle(title);

            if (img != null && img.getSize() > 0) {
                String[] split = img.getOriginalFilename().split("\\.");
                String subfix = split.length > 1 ? split[split.length - 1] : ".jpg";
                String savePath = "" + new Date().getTime() / 1000 + "_" + (int) (Math.random() * 1000) + "." +  subfix;
                try {
                    File saveDir = new File(MyConfig.hot_img_savePath);
                    if (!saveDir.exists()) {
                        saveDir.mkdirs();
                    }
                    File saveFile = new File(MyConfig.hot_img_savePath + savePath);
                    Files.copy(img.getInputStream(), saveFile.toPath());
                }
                catch (Exception e) {
                    return ResponseUtil.fail("图片保存失败");
                }
                hotImg.setUrl(savePath);
            }

            hotImg.setVisible(visible);

            hotImgService.update(hotImg);
            return ResponseUtil.success("焦点图信息修改成功", hotImg);
        }
        else {
            return ResponseUtil.fail("缺少参数");
        }
    }

}
