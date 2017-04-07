import java.io.PrintWriter;
import java.util.*;

public class PM
{
	static int size = 512 * 1024;// 524288
	static int [] PMArray = new int[size]; 
	BitMap bm = new BitMap();
	TLB tlb = new TLB();
	// first 512 occupied by ST. Means all input (x, y) )PT address will be placed here and created on another place in PM
	// rest is occupied by PT and pages.
	
	
	public PM()
	{
		for(int i = 0; i < PMArray.length;i++)
		{
			PMArray[i] = 0;
		}
	}
	
	public int breakVAtoS(int VA)
	{
		int S = 0;
		int numbaS = 511;
		
		S = (VA >> 19) & 511;
		return S;
	}
	
	public int breakVAtoP(int VA)
	{
		int p = 0;
		int numbaP = 1023;
		
		p = (VA >> 9) & 1023;
		
		return p;
	}
	
	public int breakVAtoW(int VA)
	{
		int w = 0;
		int numbaW = 511;
		
		w = (VA & 511);
		
		return w;
	}
	
	public void entryST(int segNum, int address)
	{
		PMArray[segNum] = address;
		
		if(address == -1)
		{
			//System.out.println("PT is not resident. Cannot add to BM");
			
		}
		else
		{
			bm.setBMSpacePT(address);
		}
		
	}
	
	public void entryP(int page, int segNum, int address)
	{
		PMArray[PMArray[segNum] + page] = address;
		
		if(address == -1)
		{
			//System.out.println("Page is not resident. Cannot add to BM");
		}
		else
		{
			bm.setBMSpaceP(address);
		}
	}
	public void readAccess(int s, int p, int w, PrintWriter outputStream1)
	{
		if(PMArray[s] == -1 || PMArray[PMArray[s] + p] == -1)
		{
			outputStream1.print(" ");
			outputStream1.print("pf");
			return;
		}
		if(PMArray[s] == 0 || PMArray[PMArray[s] + p] == 0)
		{
			outputStream1.print(" ");
			outputStream1.print("err");
			return;
		}
		else
		{
			outputStream1.print(" ");
			outputStream1.print(PMArray[PMArray[s] + p] + w);
		}
	}
	
	public void readAccessTLB(int s, int p, int w, PrintWriter outputStream2)
	{
		boolean isHit = false;
		int PA = -1;
		if(PMArray[s] >= 0)
		{
			if(PMArray[PMArray[s] + p] >= 0)
			{
				PA = tlb.findMatch(s*p, w, PMArray[PMArray[s] + p]);
			}
		}
		
		if(PA != -1)
		{
			if(PA == PMArray[PMArray[s] + p] +w)
			{
				outputStream2.print(" ");

				outputStream2.print("h");
				outputStream2.print(" ");

				outputStream2.print(PA);
				isHit = true;
			}
		}
		if(isHit == false)
		{
			outputStream2.print(" ");
			outputStream2.print("m");
			if(PMArray[s] == -1 || PMArray[PMArray[s] + p] == -1)
			{
				outputStream2.print(" ");
				outputStream2.print("pf");
				return;
			}
			if(PMArray[s] == 0 || PMArray[PMArray[s] + p] == 0)
			{
				outputStream2.print(" ");
				outputStream2.print("err");
				return;
			}
			else
			{
				outputStream2.print(" ");
				outputStream2.print(PMArray[PMArray[s] + p] + w);
				if(w != -1)
				{
					tlb.updateTLB(s*p, PMArray[PMArray[s] + p]);
				}
			}
		}
	}
	
