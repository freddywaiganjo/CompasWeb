/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.compas.dal.BasketDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Basket;
import com.compulynx.compas.models.BasketDetail;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.RightsDetail;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.UserGroup;

/**
 * @author Anita
 *
 */
public class BasketDalImpl implements BasketDal{

	private DataSource dataSource;

	public BasketDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	public boolean checkBasketByName(String basketName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBasketByName);
			preparedStatement.setString(1, basketName);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}


	public CompasResponse UpdateBasket(Basket basket) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int basketId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);

			if (basket.basketId == 0) {
				if (!checkBasketByName(basket.basketName)) {
					preparedStatement = connection.prepareStatement(
							Queryconstants.insertBaskets,
							Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, basket.basketCode);
					preparedStatement.setString(2, basket.basketName);
					preparedStatement.setBoolean(3,basket.active);
					preparedStatement.setDouble(4, basket.basketValue);
					preparedStatement.setInt(5, basket.createdBy);
					preparedStatement.setTimestamp(6, new java.sql.Timestamp(
							new java.util.Date().getTime()));

				} else {
					return new CompasResponse(201,
							"Basket Name is Already Exists");
				}
			} else {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateBaskets);
				preparedStatement.setString(1, basket.basketCode);
				preparedStatement.setString(2,basket.basketName);
				preparedStatement.setBoolean(3, basket.active);
				preparedStatement.setDouble(4, basket.basketValue);
				preparedStatement.setInt(5,basket.createdBy);
				preparedStatement.setTimestamp(6, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(7, basket.basketId);
				basketId = basket.basketId;

			}
			if (preparedStatement.executeUpdate() > 0) {

				// Dispose
				if (basket.basketId == 0) {
					rs = preparedStatement.getGeneratedKeys();
					rs.next();
					basketId = rs.getInt(1);
				}
				DBOperations.DisposeSql(preparedStatement, rs);
				preparedStatement = connection
						.prepareStatement(Queryconstants.deleteBasketServices);
				preparedStatement.setInt(1, basketId);
				preparedStatement.executeUpdate();

				DBOperations.DisposeSql(preparedStatement);
				// insert rights
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertBasketServices);
				for (int i = 0; i < basket.services.size(); i++) {
					preparedStatement.setInt(1,basketId );
					preparedStatement.setInt(2, basket.services.get(i).serviceId);
					preparedStatement.setDouble(3,
							basket.services.get(i).quantity);
					preparedStatement.setDouble(4,
							basket.services.get(i).price);
					preparedStatement.setInt(5, basket.services.get(i).createdBy);
					preparedStatement.setTimestamp(6, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					if (preparedStatement.executeUpdate() <= 0) {
						throw new Exception("Failed to Insert Right Id "
								+ basket.services.get(i).serviceId);
					}
				}
				connection.commit();
				return new CompasResponse(200, "Records Updated");

			} else {
				connection.rollback();
				return new CompasResponse(202, "Nothing To Update");
			}

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Basket Name is Already Exists");
			} else {
				return new CompasResponse(404, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(rs);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	
	public List<Basket> GetAllBaskets() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBaskets);

			resultSet = preparedStatement.executeQuery();
			List<Basket> baskets = new ArrayList<Basket>();
			while (resultSet.next()) {
				Basket objBasket = new Basket();
				objBasket.basketId = resultSet.getInt("ID");
				objBasket.basketCode=resultSet.getString("BasketCode");
				objBasket.basketName = resultSet.getString("BasketName");
				objBasket.active = resultSet.getBoolean("BasketStatus");
				objBasket.basketValue=resultSet.getDouble("Basketvalue");
				preparedStatement = connection
						.prepareStatement(Queryconstants.getBasketServiceById);
				preparedStatement.setInt(1, objBasket.basketId);

				resultSet2 = preparedStatement.executeQuery();
				List<BasketDetail> services = new ArrayList<BasketDetail>();
				while (resultSet2.next()) {
					services.add(new BasketDetail(
									resultSet2.getInt("Id"),
									resultSet2.getInt("serviceId"),
									resultSet2.getString("serviceName"),
									resultSet2.getDouble("quantity"),
									resultSet2.getDouble("price"),
									 200));
				}
				objBasket.services = services;
				baskets.add(objBasket);
			}
			return baskets;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(resultSet2);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	
	public Basket GetBasketById(int basketId) {
		// TODO Auto-generated method stub
		return null;
	}

}
