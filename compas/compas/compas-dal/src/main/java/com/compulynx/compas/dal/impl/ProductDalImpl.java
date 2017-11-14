package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.sql.DataSource;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import com.compulynx.compas.dal.ProductDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.AgeGroups;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.Area;
import com.compulynx.compas.models.BeneficiaryGroup;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Location;
import com.compulynx.compas.models.Product;
import com.mysql.jdbc.Statement;

public class ProductDalImpl implements ProductDal{
	private DataSource dataSource;

	public ProductDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	@Override
	public CompasResponse UpdateProduct(Product product) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			if (product.productId== 0) {
				if (CheckProductCode(product.productCode,product.merchantId))
				{
					return new CompasResponse(201, "Product Code Already Exists");
				}
				if (CheckProductName(product.productName,product.merchantId))
				{
					return new CompasResponse(201, "Product Name Already Exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertProduct);
				preparedStatement.setString(1, product.productCode);
				preparedStatement.setString(2, product.productName);
				preparedStatement.setInt(3, product.merchantId);
				preparedStatement.setBoolean(4, product.active);
				preparedStatement.setInt(5, product.createdBy);
				preparedStatement.setTimestamp(6, new java.sql.Timestamp(
						new java.util.Date().getTime()));
			} else {
				if (CheckProductNameByCode(product.productName,product.productCode,product.merchantId))
				{
					return new CompasResponse(201, "Product Name Already Exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateProduct);
				preparedStatement.setString(1, product.productName);
				preparedStatement.setInt(2, product.merchantId);
				preparedStatement.setBoolean(3, product.active);
				preparedStatement.setInt(4, product.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(6, product.productId);
				
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0)
			{
				return new CompasResponse(201, "Product Name Already Exists");
			}
			else
			{
				return new CompasResponse(404, "Exception Occured");	
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	@Override
	public List<Product> GetProducts() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getProducts);
			
			resultSet = preparedStatement.executeQuery();
			List<Product> products = new ArrayList<Product>();
			while (resultSet.next()) {
				products.add(new Product(resultSet.getInt("ID"),resultSet
						.getString("product_Code"), resultSet
						.getString("product_Name"),resultSet.getInt("merchant_ID"),resultSet
						.getString("merchantName"),resultSet.getBoolean("Active"),count));
				count++;
			}
			return products;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean CheckProductCode(String productCode,int merchantId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getProductByCode);
			preparedStatement.setString(1, productCode);
			preparedStatement.setInt(2, merchantId);
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
	public boolean CheckProductName(String productName,int merchantId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getProductByName);
			preparedStatement.setString(1, productName);
			preparedStatement.setInt(2, merchantId);
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
	public boolean CheckProductNameByCode(String productName,String productCode,int merchantId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getProductNameByCode);
			preparedStatement.setString(1, productName);
			preparedStatement.setString(2, productCode);
			preparedStatement.setInt(3, merchantId);
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

	@SuppressWarnings("resource")
	public CompasResponse UpdateBnfGroup(BeneficiaryGroup bnfGrp) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);

			if (bnfGrp.bnfGrpId== 0) {
				if (CheckBnfGrpCode(bnfGrp.bnfGrpCode,bnfGrp.productId))
				{
					return new CompasResponse(201, "Beneficicary Group Code Already Exists");
				}
				if (CheckBnfGrpName(bnfGrp.productName,bnfGrp.productId))
				{
					return new CompasResponse(201, "Beneficicary Group Name Already Exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertBnfGrp,Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, bnfGrp.bnfGrpCode);
				preparedStatement.setString(2, bnfGrp.bnfGrpName);
				preparedStatement.setInt(3, bnfGrp.productId);
				preparedStatement.setBoolean(4, bnfGrp.active);
				preparedStatement.setInt(5, bnfGrp.createdBy);
				preparedStatement.setTimestamp(6, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setDouble(7, bnfGrp.houseHoldValue);
				preparedStatement.setInt(8, bnfGrp.minCap);
				preparedStatement.setInt(9, bnfGrp.maxCap);
			} else {
				if (CheckBnfGrpNameByCode(bnfGrp.bnfGrpName,bnfGrp.bnfGrpCode,bnfGrp.productId))
				{
					return new CompasResponse(201, "Beneficicary Group Name Already Exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateBnfGrp);
				
				preparedStatement.setString(1, bnfGrp.bnfGrpName);
				preparedStatement.setInt(2, bnfGrp.productId);
				preparedStatement.setBoolean(3, bnfGrp.active);
				preparedStatement.setInt(4, bnfGrp.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setDouble(6, bnfGrp.houseHoldValue);
				preparedStatement.setInt(7, bnfGrp.minCap);
				preparedStatement.setInt(8, bnfGrp.maxCap);
				preparedStatement.setInt(9, bnfGrp.bnfGrpId);
				
			}
			if(preparedStatement.executeUpdate()>0){
				if (bnfGrp.bnfGrpId == 0) {
					resultSet = preparedStatement.getGeneratedKeys();
					resultSet.next();
					bnfGrp.bnfGrpId = resultSet.getInt(1);
				}
				DBOperations.DisposeSql(preparedStatement);
				preparedStatement=connection.prepareStatement(Queryconstants.insertBnfGrpAgeGroupInfo);
				for(int i=0;i<bnfGrp.ageGroups.size();i++){
					preparedStatement.setInt(1, bnfGrp.bnfGrpId);
					preparedStatement.setInt(2, bnfGrp.ageGroups.get(i).ageGrpId);
					//preparedStatement.setDouble(3, bnfGrp.ageGrpups.get(i).cash);
					preparedStatement.setBoolean(3, bnfGrp.ageGroups.get(i).isActive);
					preparedStatement.setInt(4, bnfGrp.createdBy);
					preparedStatement.setTimestamp(5, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					if (preparedStatement.executeUpdate() <= 0) {
						throw new Exception("Failed to Insert Age Group "
								+ bnfGrp.ageGroups.get(i).ageGrpId);
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
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0)
			{
				return new CompasResponse(201, "Beneficiary Group Name Already Exists");
			}
			else
			{
				return new CompasResponse(404, "Exception Occured");	
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	@Override
	public List<BeneficiaryGroup> GetBnfGroups() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet1 = null;
		int noofbnfs=0;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBnfGrps);
			
			resultSet = preparedStatement.executeQuery();
			
			
			List<BeneficiaryGroup> bnfgrps = new ArrayList<BeneficiaryGroup>();
			while (resultSet.next()) {
				preparedStatement1=connection.prepareStatement(Queryconstants.getNoOfBnfs);
				preparedStatement1.setInt(1, resultSet.getInt("id"));
				resultSet1=preparedStatement1.executeQuery();
				if(resultSet1.next()){
					noofbnfs=resultSet1.getInt("id");
				}
				BeneficiaryGroup  objBnfGrp = new BeneficiaryGroup();
				objBnfGrp.bnfGrpId=resultSet.getInt("ID");
				objBnfGrp.bnfGrpCode=resultSet.getString("bnfgrp_Code");
				objBnfGrp.bnfGrpName=resultSet.getString("bnfgrp_name");
				objBnfGrp.productId=resultSet.getInt("product_id");
				objBnfGrp.productName=resultSet.getString("product_name");
				objBnfGrp.active=resultSet.getBoolean("Active");
				
				preparedStatement1=connection.prepareStatement(Queryconstants.getAgeGrpsByBnfGrps);
				preparedStatement1.setInt(1, objBnfGrp.bnfGrpId);
				resultSet1=preparedStatement1.executeQuery();
				ArrayList<AgeGroups> agegrps= new ArrayList<AgeGroups>(); 
				while(resultSet1.next()){
				AgeGroups objAgeGrp = new AgeGroups();
				objAgeGrp.ageGrpId=resultSet1.getInt("id");
				objAgeGrp.ageGrpDesc=resultSet1.getString("agegrp_name");
				objAgeGrp.isActive=resultSet1.getBoolean("isactive");
				agegrps.add(objAgeGrp);
				}
				objBnfGrp.ageGroups=agegrps;
				objBnfGrp.noOfBnfs=noofbnfs;
				bnfgrps.add(objBnfGrp);
				count++;
			}
			return bnfgrps;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean CheckBnfGrpCode(String bnfGrpCode,int productId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBnfGrpByCode);
			preparedStatement.setString(1, bnfGrpCode);
			preparedStatement.setInt(2, productId);
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
	public boolean CheckBnfGrpName(String bnfGrpName,int productId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBnfGrpByName);
			preparedStatement.setString(1, bnfGrpName);
			preparedStatement.setInt(2, productId);
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
	public boolean CheckBnfGrpNameByCode(String bnfGrpName,String bnfGrpCode,int productId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBnfGrpNameByCode);
			preparedStatement.setString(1, bnfGrpName);
			preparedStatement.setString(2, bnfGrpCode);
			preparedStatement.setInt(3, productId);
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
	public List<AgeGroups> GetAgeGroups() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAgeGroups);
			resultSet = preparedStatement.executeQuery();
			List<AgeGroups> ageGrps = new ArrayList<AgeGroups>();
			while (resultSet.next()) {
				AgeGroups objAgeGrps= new AgeGroups();
				objAgeGrps.ageGrpId=resultSet.getInt("ID");
				objAgeGrps.ageGrpDesc=resultSet.getString("agegrp_name");
				ageGrps.add(objAgeGrps);
				count++;
			}
			return ageGrps;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Location> GetAgeGroupsByBnfGrp(int bnfgrpId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement1=connection.prepareStatement(Queryconstants.getLocations);
			resultSet1=preparedStatement1.executeQuery();
			ArrayList<Location> locations= new ArrayList<Location>();
			while(resultSet1.next()){
				boolean isactive =false;
				Location objLoc= new Location();
				objLoc.locationId=resultSet1.getInt("id");
				objLoc.locationName=resultSet1.getString("location_name");
			//	objLoc.isActive=false;
				preparedStatement = connection
						.prepareStatement(Queryconstants.getCashDetialsByLoc);
				preparedStatement.setInt(1, bnfgrpId);
				preparedStatement.setInt(2, objLoc.locationId);
				preparedStatement.setInt(3, bnfgrpId);
				preparedStatement.setInt(4, objLoc.locationId);
				resultSet = preparedStatement.executeQuery();
				List<AgeGroups> ageGrps = new ArrayList<AgeGroups>();
				
				while (resultSet.next()) {
					AgeGroups objAgeGrps= new AgeGroups();
					objAgeGrps.ageGrpId=resultSet.getInt("ID");
					objAgeGrps.ageGrpDesc=resultSet.getString("agegrp_name");
					objAgeGrps.cash=resultSet.getDouble("cash");
					 isactive =resultSet.getBoolean("isactive");
					ageGrps.add(objAgeGrps);
					count++;
				}
				objLoc.isActive=isactive;
				objLoc.ageGroups=ageGrps;
				locations.add(objLoc);
			}
			
			
			return locations;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	public CompasResponse UpdateLocAgeGrp(BeneficiaryGroup bnfgrp) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);

			preparedStatement=connection.prepareStatement(Queryconstants.deleteLocCashDtl);
			preparedStatement.setInt(1, bnfgrp.bnfGrpId);
			preparedStatement.executeUpdate();
				DBOperations.DisposeSql(preparedStatement);
				preparedStatement=connection.prepareStatement(Queryconstants.insertLocCashAgeGrp);
				for(int i=0;i<bnfgrp.locations.size();i++){
					preparedStatement.setInt(1, bnfgrp.bnfGrpId);
					preparedStatement.setInt(2,bnfgrp.locations.get(i).locationId);
					preparedStatement.setBoolean(3, bnfgrp.locations.get(i).isActive);
					for(int j=0;j<bnfgrp.locations.get(i).ageGroups.size();j++){
						preparedStatement.setInt(4, bnfgrp.locations.get(i).ageGroups.get(j).ageGrpId);
						preparedStatement.setDouble(5, bnfgrp.locations.get(i).ageGroups.get(j).cash);
						preparedStatement.setInt(6, bnfgrp.createdBy);
						preparedStatement.setTimestamp(7, new java.sql.Timestamp(
								new java.util.Date().getTime()));
						if (preparedStatement.executeUpdate() <= 0) {
							throw new Exception("Failed to Insert Age Group "
									+ bnfgrp.ageGroups.get(i).ageGrpId);
						}
					}
					
					
					
				}
				
				connection.commit();
				return new CompasResponse(200, "Records Updated");

		
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0)
			{
				return new CompasResponse(201, "Beneficiary Group Name Already Exists");
			}
			else
			{
				return new CompasResponse(404, "Exception Occured");	
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

}
