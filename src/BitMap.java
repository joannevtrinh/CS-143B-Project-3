
public class BitMap 
{
	int size = 1024;
	int [] BM = new int[size];
	
	public BitMap()
	{
		BM[0] = 1; //ST occupation
		
		for(int i = 1; i < size; i++)
		{
			BM[i] = 0;
		}
	}

	public int[] getBM() 
	{
		return BM;
	}

	public void setBM(int[] bM)
	{
		BM = bM;
	}
	
	public void setBMSpacePT(int index)
	{
		int indexBM = index / 512;
		BM[indexBM] = 1;
		BM[indexBM + 1] = 1;
	}
	
	public void setBMSpaceP(int index)
	{
		int indexBM = index / 512;
		BM[indexBM] = 1;
	}
	
	public int findSpacePT()
	{
		
		for(int i = 1; i < size; i++)
		{
			if(BM[i] == 0 && BM[i+1] == 0)
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public int findSpaceP()
	{
		for(int i = 1; i < size; i++)
		{
			if(BM[i] == 0)
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public void updateBM()
	{
		
	}

	
	
}
