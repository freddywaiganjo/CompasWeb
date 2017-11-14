package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.compas.dal.OrganizationDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Organization;
import com.compulynx.compas.models.CompasResponse;

public class OrganizationDalImpl implements OrganizationDal {

	private DataSource dataSource;

	public OrganizationDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public CompasResponse UpdateOrganization(Organization organization) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			if (organization.organizationId == 0) {
				if (GetOrganizationByName(organization.organizationName))
				{
					return new CompasResponse(201, "Organization Name Already Exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertOrganization);
				preparedStatement.setString(1, organization.organizationName);
				preparedStatement.setInt(2, organization.createdBy);
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(
						new java.util.Date().getTime()));
			} else {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateOrganization);
				preparedStatement.setString(1, organization.organizationName);
				preparedStatement.setInt(2, organization.createdBy);
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(4, organization.organizationId);
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0)
			{
				return new CompasResponse(201, "Organization Name Already Exists");
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

	public boolean GetOrganizationByName(String organizationName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getOrganizationByName);
			preparedStatement.setString(1, organizationName);

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

	public List<Organization> GetOrganizations() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getOrganizations);

			resultSet = preparedStatement.executeQuery();
			List<Organization> brokers = new ArrayList<Organization>();
			while (resultSet.next()) {
				brokers.add(new Organization(resultSet.getInt("ID"), resultSet
						.getString("OrganizationName"),
						resultSet.getInt("CreatedBy"), 200));
			}
			return brokers;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public Organization GetOrganizationById(int organizationId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getOrganizationById);
			preparedStatement.setInt(1, organizationId);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return new Organization(resultSet.getInt("ID"),
						resultSet.getString("OrganizationName"),
						resultSet.getInt("CreatedBy"), 200);
			} else {
				return new Organization(201);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new Organization(404);
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}


}
