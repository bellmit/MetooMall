package com.metoo.core.tools.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.metoo.core.tools.CommUtil;
import com.metoo.foundation.domain.Accessory;
import com.metoo.foundation.service.IAccessoryService;
import com.metoo.foundation.service.ISysConfigService;

/**
 * 
 * <p>
 * Title: DatabaseTools.java
 * </p>
 * 
 * <p>
 * Description: MySql的备份和还原，不依赖本地mysql安装，不使用mysql命令完成数据库备份及还原，仅仅支持mysql数据库
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * <p>
 * Company: 湖南**科技有限公司 www.koala.com
 * </p>
 * 
 * @author erikzhang
 * 
 * @date 2012-08-28
 * 
 * @version koala_b2b2c v2.0 2015版
 */
@Repository
@SuppressWarnings("serial")
public class DatabaseTools implements IBackup {

	@Autowired
	private PublicMethod publicMethod;
	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IAccessoryService accessoryService;
	
	public DatabaseTools() {
	}

	/**
	 * 创建mysql备份数据
	 * 
	 * @param path
	 *            备份路径
	 * @param tables
	 *            将要备份的数据表列表
	 * @param size
	 *            分卷备份大小
	 * @return
	 * @throws Exception
	 */
	public boolean createSqlScript(HttpServletRequest request, String path,
			String name, String size, String tables) throws Exception {
		int count = 1;
		boolean ret = true;
		float psize = CommUtil.null2Float(size);
		List<String> tablelists = publicMethod.getAllTableName("show tables");
		List<String> backup_list = new ArrayList<String>();
		if (tables != null && !tables.equals("")) {
			backup_list = Arrays.asList(tables.split(","));
		} else {
			backup_list = tablelists;
		}
		// 先写建表语句
		try {
			File file = new File(path + File.separator + name + "_" + count
					+ ".sql");
			PrintWriter pwrite;
			pwrite = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(file, true), "UTF-8"), true);
			// 写入头部信息
			pwrite.println(AppendMessage.headerMessage());
			pwrite.println("SET FOREIGN_KEY_CHECKS=0;" + "\n");
			for (String table : backup_list) {
				StringBuilder strBuilder = new StringBuilder();
				strBuilder.append("show create table ").append(table);
				List<String> list = publicMethod.getAllColumns(strBuilder
						.toString());
				for (String line : list) {
					// 及时计算分卷文件大小
					double fsize = CommUtil.div(file.length(), 1024);
					if (fsize > psize) {
						pwrite.flush();
						// pwrite.close();
						count++;
						file = new File(path + File.separator + name + "_"
								+ count + ".sql");
						pwrite = new PrintWriter(new OutputStreamWriter(
								new FileOutputStream(file, true), "UTF-8"),
								true);
						// 写入头部信息
						pwrite.println(AppendMessage.headerMessage());
					}
					request.getSession(false).setAttribute("db_mode", "backup");
					request.getSession(false).setAttribute("db_bound", count);
					request.getSession(false).setAttribute("db_error", 0);
					request.getSession(false).setAttribute("db_result", 0);
					// 在建表前加说明
					pwrite.println(AppendMessage.tableHeaderMessage(table));
					// 生成建表语句
					pwrite.println("DROP TABLE IF EXISTS " + " `" + table
							+ "`;");
					pwrite.println(line + ";" + "\n");
				}
			}
			pwrite.flush();
			pwrite.close();
		} catch (Exception e) {
			ret = false;
			e.printStackTrace();
			throw new Exception("出现错误,创建备份文件失败!");
		}
		// 后写数据插入语句
		count++;
		try {
			File file = new File(path + File.separator + name + "_" + count
					+ ".sql");
			PrintWriter pwrite;
			pwrite = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(file, true), "UTF-8"), true);
			// 写入头部信息
			pwrite.println(AppendMessage.headerMessage());
			pwrite.println(AppendMessage.insertHeaderMessage());
			for (String table : backup_list) {
				if (!CommUtil.null2String(table).equals("")) {
					// 生成insert语句
					List<String> insertList = getAllDatas(table.toString());
					for (int i = 0; i < insertList.size(); i++) {
						double fsize = CommUtil.div(file.length(), 1024);
						if (fsize > psize) {
							pwrite.flush();
							// pwrite.close();
							count++;
							file = new File(path + File.separator + name + "_"
									+ count + ".sql");
							pwrite = new PrintWriter(new OutputStreamWriter(
									new FileOutputStream(file, true), "UTF-8"),
									true);
							// 写入头部信息
							pwrite.println(AppendMessage.headerMessage());
						}
						request.getSession(false).setAttribute("db_mode",
								"backup");
						request.getSession(false).setAttribute("db_bound",
								count);
						request.getSession(false).setAttribute("db_error", 0);
						request.getSession(false).setAttribute("db_result", 0);
						pwrite.flush();
						pwrite.println(insertList.get(i));
					}
				}
			}
			pwrite.flush();
			pwrite.close();
			// 备份完成
			request.getSession(false).setAttribute("db_result", 1);
		} catch (Exception e) {
			ret = false;
			e.printStackTrace();
			throw new Exception("出现错误,创建备份文件失败!");
		}
		return ret;
	}

	/**
	 * 还原mysql备份
	 */
	public boolean executSqlScript(String filePath) throws Exception {
		Connection conn = null;
		Statement stmt = null;
		List<String> sqlStrList = null;
		boolean ret = true;
		try {
			sqlStrList = publicMethod.loadSqlScript(filePath);
			conn = publicMethod.getConnection();
			stmt = conn.createStatement();
			// 禁止自动提交
			conn.setAutoCommit(false);
			for (String sqlStr : sqlStrList) {
				int index = sqlStr.indexOf("INSERT");
				if (-1 == index) {
					stmt.addBatch(sqlStr);
					System.out.println("Create语句：");
					System.out.println(sqlStr);
				}
			}
			stmt.executeBatch();
			// INSERT语句跟建表语句分开执行，防止未建表先INSERT
			for (String sqlStr : sqlStrList) {
				int index = sqlStr.indexOf("INSERT");
				if (-1 != index) {
					System.out.println(sqlStr);
					int status = stmt.executeUpdate(sqlStr);
					System.out.println("执行结果：" + status);
				}
			}
			stmt.executeBatch();
			conn.commit();
		} catch (Exception ex) {
			ret = false;
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			conn.rollback();
			ex.printStackTrace();
		}
		return ret;
	}

	/**
	 * 根据数据表名称生成insert语句
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<String> getAllDatas(String tableName) throws Exception {
		List<String> list = new ArrayList<String>();
		StringBuilder typeStr = null;
		List<TableColumn> columnList;
		StringBuilder sqlStr;
		ResultSet rs = null;
		StringBuilder columnsStr;
		try {
			// 生成查询语句
			typeStr = new StringBuilder();
			sqlStr = new StringBuilder();
			columnsStr = new StringBuilder().append("describe ").append(
					tableName);
			columnList = publicMethod.getDescribe(columnsStr.toString());
			sqlStr.append("SELECT ");
			for (TableColumn bColumn : columnList) {
				// 处理BLOB类型的数据
				String columnsType = bColumn.getColumnsType();
				if ("longblob".equals(columnsType)
						|| "blob".equals(columnsType)
						|| "tinyblob".equals(columnsType)
						|| "mediumblob".equals(columnsType)) {
					typeStr.append("hex(" + "`" + bColumn.getColumnsFiled()
							+ "`" + ") as " + "`" + bColumn.getColumnsFiled()
							+ "`" + " ,");
				} else {
					typeStr.append("`" + bColumn.getColumnsFiled() + "`" + " ,");
				}
			}
			sqlStr.append(typeStr.substring(0, typeStr.length() - 1));
			sqlStr.append(" FROM ").append("`" + tableName + "`;");
			rs = publicMethod.queryResult(sqlStr.toString());
			while (rs.next()) {
				// 查询insert语句所需的数据
				StringBuffer insert_sql = new StringBuffer();
				insert_sql.append("INSERT INTO " + tableName + " ("
						+ typeStr.substring(0, typeStr.length() - 1)
						+ ") VALUES (");
				Vector<Object> vector = new Vector<Object>();
				for (TableColumn dbColumn : columnList) {
					String columnsType = dbColumn.getColumnsType();
					String columnsFile = dbColumn.getColumnsFiled();
					if (null == rs.getString(columnsFile)) {
						vector.add(rs.getString(columnsFile));
						// 处理BIT类型的数据
					} else if ("bit".equals(columnsType.substring(0, 3))) {
						vector.add(Integer.valueOf(rs.getString(columnsFile))
								.intValue());
					} else if ("bit".equals(columnsType.substring(0, 3))
							&& 0 == Integer.valueOf(rs.getString(columnsFile))
									.intValue()) {
						vector.add("\'" + "\'");
					} else if ("longblob".equals(columnsType)
							|| "blob".equals(columnsType)
							|| "tinyblob".equals(columnsType)
							|| "mediumblob".equals(columnsType)) {
						vector.add("0x" + rs.getString(columnsFile));
						// 处理
					} else if ("text".equals(columnsType)
							|| "longtext".equals(columnsType)
							|| "tinytext".equals(columnsType)
							|| "mediumtext".equals(columnsType)) {
						String tempStr = rs.getString(columnsFile);
						tempStr = tempStr.replaceAll("\'", "\\'").replaceAll(
								"'", "''");
						tempStr = tempStr.replaceAll("\"", "\\\"")
								.replaceAll("\r", "\\\\r")
								.replaceAll("\n", "\\\\n")
								.replaceAll("<!--[\\w\\W\\r\\n]*?-->", "")
								.replaceAll("——", "-");
						vector.add("\'" + tempStr + "\'");
					} else {
						String tempStr = rs.getString(columnsFile);
						tempStr = tempStr.replaceAll("\'", "\\'").replaceAll(
								"'", "''");
						tempStr = tempStr.replaceAll("\"", "\\\"")
								.replaceAll("\r", "\\\\r")
								.replaceAll("\n", "\\\\n")
								.replaceAll("<!--[\\w\\W\\r\\n]*?-->", "")
								.replaceAll("——", "-");
						;
						vector.add("\'" + tempStr + "\'");
					}
				}
				String tempStr = vector.toString();
				tempStr = tempStr.substring(1, tempStr.length() - 1) + ");";
				insert_sql.append(tempStr);
				list.add(insert_sql.toString());
			}
		} catch (Exception e) {
			throw e;
		}
		return list;
	}

	/**
	 * 查询生成Insert语句所需的数据,该方法和上面的一样，只是未做一些中文字符替换，在修改上面方法出错时可以参考该方法
	 * 
	 * @param tableName
	 * @return List<Vector<Object>>
	 * @throws Exception
	 */
	public List<Vector<Object>> getAllDatas1(String tableName) throws Exception {
		List<Vector<Object>> list;
		Vector<Object> vector;
		StringBuilder typeStr = null;
		List<TableColumn> columnList;
		StringBuilder sqlStr;
		ResultSet rs = null;
		StringBuilder columnsStr;
		try {
			// 生成查询语句
			typeStr = new StringBuilder();
			sqlStr = new StringBuilder();
			columnsStr = new StringBuilder().append("describe ").append(
					tableName);
			columnList = publicMethod.getDescribe(columnsStr.toString());
			sqlStr.append("SELECT ");
			for (TableColumn bColumn : columnList) {
				// 处理BLOB类型的数据
				String columnsType = bColumn.getColumnsType();
				if ("longblob".equals(columnsType)
						|| "blob".equals(columnsType)
						|| "tinyblob".equals(columnsType)
						|| "mediumblob".equals(columnsType)) {
					typeStr.append("hex(" + "`" + bColumn.getColumnsFiled()
							+ "`" + ") as " + "`" + bColumn.getColumnsFiled()
							+ "`" + " ,");
				} else {
					typeStr.append("`" + bColumn.getColumnsFiled() + "`" + " ,");
				}
			}
			sqlStr.append(typeStr.substring(0, typeStr.length() - 1));
			sqlStr.append(" FROM ").append("`" + tableName + "`;");

			// 查询insert语句所需的数据
			list = new ArrayList<Vector<Object>>();
			rs = publicMethod.queryResult(sqlStr.toString());
			while (rs.next()) {
				vector = new Vector<Object>();
				for (TableColumn dbColumn : columnList) {
					String columnsType = dbColumn.getColumnsType();
					String columnsFile = dbColumn.getColumnsFiled();
					if (null == rs.getString(columnsFile)) {
						vector.add(rs.getString(columnsFile));
						// 处理BIT类型的数据
					} else if ("bit".equals(columnsType.substring(0, 3))) {
						vector.add(Integer.valueOf(rs.getString(columnsFile))
								.intValue());
					} else if ("bit".equals(columnsType.substring(0, 3))
							&& 0 == Integer.valueOf(rs.getString(columnsFile))
									.intValue()) {
						vector.add("\'" + "\'");
					} else if ("longblob".equals(columnsType)
							|| "blob".equals(columnsType)
							|| "tinyblob".equals(columnsType)
							|| "mediumblob".equals(columnsType)) {
						vector.add("0x" + rs.getString(columnsFile));
						// 处理
					} else if ("text".equals(columnsType)
							|| "longtext".equals(columnsType)
							|| "tinytext".equals(columnsType)
							|| "mediumtext".equals(columnsType)) {
						String tempStr = rs.getString(columnsFile);
						tempStr = tempStr.replace("\'", "\\'");
						tempStr = tempStr.replace("\"", "\\\"");
						vector.add("\'" + tempStr + "\'");
					} else {
						vector.add("\'" + rs.getString(columnsFile) + "\'");
					}
				}
				list.add(vector);
			}
		} catch (Exception e) {
			throw e;
		}
		return list;
	}

	public List<String> getTables() throws Exception {
		List<String> tables = new ArrayList<String>();
		Connection conn = null;
		try {
			conn = this.publicMethod.getConnection();
			ResultSet rs = conn.getMetaData().getTables("", "", "", null);
			while (rs.next()) {
				tables.add(rs.getString("TABLE_NAME"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.publicMethod.closeConn();
		}
		return tables;
	}

	/**
	 * 获取mysql数据库版本号
	 * 
	 * @return
	 */
	public String queryDatabaseVersion() {
		java.sql.Connection conn = null;
		String version = "未知版本号";
		try {
			conn = this.publicMethod.getConnection();
			DatabaseMetaData md = conn.getMetaData();
			return md.getDatabaseProductName() + " "
					+ md.getDatabaseProductVersion().toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return version;
	}

	/**
	 * 执行sql语句
	 * 
	 * @param sql
	 * @return
	 */
	public boolean execute(String sql) {
		Connection conn = null;
		boolean ret = true;
		try {
			conn = this.publicMethod.getConnection();
			java.sql.Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (Exception e) {
			ret = false;
			e.printStackTrace();
		} finally {
			this.publicMethod.closeConn();
		}
		return ret;
	}
	
	public Map selectIns(String sql, Map params, String param){
		Map<String, Integer> map = new HashMap<String, Integer>();
		ResultSet rs = null;
		try {
			rs = publicMethod.queryResult(sql.toString());
			if(rs != null){
				while (rs.next()) {
					if(params != null && params.size() > 0){
						Long id = -1L;
						int count = 0;;
						for(Object key : params.keySet()){
							id = rs.getLong(key.toString());
							count = rs.getInt(params.get(key).toString());
						}
						map.put(id.toString(), count);
					}
					if(param != null && !param.equals("")){
						int lucky = rs.getInt(param);
						map.put("lucky", lucky);
					}
				}
			}
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.publicMethod.closeConn();
		}
		return null;
	}
	
	/**
	 * 查询商品属性
	 * @param sql
	 * @param properties
	 * @return
	 */
	public Set<Map> select_goods(String sql,List<String> properties){
		String imageWebServer = this.configService.getSysConfig().getImageWebServer();
		Set<Map> goodsSet = new HashSet<Map>();
		Map<String, Integer> map = new HashMap<String, Integer>();
		ResultSet rs = null;
		try {
			rs = publicMethod.queryResult(sql.toString());
			if(rs != null){
				while (rs.next()) {
					if(properties != null && properties.size() > 0){
						Long goods_id;
						String name = "";
						String ksa_name = "";
						String img;
						BigDecimal price;
						BigDecimal current_price;
						BigDecimal goods_discount_rate;
						int goods_status;
						int goods_collect;
						Map goodsMap = new HashMap();
						goods_id = rs.getLong("goods_vice_id");
						name = rs.getString("goods_name");
						ksa_name = rs.getString("ksa_goods_name");
						price = rs.getBigDecimal("goods_price");
						current_price = rs.getBigDecimal("goods_current_price");
						goods_status = rs.getInt("goods_status");
						goods_discount_rate = rs.getBigDecimal("goods_discount_rate");
						goodsMap.put("goods_id", goods_id);
						goodsMap.put("goods_name", ksa_name != null ? ksa_name : name);
						goodsMap.put("goods_price", price);
						goodsMap.put("goods_current_price", current_price);
						goodsMap.put("goods_discount_rate", goods_discount_rate);
						goodsMap.put("goods_status", goods_status);
						Accessory acc = this.accessoryService.getObjById(rs.getLong("goods_main_photo_id"));
						if (acc != null) {
							goodsMap.put("goods_img", imageWebServer + "/" + acc.getPath() + "/" + acc.getName());
						}
						goodsSet.add(goodsMap);
					}
				}
			}
			return goodsSet;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.publicMethod.closeConn();
		}
		return null;
	}
	
	// 获取指定参数的数据集合
	public ResultSet selectIn(String sql){
		ResultSet rs = null;
		try {
			rs = publicMethod.queryResult(sql.toString());
			return rs;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.publicMethod.closeConn();
		}
		return null;
	}

	@Override
	public boolean export(String tables, String path) {
		// TODO Auto-generated method stub
		boolean ret = true;
		try {
			File file = new File(path);
			PrintWriter pwrite;
			pwrite = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(file, true), "UTF-8"), true);
			// 写入头部信息
			pwrite.println(AppendMessage.headerMessage());
			pwrite.println(AppendMessage.insertHeaderMessage());
			List<String> list = Arrays.asList(tables.split(","));
			for (String table : list) {
				// 生成insert语句
				List<String> insertList = getAllDatas(table.toString());
				for (int i = 0; i < insertList.size(); i++) {
					pwrite.flush();
					pwrite.println(insertList.get(i));
				}
			}
			pwrite.flush();
			pwrite.close();
		} catch (Exception e) {
			ret = false;
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public ResultSet query(String sql) {
		// TODO Auto-generated method stub
		Connection conn = null;
		ResultSet rs = null;
		boolean ret = true;
		try {
			conn = this.publicMethod.getConnection();
			java.sql.Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			try {
				while(rs.next()){ 
					int ids = rs.getInt(1);// 获取第一个列的值 
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			ret = false;
			e.printStackTrace();
		} finally {
			this.publicMethod.closeConn();
		}
		return rs;
	}
	
	@Override
	public int queryNum(String sql) {
		// TODO Auto-generated method stub
		Connection conn = null;
		ResultSet rs = null;
		boolean ret = true;
		int num = 0;
		try {
			conn = this.publicMethod.getConnection();
			java.sql.Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			try {
				while(rs.next()){ 
					 num = rs.getInt(1);// 获取第一个列的值 
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			ret = false;
			e.printStackTrace();
		} finally {
			this.publicMethod.closeConn();
		}
		return num;
	}

}
