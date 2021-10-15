package com.metoo.view.web.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.metoo.core.mv.JModelAndView;
import com.metoo.core.tools.CommUtil;
import com.metoo.foundation.domain.Article;
import com.metoo.foundation.domain.ArticleClass;
import com.metoo.foundation.domain.Document;
import com.metoo.foundation.domain.query.DocumentQueryObject;
import com.metoo.foundation.service.IArticleClassService;
import com.metoo.foundation.service.IArticleService;
import com.metoo.foundation.service.IDocumentService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.foundation.service.IUserConfigService;
import com.metoo.module.app.buyer.domain.Result;

/**
 * 
* <p>Title: DocumentViewAction.java</p>

* <p>Description:系统文章前台显示控制器 </p>

* <p>Copyright: Copyright (c) 2014</p>

* <p>Company: 沈阳网之商科技有限公司 www.koala.com</p>

* @author erikzhang

* @date 2014-5-6

* @version koala_b2b2c v2.0 2015版 
 */
@Controller
public class DocumentViewAction {
	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IUserConfigService userConfigService;
	@Autowired
	private IDocumentService documentService;
	@Autowired
	private IArticleService articleService;
	@Autowired
	private IArticleClassService articleClassService;
	
	@RequestMapping("/doc.htm")
	public ModelAndView doc(HttpServletRequest request,
			HttpServletResponse response, String mark) {
		ModelAndView mv = new JModelAndView("article.html", configService
				.getSysConfig(), this.userConfigService.getUserConfig(), 1,
				request, response);
		mv.addObject("doc", "doc");
		Document obj = this.documentService.getObjByProperty(null,"mark", mark);
		mv.addObject("obj", obj);
		List<ArticleClass> acs = this.articleClassService
				.query("select obj from ArticleClass obj where obj.parent.id is null order by obj.sequence asc",
						null, -1, -1);
		List<Article> articles = this.articleService.query(
				"select obj from Article obj order by obj.addTime desc", null,
				0, 6);
		mv.addObject("articles", articles);
		mv.addObject("acs", acs);
		return mv;
	}
	
	/**
	 * 避免前端更新暂时不更新参数字段
	 * 
	 * @param request
	 * @param response
	 * @param currentPage
	 * @param orderBy
	 * @param orderType
	 * @param title 改为 mark
	 */
	@RequestMapping("/app/v1/app_document.json")
	public void app_document_list(HttpServletRequest request, HttpServletResponse response, String currentPage,
			String orderBy, String orderType, String title) {
		ModelAndView mv = new JModelAndView("", configService.getSysConfig(), this.userConfigService.getUserConfig(), 0,
				request, response);
		Result result = null;
		String url = this.configService.getSysConfig().getAddress();
		if (url == null || url.equals("")) {
			url = CommUtil.getURL(request);
		}
		String language = CommUtil.language(request);
		Map params = new HashMap();
		params.put("mark", title);
		List<Document> documents = null;
		if(language.equals("sa")){
			documents = this.documentService
					.query("SELECT new Document(id, addTime, title, title_sa, content_sa) "
							+ "FROM Document obj WHERE obj.mark=:mark", params, -1, -1);
		}else{
			documents = this.documentService
					.query("SELECT new Document(id, title, content) "
							+ "FROM Document obj WHERE obj.mark=:mark", params, -1, -1);
		}
		try {
			response.getWriter().print(Json.toJson(new Result(4200, "Success", documents), JsonFormat.compact()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/app/v1/app_document_policy.json")
	public void app_document_policy(HttpServletRequest request, HttpServletResponse response, String currentPage,
			String orderBy, String orderType, String mark) {
		ModelAndView mv = new JModelAndView("", configService.getSysConfig(), this.userConfigService.getUserConfig(), 0,
				request, response);
		Result result = null;
		String url = this.configService.getSysConfig().getAddress();
		if (url == null || url.equals("")) {
			url = CommUtil.getURL(request);
		}
		String params = "";
		Document document = null;
		if (mark != null && !("").equals(mark)) {
			document = this.documentService.getObjByProperty(null, "mark", mark);
		}
		Map documentmap = new HashMap();
		documentmap.put("document_title", document.getTitle());
		documentmap.put("document_id", document.getId());
		documentmap.put("document_content", document.getContent());
		documentmap.put("document_mark", document.getMark());
		if (documentmap.isEmpty()) {
			result = new Result(1, "No article");
		} else {
			result = new Result(4200, "Success", documentmap);
		}
		try {
			response.getWriter().print(Json.toJson(result, JsonFormat.compact()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
