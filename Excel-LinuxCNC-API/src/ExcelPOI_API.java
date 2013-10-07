import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.charts.AxisCrosses;
import org.apache.poi.ss.usermodel.charts.AxisPosition;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.ss.usermodel.charts.ChartLegend;
import org.apache.poi.ss.usermodel.charts.DataSources;
import org.apache.poi.ss.usermodel.charts.LegendPosition;
import org.apache.poi.ss.usermodel.charts.ScatterChartData;
import org.apache.poi.ss.usermodel.charts.ValueAxis;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelPOI_API {
	
	private String fileName;
	private Workbook wb;
	private CreationHelper createHelper;
	private Sheet sheet;
	
	public ExcelPOI_API(String excelFile) {
		this.fileName = excelFile;
	}
	
	public ExcelPOI_API init() {
		wb = new XSSFWorkbook();
		createHelper = wb.getCreationHelper();
		sheet = wb.createSheet();
		
		return this;
	}
	
	public void insertCell(int row, int col, String val) {
		Row rowRow = sheet.getRow(row);
		
		if (rowRow == null) {
			rowRow = sheet.createRow(row);
		}
		
		rowRow.createCell(col).setCellValue(val);
	}
	
	public void insertCell(int row, int col, int val) {
		Row rowRow = sheet.getRow(row);
		
		if (rowRow == null) {
			rowRow = sheet.createRow(row);
		}
		
		rowRow.createCell(col).setCellValue(val);
	}
	
	public void insertCell(int row, int col, boolean val) {
		Row rowRow = sheet.getRow(row);
		
		if (rowRow == null) {
			rowRow = sheet.createRow(row);
		}
		
		rowRow.createCell(col).setCellValue(val);
	}
	
	public void insertCell(int row, int col, double val) {
		Row rowRow = sheet.getRow(row);
		
		if (rowRow == null) {
			rowRow = sheet.createRow(row);
		}
		
		rowRow.createCell(col).setCellValue(val);
	}
	
	public void insertCell(int row, int col, Date val) {
		Row rowRow = sheet.getRow(row);
		
		if (rowRow == null) {
			rowRow = sheet.createRow(row);
		}
		
		rowRow.createCell(col).setCellValue(val);
	}
	
	public void WriteAndClose() {
		// createChart();
		
		FileOutputStream FOS = null;
		
		try {
			FOS = new FileOutputStream(fileName);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try {
			wb.write(FOS);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			FOS.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createChart() {
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 5, 10, 15);
		
		Chart chart = drawing.createChart(anchor);
		ChartLegend legend = chart.getOrCreateLegend();
		legend.setPosition(LegendPosition.TOP_RIGHT);
		
		ScatterChartData data = chart.getChartDataFactory().createScatterChartData();
		
		ValueAxis bottomAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.BOTTOM);
		ValueAxis leftAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
		leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
		
		final int NUM_OF_ROWS = 3;
	    final int NUM_OF_COLUMNS = 4;
		ChartDataSource<Number> xs = DataSources.fromNumericCellRange(sheet, new CellRangeAddress(0, 0, 0, NUM_OF_COLUMNS - 1));
		ChartDataSource<Number> ys1 = DataSources.fromNumericCellRange(sheet, new CellRangeAddress(1, 1, 0, NUM_OF_COLUMNS - 1));
		ChartDataSource<Number> ys2 = DataSources.fromNumericCellRange(sheet, new CellRangeAddress(2, 2, 0, NUM_OF_COLUMNS - 1));
		
		data.addSerie(xs, ys1);
		data.addSerie(xs, ys2);
		
		chart.plot(data, bottomAxis, leftAxis);
	}
}
