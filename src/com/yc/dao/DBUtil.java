package com.yc.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * java对数据库的jdbc操作
 * 
 * @author jp
 *
 */
public class DBUtil {
	static {
		try {
			ResourceBundle rb = ResourceBundle.getBundle("db"); // 加载db.properties文件中的内容转换成key/value的数据
			Class.forName(rb.getString("driverClassName"));// 加载数据驱动
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("加载数据驱动失败!!!", e);
		}
	}

	/**
	 * 建立与数据库连接
	 * 
	 * @return 数据库连接对象
	 * @throws SQLException 
	 * @throws NamingException
	 */
	public static Connection getConn()  {
		try {
			//采用数据库联接池方案
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			DataSource ds = (DataSource) envCtx.lookup("jdbc/res");
			return ds.getConnection();
		} catch (Exception e) {
			ResourceBundle rb = ResourceBundle.getBundle("db"); // 加载db.properties文件中的内容转换成key/value的数据
			try {
				return DriverManager.getConnection(rb.getString("url"), rb.getString("user"), rb.getString("password"));
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 对数据库中表进行增， 删，改的操作
	 * 
	 * @param sql增
	 *            ， 删，改 的sql语句
	 * @param params增
	 *            ， 删，改 的sql语句中占位符(?)的值
	 * @return 受影响的行数
	 */
	public static int doUpdate(String sql, Object... params) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = getConn();
			st = con.prepareStatement(sql);
			setPlaceHodlerValue(st, params);
			return st.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("执行sql语句：" + sql + "失败!!!", e);
		} finally {
			close(con, st, null);
		}
	}

	/**
	 * 对数据库中表进行查的操作
	 * 
	 * @param sql查的sql语句
	 * @param params查的sql语句中占位符
	 *            (?)的值
	 * @return map：key是字段名， value是字段值
	 */
	public static Map<String, Object> get(String sql, Object... params) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = getConn();
			st = con.prepareStatement(sql);
			setPlaceHodlerValue(st, params);
			rs = st.executeQuery();
			if (rs.next()) {
				ResultSetMetaData rsmd = st.getMetaData(); // 取到数据集的元数据
				int columnCount = rsmd.getColumnCount();
				Map<String, Object> result = new HashMap<String, Object>();
				for (int i = 0; i < columnCount; i++) {
					Object val = rs.getObject(i + 1);
					String key = rsmd.getColumnName(i + 1).toUpperCase();
					result.put(key, val);
				}
				return result;
			} else {
				return null;
			}

		} catch (SQLException e) {
			throw new RuntimeException("执行sql语句：" + sql + "失败!!!", e);
		} finally {
			close(con, st, rs);
		}
	}

	/**
	 * 对数据库中表进行查的操作
	 * 
	 * @param sql查的sql语句
	 * @param params查的sql语句中占位符
	 *            (?)的值
	 * @return clazz类型的对象
	 */
	public static <T> T get(Class<T> clazz, String sql, Object... params) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = getConn();
			st = con.prepareStatement(sql);
			setPlaceHodlerValue(st, params);
			rs = st.executeQuery();
			if (rs.next()) {
				try {
					T t = clazz.newInstance();
					Field[] fs = clazz.getDeclaredFields(); // 根据类的类对象
															// ，取到这个类的所有属性对象集合
					for (Field field : fs) {
						String fieldType = field.getType().getName(); // 取到属性的类型
						try {
							field.setAccessible(true); // 使用属性可以赋值
							
							if (fieldType.intern() == "java.lang.Integer" || fieldType.intern() == "int") {
								field.set(t, rs.getInt(field.getName())); // 给属性赋值
							} else if (fieldType.intern() == "java.lang.Double" || fieldType.intern() == "double") {
								field.set(t, rs.getDouble(field.getName()));
							} else if (fieldType.intern() == "java.util.Date") {
								field.set(t, new java.util.Date(rs.getTimestamp(field.getName()).getTime()));
							} else if (fieldType.intern() == "java.sql.Date") {
								field.set(t, rs.getDate(field.getName()));
							} else if (fieldType.intern() == "java.io.InputStream") {
								InputStream in = rs.getBlob(field.getName()).getBinaryStream();
								ByteArrayOutputStream out = new ByteArrayOutputStream();
								byte[] bs = new byte[1024];
								int len = 0;
								while ((len = in.read(bs)) != -1) {
									out.write(bs, 0, len);
								}
								out.flush();
								field.set(t, new ByteArrayInputStream(out.toByteArray()));
								in.close();
								out.close();
							} else {
								field.set(t, rs.getObject(field.getName()));
							}
						} catch (Exception e) {
						}
					}
					return t;
				} catch (Exception e) {
					throw new RuntimeException("创建查询返回" + clazz.getName() + "对象失败!!!", e);
				}
			} else {
				return null;
			}

		} catch (SQLException e) {
			throw new RuntimeException("执行sql语句：" + sql + "失败!!!", e);
		} finally {
			close(con, st, rs);
		}
	}

	/**
	 * 对数据库中表进行读取或查询多条的操作
	 * 
	 * @param sql读取或查询多条的sql语句
	 * @param params读取或查询多条的sql语句中占位符
	 *            (?)的值
	 * @return List<Map<String, Object>>：每一个list中的map都是表中的一行记录，map中key是字段名， value是字段值
	 */
	public static List<Map<String, Object>> list(String sql, Object... params) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Map<String, Object>> results = null;
		try {
			con = getConn();
			st = con.prepareStatement(sql);
			setPlaceHodlerValue(st, params);
			rs = st.executeQuery();
			results = new ArrayList<Map<String, Object>>();
			boolean flag = false;
			while (rs.next()) {
				flag = true;
				ResultSetMetaData rsmd = st.getMetaData(); // 取到数据集的元数据
				int columnCount = rsmd.getColumnCount();
				Map<String, Object> result = new HashMap<String, Object>();
				for (int i = 0; i < columnCount; i++) {
					Object val = rs.getObject(i + 1);
					String key = rsmd.getColumnName(i + 1).toUpperCase();
					result.put(key, val);
				}
				results.add(result);
			}

			return flag ? results : null;

		} catch (SQLException e) {
			throw new RuntimeException("执行sql语句：" + sql + "失败!!!", e);
		} finally {
			close(con, st, rs);
		}
	}

	@SuppressWarnings("resource")
	/**
	 * 对数据库中表进行读取或查询多条的操作
	 * 
	 * @param clazz要返回对象类型的类型
	 * @param sql读取或查询多条的sql语句
	 * @param params读取或查询多条的sql语句中占位符(?)的值
	 * @return list的clazz类型的对象集合， 每个clazz类型的对象对应表中的每一条记录
	 */
	public static <T> List<T> list(Class<T> clazz, String sql, Object... params) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = getConn();
			st = con.prepareStatement(sql);
			setPlaceHodlerValue(st, params);
			rs = st.executeQuery();
			List<T> ts = new ArrayList<T>();
			boolean flag = false;
			while (rs.next()) {
				flag = true;
				try {
					T t = clazz.newInstance();
					Field[] fs = clazz.getDeclaredFields(); // 根据类的类对象
															// ，取到这个类的所有属性对象集合
					for (Field field : fs) {
						String fieldType = field.getType().getName();
						try {
							field.setAccessible(true);
							if (fieldType.intern() == "java.lang.Integer" || fieldType.intern() == "int") {
								field.set(t, rs.getInt(field.getName()));
							} else if (fieldType.intern() == "java.lang.Double" || fieldType.intern() == "double") {
								field.set(t, rs.getDouble(field.getName()));
							} else if (fieldType.intern() == "java.util.Date") {
								field.set(t, new java.util.Date(rs.getTimestamp(field.getName()).getTime()));
							} else if (fieldType.intern() == "java.io.InputStream") {
								InputStream in = rs.getBlob(field.getName()).getBinaryStream();
								ByteArrayOutputStream out = new ByteArrayOutputStream();
								byte[] bs = new byte[1024];
								int len = 0;
								while ((len = in.read(bs)) != -1) {
									out.write(bs, 0, len);
								}
								out.flush();
								field.set(t, new ByteArrayInputStream(out.toByteArray()));
								in.close();
								out.close();
							} else {
								field.set(t, rs.getObject(field.getName()));
							}
						} catch (Exception e) {
						}
					}
					ts.add(t);
				} catch (Exception e) {
					throw new RuntimeException("创建查询返回" + clazz.getName() + "对象失败!!!", e);
				}
			}
			return flag ? ts : null;
		} catch (Exception e) {
			throw new RuntimeException("执行sql语句：" + sql + "失败!!!", e);
		} finally {
			close(con, st, rs);
		}
	}

	/**
	 * 给占位符注值
	 * 
	 * @param st
	 *            执行sql预处理工具对象
	 * @param params增
	 *            ， 删，改 的sql语句中占位符(?)的值
	 * @throws SQLException注值出错
	 */
	private static void setPlaceHodlerValue(PreparedStatement st, Object... params) throws SQLException {
		if (params != null && params.length != 0) {
			for (int i = 0; i < params.length; i++) {
				try {
					if (params[i] instanceof java.util.Date) {
						st.setDate(i + 1, new Date(((java.util.Date) params[i]).getTime()));
					} else if (params[i] instanceof java.io.InputStream) {
						st.setBlob(i + 1, (java.io.InputStream) params[i]);
					} else {
						st.setObject(i + 1, params[i]);
					}
				} catch (SQLException e) {
					throw new SQLException("给第" + (i + 1) + "个占位符注入[" + params[i] + "]值时出错!!!", e);
				}
			}
		}
	}

	/**
	 * 数据库关闭操作
	 * 
	 * @param con数据库连接对象
	 * @param st数据库执行sql语句工具对象
	 * @param rs数据库查询操作返回数据集对象
	 */
	public static void close(Connection con, Statement st, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close(); // 关闭数据集
			} catch (SQLException e) {
				throw new RuntimeException("关闭数据集失败!!!", e);
			}
		}
		if (st != null) {
			try {
				st.close(); // 关闭执行sql语句工具
			} catch (SQLException e) {
				throw new RuntimeException("关闭执行sql语句工具失败!!!", e);
			}
		}
		try {
			if (con != null && !con.isClosed()) {
				try {
					con.close();// 关闭数据库连接
				} catch (SQLException e) {
					throw new RuntimeException("关闭数据库连接失败!!!", e);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("关闭数据库连接失败!!!", e);
		}
	}
}
