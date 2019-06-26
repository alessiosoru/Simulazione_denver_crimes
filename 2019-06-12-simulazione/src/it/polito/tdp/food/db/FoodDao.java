package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenza;


public class FoodDao {

	public List<Food> listAllFood(){
		String sql = "SELECT * FROM food " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_id"),
							res.getInt("food_code"),
							res.getString("display_name"), 
							res.getInt("portion_default"), 
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"),
							res.getDouble("calories")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiment(){
		String sql = "SELECT * FROM condiment " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_id"),
							res.getInt("food_code"),
							res.getString("display_name"), 
							res.getString("condiment_portion_size"), 
							res.getDouble("condiment_calories")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}

	public List<it.polito.tdp.food.model.Condiment> listCondimentByCal(
			Map<Integer, it.polito.tdp.food.model.Condiment> condimentIdMap, Integer calorie) {
		String sql = "SELECT * FROM condiment WHERE condiment_calories< ? " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, calorie);
			
			List<it.polito.tdp.food.model.Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					it.polito.tdp.food.model.Condiment c = new it.polito.tdp.food.model.Condiment(res.getInt("condiment_id"),
							res.getInt("food_code"),
							res.getString("display_name"), 
							res.getString("condiment_portion_size"), 
							res.getDouble("condiment_calories")
							);
//					System.out.println(c.toString());
					list.add(c);
					condimentIdMap.put(c.getFood_code(), c);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}

	public List<Adiacenza> getAdiacenze(Integer calorie) {
		String sql = "SELECT fc1.condiment_food_code AS code1, fc2.condiment_food_code AS code2, COUNT(*) AS cnt " + 
				"FROM food_condiment as fc1, food_condiment as fc2 " + 
				"WHERE fc1.food_code=fc2.food_code AND " + 
				"fc1.condiment_food_code<fc2.condiment_food_code " +
				"GROUP BY fc1.condiment_food_code, fc2.condiment_food_code " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Adiacenza> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Adiacenza a =new Adiacenza(res.getInt("code1"), 
							res.getInt("code2"), res.getInt("cnt"));
					list.add(a);
//					System.out.println(a.toString());
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public int getNumeroCibiIngrediente(Integer condimentFoodCode) {
		String sql = "SELECT COUNT(*) AS cnt " + 
				"FROM food_condiment " + 
				"WHERE condiment_food_code = ? " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, condimentFoodCode);
			
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				try {
					conn.close();
					return res.getInt("cnt");
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return -1;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
}
