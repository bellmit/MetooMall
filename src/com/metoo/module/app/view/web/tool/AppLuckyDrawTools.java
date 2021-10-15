package com.metoo.module.app.view.web.tool;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metoo.core.tools.database.DatabaseTools;

@Component
public class AppLuckyDrawTools {

	@Autowired
	private DatabaseTools databaseTools;

	public int getLuckyDraw(){
		String query = "select * from metoo_lucky_draw where switchs = 1";
		ResultSet res = this.databaseTools.selectIn(query);
		int lucky = 0;
		try {
			while (res.next()) {
				lucky = res.getInt("order");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lucky;
	}
}
