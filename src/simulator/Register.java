package simulator;
/**
 * @author Group #1
 * Class Register can be instantiated for every specific register for our simulator
 * 
 * It has two properties as blew:
 * *data contains the content of the register
 * *dataLength defines the length of data, which can be 1/2/4/16 for different types of registers
 * 
 * It has methods as show:
 * * get():get the data from register
 * * put():put the data into register
 * * putTo():put the data the register has to another register or the memory
 * * 		There is no need for a method called getFrom(), because it can be achieved by varying the sender and receiver.
 * * overflow():check if the data we want to put into the register bigger that it can hold
 * 
 * 
 */
public class Register {
	
	Integer data;
	int dataLength;
	
	Simulator mySimulator;//Register must have a reference of the main class to use UI(to display itself).
	
	public Register(int dataLength, Simulator mySimulator)
	{
		data = new Integer(0);
		this.dataLength = dataLength;
		this.mySimulator = mySimulator;
	}
		
	/**
	 * This is the method we use to set the content of the register
	 * @param s is the data we want to put into the register
	 * @return true if put succeed
	 */
	public boolean put(Integer s)
	{
		//System.out.print(Integer.toBinaryString(s)+"  adshfasdh\n");
		if(overflow(s) == true)
			return false;
		else
		{
			data = s;
			
			mySimulator.ui.refresh();
			return true;
		}
			
	}

	/**
	public boolean putLow(Integer s)
	{
		if(overflow(s,1) == true)
			return false;
		else
		{
			data = s;
			
			mySimulator.ui.refresh();
		}
			return true;
	}
	*/
	
	/**
	 * This is the method we use to simply the process we get the contents of a register to the current register
	 * It corresponds instructions like R0 --> MAR 
	 * @param r is the register we want to get data from
	 * @return true if succeed
	 */
	public boolean put(Register r)
	{
		return this.put(r.get());
	}
	
		
	/**
	 * This is the method we use to get the content of the current register
	 * @param none
	 * @return the content of the register. It should be a Integer as we defined above
	 */
	public Integer get()
	{
		return data;
	}
	
	/**
	 * This is the method is used to check if the data we want to put into the register larger than it can hold
	 * @param s is the data we want to put in
	 * @return true if the data larger than the register can hold, than is where a overflow occurs
	 * flag = 0: support negative integer
	 * flag = 1: cannot support negative integer
	 */
	public boolean overflow(Integer s)
	{
		double value = s.doubleValue();
		double low_bound = -StrictMath.pow(2,dataLength-1);
		double upper_bound = StrictMath.pow(2,dataLength);
		
		//System.out.println(low_bound+"  "+upper_bound);
		
		if(value > low_bound && value < upper_bound)
			return false;
		else
		{
			System.out.println("Too big to put into the register!");
			return true;
		}
		
		/**
		if (flag == 0)
		{
			if(s.intValue() < 0)
			s = -s;
		
			int size = Integer.toBinaryString(s).length();
		
			if(size > (dataLength-1))
			{
				System.out.println("The data is too large to put into this register!1");
				return true;
			}else{
				return false;
			}
		}
		else if (flag == 1)
		{
			int size = Integer.toBinaryString(s).length();
			if(size > dataLength)
			{
				System.out.println("The data is too large to put into this register!2");
				return true;
			}else{
				return false;
			}
		}
		else
			return true;
		*/
	}
	
	
}
