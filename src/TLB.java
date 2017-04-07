
import java.util.*;

public class TLB 
{
	int size = 4;
	holder [] TLBArray = new holder[4];
	boolean foundMatch = false;
	
	public TLB()
	{
		holder temp = new holder(-1, -1 , 0);
		for(int i = 0; i < size; i++)
		{
			temp = new holder(-1, -1, 0);
			TLBArray[i] = temp;
		}
	}
	
	public int findMatch(int sp, int w, int frameAddress)
	{
		int matchIndex = -1;
		int PA = -1;
		boolean foundMatch = false;
		for(int i = 0; i < TLBArray.length; i++)
		{
			//if sp == one of the 4 in TLB, foundMatch == true;
			if(TLBArray[i].getSp() == sp && TLBArray[i].getF() == frameAddress) 
			{
				foundMatch = true;
				matchIndex = i;
			}
		}
		
		if(foundMatch == true)
		{
			PA = TLBArray[matchIndex].getF() + w;
			
			for(int i = 0; i < TLBArray.length; i++)
			{
				if(TLBArray[matchIndex].getLRU() < TLBArray[i].getLRU())
				{
					int temp = TLBArray[i].getLRU();
					TLBArray[i].setLRU(temp-1);
				}
			}
			TLBArray[matchIndex].setLRU(3);
		}
		else
		{
			foundMatch = false;
		}
		
		
		return PA;
		
	}
	
	public void updateTLB(int sp, int f)
	{
		int updatedIndex = 0;
		
		for(int i = 0; i < TLBArray.length; i++)
		{
			if(TLBArray[i].getLRU() == 0)
			{
				TLBArray[i].setLRU(3);
				TLBArray[i].setSp(sp);
				TLBArray[i].setF(f);
				updatedIndex = i;
				break;
			}
		}
		
		for(int i = 0; i < TLBArray.length; i++)
		{
			if(TLBArray[i] == TLBArray[updatedIndex])
			{
				
			}
			else
			{
				int temp;
				temp = TLBArray[i].getLRU();
				if(temp > 0)
				{
					TLBArray[i].setLRU(temp-1);
				}
			}
		}
		
	}
}
