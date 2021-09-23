package com.metoo.module.app.manage.buyer.tool;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metoo.core.language.Google;
import com.metoo.core.language.NameValue;
import com.metoo.foundation.domain.SearchKeyword;
import com.metoo.foundation.service.ISearchKeywordService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class AppSearchKeywordTools {

	@Autowired
	private ISearchKeywordService searchKeywordService;

	/**
	 * 
	 * @param keyword
	 * @return
	 * @description 查询此用户搜索是否已收录
	 */
	public String getEnWord(String arabic_word, String language) {
		String keyword = null;
		Map params = new HashMap();
		params.put("arabic_word", arabic_word);
		List<SearchKeyword> searchKeywords = this.searchKeywordService
				.query("select obj from SearchKeyword obj where obj.arabic_word=:arabic_word", params, -1, -1);
		if (searchKeywords.size() == 0) {
			Google google = new Google("https://translation.googleapis.com/language/translate/v2",
					"AIzaSyDC4pcWmrtp8flJd4iHULadvOszOVh57Io", "en", language, "nmt");
			String tran = google.translation(arabic_word);
			JSONObject tran_jSONObject = JSONObject.fromObject(tran);
			JSONObject tran_data = (JSONObject) tran_jSONObject.get("data");
			JSONArray tran_index = (JSONArray) tran_data.get("translations");
			JSONObject translatedText = (JSONObject) tran_index.get(0);
			String test = (String) translatedText.get("translatedText");
			keyword = StringEscapeUtils.unescapeHtml4(test);
			SearchKeyword searchKeyword = new SearchKeyword();
			searchKeyword.setAddTime(new Date());
			searchKeyword.setWord(keyword);
			searchKeyword.setArabic_word(arabic_word);
			searchKeyword.setLanguage(language);
			this.searchKeywordService.save(searchKeyword);
		}else{
			for(SearchKeyword searchKeyword: searchKeywords){
				if(searchKeyword.getArabic_word().equals(arabic_word)){
					keyword = searchKeyword.getWord();
				}
			}
		}
		return keyword;
	}
}
