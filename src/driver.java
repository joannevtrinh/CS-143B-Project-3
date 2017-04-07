import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;


public class driver
{
	PM physicalMemory = new PM();
	private Scanner x;
	private Scanner y;
	PrintWriter outputStream1;
	PrintWriter outputStream2;

	public void openOutputFile()
	{
		try
		{
			outputStream1 = new PrintWriter("/Volumes/cs143b/87597916-notTLB.txt");
			outputStream2 = new PrintWriter("/Volumes/cs143b/87597916-TLB.txt");
		}
		catch(FileNotFoundException e)
		{
			System.out.println("OutputFile cannot be opened.");
		}

	}
	public void openFile()
	{
		try
		{
			x = new Scanner(new File("/Volumes/cs143b/input1.txt"));
			y = new Scanner(new File("/Volumes/cs143b/input2.txt"));
		}
		catch(Exception e)
		{
			System.out.println("Cannot find file");
		}
	}
	
	public void readFileInit()
	{		
		String test = x.nextLine();
		//System.out.println(test);
		String [] firstLine = test.split("\\s");
		
		/*for(int i =0; i < firstLine.length; i++)
		{
			System.out.println(firstLine[i]);
		}
		*/
		
		for(int i = 0; i < firstLine.length; i++)
		{
			String a = firstLine[i];
			String b = firstLine[i+1];
			
			int c = Integer.parseInt(a);
			int d = Integer.parseInt(b);
			
			physicalMemory.entryST(c, d);
			
			i++;
		}
		
		String test1 = x.nextLine();
		//System.out.println(test1);
		String [] secondLine = test1.split("\\s");
		for(int i = 0; i < secondLine.length; i++)
		{
			String a = secondLine[i];
			String b = secondLine[i + 1];
			String c = secondLine[i + 2];
			
			int d = Integer.parseInt(a);
			int e = Integer.parseInt(b);
			int f = Integer.parseInt(c);
			
			physicalMemory.entryP(d, e, f);
			
			i = i +2;

		}
		
		
		
	}
	
	public void readFileInput()
	{
		while(y.hasNext())
		{
			String a = y.next();
			String b = y.next();
			
			int c = Integer.parseInt(a);
			int d = Integer.parseInt(b);
			
			physicalMemory.command(c, d, outputStream1);
			physicalMemory.commandTLB(c, d, outputStream2);
			
		}
		x.close();

		y.close();
		outputStream1.close();
		outputStream2.close();
	}
	
	public static void main(String [] args)
	{
		driver test = new driver();
		test.openOutputFile();
		test.openFile();
		test.readFileInit();
		test.readFileInput();
	}
	
}

