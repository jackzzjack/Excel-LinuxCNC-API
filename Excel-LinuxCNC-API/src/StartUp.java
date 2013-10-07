import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * ARGS: logfile colnumber exceloutput interval(sec)
 * @author Yu-Ting
 *
 */
public class StartUp {

	private final static int _ARGS = 4;
	
	public static void main(String[] args) {
		if (args.length != _ARGS) {
			System.out.println("ARGS: logfile colnumber exceloutput interval(sec)");
			System.out.println("You need " + _ARGS + " arguments.");
		} else {
			String fileName = args[0];
			int colno = Integer.valueOf(args[1]);
			String excelOutput = args[2];
			int interval = Integer.valueOf(args[3]);
			
			String preString = ProcessFrontEnd(fileName);
			
			ArrayList<String> totalElement = Delimiter(preString, colno);
			if (totalElement.size() == 0) {
				System.out.println("Process exit 1\n");
			}
			
			ProcessBackEnd(totalElement, colno, excelOutput, interval);
		}
	}

	private static void DebugProcessBackEnd(String[] delim, int colno) {
		for (int i=0; i < (delim.length/colno); i++) {
			for (int every=0; every < colno; every++) {
				// System.out.print(delim[i*colno+every] + " ");
				// GoToExcelPOI(i*colno+every, delim[i*colno+every], colno);
			}
			System.out.println();
		}
	}
	
	private static void ProcessBackEnd(ArrayList<String> totalElement, int colno, String output, int interval) {
		ExcelPOI_API api = new ExcelPOI_API(output).init();
		int lines = totalElement.size()/colno;
		
		for (int i=0; i < lines; i++) {
			for (int every=0; every < colno; every++) {
				// System.out.print(delim[i*colno+every] + " ");
				// GoToExcelPOI(i*colno+every, delim[i*colno+every], colno);
				api.insertCell(i, every, totalElement.get(i*colno+every));
			}
		}
		
		drawOutHeader(colno, api);
		
		int group_no = 1;
		int write_count = 0;
		int peek_count = 0;
		int read_count = 0;
		int timeInterval_start = Integer.valueOf(totalElement.get(2));
		for (int i=0; i < lines; i++) {
			// Same
			if (timeInterval_start == Integer.valueOf(totalElement.get(i*colno+2))) {
				if (totalElement.get(i*colno+4).equals("Write") == true)
					write_count++;
				else if (totalElement.get(i*colno+4).equals("Peek") == true)
					peek_count++;
				else if (totalElement.get(i*colno+4).equals("Read") == true)
					read_count++;
			} else {
			// Not Same (Output)
				dumpOutToExcel(write_count, peek_count, read_count, colno, group_no, api);
				if (timeInterval_start == 60)
					timeInterval_start = 0;
				else
					timeInterval_start++;
				
				i--;
				
				write_count = 0;
				peek_count = 0;
				read_count = 0;
				group_no++;
			}
		}
		
		dumpOutToExcel(write_count, peek_count, read_count, colno, group_no, api);
		
		api.WriteAndClose();
	}

	private static void drawOutHeader(int colno, ExcelPOI_API api) {
		api.insertCell(1, colno+2, "Group");
		api.insertCell(2, colno+2, "Write");
		api.insertCell(2+1, colno+2, "Peek");
		api.insertCell(2+2, colno+2, "Read");
	}

	private static void dumpOutToExcel(int write_count, int peek_count, int read_count, int colno, int group_no, ExcelPOI_API api) {
		api.insertCell(1, colno+2+group_no, Integer.valueOf(group_no));
		api.insertCell(2, colno+2+group_no, Integer.valueOf(write_count));
		api.insertCell(2+1, colno+2+group_no, Integer.valueOf(peek_count));
		api.insertCell(3+1, colno+2+group_no, Integer.valueOf(read_count));
	}

	private static ArrayList<String> Delimiter(String preString, int colno) {		
		String[] lines = preString.split("[\r\n]");
		String[] line = null;
		ArrayList<String> total = new ArrayList<>();
		
		for (int i=0; i < lines.length; i++) {
			line = lines[i].trim().split(",");
			for (int e=0; e < line.length; e++) {
				total.add(line[e].trim());
			}
		}
		
		return total;
	}

	private static String ProcessFrontEnd(String fileName) {
		char ch;
		StringBuffer SB = new StringBuffer();
		FileReader FR = null;
		
		try {
			FR = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			while (FR.ready()) {
				ch = (char) FR.read();
				SB.append(ch);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			FR.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return SB.toString();
	}
}