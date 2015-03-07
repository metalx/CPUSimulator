package simulator;

public class VectorProcessor {
	
	private Integer address1,address2;
	Simulator mySimulator;
	ALU ALU;
	Register R0,MAR,op1,op2,result;
	REG_MDR MDR;
	
	public VectorProcessor(Simulator mySimulator)
	{
		this.address1 = new Integer(0);
		this.address2 = new Integer(0);
		this.mySimulator = mySimulator;
		this.R0  = mySimulator.GPR[0];
		this.MAR = mySimulator.MAR;
		this.MDR = mySimulator.MDR;
		
		this.ALU = mySimulator.myALU;
		op1 = mySimulator.myALU.op1;
		op2 = mySimulator.myALU.op2;
		result = mySimulator.myALU.result;
	}

	public void setAddr1(Integer i)
	{
		address1 = i;
	}
	
	public void setAddr1(REG_MDR r)
	{
		Integer i = r.get();
		setAddr1(i);
	}
	
	public void setAddr2(Integer i)
	{
		address2 = i;
	}
	
	public void setAddr2(REG_MDR r)
	{
		Integer i = r.get();
		setAddr2(i);
	}
	
	private void nextPair()
	{
		address1++;
		address2++;
	}
	
	public void add()
	{
    	mySimulator.ui.display("\n");
		
		for(;R0.get()>0;R0Min1())
		{
			load();
			ALU.add();
			store();
			nextPair();
		}
	}
	
	public void sub()
	{
    	mySimulator.ui.display("\n");
    	for(;R0.get()>0;R0Min1())
		{
			load();
			ALU.sub();
			store();
			nextPair();
		}
	}
	
	private void load()
	{
	
		MAR.put(address1);
    	mySimulator.ui.display("MAR <-- address1\n");
    		
    	MDR.getMem();
    	mySimulator.ui.display("MDR <-- M(MAR)\n");

    	op1.put(MDR.get());
    	mySimulator.ui.display("op1 <-- RF(RS1)\n");
    		
    	MAR.put(address2);
    	mySimulator.ui.display("MAR <-- address1\n");
    		
    	MDR.getMem();
    	mySimulator.ui.display("MDR <-- M(MAR)\n");

    	op2.put(MDR.get());
    	mySimulator.ui.display("op1 <-- RF(RS1)\n");	
	}
	
	private void store()
	{
		MDR.put(result);
    	mySimulator.ui.display("MDR <-- result\n");
    		
    	MAR.put(address1);
    	mySimulator.ui.display("MAR <-- address1\n");
    		
    	MDR.putMem();
    	mySimulator.ui.display("M(MAR) <-- MDR\n");
	}

	
	public void R0Min1()
	{
		R0.put(R0.get()-1);
	}
}