	public void writeAccessTLB(int s, int p, int w, PrintWriter outputStream2)
	{
		boolean isPF = false;
		boolean isHit = false;
		int PA = -1;
		if(PMArray[s] >= 0)
		{
			if(PMArray[PMArray[s] + p] >= 0)
			{
				PA = tlb.findMatch(s*p, w, PMArray[PMArray[s] + p]);
			}
		}
		if(PA != -1)
		{
			if(PA == PMArray[PMArray[s] + p]+w)
			{
				outputStream2.print(" ");
				outputStream2.print("h");
				outputStream2.print(" ");
				outputStream2.print(PA);
				isHit = true;
			}
		
		}
		if(isHit == false)
		{
			outputStream2.print(" ");
			outputStream2.print("m");
			if(PMArray[s] == -1 || PMArray[PMArray[s] + p] == -1)
			{
				outputStream2.print(" ");
				outputStream2.print("pf");
				isPF = true;
				return;
			}
			if(PMArray[s] == 0)
			{
				int freeIndexPT = bm.findSpacePT() * 512;
				
				PMArray[s] = freeIndexPT;
				bm.setBMSpacePT(freeIndexPT);
				isPF = false;
			}
			if(PMArray[PMArray[s] + p] == 0)
			{
				int freeIndexP = bm.findSpaceP() * 512;
				
				PMArray[PMArray[s]+p] = freeIndexP;
				
				bm.setBMSpaceP(freeIndexP);
				isPF = false;
			}
			
			if(isPF == false)
			{
				outputStream2.print(" ");
				outputStream2.print(PMArray[PMArray[s] + p] + w);
				if(w != -1)
				{
					tlb.updateTLB(s*p, PMArray[PMArray[s] + p]);
				}
			}
		}
	}
	
	public void writeAccess(int s, int p, int w, PrintWriter outputStream1)
	{
		boolean isPF = false;
		if(PMArray[s] == -1 || PMArray[PMArray[s] + p] == -1)
		{
			outputStream1.print(" ");
			outputStream1.print("pf");
			isPF = true;
			return;
		}
			if(PMArray[s] == 0 && isPF == false)
			{
				int freeIndexPT = bm.findSpacePT() * 512;
				
				PMArray[s] = freeIndexPT;
				bm.setBMSpacePT(freeIndexPT);
				isPF = false;
			}
			if(PMArray[PMArray[s] + p] == 0 && isPF == false)
			{
				int freeIndexP = bm.findSpaceP() * 512;
				
				PMArray[PMArray[s]+p] = freeIndexP;
				
				bm.setBMSpaceP(freeIndexP);
				isPF = false;
			}
			
			if(isPF == false)
			{
				outputStream1.print(" ");
				outputStream1.print((PMArray[PMArray[s] + p] + w));
				
			}
	}
	
	public void command(int rORw, int VA, PrintWriter outputStream1)
	{
		int s = breakVAtoS(VA);
		int p = breakVAtoP(VA);
		int w = breakVAtoW(VA);
		
		if(rORw == 1)
		{
			writeAccess(s, p , w, outputStream1);
		}
		if(rORw == 0)
		{
			readAccess(s, p, w, outputStream1);
		}
	}
	
	public void commandTLB(int rORw, int VA, PrintWriter outputStream2)
	{
		int s = breakVAtoS(VA);
		int p = breakVAtoP(VA);
		int w = breakVAtoW(VA);
		
		if(rORw == 1)
		{
			writeAccessTLB(s, p , w, outputStream2);
		}
		if(rORw == 0)
		{
			readAccessTLB(s, p, w, outputStream2);
		}
	}
	
	
	/*
	//public static void main(String [] args)
	{
		PM test = new PM();
		
		/* Passed this test case
		test.entryST(2, 2048);
		test.entryST(0, 1024);
		test.entryST(511, 523264);
		test.entryST(3, -1);
		test.entryST(4, 7680);
		test.entryST(6, 8704);
		test.entryST(7, 9728);
		
		test.entryP(0, 2, 512);
		test.entryP(1023, 511, 3072);
		test.entryP(1023, 4, 26624);
		test.entryP(0, 7, 15360);
		test.entryP(0, 6, -1);
		/*Passed with TLB
		test.commandTLB(1, 0);
		test.commandTLB(0, 0);
		test.commandTLB(0, 1048576);
		test.commandTLB(1, 268434944);
		test.commandTLB(1, 524288);
		test.commandTLB(1, 524288);
		test.commandTLB(0, 524288);
		test.commandTLB(0, 1572864);
		
		
		test.commandTLB(1, 1572864);
		test.commandTLB(0, 2097152);
		test.commandTLB(0, 2621440);
		test.commandTLB(0, 2621439);
		test.commandTLB(0, 3670016);
		test.commandTLB(0, 3145728);
		test.commandTLB(1, 3145728);

		//1 0 0 0 0 1048576 1 268434944 1 524288 1 524288 0 524288 0 1572864 
		//1 1572864 0 2097152 0 2621440 0 2621439 0 3670016 0 3145728 1 3145728 1 3670016
		/*Passed without TLB
		test.command(1, 0);
		test.command(0, 0);
		test.command(0, 1048576);
		test.command(1, 268434944);
		test.command(1, 524288);
		test.command(1, 524288);
		test.command(0, 524288);
		test.command(0, 1572864);
		test.command(1, 1572864);
		test.command(0, 2097152);
		test.command(0, 2621440);
		test.command(0, 2621439);
		test.command(0, 3670016);
		test.command(0, 3145728);
		test.command(1, 3145728);
		test.command(1, 3670016);
		*/
		
