package org.javachina.mydb.db.manager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.javachina.mydb.db.DBModel;
import org.javachina.mydb.db.dto.Person;
import org.javachina.mydb.db.sqlmodel.SelectModel;

public class DBManager {
	public void execute(String sql) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException,
			InstantiationException, NoSuchFieldException, SecurityException {
		// insert into person (id,name,age) values(1001,张三,30)
		sql = sql.trim().toLowerCase();
		if (sql.indexOf("insert") == 0) {
			// 执行增加操作
			// 取得表名
			String tableName = sql.substring(sql.indexOf("into") + 4, sql.indexOf("(")).trim();
			// System.out.println("表名："+tableName);
			// 取得属性字符串
			String paramStr = sql.substring(sql.indexOf("(") + 1, sql.indexOf(")")).trim();
			// System.out.println("属性字符串："+paramStr);
			// 取得值字符串

			String valueStr = sql.substring(sql.lastIndexOf("(") + 1, sql.lastIndexOf(")")).trim();
			// System.out.println("值字符串："+valueStr);

			// 取得数据库对象
			DBModel db = null;
			// 第一次的时候可以创建一个新的对象,否则直接从文件读取
			try {
				InputStream is = new FileInputStream("F:/exercisemydb/db2.dat");
				ObjectInputStream ois = new ObjectInputStream(is);
				db = (DBModel) ois.readObject();
			} catch (FileNotFoundException e1) {
				db = DBModel.getInstance();
				;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 取得所有属性
			Class clazz = Class.forName("org.javachina.mydb.db.DBModel");

			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				// System.out.println(field.getName());
				String fieldName = field.getName();
				if (fieldName.contains("Table")) {
					String tableNameIndb = fieldName.substring(0, fieldName.lastIndexOf("Table"));
					if (tableNameIndb.equals(tableName)) {
						// System.out.println(tableNameIndb);
						// 取得请求的表
						field.setAccessible(true);
						ArrayList table = (ArrayList) field.get(db);
						field.setAccessible(false);

						// 取得参数列表
						String[] params = paramStr.split(",");
						// 取得值列表
						String[] values = valueStr.split(",");

						// 创建一个对象，用来装数据，放入数据库。
						Class objPersistClazz = Class.forName("org.javachina.mydb.db.dto."
								+ tableName.substring(0, 1).toUpperCase() + tableName.substring(1, tableName.length()));
						// 创建一个对象实例
						Object objForPersist = objPersistClazz.newInstance();

						for (int i = 0; i < params.length; i++) {
							String param = params[i].trim();
							String value = values[i].trim();
							Field f = objPersistClazz.getDeclaredField(param);
							f.setAccessible(true);
							// 因为参数存在简单类型，所以要进行判断后赋值
							if (f.getType().getName().equals("int")) {
								f.set(objForPersist, Integer.parseInt(value));
							} else if (f.getType().getName().equals("double")) {
								f.set(objForPersist, Double.parseDouble(value));
							} else if (f.getType().getName().equals("boolean")) {
								f.set(objForPersist, Boolean.parseBoolean(value));
							} else {

								f.set(objForPersist, value);
							}
							f.setAccessible(false);
						}
						table.add(objForPersist);
					}
				}
			}

			OutputStream os = null;
			ObjectOutputStream oos = null;

			try {
				os = new FileOutputStream("F:/exercisemydb/db2.dat");
				oos = new ObjectOutputStream(os);
				oos.writeObject(db);
				oos.flush();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (oos != null) {
						oos.close();
					}
					if (os != null) {
						os.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else if (sql.indexOf("update") == 0) {
			// 执行修改操作"

		} else if (sql.indexOf("delete") == 0) {
			// 执行删除操作

		}
	}

//	public ArrayList<Map> executeQuery(String sql)
//			throws IOException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
//		ArrayList<Map> result = new ArrayList<Map>();
//		
//		//解析查询字符串
//		
//		// 从硬盘上载入数据库信息
//		InputStream is = new FileInputStream("F:/exercisemydb/db2.dat");
//		ObjectInputStream ois = new ObjectInputStream(is);
//
//		// 转换成数据库对象
//		DBModel db = (DBModel) ois.readObject();
//
//		Class clazz = Class.forName(db.getClass().getName());
//
//		// 取得所有属性
//		Field[] fields = clazz.getDeclaredFields();
//
//		// 遍历所有属性
//		for (Field field : fields) {
//			field.setAccessible(true);
//			String tName = tableName + "Table";
//			if (tName.equals(field.getName())) {
//				ArrayList table = (ArrayList) field.get(db);
//
//				// 遍历该表
//				for (int i = 0; i < table.size(); i++) {
//					// 取得表中的每个记录
//					Object tableItem = table.get(i);
//
//					// 取得该对象的类模型，目的是取得所有属性
//					Class fieldClazz = Class.forName(tableItem.getClass().getName());
//
//					if (queryString.equals("*")) {
//						// 返回person所有属性
//						if (isWhereExit) {
//							// 如果存在查询条件
//							String[] temp = queryPair.split(" ");
//							//判断是否存在and
//							boolean isAndExist = false;
//							for(String ts:temp){
//								if(ts.trim().equals("and"));
//								isAndExist=true;
//								break;
//							}
//							
//							//切割条件字符串
//							Map<String, String> pairMap = new HashMap<String,String>();
//							
//							if(isAndExist){
//								String[] ps =queryPair.split("and");
//								for (String p : ps) {
//									String[] kv = p.split("=");
//									pairMap.put(kv[0].trim(), kv[1].trim());
//								}
//							}else{
//								String[] kv = queryPair.split("=");
//								pairMap.put(kv[0].trim(), kv[1].trim());
//							}
//							//pairMap有值了
//							
//							//用来放符合条件的值
//							Map<String, Object> map = new HashMap<String,Object>();
//							
//							Field[] targetItemFields = fieldClazz.getDeclaredFields();
//							
//							for (Field item : targetItemFields) {
//								item.setAccessible(true);
//								String fieldName = item.getName();
//								Object value = item.get(tableItem);
//								if(pairMap.containsKey(fieldName)){
//									String mapValue = pairMap.get(fieldName);
//									if(value.getClass().getName().equals("java.lang.Integer")){
//										Integer it = (Integer)value;
//										if(it.intValue()==Integer.parseInt(mapValue)){
//											map.put(fieldName, value);
//										}
//									}else{
//										if(mapValue.equals(value)){
//											map.put(fieldName, value);
//										}
//									}
//								}
//								
//								item.setAccessible(false);
//							}
//		
//							result.add(map);
//						} else {
//							// 没有任何查询条件
//							Field[] targetItemFields = fieldClazz.getDeclaredFields();
//							Map<String, Object> map = new HashMap<String, Object>();
//							for (Field item : targetItemFields) {
//								item.setAccessible(true);
//								String fieldName = item.getName();
//								Object value = item.get(tableItem);
//								map.put(fieldName, value);
//								item.setAccessible(false);
//							}
//							result.add(map);
//						}
//					} else {
//						// 根据查询字符串确定要查询哪些属性
//						if (isWhereExit) {
//
//						} else {
//
//						}
//					}
//
//				}
//
//			}
//			field.setAccessible(false);
//		}
//		return result;
//	}
/*
 * 解析请求字符串
 */
	public SelectModel parseSelectSql(String sql){
				SelectModel model = new SelectModel();
		 		// 规范输入的sql
				sql = sql.toLowerCase().trim();

				// 1.判断是否存在where
				String[] srcs = sql.split(" ");
				boolean isWhereExit = false;
				for (String string : srcs) {
					if (string.equals("where")) {
						isWhereExit = true;
					}
				}
				// System.out.println(isExist?"存在":"不存在");
				// 2. sql解析 （1）取得表名 （2）取得查询的字段名称 （3） 取得查询条件
				// (1).取得表名
				String tableName = null;
				// (3) 取得查询条件
				String queryPair = null;
				if (isWhereExit) {
					tableName = sql.substring(sql.indexOf("from") + 4, sql.indexOf("where")).trim();
					queryPair = sql.substring(sql.indexOf("where") + 5, sql.length());

				} else {
					tableName = sql.substring(sql.indexOf("from") + 4, sql.length()).trim();

				}
				//设置tableName
				model.setTableName(tableName);
				// (2)取查询字符串
				String queryString = sql.substring(6, sql.indexOf("from")).trim();
				
				String[] params = null;
				if(queryString.equals("*")){
					params = new String[]{"*"};
				}else{
					params = queryString.split(",");
					for(int i = 0;i<params.length;i++){
						params[i] = params[i].trim();
					}
				}
				
				//设置请求字符串
				model.setQueryParams(params);
				
				//设置名值对
				Map<String, String> pairMap = new HashMap<String,String>();
				if (isWhereExit) {
					// 如果存在查询条件
					String[] temp = queryPair.split(" ");
					//判断是否存在and
					boolean isAndExist = false;
					for(String ts:temp){
						if(ts.trim().equals("and"));
						isAndExist=true;
						break;
					}
					if(isAndExist){
						String[] ps =queryPair.split("and");
						for (String p : ps) {
							String[] kv = p.split("=");
							pairMap.put(kv[0].trim(), kv[1].trim());
						}
					}else{
						String[] kv = queryPair.split("=");
						pairMap.put(kv[0].trim(), kv[1].trim());
					}
					
				 model.setPairMap(pairMap);
				}else{
					model.setPairMap(null);
				}
				
				return model;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, IllegalArgumentException,
			IllegalAccessException, InstantiationException, NoSuchFieldException, SecurityException, IOException {
		// for(int i=0;i<100;i++){
//		new DBManager().execute("insert into person (id,name,age) values(1001,张三,30)");
//		new DBManager().execute("insert into person (id,name,age) values(1002,格鲁,20)");

		// }
		// DBModel db = null;
		// try {
		// InputStream is = new FileInputStream("F:/exercisemydb/db2.dat");
		// ObjectInputStream ois = new ObjectInputStream(is);
		// db = (DBModel)ois.readObject();
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// ArrayList<Person> lists = db.getPersonTable();
		// int count=0;
		// for (Person person : lists) {
		// count++;
		// System.out.println(person.getId()+":"+person.getName()+":"+person.getAge());
		// }
		// System.out.println(count);
		String sql1 = "select * from Person";
		String sql2 = "select * from Person where id = 1001 and name = 张三 and age=30";
		String sql3 = "select name , age from person where id = 1001";
		SelectModel model = new DBManager().parseSelectSql(sql3);
		System.out.println(model.getTableName());
		System.out.println(model.getPairMap());
		for(int i = 0;i<model.getQueryParams().length;i++){
			System.out.println(model.getQueryParams()[i]);
		}

	}
}
