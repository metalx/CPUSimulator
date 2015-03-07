package simulator;
import java.io.IOException;


/**
 * @author Group #1
 * 
 * This is the main class that contains everything.
 * 
 * Firstly, it has a UI screen which can be used to operate the simulation.
 * And also, it includes everything elements a simulation needs, as listed blow and opreates them as a whole.
 * 
 * The elements it has:
 * Registers: 16bits-4GPRs,PC,MAR,MDR,
 * Memory
 * ALU
 * Instructor executor
 * ASMloader: load ASM program into memory
 * 
 */
public class Simulator implements Parameters{

	public UI ui;
	Register MAR,RS1,RS2,PC,IR,X0,CC,MFR;
	REG_MDR MDR;
	
	Register[] GPR = new Register[4];
	Register[] FR = new Register[2];
	
	Memory myMemory;
	Cache myCache;
	ALU myALU;
	ASMLoader myASMLoader;
	Executer myExecuter;
	Keyboard keyboard;
	VectorProcessor myVP;
	
	
	public Simulator()
	{
		ui = new UI(this);
		myALU = new ALU(this);
		myASMLoader = new ASMLoader(this);
		
		for(int i=0;i<4;i++)
		{
			GPR[i] = new Register(16,this);
		}
		for(int i=0;i<2;i++)
		{
			FR[i] = new Register(32,this);
		}
		
		MAR = new Register(16,this);
		MDR = new REG_MDR(16,this);
		PC = new Register(16,this);
		IR = new Register(16,this);
		X0 = new Register(16,this);
		RS1 = new Register(16,this);
		RS2 = new Register(16,this);
		CC = new Register(4,this);
		MFR = new Register(4,this);
		
		keyboard = new Keyboard(this);
		myMemory =  new Memory(this);
		myCache = new Cache(this);
		myMemory.myCache = myCache;
		myExecuter = new Executer(this);
		myVP = new VectorProcessor(this);
		
		this.myMemory.memoryBank[0%2][0/2] = routines_16;
		
	}
	public void RefressSm()
	{

		myALU = new ALU(this);
		myASMLoader = new ASMLoader(this);
		
		for(int i=0;i<4;i++)
		{
			GPR[i] = new Register(16,this);
		}
		for(int i=0;i<2;i++)
		{
			FR[i] = new Register(32,this);
		}
		
		MAR = new Register(16,this);
		MDR = new REG_MDR(16,this);
		PC = new Register(16,this);
		IR = new Register(16,this);
		X0 = new Register(16,this);
		RS1 = new Register(16,this);
		RS2 = new Register(16,this);
		CC = new Register(4,this);
		MFR = new Register(4,this);
		
		keyboard = new Keyboard(this);
		myMemory =  new Memory(this);
		myCache = new Cache(this);
		myMemory.myCache = myCache;
		myExecuter = new Executer(this);
		myVP = new VectorProcessor(this);
		
		this.myMemory.memoryBank[0%2][0/2] = routines_16;
		this.ui.refresh();
		
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		new Simulator();
		//System.out.println("asdf");
		
		
		/*ASMLoader myASMLoader = new ASMLoader(mySimulator);
		myASMLoader.getFile("./test.txt");
		myASMLoader.translate2MC();

		
		Executer myExecuter = new Executer(mySimulator);
		myExecuter.start();*/
		
		
		
	}

}
