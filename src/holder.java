import java.util.*;

public class holder 
{
	int sp;
	int f;
	int LRU;
	
	public holder()
	{
		
	}
	
	public holder(int sp, int f, int LRU)
	{
		this.sp = sp;
		this.f = f;
		this.LRU = LRU;
	}

	public int getSp() {
		return sp;
	}

	public void setSp(int sp) {
		this.sp = sp;
	}

	public int getF() {
		return f;
	}

	public void setF(int f) {
		this.f = f;
	}

	public int getLRU() {
		return LRU;
	}

	public void setLRU(int lRU) {
		LRU = lRU;
	}
	
	
	
}
