package simulator;

/**
 * @author Group #1
 * One component of CPU
 * It has four Register named op1, op2, result,remainder
 * ALU uses the data of op1, op2 and then puts the result to result
 * remainder is used for storing the remainder of divide operation.
 *
 */
public class ALU {
	
	Register op1,op2,result,remainder;
	Register F_op1, F_op2, F_result;
	Simulator mySimulator;
	
	public ALU(Simulator mySimulator)
	{
		this.mySimulator = mySimulator;
		op1 = new Register(16,mySimulator);
		op2 = new Register(16,mySimulator);
		result = new Register(32,mySimulator);
		remainder = new Register(16,mySimulator);
		F_op1 = new Register(32,mySimulator);
		F_op2 = new Register(32,mySimulator);
		F_result = new Register(32,mySimulator);
	}
	
	/*
	 * The method here are all self-explained
	 * They do exactly the same things as their names showed.
	 */
	public boolean add()
	{
		mySimulator.ui.display("ADD op1, op2\n");
		return result.put(op1.get() + op2.get());		
	}
	
	public boolean sub()
	{
		mySimulator.ui.display("SUB op1, op2\n");
		return result.put(op1.get() - op2.get());
	}
	
	public boolean multiply()
	{
		mySimulator.ui.display("MUL op1, op2\n");
		return result.put(op1.get() * op2.get());
	}
	
	public boolean divide()
	{
		mySimulator.ui.display("DIV op1, op2\n");
		return result.put(op1.get() / op2.get()) && remainder.put(op1.get() % op2.get());
	}
	
	public boolean and()
	{
		//System.out.println(op1.get() & op2.get());
		mySimulator.ui.display("AND op1, op2\n");
		return result.put(op1.get() & op2.get());
	}
	
	public boolean orr()
	{
		//System.out.println();
		mySimulator.ui.display("ORR op1, op2\n");
		//System.out.println("pre-wrong\n");
		return result.put(op1.get() | op2.get());
	}
	
	public boolean not()
	{
		//System.out.println(~8);
		mySimulator.ui.display("NOT op1, op2\n");
		return result.put(~(op1.get()));
	}
	
	
	/**
	 * @param count
	 * @param RorL
	 * @param AorL
	 * @return
	 */
	public boolean shift(int count,String RorL,String AorL)
	{
		mySimulator.ui.display("SRC\n");
		
		String data = UI.to16(op1.get());
	
		if(AorL.compareTo("0") == 0)
		{
			
			String firstBit = data.substring(0, 1);
			data = data.substring(1,16);
			
			if(RorL.compareTo("0") == 0)
			{
				data = data.substring(0, 15-count);
				for(int i=count;i>0;i--)
					data = "0".concat(data);
			}
			else
			{
				data = data.substring(count,15);
				for(int i=count;i>0;i--)
					data = data.concat("0");
			}
			data = firstBit.concat(data);
		}
		else
		{
			if(RorL.compareTo("0") == 0)
			{
				data = data.substring(0, 16-count);
				for(int i=count;i>0;i--)
					data = "0".concat(data);
			}
			else
			{
				data = data.substring(count,16);
				for(int i=count;i>0;i--)
					data = data.concat("0");
			}
		}
		
		Integer i = Integer.parseInt(data, 2);
		return result.put(i);
		/*
		System.out.println(i);
		short s = (short)i.intValue();
		i = new Integer(s);
		System.out.println(i);
		return result.putLow(i);
		*/
	}
	
	public boolean rotate(int count,String RorL,String AorL)
	{
		mySimulator.ui.display("RRC\n");
		
		String data = UI.to16(op1.get());
		
		if(AorL.compareTo("a") == 0)
		{
			
			String firstBit = data.substring(0, 1);
			data = data.substring(1,16);
			
			if(RorL.compareTo("r") == 0)
			{
				String data1 = data.substring(0, 15-count);
				String data2 = data.substring(15-count, 15);
				data = data2.concat(data1);
			}
			else
			{
				String data1 = data.substring(count,15);
				String data2 = data.substring(0, count);
				data = data1.concat(data2);
			}
			data = firstBit.concat(data);
		}
		else
		{
			if(RorL.compareTo("r") == 0)
			{
				String data1 = data.substring(0, 16-count);
				String data2 = data.substring(16-count, 16);
				data = data2.concat(data1);
			}
			else
			{
				String data1 = data.substring(count,16);
				String data2 = data.substring(0, count);
				data = data1.concat(data2);
			}
		}
		
		Integer i = Integer.parseInt(data, 2);
		short s = (short)i.intValue();
		i = new Integer(s);
		
		return result.put(i);	
	}
	
	public boolean F_add()
	{
		mySimulator.ui.display("FADD op1 op2\n");
		
		Integer i = F_op1.get();
		Integer j = F_op2.get();
		
		float a = Float.intBitsToFloat(i);
		float b = Float.intBitsToFloat(j);
		float c = a + b;
		
		Integer k = Float.floatToIntBits(c);
		
		return F_result.put(k);
	}
	
	public boolean F_sub()
	{
		mySimulator.ui.display("FSUB op1 op2\n");
		Integer i = F_op1.get();
		Integer j = F_op2.get();
		
		float a = Float.intBitsToFloat(i);
		float b = Float.intBitsToFloat(j);
		float c = a - b;
		
		Integer k = Float.floatToIntBits(c);
		
		return F_result.put(k);
	}
}
