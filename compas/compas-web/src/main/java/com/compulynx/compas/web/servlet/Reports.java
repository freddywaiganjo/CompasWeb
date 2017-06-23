package com.compulynx.compas.web.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;








import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

import com.compulynx.compas.dal.operations.Utility;
import com.compulynx.compas.models.CompasProperties;
import com.compulynx.compas.models.CompasResponse;

//import org.springframework.beans.factory.annotation.Autowired;

/**
 * Servlet implementation class Reports
 */
@Component
public class Reports extends HttpServlet {

	private static final Logger logger = Logger.getLogger(Reports.class
			.getName());
	String fileName = "";
	String PATH = "";
	CompasResponse response = null;
	/**
	 * Default constructor.
	 */
	public Reports() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	private DataSource dataSource;

	public Reports(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public Connection getDbConnection() {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return connection;
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		// System.out.println("Called Servlet Reports");
		JasperReport jasperReport = null;
		JasperPrint jasperPrint = null;
		JasperDesign jasperDesign = null;
		Map<String, String> parameters = new HashMap();
		ServletOutputStream outstream = null;
		Connection connection = null;
		String reportType = request.getParameter("type");
		String exportType = request.getParameter("eType");
		CompasProperties compasProperties = new CompasProperties();
		Utility util = new Utility();
		String param = "report.filepath";
		compasProperties = util.getCompasProperties(param);
		logger.info("Done Reading Props File##"
				+ compasProperties.bnfUploadFilePath);
		// TOMCAT_HOME = System.getProperty("catalina.base");
		// fileName = fileDetail.getFileName();
		PATH = compasProperties.bnfUploadFilePath;
		logger.info("Reports File Path##" + PATH);
		try {
			InitialContext initialContext = new InitialContext();
			Context context = (Context) initialContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) context.lookup("jdbc/compasDS");
			connection = ds.getConnection();

	
//			if (reportType.equalsIgnoreCase("MS")) {
//				String fromDate = request.getParameter("FrDt");
//				String toDate = request.getParameter("ToDt");
//				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
//				// Parsing the date
//				DateTime from = dtf.parseDateTime(fromDate);
//				DateTime to = dtf.parseDateTime(toDate);
//				// Format for output
//				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");
//			//	String memberNo = request.getParameter("MemNo");
//				parameters.put("FromDt", fromDate);
//				parameters.put("ToDt", toDate);
//				//parameters.put("MemNo", memberNo);
//				parameters.put("dtFrm", dtFrm.print(from).toString());
//				parameters.put("toFrm", dtFrm.print(to).toString());
//
//				jasperDesign = JRXmlLoader
//						.load(REPORTS_PATH+"/RptMemberStatement.jrxml");
//				jasperReport = JasperCompileManager.compileReport(jasperDesign);
//				jasperPrint = JasperFillManager.fillReport(jasperReport,
//						parameters, connection);
//
//				if (jasperPrint.getPages().size() != 0) {
//					byte[] pdfasbytes = JasperExportManager
//							.exportReportToPdf(jasperPrint);
//					outstream = response.getOutputStream();
//					response.setContentType("application/pdf");
//					response.setContentLength(pdfasbytes.length);
//					outstream.write(pdfasbytes, 0, pdfasbytes.length);
//
//				} else {
//					System.out.println("No data");
//				}
//			}
			if (reportType.equalsIgnoreCase("PT")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				String agentId = request.getParameter("agentId");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");

				
				parameters.put("FromDt", fromDate);
				parameters.put("ToDt", toDate);
				parameters.put("agentId", agentId);

				jasperDesign = JRXmlLoader
						.load(PATH+"RptCashTxn.jrxml");
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				jasperPrint = JasperFillManager.fillReport(jasperReport,
						parameters, connection);

				if (jasperPrint.getPages().size() != 0) {
					byte[] pdfasbytes = JasperExportManager
							.exportReportToPdf(jasperPrint);
					outstream = response.getOutputStream();
					response.setContentType("application/pdf");
					response.setContentLength(pdfasbytes.length);
					outstream.write(pdfasbytes, 0, pdfasbytes.length);

				} else {
					System.out.println("No data");
				}
			}
			if (reportType.equalsIgnoreCase("DV")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				String deviceId = request.getParameter("DeviceId");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");

				
				parameters.put("FromDt", fromDate);
				parameters.put("ToDt", toDate);
				parameters.put("DeviceId", deviceId);

				jasperDesign = JRXmlLoader
						.load(PATH+"RptDeviceTxn.jrxml");
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				jasperPrint = JasperFillManager.fillReport(jasperReport,
						parameters, connection);

				if (jasperPrint.getPages().size() != 0) {
					byte[] pdfasbytes = JasperExportManager
							.exportReportToPdf(jasperPrint);
					outstream = response.getOutputStream();
					response.setContentType("application/pdf");
					response.setContentLength(pdfasbytes.length);
					outstream.write(pdfasbytes, 0, pdfasbytes.length);

				} else {
					System.out.println("No data");
				}
			}
			if (reportType.equalsIgnoreCase("AT")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				String bnfGrpId = request.getParameter("bnfGrpId");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");

				
				parameters.put("FromDt", fromDate);
				parameters.put("ToDt", toDate);
				parameters.put("bnfGrpId", bnfGrpId);

				jasperDesign = JRXmlLoader
						.load(PATH+"RptAprvdTopup.jrxml");
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				jasperPrint = JasperFillManager.fillReport(jasperReport,
						parameters, connection);

				if (jasperPrint.getPages().size() != 0) {
					byte[] pdfasbytes = JasperExportManager
							.exportReportToPdf(jasperPrint);
					outstream = response.getOutputStream();
					response.setContentType("application/pdf");
					response.setContentLength(pdfasbytes.length);
					outstream.write(pdfasbytes, 0, pdfasbytes.length);

				} else {
					System.out.println("No data");
				}
			}
			if (reportType.equalsIgnoreCase("BS")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				String cardNo = request.getParameter("cardNo");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");

				
				parameters.put("FromDt", fromDate);
				parameters.put("ToDt", toDate);
				parameters.put("cardNo", cardNo);
				if (exportType.equalsIgnoreCase("P")) {
				jasperDesign = JRXmlLoader
						.load(PATH+"RptBnfStatement.jrxml");
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				jasperPrint = JasperFillManager.fillReport(jasperReport,
						parameters, connection);

				if (jasperPrint.getPages().size() != 0) {
					byte[] pdfasbytes = JasperExportManager
							.exportReportToPdf(jasperPrint);
					outstream = response.getOutputStream();
					response.setContentType("application/pdf");
					response.setContentLength(pdfasbytes.length);
					outstream.write(pdfasbytes, 0, pdfasbytes.length);

				} else {
					System.out.println("No data");
				}
				}else{
					System.out.println("calling servlet## Summary Excel");
					jasperDesign = JRXmlLoader
							.load(PATH+"RptBnfStatement_xlsx.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
					       ByteArrayOutputStream baos = new ByteArrayOutputStream();
					                exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
					             exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					             exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos); // fill byte array output stream

					             exporter.exportReport();
					             
					             response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					             response.setHeader("Content-disposition", "attachment; filename=" + "BeneficiaryStatement"+".xlsx");
					             response.setContentLength(baos.size());
					             response.getOutputStream().write(baos.toByteArray());      

					} else {
						System.out.println("No data");
					}
				}
			}
			if (reportType.equalsIgnoreCase("PR")) {
				
				String locationId = request.getParameter("locationId");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				//DateTime from = dtf.parseDateTime(fromDate);
				//DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");

				
				//parameters.put("FromDt", fromDate);
				//parameters.put("ToDt", toDate);
				parameters.put("locationId", locationId);
				if (exportType.equalsIgnoreCase("P")) {
				jasperDesign = JRXmlLoader
						.load(PATH+"RptPriceDetails.jrxml");
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				jasperPrint = JasperFillManager.fillReport(jasperReport,
						parameters, connection);

				if (jasperPrint.getPages().size() != 0) {
					byte[] pdfasbytes = JasperExportManager
							.exportReportToPdf(jasperPrint);
					outstream = response.getOutputStream();
					response.setContentType("application/pdf");
					response.setContentLength(pdfasbytes.length);
					outstream.write(pdfasbytes, 0, pdfasbytes.length);

				} else {
					System.out.println("No data");
				}
				}else{
					System.out.println("calling servlet## Summary Excel");
					jasperDesign = JRXmlLoader
							.load(PATH+"RptBnfStatement_xlsx.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
					       ByteArrayOutputStream baos = new ByteArrayOutputStream();
					                exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
					             exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					             exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos); // fill byte array output stream

					             exporter.exportReport();
					             
					             response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					             response.setHeader("Content-disposition", "attachment; filename=" + "BeneficiaryStatement"+".xlsx");
					             response.setContentLength(baos.size());
					             response.getOutputStream().write(baos.toByteArray());      

					} else {
						System.out.println("No data");
					}
				}
			}


