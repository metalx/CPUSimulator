package simulator;
/**
 * @author Group #1
 * Class Memory Implements a simple memory.
 * It has organized by Integer Array called memoryCell
 * memoryCell contains the data stored in the memory, which will all be initialed as null.
 * 
 * Class Memory has similar method as Register does:
 * * get():return the data from specific position of memory
 * * put():put the data into specific position of memory
 * * putTo():put the data from the specific position into a register
 *
 *We add cache into the memory system.
 *The cache is invisible to users and other parts of the Simulator.
 *We can just ask data from the memory. Memory will let Cache decide if it is a hit, and whether should the data come from cache or memroy.
 *
 */
public class Memory implements Parameters{
	
	
	int ifFetch = 0;
	
	Simulator mySimulator;
	//Integer memoryCell[] = new Integer[memorySize];
	Integer memoryBank[][] = new Integer[bankSize][memorySizePerBank];
	Cache myCache;
	
	public Memory(Simulator mySimulator)
	{
		
		this.mySimulator = mySimulator;
		/*
		 * No bank version
		for(int i=0;i<memorySize;i++)
			memoryCell[i] = 0;
		*/
		for(int j=0;j<memorySize/2;j++)
		{
			//old version, we charge it for the future extend
			//memoryBank[0][j] = 0;
			//memoryBank[1][j] = 0;
			for(int i=0;i<bankSize;i++)
			{
				memoryBank[i][j] = 0;
			}
		}
		
	}
	
	
	/**
	 * This is the method we use to get the content of the memory
	 * To get the data, we should firstly get the effective address from MAR
	 * Then we should also check if the EA out of the range of the memory
	 * If no, return the content
	 * 
	 * @param none
	 * @return null if out of memory;Or the content of specific address
	 */
	public Integer get()
	{
		int EA = mySimulator.MAR.get();
		
		//TEST
		if(EA == 63 && ifFetch == 0)
			mySimulator.MAR.put(500);
		if(EA == 62 && ifFetch == 0)
			mySimulator.MAR.put(501);
		
		if(EA > memorySize)
		{
			System.out.println("out of memory");
			return null;
		}else{
			//System.out.println(memoryCell[EA]+" "+EA);
			//System.out.println(myCache.read()+"\n");
			return myCache.read();
			
			//old version without cache
			//return memoryCell[EA];
		}
	}
	
	/**
	 * This is the method we use to get the data from the memory
	 * The same as the get() method, we should do the EA and overflow check first
	 * 
	 * @param Integer data is the content we want to put into.
	 * @return true if put succeed
	 */
	public boolean put(Integer data)
	{
		int EA = mySimulator.MAR.get();
		
		//TEST
				if(EA == 63 && ifFetch == 0)
					mySimulator.MAR.put(500);
				if(EA == 62 && ifFetch == 0)
					mySimulator.MAR.put(501);
		
		if(EA > memorySize)
		{
			System.out.println("out of memory");
			return false;
		}
		else
		{
			
			myCache.write(data);
			//System.out.println(memoryCell[EA]);
			//System.out.println(data+"\n");
			
			/*
			 //old version without cache
			 memoryCell[EA] = data;
			 */
			return true;
		}	
	}	
}
