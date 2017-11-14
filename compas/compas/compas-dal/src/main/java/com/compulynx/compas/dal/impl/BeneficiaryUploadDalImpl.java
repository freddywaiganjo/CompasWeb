/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Logger;

import javax.sql.DataSource;
import javax.swing.text.html.HTMLDocument.Iterator;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.compulynx.compas.dal.BeneficicaryUploadDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Customer;
import com.compulynx.compas.models.MemberCard;

/**
 * @author Anita
 *
 */
public class BeneficiaryUploadDalImpl implements BeneficicaryUploadDal {
	private static final Logger logger = Logger
			.getLogger(BeneficiaryUploadDalImpl.class.getName());
	private DataSource dataSource;
	private static final String CHAR_LIST =
	        "123456789";
	    private static final int RANDOM_STRING_LENGTH = 4;
	public BeneficiaryUploadDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	 /**
	    * This method generates random string
	    * @return
	    */
	   public static String generateRandomString(){
	       StringBuffer randStr = new StringBuffer();
	       for(int i=0; i<RANDOM_STRING_LENGTH; i++){
	           int number = getRandomNumber();
	           char ch = CHAR_LIST.charAt(number);
	           randStr.append(ch);
	       }
	       System.out.println("String##"+randStr);
	       return randStr.toString();
	   }
	   private static int getRandomNumber() {
	       int randomInt = 0;
	       Random randomGenerator = new Random();
	       randomInt = randomGenerator.nextInt(CHAR_LIST.length());
	       if (randomInt - 1 == -1) {
	           return randomInt;
	       } else {
	           return randomInt - 1;
	       }
	   }
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.compulynx.compas.dal.BeneficicaryUploadDal#UploadAccount(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public CompasResponse UploadAccount(String filePath, String uploadedBy) {
		List<Customer> list = new ArrayList<Customer>();
		List<Customer> toUpload = new ArrayList<Customer>();
		try {
			list = getAccountsExcel(filePath, uploadedBy);

			for (Customer detail : list) {

				// if(validateSupplierCode(detail.supplierCode.trim(),0).respCode==202){
				// continue;
				// } else{
				toUpload.add(detail);
				// }

			}
			System.out.println("List##" + list.size());
			if (toUpload.size() > 0) {
				for (Customer detail : toUpload) {

					if (UpdateImportBeneficiary(detail).respCode != 200) {
						continue;
					}
				}

				return new CompasResponse(200, "Uploaded Successfully");
			} else {
				return new CompasResponse(201, "Oops! No Records to upload");
			}

		} catch (Exception ex) {
			return new CompasResponse(201,
					"Server Error occurred, Please try again");
		}
	}

	private ArrayList<Customer> getAccountsExcel(String pathToFile,
			String createdBy) throws IOException {
		ArrayList<Customer> bnfObjs = new ArrayList<Customer>();

		try {

			System.out.println("FileNameInUploadExcel##" + pathToFile);
			FileInputStream file = new FileInputStream(new File(pathToFile));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			// XSSFSheet sheet = workbook.getSheetAt(0);
			workbook.getNumberOfSheets();
			XSSFSheet sheet = workbook.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			System.out.println("Number of rows>>" + rows);
			DataFormatter df = new DataFormatter();
			for (int r = 1; r < rows; r++) {
				System.out.println("Row>>" + r);
				XSSFRow row = sheet.getRow(r);
				XSSFCell cell;
				// cell= row.getCell(0);
				// System.out.println("cell0>>"+cell);

				Customer beneficiary = new Customer();
				beneficiary.memberId = 0;
				System.out.println("cell##"
						+ df.formatCellValue(row.getCell(0)).trim());
				// if(df.formatCellValue(row.getCell(0)).trim().equals("")){
				//
				// continue;
				// } else{
				// cua.supplierCode
				// =df.formatCellValue(row.getCell(0)).trim().replaceAll("\\D+","").trim();
				// trans.customerId =
				// df.formatCellValue(row.getCell(0)).trim().replaceAll("\\D+","").trim();
				beneficiary.firstName = df.formatCellValue(row.getCell(0));
				// beneficiary.surName = df.formatCellValue(row.getCell(1));
				beneficiary.dateOfBirth = df.formatCellValue(row.getCell(1));
				beneficiary.idPassPortNo = df.formatCellValue(row.getCell(2));
				beneficiary.gender = df.formatCellValue(row.getCell(3));
				beneficiary.height= df.formatCellValue(row.getCell(4));
				//String familysize = df.formatCellValue(row.getCell(5));
				//beneficiary.familySize = Integer.valueOf(familysize);
				beneficiary.createdBy = Integer.valueOf(createdBy);
				beneficiary.bnfGrpId = Integer.valueOf(df.formatCellValue(row
						.getCell(5)));
				beneficiary.programmeId = Integer.valueOf(df
						.formatCellValue(row.getCell(6)));
				beneficiary.locationId= Integer.valueOf(df
						.formatCellValue(row.getCell(7)));
				beneficiary.branchId= Integer.valueOf(df
						.formatCellValue(row.getCell(8)));
				bnfObjs.add(beneficiary);
				// }

			}
			System.out.println("Number of rows##" + rows);
			file.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bnfObjs;
	}

	public CompasResponse ValidateCustomerCode(String supplierCode, int id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			logger.info("Beneficicary Code Validate: " + supplierCode);
			connection = dataSource.getConnection();
			// preparedStatement =
			// connection.prepareStatement(Queryconstants.validateSupplierCode);
			preparedStatement.setString(1, supplierCode);
			preparedStatement.setInt(2, id);
			resultSet = preparedStatement.executeQuery();
			int count = 0;
			if (resultSet.next()) {
				count = resultSet.getInt("count");
				logger.info("This is count>>" + count);
				if (count > 0) {
					return new CompasResponse(202,
							"Beneficiary Code is Already Taken");
				}
			}
			return new CompasResponse(200, "Code valid");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(201,
					"Server Error occurred, Please try again");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public boolean GetMemberByNo(String memberNo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkMemberexists);
			preparedStatement.setString(1, memberNo);

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
	public long generateNumber() {
		long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
		return number;// (long) (Math.random() * 100000 + 4444300000L);
	}
	public CompasResponse UpdateImportBeneficiary(Customer beneficiary) {

        logger.info("Creating/Updating the Beneficiary>>"+beneficiary.firstName+"  "+beneficiary.surName);
        Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		int beneficiaryId=0;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;

		try {
			connection = dataSource.getConnection();
           // connection.setAutoCommit(false);

            //if (GetMemberByNo(beneficiary.idPassPortNo)) {
			
					//	return new CompasResponse(201,
								//"Beneficiary Already Exists With Entered UHNCR ID");
			//} else{
          
                logger.info("Beneficiary==0 >>"+"benefifciaryCreation");
                preparedStatement = connection.prepareStatement(Queryconstants.insertMemberMaster,Statement.RETURN_GENERATED_KEYS);
            	preparedStatement.setString(1, beneficiary.memberNo);
                preparedStatement.setString(2, beneficiary.surName);
				preparedStatement.setString(3, beneficiary.title);
				preparedStatement.setString(4, beneficiary.firstName);
				preparedStatement.setString(5, beneficiary.otherName);
				preparedStatement.setInt(6, 0);
				preparedStatement.setInt(7, 0);
				preparedStatement.setString(8, beneficiary.idPassPortNo);
				preparedStatement.setString(9, beneficiary.gender);
				// System.out.println("memberdob" + member.dateOfBirth);
				preparedStatement.setString(10, beneficiary.dateOfBirth);
				preparedStatement.setString(11, beneficiary.maritalStatus);
				preparedStatement.setString(12, beneficiary.nhifNo);
				preparedStatement.setString(13, beneficiary.height);
				preparedStatement.setString(14, beneficiary.weight);
				preparedStatement.setString(15, beneficiary.employerName);
				preparedStatement.setString(16, beneficiary.occupation);
				preparedStatement.setString(17, beneficiary.nationality);
				preparedStatement.setString(18, beneficiary.postalAdd);
				preparedStatement.setString(19, beneficiary.physicalAdd);
				preparedStatement.setString(20, beneficiary.homeTelephone);
				preparedStatement.setString(21, beneficiary.officeTelephone);
				preparedStatement.setString(22, beneficiary.cellPhone);
				preparedStatement.setString(23, beneficiary.email);
				preparedStatement.setString(24, beneficiary.memberPic);
				preparedStatement.setInt(25, beneficiary.createdBy);
				preparedStatement.setTimestamp(26, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(27, "N");
				preparedStatement.setInt(28, beneficiary.familySize);
				preparedStatement.setInt(29, beneficiary.bnfGrpId);
				preparedStatement.setString(30, "N");
				preparedStatement.setInt(31, beneficiary.locationId);
				preparedStatement.setInt(32, beneficiary.branchId);
				preparedStatement.setBoolean(33, true);
				
//              System.out.println("Stmt$$"+preparedStatement.toString());

                int res =0;
                res = preparedStatement.executeUpdate();
                if (res <= 0) {

                //	connection.rollback();
    				return new CompasResponse(202, "Nothing To Update");
        			
                }else{
                	// Dispose
    			
    					resultSet = preparedStatement.getGeneratedKeys();
    					resultSet.next();
    					beneficiaryId = resultSet.getInt(1);
    					beneficiary.memberId=beneficiaryId;
    					beneficiary.cardNumber= "444088" + generateNumber();
    					beneficiary.pin=generateRandomString();
    				DBOperations.DisposeSql(preparedStatement, resultSet);
    				
    				if(UpdateCardIssuance(beneficiary).respCode==200){
    				
    				preparedStatement = connection
    						.prepareStatement(Queryconstants.deleteCustomerProgramme);
    				preparedStatement.setInt(1, beneficiaryId);
    				preparedStatement.executeUpdate();

    				DBOperations.DisposeSql(preparedStatement);
    				// insert rights
    				preparedStatement = connection
    						.prepareStatement(Queryconstants.insertCustomerProgramme);
    			
    					preparedStatement.setInt(1, beneficiaryId);
    					preparedStatement.setInt(2,
    							beneficiary.programmeId);
    					preparedStatement.setDouble(3,
    							0);
    					preparedStatement.setBoolean(4,
    							true);
    					preparedStatement.setInt(5,
    							beneficiary.createdBy);
    					preparedStatement.setTimestamp(6, new java.sql.Timestamp(
    							new java.util.Date().getTime()));
    					if (preparedStatement.executeUpdate() <= 0) {
    						throw new Exception("Failed to Insert Programme Id "
    								+ beneficiary.programmeId);
    					}
    					
    					DBOperations.DisposeSql(preparedStatement);
    					
    					preparedStatement=connection.prepareStatement(Queryconstants.getAgeGroupsByBnfGrp);
    					resultSet1=preparedStatement.executeQuery();
    					while(resultSet1.next()){
    						//insert beneficiary age group detials
    						preparedStatement1=connection.prepareStatement(Queryconstants.insertBnfHHdetials);
    						preparedStatement.setInt(1, beneficiary.memberId);
    						preparedStatement.setInt(2, beneficiary.bnfGrpId);
    						preparedStatement1.setInt(3, resultSet1.getInt("agegrp_id"));
    						preparedStatement1.setInt(4, 0);
    						preparedStatement1.setInt(5,
        							beneficiary.createdBy);
    						preparedStatement1.setTimestamp(6, new java.sql.Timestamp(
        							new java.util.Date().getTime()));
    						if(preparedStatement1.executeUpdate()<=0){
    							throw new Exception("Failed to Insert Beneficiary Age Group Deatils "
        								+ beneficiary.memberId);
    						}
    						
    					}
    				}else{
                    	//connection.rollback();
        				return new CompasResponse(202, "Failed To Update Card Number");
                    }
                }
                
            
			//}
            

         //   connection.commit();
			return new CompasResponse(200, "Records Updated Successfully");
		} catch (Exception ex) {
			ex.printStackTrace();
			
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	

	public CompasResponse UpdateCardIssuance(Customer card) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			if (checkCardNumber(card.cardNumber)) {
				return new CompasResponse(201, "Crad Number Already Exists");
			}

			preparedStatement = connection
					.prepareStatement(Queryconstants.insertCardIssuance);
			preparedStatement.setString(1, card.cardNumber);
			preparedStatement.setString(2, "1234567890");
			preparedStatement.setTimestamp(3, new java.sql.Timestamp(
					new java.util.Date().getTime()));
			preparedStatement.setString(4, card.expiryDate);
			preparedStatement.setString(5, "I");
			preparedStatement.setInt(6, card.memberId);
			preparedStatement.setInt(7, card.programmeId);
			preparedStatement.setInt(8, card.createdBy);
			preparedStatement.setTimestamp(9, new java.sql.Timestamp(
					new java.util.Date().getTime()));
			preparedStatement.setString(10, card.pin);
			if (preparedStatement.executeUpdate() > 0) {
				DBOperations.DisposeSql(preparedStatement);
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateCardStatus);
				preparedStatement.setString(1, "I");
				preparedStatement.setInt(2, card.memberId);
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Card Number Already Exists");
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
	public boolean checkCardNumber(String cardNo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkCardNumber);
			preparedStatement.setString(1, cardNo);

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
	
	public CompasResponse UploadHHDetails(String filePath, String uploadedBy,int locationId) {
		HashMap<String, List<String>> list = new HashMap<String, List<String>>();
		List<Customer> toUpload = new ArrayList<Customer>();
		try {
			list = getHHDetailsExcel(filePath, uploadedBy);

			/*for (Customer detail : list) {

				// if(validateSupplierCode(detail.supplierCode.trim(),0).respCode==202){
				// continue;
				// } else{
				toUpload.add(detail);
				// }

			}*/
			System.out.println("List##" + list.size());
			if (list.size() > 0) {
				//for (int i=0;i<list.size();i++) {

					insertHHDetails(list,locationId);// != null) {
						
					//}
				//}

				return new CompasResponse(200, "Uploaded Successfully");
			} else {
				return new CompasResponse(201, "Oops! No Records to upload");
			}

		} catch (Exception ex) {
			return new CompasResponse(201,
					"Server Error occurred, Please try again");
		}
	}
	
	public  HashMap<String, List<String>> getHHDetailsExcel(String pathToFile,
			String createdBy) throws IOException {
		HashMap<String, List<String>> listRation = new HashMap<String, List<String>>();
		try {

			System.out.println("FileNameInUploadExcel##" + pathToFile);
			FileInputStream file = new FileInputStream(new File(pathToFile));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			// XSSFSheet sheet = workbook.getSheetAt(0);
			workbook.getNumberOfSheets();
			XSSFSheet sheet = workbook.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			System.out.println("Number of rows>>" + rows);
			DataFormatter df = new DataFormatter();

			ArrayList<String> ration = new 	ArrayList<String> ();
			for (int r = 1; r < rows; r++) {
				System.out.println("Row>>" + r);
				XSSFRow row = sheet.getRow(r);
				XSSFCell cell;				
				ration = new ArrayList<String> ();
				ration.add(0,df.formatCellValue(row.getCell(0)));
				ration.add(1,df.formatCellValue(row.getCell(1)));
				ration.add(2,df.formatCellValue(row.getCell(2)));
				ration.add(3,df.formatCellValue(row.getCell(3)));
				ration.add(4,df.formatCellValue(row.getCell(4)));
				ration.add(5,df.formatCellValue(row.getCell(5)));
				ration.add(6,df.formatCellValue(row.getCell(6)));
				listRation.put(df.formatCellValue(row.getCell(0)), ration);
				//bnfObjs.add(beneficiary);
				// }

			}
			
			System.out.println("Number of rows##" + listRation.size());
			file.close();
			return listRation;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listRation;
	}
	
	public CompasResponse insertHHDetails(HashMap<String, List<String>> list,int locationId) {

        logger.info("Creating/Updating the Beneficiary>>");
        Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		int beneficiaryId=0;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;

		try {
				connection = dataSource.getConnection();
				System.out.println(list.size());			
				/*//int i = 0; 				
				for(int z=0; z<list.size();z++){			
					Entry<String, List<String>> entry : list.entrySet();		
				}
				*/	
				int i=0;
				java.util.Iterator<Entry<String, List<String>>> it = list.entrySet().iterator();
			    while (it.hasNext()) {
			    	i++;
			        Map.Entry pair = (Map.Entry)it.next();
			        //System.out.println(i+ "\t"+ pair.getKey() + " = " + pair.getValue());
			        
			     //   if(i >= list.size())
			      //  {
			      //  	System.out.println("Terminated File ------------------");
			        	
			        	//break;
			       // }else{
			        	 List<String> values = (List<String>) pair.getValue();
			        	 //for(int j=0;j<values.size();j++){
								preparedStatement1=connection.prepareStatement(Queryconstants.getHHDetailsForImport);
							//	System.out.println(entry.getKey());
								preparedStatement1.setString(1, (String) pair.getKey());
								preparedStatement1.setInt(2, locationId);
								resultSet=preparedStatement1.executeQuery();
								if(resultSet.next()){
									
									//System.out.println(Integer.valueOf(values.get(1)));
									if(Integer.valueOf(values.get(1))==1){
										preparedStatement=connection.prepareStatement(Queryconstants.updateHHDetails);
										preparedStatement.setInt(1, Integer.valueOf(values.get(2)));
										preparedStatement.setInt(2, Integer.valueOf(values.get(1)));
										preparedStatement.setInt(3, resultSet.getInt("id"));
										preparedStatement.setInt(4, locationId);
										preparedStatement.executeUpdate();
									}if(Integer.valueOf(values.get(3))==2){
										DBOperations.DisposeSql(preparedStatement);
										preparedStatement=connection.prepareStatement(Queryconstants.updateHHDetails);
										preparedStatement.setInt(1, Integer.valueOf(values.get(4)));
										preparedStatement.setInt(2,Integer.valueOf(values.get(3)));
										preparedStatement.setInt(3, resultSet.getInt("id"));						
										preparedStatement.executeUpdate();
									}if(Integer.valueOf(values.get(5))==3){
										DBOperations.DisposeSql(preparedStatement);
										preparedStatement=connection.prepareStatement(Queryconstants.updateHHDetails);
										preparedStatement.setInt(1, Integer.valueOf(values.get(6)));
										preparedStatement.setInt(2, Integer.valueOf(values.get(5)));
										preparedStatement.setInt(3, resultSet.getInt("id"));						
										preparedStatement.executeUpdate();
					//				}						
								
							//	}					
								
							
								/*System.out.println(values.get(0));
								System.out.println(values.get(1));
								System.out.println(values.get(2));
								System.out.println(values.get(3));*/
							}
			        }
			    }
				
			/*	for(Entry<String, List<String>> entry : list.entrySet()){
					//while(entry.)
					System.out.println(entry.getValue().size());
					System.out.println(entry.getValue());
					  List<String> values = entry.getValue();
					  
				//	System.out.println(values.size());
					for(int i=0;i<values.size();i++){
						preparedStatement1=connection.prepareStatement(Queryconstants.getHHDetailsForImport);
					//	System.out.println(entry.getKey());
						preparedStatement1.setString(1, entry.getKey());
						resultSet=preparedStatement1.executeQuery();
						if(resultSet.next()){
							preparedStatement=connection.prepareStatement(Queryconstants.updateHHDetails);
							//System.out.println(resultSet.getInt("id"));
							if(Integer.valueOf(values.get(1))==1){
								preparedStatement.setInt(1, Integer.valueOf(values.get(2)));
								preparedStatement.setInt(2, Integer.valueOf(values.get(1)));
							}else if(Integer.valueOf(values.get(3))==2){
								preparedStatement.setInt(1, Integer.valueOf(values.get(4)));
								preparedStatement.setInt(2,Integer.valueOf(values.get(1)));
							}else if(Integer.valueOf(values.get(5))==3){
								preparedStatement.setInt(1, Integer.valueOf(values.get(6)));
								preparedStatement.setInt(2, Integer.valueOf(values.get(1)));
							}						
							preparedStatement.setInt(3, resultSet.getInt("id"));						
							preparedStatement.executeUpdate();
						}					
						
					
						System.out.println(values.get(0));
						System.out.println(values.get(1));
						System.out.println(values.get(2));
						System.out.println(values.get(3));
					}
					
				}*/
				
			return new CompasResponse(200, "Records Updated Successfully");
		} catch (Exception ex) {
			ex.printStackTrace();
			
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
}
