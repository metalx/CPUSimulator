package simulator;


/**
 * @author Group #1
 * Only MDR can access memory, so we create this class extend from Register to describe MDR and his features.
 * 
 */
public class REG_MDR extends Register{
	
	Simulator mySimulator;

	public REG_MDR(int dataLength, Simulator mySimulator) {
		
		super(dataLength,mySimulator);
		this.mySimulator = mySimulator;
	}
	
	/**
	 * This is the method we use to simply the process we put data from the register to the memory
	 * It corresponds instructions like Memory(MAR) <-- MBR
	 * Only MDR can use this method!!!
	 * @param m is the memory we want to put data into
	 * @return none
	 */
	public void putMem()
	{
		mySimulator.myMemory.put(this.get());
	
	}


	/**
	 * This is the method we use to simply the process we get data from the memory to register
	 * It corresponds instructions like Fetch M(MAR),MBR <-- M(MAR)
	 * Only MBR can use this method!!!
	 * @param Destinate register we want to put data into.
	 * @return none
	 */
	public boolean getMem()
	{
		return put(mySimulator.myMemory.get());
		//boolean test = put(mySimulator.myMemory.get());
		//return test;
		
	}
	
	/*
	public boolean getMemLow()
	{
		return put(mySimulator.myMemory.get());
		//boolean test = put(mySimulator.myMemory.get());
		//return test;
		
	}
	*/
	
	public Integer get()
	{
		return this.data;
	}
	
}