			if (reportType.equalsIgnoreCase("SU")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat
						.forPattern("dd-MMM-yyyy");
				// String memberNo = request.getParameter("MemNo");
				parameters.put("FromDt", fromDate);
				parameters.put("ToDt", toDate);
				// parameters.put("MemNo", memberNo);
				parameters.put("dtFrm", dtFrm.print(from).toString());
				parameters.put("toFrm", dtFrm.print(to).toString());

				if (exportType.equalsIgnoreCase("P")) {
					System.out.println("calling servlet## Summary Pdf");
					jasperDesign = JRXmlLoader
							.load(PATH+"RptTxnSummary.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager
								.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				}else{
					System.out.println("calling servlet## Summary Excel");
					jasperDesign = JRXmlLoader
							.load(PATH+"RptTxnSummary_xlsx.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
					       ByteArrayOutputStream baos = new ByteArrayOutputStream();
					                exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
					             exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					             exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos); // fill byte array output stream

					             exporter.exportReport();
					             
					             response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					             response.setHeader("Content-disposition", "attachment; filename=" + "TransactionSummary"+".xlsx");
					             response.setContentLength(baos.size());
					             response.getOutputStream().write(baos.toByteArray());      

					} else {
						System.out.println("No data");
					}
				}
				
			}
				if (reportType.equalsIgnoreCase("CN")) {
					String fromDate = request.getParameter("FrDt");
					String toDate = request.getParameter("ToDt");
					DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
					// Parsing the date
					DateTime from = dtf.parseDateTime(fromDate);
					DateTime to = dtf.parseDateTime(toDate);
					// Format for output
					DateTimeFormatter dtFrm = DateTimeFormat
							.forPattern("dd-MMM-yyyy");
					// String memberNo = request.getParameter("MemNo");
					parameters.put("FromDt", fromDate);
					parameters.put("ToDt", toDate);
					// parameters.put("MemNo", memberNo);
					parameters.put("dtFrm", dtFrm.print(from).toString());
					parameters.put("toFrm", dtFrm.print(to).toString());

					if (exportType.equalsIgnoreCase("P")) {
						System.out.println("calling servlet## Summary Pdf");
						jasperDesign = JRXmlLoader
								.load(PATH+"RptConsumptionSummary.jrxml");
						jasperReport = JasperCompileManager
								.compileReport(jasperDesign);
						jasperPrint = JasperFillManager.fillReport(jasperReport,
								parameters, connection);

						if (jasperPrint.getPages().size() != 0) {
							byte[] pdfasbytes = JasperExportManager
									.exportReportToPdf(jasperPrint);
							outstream = response.getOutputStream();
							response.setContentType("application/pdf");
							response.setContentLength(pdfasbytes.length);
							outstream.write(pdfasbytes, 0, pdfasbytes.length);

						} else {
							System.out.println("No data");
						}
					}else{
						System.out.println("calling servlet## Summary Excel");
						jasperDesign = JRXmlLoader
								.load(PATH+"RptConsumptionSummary_xlsx.jrxml");
						jasperReport = JasperCompileManager
								.compileReport(jasperDesign);
						jasperPrint = JasperFillManager.fillReport(jasperReport,
								parameters, connection);

						if (jasperPrint.getPages().size() != 0) {
							JRXlsxExporter exporter = getCommonXlsxExporter();
						       ByteArrayOutputStream baos = new ByteArrayOutputStream();
						                exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
						                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						                exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						                exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						             exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						             exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos); // fill byte array output stream

						             exporter.exportReport();
						             
						             response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						             response.setHeader("Content-disposition", "attachment; filename=" + "ConsumptionSummary"+".xlsx");
						             response.setContentLength(baos.size());
						             response.getOutputStream().write(baos.toByteArray());      

						} else {
							System.out.println("No data");
						}
					}
			}
				if (reportType.equalsIgnoreCase("MS")) {
					String fromDate = request.getParameter("FrDt");
					String toDate = request.getParameter("ToDt");
					DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
					// Parsing the date
					DateTime from = dtf.parseDateTime(fromDate);
					DateTime to = dtf.parseDateTime(toDate);
					// Format for output
					DateTimeFormatter dtFrm = DateTimeFormat
							.forPattern("dd-MMM-yyyy");
					// String memberNo = request.getParameter("MemNo");
					parameters.put("FromDt", fromDate);
					parameters.put("ToDt", toDate);
					// parameters.put("MemNo", memberNo);
					parameters.put("dtFrm", dtFrm.print(from).toString());
					parameters.put("toFrm", dtFrm.print(to).toString());

					if (exportType.equalsIgnoreCase("P")) {
						System.out.println("calling servlet## Summary Pdf");
						jasperDesign = JRXmlLoader
								.load(PATH+"RptMemberStatement.jrxml");
						jasperReport = JasperCompileManager
								.compileReport(jasperDesign);
						jasperPrint = JasperFillManager.fillReport(jasperReport,
								parameters, connection);

						if (jasperPrint.getPages().size() != 0) {
							byte[] pdfasbytes = JasperExportManager
									.exportReportToPdf(jasperPrint);
							outstream = response.getOutputStream();
							response.setContentType("application/pdf");
							response.setContentLength(pdfasbytes.length);
							outstream.write(pdfasbytes, 0, pdfasbytes.length);

						} else {
							System.out.println("No data");
						}
					}else{
						System.out.println("calling servlet## Summary Excel");
						jasperDesign = JRXmlLoader
								.load(PATH+"RptAllTransaction_xlsx.jrxml");
						jasperReport = JasperCompileManager
								.compileReport(jasperDesign);
						jasperPrint = JasperFillManager.fillReport(jasperReport,
								parameters, connection);

						if (jasperPrint.getPages().size() != 0) {
							JRXlsxExporter exporter = getCommonXlsxExporter();
						       ByteArrayOutputStream baos = new ByteArrayOutputStream();
						                exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
						                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						                exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						                exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						             exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						             exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos); // fill byte array output stream

						             exporter.exportReport();
						             
						             response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						             response.setHeader("Content-disposition", "attachment; filename=" + "All Transactions"+".xlsx");
						             response.setContentLength(baos.size());
						             response.getOutputStream().write(baos.toByteArray());      

						} else {
							System.out.println("No data");
						}
					}
			}
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	 private JRXlsxExporter getCommonXlsxExporter(){
	     JRXlsxExporter exporter = new JRXlsxExporter();
	     exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
	     exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
	     exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE);
	     exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
	     exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
	     exporter.setParameter(JExcelApiExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
	     
	     //exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);

	     return exporter;
	 }

}
