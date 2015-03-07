package simulator;

/**
 * @author Group#1
 * This is the cache class we implements.
 * We use direct-map and write-back as our strategy.
 * The cache size, include the total cache size, how many rows it has and how many blocks each row has, are defined in the Parameters class.
 * 
 * Data structure
 * cacheCell[][] is used to store data.Two parameter here refer to row and block number.
 * tag[] marks which block in memory the specific row stores.
 * dirty[] marks if the row has been changed since last update.
 * 
 * Method
 * main method:
 * *ifHit():decide if the data block is in cache
 * *replace(): if the block is not in cache, refresh cache according to our strategy.
 * *read(),write():get the data according to our strategy
 * 
 * other method:
 * *getRow():get the corresponding row number according to the given address
 * *getWord():get the corresponding word number in a row according to the given address
 * *getTag():get the corresponding tag number of specific row.
 */
public class Cache implements Parameters{
	
	Memory myMemory;
	Register MAR;
	Simulator mySimulator;
	
	int tag[] = new int[rowSize];
	boolean dirty[] = new boolean[rowSize];
	
	Integer cacheCell[][] = new Integer[rowSize][blockSize];

	public Cache(Simulator mySimulator)
	{
		this.mySimulator = mySimulator;
		this.myMemory = mySimulator.myMemory;
		this.MAR = mySimulator.MAR;
		
		for(int i=0;i<rowSize;i++)
		{
			tag[i] = -1;
			dirty[i] = false;
		}
	}
	
	/**
	 * read() is how  we get data from cache(memory)
	 * Firstly, it test if cache hits uses ifHit() function
	 * *if not,use replace() function to update cache
	 * then use getRow() and getWor() to find the required cache block
	 * @return required data
	 */
	public Integer read()
	{
		if(!ifHit())
			replace();
		//System.out.println(getRow()+"\n"+getWord()+"\n"+cacheCell[0][0]);
		return cacheCell[getRow()][getWord()];
	}
	
	/**
	 * write() is how we put data into cache(memory)
	 * The same as we do in the read(), firstly test if cache hits
	 * *if not true, i.e.the data is block is not in the cache, then directly change the data in memory and updated it in cache
	 * *if true, as we use write-back strategy,we only update the data in cache
	 * @param the data we want to write in
	 */
	public void write(Integer data)
	{
		int address = MAR.get().intValue();
		if(!ifHit())
		{
			//myMemory.memoryCell[address] = data;
			myMemory.memoryBank[address%bankSize][address/bankSize] = data;
			replace();
		}
		else{
			cacheCell[getRow()][getWord()] = data;
			dirty[getRow()] = true;
		}
	}
	
	/**
	 * replace is used both in write() and read()
	 * As we used write-back strategy,every time we update a row in cache
	 * we should firstly check if the row has been modified since last update.
	 * *if true, we should write this row back to update memory before we delete it from cache
	 * Then we use the given address to update an entire row
	 */
	private void replace()
	{
		int address = tag[getRow()] * blockSize;
		if(dirty[getRow()] == true)
			for(int i=0;i<blockSize;i++)
				//myMemory.memoryCell[address+i] = cacheCell[getRow()][i];
				myMemory.memoryBank[(address+i)%bankSize][(address+i)/bankSize] = cacheCell[getRow()][i];
				
		address = getTag()*blockSize;
		for(int i=0;i<blockSize;i++)
		{
			//cacheCell[getRow()[i] = myMemory.memoryCell[address+i];
			cacheCell[getRow()][i] = myMemory.memoryBank[(address+i)%bankSize][(address+i)/bankSize];
			//System.out.println(cacheCell[getRow()][i]);
		}
		tag[getRow()] = getTag();
	}
	
	/**
	 * ifHit() test if a specific data block is in the cache
	 * This test is to compare if the tag stored in a specific row(direct-map,only one row should be compare),is equal to the given tag. 
	 * The given tag is get from getTag()
	 * @return true if hits,false if not hits
	 */
	private boolean ifHit()
	{
		//System.out.println(getTag());
		if(getTag() == tag[getRow()])
			return true;
		else
			return false;		
	}
	
	/**
	 * we use this function to get the tag number of given address in the memory
	 * @return tag number for the given address
	 */
	private int getTag()
	{
		//Integer j = myMemory.memoryCell[1];
		/*System.out.println(6);
		Integer i = new Integer(0);
		
		MAR.put(i);
		//Integer i = MAR.get();*/
		//System.out.println(MAR.get());
		return (MAR.get().intValue()/blockSize);
	}
	
	/**
	 * we use this function to calculate which row should the data of given address be in.
	 * @return row number for the given address
	 */
	private int getRow()
	{
		return getTag()%rowSize;
	}
	
	/**
	 * As a row has several block, we use this function to decide which one the data should be in.
	 * @return block number in a row
	 */
	private int getWord()
	{
		return (MAR.get().intValue()%blockSize);
	}
	
}
