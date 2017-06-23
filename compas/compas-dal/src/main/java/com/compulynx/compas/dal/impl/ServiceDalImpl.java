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

import javax.print.attribute.standard.Severity;
import javax.sql.DataSource;

import com.compulynx.compas.dal.ServiceDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.AgeGroups;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Currency;
import com.compulynx.compas.models.Device;
import com.compulynx.compas.models.Location;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.Params;
import com.compulynx.compas.models.ServiceDtlVO;
import com.compulynx.compas.models.StringUtil;
import com.compulynx.compas.models.Subservice;
import com.compulynx.compas.models.Uom;
import com.compulynx.compas.models.Voucher;

/**
 * @author Anita
 *
 */
public class ServiceDalImpl implements ServiceDal {
	private DataSource dataSource;

	public ServiceDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public boolean CheckServiceCode(String serviceCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getServiceByCode);
			preparedStatement.setString(1, serviceCode);

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
	public boolean CheckServiceName(String serviceName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getServiceByName);
			preparedStatement.setString(1, serviceName);

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
	public boolean CheckServiceNameByCode(String serviceName,String serviceCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getServiceNameByCode);
			preparedStatement.setString(1, serviceName);
			preparedStatement.setString(2, serviceCode);
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
	public CompasResponse UpdateService(Service service) {
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			if (service.serviceId == 0) {
				if (CheckServiceCode(service.serviceCode)) {
					return new CompasResponse(201, "Service Code Already Exists");
				}
				if (CheckServiceName(service.serviceName)) {
					return new CompasResponse(201, "Service Name Already Exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertServiceDetails);
				preparedStatement.setString(1, service.serviceCode);
				preparedStatement.setString(2, service.serviceName);
				preparedStatement.setString(3, service.compoType);
				preparedStatement.setString(4, service.compoName);
				preparedStatement.setBoolean(5, service.active);
				preparedStatement.setInt(6, service.createdBy);
				preparedStatement.setTimestamp(7, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(8, service.categoryId);
				preparedStatement.setString(9, service.image);
				preparedStatement.setString(10, service.serviceDesc);
				preparedStatement.setDouble(11, service.minPrice);
				preparedStatement.setDouble(12, service.maxPrice);
			} else {
				if (CheckServiceNameByCode(service.serviceName,service.serviceCode)) {
					return new CompasResponse(201, "Service Name Already Exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateServiceDetails);
				preparedStatement.setString(1, service.serviceName);
				preparedStatement.setString(2, service.compoType);
				preparedStatement.setString(3, service.compoName);
				preparedStatement.setBoolean(4, service.active);
				preparedStatement.setInt(5, service.createdBy);
				preparedStatement.setTimestamp(6, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(7, service.categoryId);
				preparedStatement.setString(8, service.image);
				preparedStatement.setString(9, service.serviceDesc);
				preparedStatement.setDouble(10, service.minPrice);
				preparedStatement.setDouble(11, service.maxPrice);
				preparedStatement.setInt(12, service.serviceId);
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Service Name Already Exists");
			} else {
				return new CompasResponse(202, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(202, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Service> GetAllServices() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getServices);

			resultSet = preparedStatement.executeQuery();
			List<Service> services = new ArrayList<Service>();
			while (resultSet.next()) {
				services.add(new Service(resultSet.getInt("ID"), resultSet
						.getString("ServiceCode"), resultSet
						.getString("ServiceName"), resultSet
						.getBoolean("Active"), resultSet.getInt("CreatedBy"),
						200,resultSet.getString("componame"),resultSet.getString("compotype"),count,
						resultSet.getInt("category_Id"),resultSet.getString("category_name"),
						resultSet.getString("image"),resultSet.getString("serviceDesc"),resultSet.getDouble("min_price"),resultSet.getDouble("max_price")));
				count++;
			}
			return services;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	
	public List<Params> GetAllParams() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getParams);

			resultSet = preparedStatement.executeQuery();
			List<Params> params = new ArrayList<Params>();
			while (resultSet.next()) {
				params.add(new Params(resultSet.getInt("ID"), resultSet
						.getString("ParamName"),
						resultSet.getBoolean("Active"), resultSet
								.getInt("CreatedBy"), 200,count));
				count++;
			}
			return params;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	public CompasResponse UpdateParam(Params param) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			
			if (param.paramId == 0) {
				if (checkParamName(param.paramName)) {
					return new CompasResponse(201, "Param Name Already Exists");
				}

				preparedStatement = connection
						.prepareStatement(Queryconstants.insertParamInfo);
				preparedStatement.setString(1, param.paramName);
				preparedStatement.setBoolean(2, param.active);
				preparedStatement.setInt(3, param.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
			} else {
				

				preparedStatement = connection
						.prepareStatement(Queryconstants.updateParamInfo);
				preparedStatement.setString(1, param.paramName);
				preparedStatement.setBoolean(2, param.active);
				preparedStatement.setInt(3, param.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(5, param.paramId);
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Param name Already Exists");
			} else {
				return new CompasResponse(202, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(202, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection,preparedStatement,resultSet);
		}
	}
	public boolean checkParamName(String paramName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getParamName);
			preparedStatement.setString(1, paramName);

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
	
	public List<Subservice> getAllSubservices(int page) {
		String sql = "\n" +
                "SELECT TOP 10* FROM (SELECT id,0 as parent_type,ParentServiceId as parent_id,SubserviceName as ser_name,level, CASE active WHEN '1' THEN 'ACTIVE' ELSE  'INACTIVE' END AS ACTIVE\n" +
                "  FROM SubServiceMap) a\n" +
//                " LEFT OUTER JOIN (SELECT TOP "+page+" ParentServiceId as k,SubserviceName as a,\n" +
                " LEFT OUTER JOIN (SELECT TOP "+page+" id as k,SubserviceName as a,\n" +
                "CASE active WHEN '1' THEN 'ACTIVE' ELSE  'INACTIVE' END AS d FROM SubServiceMap) t\n" +
                "ON t.k= a.id WHERE t.k IS NULL ";

       System.out.println("getAllSubservices##"+"\t"+page);

       System.out.println("GetSubServices##"+sql);

		List<Subservice> subservices = null;
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				){
			ResultSet rs = stmt.executeQuery();

			rs.last();

			if(rs.getRow() > 0) {
				Subservice subservice = null;
				subservices = new ArrayList<Subservice>();

				rs.beforeFirst();
				while(rs.next()) {
					subservice = new Subservice();
					subservice.setId(rs.getInt("id"));
					subservice.setName(rs.getString("ser_name"));
					subservice.setParentId(rs.getInt("parent_id"));
					subservice.setParentType(rs.getInt("parent_type"));
					subservice.setLevel(rs.getInt("level"));

					subservices.add(subservice);
				}

				return subservices;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	public List<ServiceDtlVO> getAllServiceDtls(int page) {
//	        String sql="SELECT distinct TOP 10* FROM (SELECT  m.Id as ser_id, m.ServiceValue as fee,md.serviceid, (SELECT LEFT(paramid, LEN(paramid) - 1)\n" +
//	                "FROM (\n" +
//	                "    SELECT distinct CONVERT(VARCHAR, ParamId)  + ','\n" +
//	                "    FROM ParamDetails where ServiceId=md.ServiceId and Isactive=1\n" +
//	                "    FOR XML PATH ('')\n" +
//	                "  ) c (paramid)) as params FROM  ParamDetails md,SubServiceMap m where md.Isactive=1 and m.ParentServiceId=md.ServiceId) a\n" +
//	                " LEFT OUTER JOIN (SELECT TOP "+page+" m.Id as k,m.id as a FROM  ParamDetails md,SubServiceMap m \n" +
//	                " where md.Isactive=1 and m.ParentServiceId=md.ServiceId) t\n" +
//	                "ON t.k= a.ser_id WHERE t.k IS NULL ";

//	        String sql="SELECT distinct TOP 10* FROM (SELECT  m.Id as ser_id, m.ServiceValue as fee,md.serviceid, (SELECT LEFT(paramid, LEN(paramid) - 1)\n" +
//	                "FROM (\n" +
//	                "    SELECT distinct CONVERT(VARCHAR, ParamId)  + ','\n" +
//	                "    FROM ParamDetails where ServiceId=md.ServiceId and Isactive=1\n" +
//	                "    FOR XML PATH ('')\n" +
//	                "  ) c (paramid)) as params FROM  ParamDetails md,SubServiceMap m where  m.ParentServiceId=md.ServiceId) a\n" +
//	                " LEFT OUTER JOIN (SELECT TOP "+page+" m.Id as k,m.id as a FROM  ParamDetails md,SubServiceMap m \n" +
//	                " where m.ParentServiceId=md.ServiceId) t\n" +
//	                "ON t.k= a.ser_id WHERE t.k IS NULL ";

	        String sql="SELECT distinct TOP 10* FROM (SELECT  m.Id as ser_id, m.ServiceValue as fee,md.serviceid,\n" +
	                " (SELECT LEFT(paramid, LEN(paramid) - 1)\n" +
	                "FROM (\n" +
	                "    SELECT distinct CONVERT(VARCHAR, ParamId)  + ','\n" +
	                "    FROM ParamDetails where ServiceId=md.ServiceId and Isactive=1\n" +
	                "    FOR XML PATH ('')\n" +
	                "  ) c (paramid)) as params FROM  ParamDetails md,SubServiceMap m where m.ParentServiceId=md.ServiceId) a\n" +
	                " LEFT OUTER JOIN (SELECT distinct TOP "+page+" m.Id as k,m.id as a FROM  ParamDetails md,SubServiceMap m \n" +
	                " where  m.ParentServiceId=md.ServiceId ) t\n" +
	                "ON t.k= a.ser_id WHERE t.k IS NULL ";

	        System.out.println("GetAllServiceDtls##"+sql);
			try (
					Connection conn = dataSource.getConnection();
					PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					){

				ResultSet rs = stmt.executeQuery();

				if(rs.last() && rs.getRow() > 0) {
					ServiceDtlVO servicedtl;
					List<ServiceDtlVO> servicedtls = new ArrayList<ServiceDtlVO>();
					rs.beforeFirst();
					while(rs.next()) {
						servicedtl = new ServiceDtlVO();

						servicedtl.setServiceId(rs.getInt("ser_id"));
						servicedtl.setCharge(String.valueOf(rs.getInt("fee")));
						servicedtl.setParam(StringUtil.hasValue(rs.getString("params")) ? rs.getString("params") : "-1");
						servicedtls.add(servicedtl);
	                   System.out.println("ParamValue##"+servicedtl.getParam());
					}

					
					return servicedtls;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return null;
		}
	public List<Params> getParams(int market, int page) {
		System.out.println("Start : getParams");
		String sql = "SELECT TOP 10 * FROM (SELECT id,ParamName as name,'ALPHA' AS param_type, CASE active WHEN '1' THEN 'ACTIVE' ELSE  'INACTIVE' END AS ACTIVE\n" +
                "  FROM ParamMaster) a\n" +
                " LEFT OUTER JOIN (SELECT TOP "+page+" Id as k,ParamName as a,\n" +
                "CASE active WHEN '1' THEN 'ACTIVE' ELSE  'INACTIVE' END AS d FROM ParamMaster) t\n" +
                "ON t.k= a.Id WHERE t.k IS NULL ";

		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				){
			//stmt.setInt(1, market);

			ResultSet rs = stmt.executeQuery();

			rs.last();

			if(rs.getRow() > 0) {
				
				List<Params> params = new ArrayList<Params>();
				rs.beforeFirst();

				while(rs.next()) {
					Params param = new Params();
					param.paramId=rs.getInt("id");
					param.paramName=rs.getString("name");
					param.paramType= "ALPHA";

					params.add(param);
				}

				System.out.println("End : getParams");
				return params;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
		return null;
	}
	public List<Currency> GetCurrencies() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCurrencies);

			resultSet = preparedStatement.executeQuery();
			List<Currency> services = new ArrayList<Currency>();
			while (resultSet.next()) {
				services.add(new Currency(resultSet.getInt("ID"), resultSet
						.getString("curr_code")));
			}
			return services;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	public List<Uom> GetUoms() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getUoms);

			resultSet = preparedStatement.executeQuery();
			List<Uom> uoms = new ArrayList<Uom>();
			while (resultSet.next()) {
				uoms.add(new Uom(resultSet.getInt("ID"), resultSet
						.getString("uom_abbr")));
			}
			return uoms;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	public List<Voucher> GetVoucherServices(int programmeId,int locationId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement1=connection.prepareStatement(Queryconstants.getVoucherServices);
			preparedStatement1.setInt(1, programmeId);
			preparedStatement1.setInt(2, locationId);
			preparedStatement1.setInt(3, programmeId);
			preparedStatement1.setInt(4, locationId);
			resultSet1=preparedStatement1.executeQuery();
			ArrayList<Voucher> locations= new ArrayList<Voucher>();
			while(resultSet1.next()){
				boolean isactive =false;
				Voucher objLoc= new Voucher();
				objLoc.voucherId=resultSet1.getInt("voucherId");
				objLoc.voucherDesc=resultSet1.getString("voucher_name");
			//	objLoc.isActive=false;
				preparedStatement = connection
						.prepareStatement(Queryconstants.getServicesForPriceConfig);
				preparedStatement.setInt(1, objLoc.voucherId);
				preparedStatement.setInt(2, locationId);
				preparedStatement.setInt(3, objLoc.voucherId);
				preparedStatement.setInt(4, locationId);
				resultSet=preparedStatement.executeQuery();
				List<Service> ageGrps = new ArrayList<Service>();
				
				while (resultSet.next()) {
					Service objAgeGrps= new Service();
					objAgeGrps.serviceId=resultSet.getInt("Service_Id");
					objAgeGrps.serviceName=resultSet.getString("serviceName");
					objAgeGrps.unit=resultSet.getDouble("unit");
					objAgeGrps.minPrice=resultSet.getDouble("min_price");
					objAgeGrps.maxPrice=resultSet.getDouble("max_price");
					//objAgeGrps.cash=resultSet.getDouble("cash");
					// isactive =resultSet.getBoolean("isactive");
					ageGrps.add(objAgeGrps);
					count++;
				}
				objLoc.isActive=isactive;
				objLoc.services=ageGrps;
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
	
	public CompasResponse UpdatePriceConfig(Programme prg) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement=connection.prepareStatement(Queryconstants.deletePriceConfig);
			preparedStatement.setInt(1, prg.serviceId);
			preparedStatement.executeUpdate();
			DBOperations.DisposeSql(preparedStatement);
			
				for(int j=0;j<prg.services.size();j++){
					preparedStatement=connection.prepareStatement(Queryconstants.insertPriceConfig);
					preparedStatement.setInt(1, prg.locationId);
					//preparedStatement.setInt(2, prg.vouchers.get(i).voucherId);
					preparedStatement.setInt(2, prg.serviceId);
					preparedStatement.setString(3, prg.services.get(j).uom);
					preparedStatement.setDouble(4, prg.services.get(j).quantity);
					preparedStatement.setDouble(5, prg.services.get(j).maxPrice);
					preparedStatement.setInt(6, prg.createdBy);
					preparedStatement.setTimestamp(7, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					if (preparedStatement.executeUpdate() <= 0) {
						throw new Exception("Failed to insert Details "
								);
					}
				
			}
			
			connection.commit();
			return new CompasResponse(200, "Records Updated");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Param name Already Exists");
			} else {
				return new CompasResponse(202, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(202, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection,preparedStatement,resultSet);
		}
	}
}