		/* Passed Test case
		test.entryST(2, 2048);
		test.entryST(5, 1024);
		test.entryST(6, 11264);
		test.entryST(11, 9216);
		test.entryST(96, 4096);
		
		test.entryP(0, 2, 512);
		test.entryP(1, 2, -1);
		test.entryP(3, 6, 18944);
		test.entryP(291, 96, -1);
		
		test.commandTLB(0, 0);
		test.commandTLB(0, 1048576);
		test.commandTLB(1, 1048586);
		test.commandTLB(1, 1049088);
		test.commandTLB(1, 524288);
		test.commandTLB(1, 524292);
		test.commandTLB(1, 524804);	
		test.commandTLB(0, 524292);
		test.commandTLB(1, 0);
		test.commandTLB(1, 1);	
		test.commandTLB(0, 0);
		test.commandTLB(0, 1);
		test.commandTLB(0, 1048586);
		test.commandTLB(0, 512);
		test.commandTLB(1, 512);
		test.commandTLB(0, 512);
		test.commandTLB(0, 528);
		test.commandTLB(1, 524900);
		test.commandTLB(1, 1572864);
		test.commandTLB(1, 2097152);
		test.commandTLB(1, 1572864);
		test.commandTLB(1, 1572865);
		test.commandTLB(1, 3145731);
		test.commandTLB(1, 33554432);
		test.commandTLB(0, 3145800);
	
		
		
		test.commandTLB(0, 0);
		test.commandTLB(0, 1024);
		test.commandTLB(1, 1024);
		test.commandTLB(1, 33685504);
		test.commandTLB(0, 33700504);
		test.commandTLB(1, 33700504);
		
		
		test.commandTLB(0, 0);
		test.commandTLB(0, 1048576);
		test.commandTLB(1, 1048586);
		test.commandTLB(1, 1049088);
		
		
		test.commandTLB(1, 524288);
		test.commandTLB(1, 16794132);
		test.commandTLB(1, 50348564);
		test.commandTLB(1, 150479636);
		
		
		
		test.commandTLB(1, 50480668);
		test.commandTLB(0, 33554435);
		test.commandTLB(0, 33558528);
		
		
		*/
		/*
		
		test.entryST(2, 2048);
		test.entryST(0, 1024);
		test.entryST(511, 523264);
		test.entryP(0, 2, 512);
		test.entryP(1023, 511, 3072);
		
		/*
		test.command(1, 0);
		test.command(0, 0);
		test.command(0, 1048576);
		test.command(1, 268434944);
		test.command(1, 524288);
		test.command(1, 524288);
		test.command(0, 524288);
		*/
		
		/*
		test.commandTLB(1, 0);
		test.commandTLB(0, 0);
		test.commandTLB(0, 1048576);
		test.commandTLB(1, 268434944);
		test.commandTLB(1, 524288);
		
		test.commandTLB(1, 524288);
		test.commandTLB(0, 524288);
		
		
		
		
		//First test case passed.
		/*
		test.entryST(2, 2048);
		test.entryP(0, 2, 512);
		test.entryP(1, 2, -1);
		test.breakVAtoS(1048576);
		test.breakVAtoP(1048576);
		test.breakVAtoW(1048576);
		test.commandTLB(0, 0);
		test.commandTLB(0, 1048576);
		test.commandTLB(1, 1048586);
		test.commandTLB(1, 1049088);
		*/
		

		/*
		 * 	1 10240
			0 1 11264
			Input VA Operations:
			1 1027 // which is 1027 = (0, 2, 3) = (s, p, w) in base 10
			Output:
			1539
		 */
		
		//System.out.println(test.PMArray[PMArray[2] +1]);
		//System.out.println(test.bm.BM[5]);
}
	

