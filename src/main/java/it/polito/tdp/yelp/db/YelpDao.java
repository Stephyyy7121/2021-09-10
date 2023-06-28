package it.polito.tdp.yelp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Review;
import it.polito.tdp.yelp.model.User;

public class YelpDao {
	
	
	public List<Business> getAllBusiness(){
		String sql = "SELECT * FROM Business";
		List<Business> result = new ArrayList<Business>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Business business = new Business(res.getString("business_id"), 
						res.getString("full_address"),
						res.getString("active"),
						res.getString("categories"),
						res.getString("city"),
						res.getInt("review_count"),
						res.getString("business_name"),
						res.getString("neighborhoods"),
						res.getDouble("latitude"),
						res.getDouble("longitude"),
						res.getString("state"),
						res.getDouble("stars"));
				result.add(business);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Review> getAllReviews(){
		String sql = "SELECT * FROM Reviews";
		List<Review> result = new ArrayList<Review>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Review review = new Review(res.getString("review_id"), 
						res.getString("business_id"),
						res.getString("user_id"),
						res.getDouble("stars"),
						res.getDate("review_date").toLocalDate(),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("review_text"));
				result.add(review);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<User> getAllUsers(){
		String sql = "SELECT * FROM Users";
		List<User> result = new ArrayList<User>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				User user = new User(res.getString("user_id"),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("name"),
						res.getDouble("average_stars"),
						res.getInt("review_count"));
				
				result.add(user);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getCity() {
		
		String sql = "SELECT DISTINCT city "
				+ "FROM business "
				+ "ORDER BY city ";
		
		List<String> result = new ArrayList<String>();
		
		Connection connection = DBConnect.getConnection();
		try {
			PreparedStatement st = connection.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				
				result.add(res.getString("city"));
			}
			
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return result;
		
	}
	
	public List<Business> getFiltereBusiness(String c) {
		
		String sql = "SELECT * "
				+ "FROM business "
				+ "WHERE city = ? ";
		
		List<Business> result = new ArrayList<>();
		Connection connection = DBConnect.getConnection();
		try {
			PreparedStatement st = connection.prepareStatement(sql);
			st.setString(1, c);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				
				String businessId = res.getString("business_id");
				String fullAddress = res.getString("full_address");
				String active = res.getString("active");
				String categories = res.getString("categories");
				String city = res.getString("city");
				int reviewCount = res.getInt("review_count");
				String businessName = res.getString("business_name");
				String neighborhoods = res.getString("neighborhoods");
				double latitude= res.getDouble("latitude");
				double longitude = res.getDouble("longitude");
				String state = res.getString("state");
				double stars = res.getDouble("stars");
				
				result.add(new Business(businessId, fullAddress, active, categories, city, reviewCount, businessName, neighborhoods, latitude, longitude, state, stars));
			}
			
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return result;
	}
	
	
}
