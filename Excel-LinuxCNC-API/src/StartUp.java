import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class StartUp {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("You need 2 arguments.");
		} else {
			String fileName = args[0];
			int colno = Integer.valueOf(args[1]);
			
			String preString = ProcessFrontEnd(fileName);
			
			String[] delim = Delimiter(preString, "[,\r\n]", colno);
			if (delim == null) {
				System.out.println("Process exit 1\n");
			}
			
			ProcessBackEnd(delim, colno);
		}
	}

	private static void ProcessBackEnd(String[] delim, int colno) {
		for (int i=0; i < (delim.length/colno); i++) {
			for (int every=0; every < colno; every++) {
				// System.out.print(delim[i*colno+every] + " ");
				// GoToExcelPOI(i*colno+every, delim[i*colno+every], colno);
			}
			System.out.println();
		}
	}

	private static String[] Delimiter(String preString, String delimiter, int colno) {
		String[] delim = preString.split(delimiter);
		if ((delim.length % colno) != 0) {
			System.err.println("The content should be a multiple of " + colno + ".\n");
			return null;
		}
		
		return delim;
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