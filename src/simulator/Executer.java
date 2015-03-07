package simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;


/**
 * @author #Group one
 * This is the class we use to complete the job controller in CPU does.
 * We should use this class Every time we need to operate a program.
 * When we start a program, use the start() method, executer get every instruction from memory one by one
 * And then operate each instruction use the specific method as we defined here, like LDA or STX, etc, to the end of this program
 * 
 */
public class Executer implements Parameters{
	
	Simulator mySimulator;
	
	//I,IX,AC,OPCODE,address is use to store each part of the instruction after we decode.
	String I, IX, AC,OPCODE,F;
	int address,immed, devid,cntCharacter,count,cntLength;
	String LorR,AorL;
	
	/**
	 * flag is used to check which operation mode we use
	 * **The two mode option is normal run, and debug mode
	 * **If we use the debug, the program will only go through one single instruction we press the debug button
	 * 
	 * notEnd is used to check if the program reaches the end 
	 */
	int flag = 1, notEnd = 1;
	
	/**
	 * We named those register exactly as we did in Class Register
	 * And actually they refer to the same thing.
	 * We use a simple name instead of using mySimulator.PC just for the readability.
	 */
	Register PC,MAR,IR,X0,RS1,RS2,op1,op2,result, remainder,F_op1,F_op2,F_result,CC,MFR;
	Register [] GPR;
	Register [] FR;
	REG_MDR MDR;
	ALU ALU;
	VectorProcessor VP;
	 
	public Executer(Simulator mySimulator)
	{
		this.mySimulator = mySimulator;
		this.I = null;
		this.IX = null;
		this.AC = null;
		this.OPCODE = null;
		
		this.cntCharacter = 1;
		this.cntLength = 0;
		
		this.PC = mySimulator.PC;
		this.MAR = mySimulator.MAR;
		this.MDR = mySimulator.MDR;
		this.X0 = mySimulator.X0;
		this.GPR = mySimulator.GPR;
		this.FR = mySimulator.FR;
		this.RS1 = mySimulator.RS1;
		this.RS2 = mySimulator.RS2;
		this.IR = mySimulator.IR;
		
		this.ALU = mySimulator.myALU;
		this.op1 = mySimulator.myALU.op1;
		this.op2 = mySimulator.myALU.op2;
		this.result = mySimulator.myALU.result;
		this.remainder = mySimulator.myALU.remainder;
		this.F_op1 = mySimulator.myALU.F_op1;
		this.F_op2 = mySimulator.myALU.F_op2;
		this.F_result = mySimulator.myALU.F_result;
		
		this.CC = mySimulator.CC;
		this.MFR = mySimulator.MFR;
		
		this.VP = new VectorProcessor(mySimulator);
	}
	
	/**
	 * This is the start of every program we want the simulator to operate
	 * This method goes like this:
	 * * PC get the first address of this program in memory, then start operation
	 * * operation was a circle like this:
	 * * * fetch the instruction from the memory
	 * * * decode the instruction
	 * * * use specific method operate the instruction
	 * * * till the end of this program
	 */
	public void start()
	{
		/**
		 * mySimulator.ui.display() is what we use to display what is currently operate now
		 * It may be a little confused we put it here instead of in Class UI
		 * We may change it to make the construction clearer.
		 */
		PC.put(initAddr);
		mySimulator.ui.display("\n\nPC <-- initial Address\n");
		
		notEnd = 1;
		
		while(flag == 1 && notEnd == 1)
		{
			mySimulator.ui.display("\nfetch\n");
			fetch();
			
			deCode(IR.data);		
			
			//execute
			
			/*
			 * Decide which instruction it is, and then use the corresponding method
			 */
			int opcodeInt = Integer.parseInt(OPCODE);
			switch (opcodeInt)
			{
			case 0:
				HLT();break;
			case 1:
				LDR();break;
			case 10:
				STR();break;
			case 11:
				LDA();break;
			case 100001:
				LDX();break;
			case 100010:
				STX();break;
			case 100:
				AMR();break;
			case 101:
				SMR();break;
			case 110:
				AIR();break;
			case 111:
				SIR();break;
			case 1000:
				JZ();break;
			case 1001:
				JNE();break;
			case 1010:
				JCC();break;
			case 1011:
				JMP();break;
			case 1100:
				JSR();break;
			case 1101:
				RFS();break;
			case 1110:
				SOB();break;
			case 10000:
				MUL();break;
			case 10001:
				DIV();break;
			case 10010:
				TER();break;
			case 10011:
				AND();break;
			case 10100:
				ORR();break;
			case 10101:
				NOT();break;
			case 11000:
				TRAP();break;
			case 110001:
				IN();break;
			case 110010:
				OUT();break;
			case 110011:
				CHK();break;
			case 11001:
				SRC();break;
			case 11010:
				RRC();break;
			case 1111:
				JGE();break;
			case 11011:
				FADD();break;
			case 11100:
				FSUB();break;
			case 11101:
				VADD();break;
			case 11110:
				VSUB();break;
			case 11111:
				CNVRT();break;
			case 100000:
				LDFR();break;
			case 100011:
				STFR();break;
				
			}
			
			PC.data++;
			mySimulator.ui.refresh();
			
			///*
			//The code below is for test
			if(PC.data == mySimulator.myASMLoader.codeLine)
			{
				for(int i=63*256;i<63+63*256;i++)
				{
					mySimulator.MAR.put(i);
					mySimulator.MDR.getMem();
					Integer a = mySimulator.MDR.get();
					System.out.println(a);
				}
				//notEnd = 0;
			}
			//*/
			
			if(PC.data >= mySimulator.myASMLoader.codeLine)
			{
				notEnd = 0;
			}
		}
	}
	
	/**
	 * We use this method when do debug mode
	 * It not finished yet
	 * can only do single instruction now, not single circle
	 */
	public void startSingle(int count)
	{
		/**
		 * mySimulator.ui.display() is what we use to display what is currently operate now
		 * It may be a little confused we put it here instead of in Class UI
		 * We may change it to make the construction clearer.
		 */
		
		if(count == 0)
			PC.put(initAddr + count);
		mySimulator.ui.display("\n\nPC <-- instruction Address\n");
				
		mySimulator.ui.display("\nfetch\n");
		fetch();
		
		deCode(IR.data);		
		
		//execute
		
		/*
		 * Decide which instruction it is, and then use the corresponding method
		 */
		int opcodeInt = Integer.parseInt(OPCODE);
		switch (opcodeInt)
		{
		case 0:
			HLT();break;
		case 1:
			LDR();break;
		case 10:
			STR();break;
		case 11:
			LDA();break;
		case 100001:
			LDX();break;
		case 100010:
			STX();break;
		case 100:
			AMR();break;
		case 101:
			SMR();break;
		case 110:
			AIR();break;
		case 111:
			SIR();break;
		case 1000:
			JZ();break;
		case 1001:
			JNE();break;
		case 1010:
			JCC();break;
		case 1011:
			JMP();break;
		case 1100:
			JSR();break;
		case 1101:
			RFS();break;
		case 1110:
			SOB();break;
		case 10000:
			MUL();break;
		case 10001:
			DIV();break;
		case 10010:
			TER();break;
		case 10011:
			AND();break;
		case 10100:
			ORR();break;
		case 10101:
			NOT();break;
		case 11000:
			TRAP();break;
		case 110001:
			IN();break;
		case 110010:
			OUT();break;
		case 110011:
			CHK();break;
		case 11001:
			SRC();break;
		case 11010:
			RRC();break;
		case 1111:
			JGE();break;
		case 11011:
			FADD();break;
		case 11100:
			FSUB();break;
		case 11101:
			VADD();break;
		case 11110:
			VSUB();break;
		case 11111:
			CNVRT();break;
		case 100000:
			LDFR();break;
		case 100011:
			STFR();break;
		}
		
		PC.data++;
		mySimulator.ui.refresh();
		
		//mySimulator.ui.display(PC.data +"  haha\n");
		//if(mySimulator.myMemory.memoryCell[PC.data] == 0)
		
		if(PC.data >= mySimulator.myASMLoader.codeLine)
		{
			notEnd = 0;
		}
	}
	
	/**
	 * Decode
	 * As the binary instruction in memory ignore 0 in the beginning, like 001 was stored as 1, We decode from the last bit.
	 * Then put each part to the corresponding buffer. 
	 */
	public void deCode(Integer instruction)
	{
		mySimulator.ui.display("\n");
		
		String instr = Integer.toBinaryString(instruction);
		int length = instr.length();
		OPCODE = instr.substring(0,length-10);
		
		//String can not be the flag of switch, so we translate it into integer.
		int opcode = Integer.parseInt(OPCODE);
		
		switch(opcode)
		{
			case 1010:
				I = instr.substring(length-10,length-9);//6,6
				IX = instr.substring(length-9,length-8);//7,7
				AC = instr.substring(length-8,length-6);//8,9
				CC.put(Integer.parseInt(AC, 2));
				address = Integer.parseInt(instr.substring(length-6, length), 2);//10,15
				mySimulator.ui.display("\ndecode\n");
				mySimulator.ui.display("opcode <-- IR[0-5]\nI <-- IR[6]\nIX <-- IR[7]\nCC <-- IR[8-9]\nADDR <-- IR[10]-15\n");
				break;
			/*case 111:case 110:
				immed = Integer.parseInt(instr.substring(length-6,length),2);
				mySimulator.ui.display("\ndecode\n");
				mySimulator.ui.display("opcode <-- IR[0-5]\nimmed <-- IR[10]-15\n");
				break;*/
			case 10000:case 10001:case 10010:case 10011:case 10100:
				RS1.put(Integer.parseInt(instr.substring(length-10,length-8),2));
				RS2.put(Integer.parseInt(instr.substring(length-8,length-6),2));
				break;
			case 10101:
				RS1.put(Integer.parseInt(instr.substring(length-10,length-8),2));
				break;
			case 110001:case 110010:case 110011:
				AC = instr.substring(length-9,length-7);//7,8 
				RS1.put(Integer.parseInt(AC, 2));
				devid = Integer.parseInt(instr.substring(length-5, length), 2);
				break;
			case 11001:case 11010:
				//System.out.println(instr);
				LorR = instr.substring(5, 6);
				AC = instr.substring(6,8);
				RS1.put(Integer.parseInt(AC, 2));
				AorL = instr.substring(8,9);
				count = Integer.parseInt(instr.substring(11, 15), 2);
				break;
			case 11111:
				I = instr.substring(length-10,length-9);//6,6
				IX = instr.substring(length-9,length-8);//7,7
				F = instr.substring(length-8,length-6);//8,9
				address = Integer.parseInt(instr.substring(length-6, length), 2);//10,15
			default:
				I = instr.substring(length-10,length-9);//6,6
				IX = instr.substring(length-9,length-8);//7,7
				AC = instr.substring(length-8,length-6);//8,9
				RS1.put(Integer.parseInt(AC, 2));
				address = Integer.parseInt(instr.substring(length-6, length), 2);//10,15
				
				mySimulator.ui.display("\ndecode\n");
				mySimulator.ui.display("opcode <-- IR[0-5]\nI <-- IR[6]\nIX <-- IR[7]\nRS1 <-- IR[8-9]\nADDR <-- IR[10]-15\n");
				break;	
		}
		
		
	}
	
	
	/*
	 * The methods below all speak for themselves
	 * we can get what they do just from the name they use and the display sentences below
	 * Basically, they do every micro-instruction like the real machine do.
	 */
	public void fetch()
	{
		mySimulator.ui.display("\n");
		
		mySimulator.myMemory.ifFetch = 1;
		
		MAR.put(PC);
		mySimulator.ui.display("fetch M(MAR) PCVALUE:"+PC.data+"\n");
		
		MDR.getMem();
		mySimulator.ui.display("MDR <-- M(MAR)\n");

		IR.put(MDR.get());	
		mySimulator.ui.display("IR <-- C(MDR)\n");
		
		mySimulator.myMemory.ifFetch = 0;
	}
	
	public void transferR2M(int memoryAddress, Register R)
	{
		MDR.put(R);
		MAR.put(memoryAddress);
		MDR.putMem();
	}
	
	public void machineFault(int faultID)
	{
		mySimulator.ui.display("!!!!!machineFault"+faultID+"!!!!!\n");
		MFR.put(faultID);
		transferR2M(fault_temp,IR);
		transferR2M(fault_temp+1,PC);
		transferR2M(fault_temp+2,X0);
		transferR2M(fault_temp+3,GPR[0]);
		transferR2M(fault_temp+4,GPR[1]);
		transferR2M(fault_temp+5,GPR[2]);
		transferR2M(fault_temp+6,GPR[3]);

		MAR.put(1);
		MDR.getMem();
		
		
		
		PC.put(MDR);
	}
	
	public void SRC()
	{
		mySimulator.ui.display("\n");
		op1.put(GPR[RS1.get()].get());
		mySimulator.ui.display("op1 <-- RF(RS1)\n");
		
		ALU.shift(count,LorR,AorL);
		
		GPR[RS1.get()].put(result.get());
		mySimulator.ui.display("RF(RS1) <-- result\n");
	}
	
	public void RRC()
	{
		mySimulator.ui.display("\n");
		op1.put(GPR[RS1.get()].get());
		mySimulator.ui.display("op1 <-- RF(RS1)\n");
		
		ALU.rotate(count,LorR,AorL);
		
		GPR[RS1.get()].put(result.get());
		mySimulator.ui.display("RF(RS1) <-- result\n");
	}
	
	public void HLT()
	{
		mySimulator.ui.display("\n");
		
		notEnd = 0;
		mySimulator.ui.display("Simulator Has Stopped in HLT!\n");
	}
	
	public void TRAP()
	{
		mySimulator.ui.display("TRAP START\n");
		
		if (GPR[RS1.get()].get()<0 || GPR[RS1.get()].get()>15)
		{
			machineFault(1);
			return;
		}	

		op1.put(GPR[RS1.get()].get());	
		mySimulator.ui.display("op1 <-- R"+RS1.get()+"\n");
		
		
		MAR.put(0);
		MDR.getMem();
		op2.put(MDR);
		mySimulator.ui.display("op2 <-- Memory(0)\n");
		//mySimulator.ui.display("op2 <-- RF(RS2)");
		
		ALU.and();
		
		MDR.put(PC);
		GPR[3].put(MDR);
			
		PC.put(result.get());
			
		mySimulator.ui.display("TRAP OK!\n");
	}
	
	public void LDR()
	{
		mySimulator.ui.display("\n");
		
		MAR.put(getEA("MAR"));
		
		MDR.getMem();
		mySimulator.ui.display("MDR <-- M(MAR)\n");
		
		
		GPR[RS1.data].put(MDR);
		mySimulator.ui.display("RF(RS1) <-- MDR\n");
	}
	
	public void STR()
	{
		mySimulator.ui.display("\n");
		
		MAR.put(getEA("MAR"));
		
		MDR.put(GPR[RS1.data]);
		mySimulator.ui.display("MDR <-- RF(RS1)\n");
		
		MDR.putMem();
		mySimulator.ui.display("M(MAR) <-- MDR\n");
	}
	
	public void LDA()
	{
		mySimulator.ui.display("\n");
		
		
		MDR.put(getEA("MDR"));
		GPR[RS1.data].put(MDR);
		mySimulator.ui.display("RF(RS1) <-- MDR\n");
	}
	
	public void LDX()
	{
		mySimulator.ui.display("\n");
		
		MAR.put(getEA("MAR"));
		
		MDR.getMem();
		mySimulator.ui.display("MDR <-- M(MAR)\n");
		
		X0.put(MDR);
		mySimulator.ui.display("X0 <-- MDR\n");
		
	}
	
	public void STX()
	{
		mySimulator.ui.display("\n");
		
		MAR.put(getEA("MAR"));
		
		MDR.put(X0);
		mySimulator.ui.display("MDR <-- X0\n");
		
		MDR.putMem();
		mySimulator.ui.display("MDR <-- M(MAR)\n");
	}
	
	
	public void MUL()
	{
		mySimulator.ui.display("\n");
		//if((RS1.get()==0 || RS1.get()==2) && (RS2.get()==0 || RS2.get()==2))
		op1.put(GPR[RS1.get()].get());
		mySimulator.ui.display("op1 <-- RF(RS1)");
		
		op2.put(GPR[RS2.get()].get());
		mySimulator.ui.display("op2 <-- RF(RS2)");
		
		ALU.multiply();
		
		/*TEST
		int length = Integer.toBinaryString(result.get()).length();
		String s = Integer.toBinaryString(result.get());
		System.out.println(RS1.get()+"\n"+RS2.get()+"\n"+length+"\n"+op2.get());
		*/
		int length = Integer.toBinaryString(result.get()).length();
		if(length>16)
		{
			//System.out.print(Integer.toBinaryString(result.get()).substring(length - 16, length));
			GPR[RS1.get()].put(Integer.parseInt(Integer.toBinaryString(result.get()).substring(0, length - 16),2));
			GPR[RS1.get() + 1].put(Integer.parseInt(Integer.toBinaryString(result.get()).substring(length - 16, length),2)); 
		}else{
			GPR[RS1.get()].put(0);
			GPR[RS1.get() + 1].put(Integer.parseInt(Integer.toBinaryString(result.get()),2)); 
		}
		mySimulator.ui.display("RF(RS1) <-- result\n");
		
		//WRONG CODE
		//GPR[RS1.get() + 1].put(Integer.parseInt(Integer.toBinaryString(result.get()).substring(16, 32),2));
		//GPR[RS1.get()].put(Integer.parseInt(Integer.toBinaryString(result.get()).substring(0, 16),2)); 
	}
	
	public void DIV()
	{
		mySimulator.ui.display("\n");
		//if((RS1.get()==0 || RS1.get()==2) && (RS2.get()==0 || RS2.get()==2))
		if(GPR[RS2.get()].get() == 0)
		{
			CC.put(CC.get() | 4);
			mySimulator.ui.display("op2 cannot be zero!\n");
		}else{
			op1.put(GPR[RS1.get()].get());
			mySimulator.ui.display("op1 <-- RF(RS1)");
			
			op2.put(GPR[RS2.get()].get());
			mySimulator.ui.display("op2 <-- RF(RS2)");
			
			ALU.divide();
			
			GPR[RS1.get()].put(result.get());
			mySimulator.ui.display("RF(RS1) <-- result\n");
			
			GPR[RS1.get() + 1].put(remainder.get());
			mySimulator.ui.display("RF(RS1+1) <-- remainder\n");
		}
	}
	
	public void TER()
	{
		mySimulator.ui.display("\n");
		if(GPR[RS1.get()].get() == GPR[RS2.get()].get())
		{
			mySimulator.ui.display("If c(rx) = c(ry)\n");
			mySimulator.ui.display("set cc(4) <1\n )");
			CC.put(CC.get() | 4);
		}else{
			mySimulator.ui.display("else");
			mySimulator.ui.display("set cc(4) <0\n )");
			CC.put(CC.get() & 11);
		}
		//System.out.println(CC.get());
	}
	
	public void AND()
	{
		mySimulator.ui.display("\n");
		
		op1.put(GPR[RS1.get()].get());	
		mySimulator.ui.display("op1 <-- RF(RS1)\n");
		
		op2.put(GPR[RS2.get()].get());
		mySimulator.ui.display("op2 <-- RF(RS2)\n");
		
		ALU.and();
		
		GPR[RS1.get()].put(result.get());
		mySimulator.ui.display("RF(RS1) <-- result\n");
	}
	
	public void ORR()
	{
		mySimulator.ui.display("\n");
		
		op1.put(GPR[RS1.get()].get());
		mySimulator.ui.display("op1 <-- RF(RS1)\n");	
		
		op2.put(GPR[RS2.get()].get());
		mySimulator.ui.display("op2 <-- RF(RS2)\n");
		
		ALU.orr();
		
		GPR[RS1.get()].put(result.get());
		mySimulator.ui.display("RF(RS1) <-- result\n");
	}
	
	public void NOT()
	{
		mySimulator.ui.display("\n");
		op1.put(GPR[RS1.get()].get());
		mySimulator.ui.display("op1 <-- RF(RS1)\n");
		
		ALU.not();
		
		GPR[RS1.get()].put(result.get());
		mySimulator.ui.display("RF(RS1) <-- result\n");
		//GPR[RS1.get()].put(-6);
		//result.put(-9);
		
		//System.out.println(result.get()+"\n"+GPR[RS1.get()].get());
	}
	
	public void AMR()
	{
		mySimulator.ui.display("\n");
		
		MAR.put(getEA("MAR"));
		
		MDR.getMem();
		mySimulator.ui.display("MDR <-- M(MAR)\n");

		op1.put(GPR[RS1.data].get());
		mySimulator.ui.display("op1 <-- RF(RS1)\n");
		
		op2.put(MDR.get());
		mySimulator.ui.display("op2 <-- MDR\n");
		
		ALU.add();
		
		GPR[RS1.data].put(result);
		mySimulator.ui.display("RF(RS1) <-- C(RS1) + C(EA)\n");
	}
	
	public void SMR()
	{
		mySimulator.ui.display("\n");
		
		MAR.put(getEA("MAR"));
		
		MDR.getMem();
		mySimulator.ui.display("MDR <-- M(MAR)\n");
		
		op1.put(GPR[RS1.data].get());
		mySimulator.ui.display("op1 <-- RF(RS1)\n");
		
		op2.put(MDR.get());
		mySimulator.ui.display("op2 <-- MDR\n");
		
		ALU.sub();
		GPR[RS1.data].put(result);
		mySimulator.ui.display("RF(RS1) <-- C(RS1) - C(EA)\n");
	}
	
	public void AIR()
	{
		if (address != 0)
		{
			mySimulator.ui.display("\n");
			
			MDR.getMem();
			mySimulator.ui.display("MDR <-- M(MAR)\n");
			
			op1.put(GPR[RS1.data].get());
			mySimulator.ui.display("op1 <-- RF(RS1)\n");
			
			op2.put(address);
			mySimulator.ui.display("op2 <-- Immed\n");
			
			ALU.add();
			
			GPR[RS1.data].put(result);
			mySimulator.ui.display("RF(RS1) <-- C(RS1) + Immed\n");
		}
		else
			mySimulator.ui.display("Do nothing! Immed = 0!\n");
	}
	
	public void SIR()
	{
		if (address != 0)
		{
			mySimulator.ui.display("\n");
			
			MDR.getMem();
			mySimulator.ui.display("MDR <-- M(MAR)\n");
			
			op1.put(GPR[RS1.data].get());
			mySimulator.ui.display("op1 <-- RF(RS1)\n");
			
			op2.put(address);
			mySimulator.ui.display("op2 <-- Immed\n");
			
			ALU.sub();
			
			GPR[RS1.data].put(result);
			mySimulator.ui.display("RF(RS1) <-- C(RS1) - Immed\n");
		}
		else
			mySimulator.ui.display("Do nothing! Immed = 0!\n");
	}
	
	public void JZ()
	{
		mySimulator.ui.display("\n");
		
		if(GPR[RS1.get()].get()==0)
		{
			MDR.put(getEA("MDR"));
			PC.put(MDR);
			mySimulator.ui.display("PC <-- MDR PCVALUEinfunction:"+PC.data+"\n");
			PC.data--;
		}
	}
	
	public void JGE()
	{
		mySimulator.ui.display("\n");
		
		if(GPR[RS1.get()].get() >= 0)
		{
			MDR.put(getEA("MDR"));
			PC.put(MDR);
			mySimulator.ui.display("PC <-- MDR PCVALUEinfunction:"+PC.data+"\n");
			PC.data--;
		}
	}
	
	public void JNE()
	{
		mySimulator.ui.display("\n");
		
		if(GPR[RS1.get()].get()!=0)
		{
			MDR.put(getEA("MDR"));
			PC.put(MDR);
			mySimulator.ui.display("PC <-- MDR\n");
			PC.data--;
		}
	}
	
	public void JCC()
	{
		mySimulator.ui.display("\n");
		
		if(CC.get()==1)
		{
			MDR.put(getEA("MDR"));
			PC.put(MDR);
			mySimulator.ui.display("PC <-- MDR\n");
			PC.data--;
		}
	}
	
	public void JMP()
	{
		mySimulator.ui.display("\n");
		
		MDR.put(getEA("MDR"));
		PC.put(MDR);
		mySimulator.ui.display("PC <-- MDR\n");
		PC.data--;
	}
	
	public void JSR()//R0 should contain pointer to arguments Argument list should end with 鈥��777 value
	{
		mySimulator.ui.display("\n");
		
		GPR[0].put(argument_entrance);
		GPR[3].put(PC.get()+1);
		mySimulator.ui.display("R3 <-- PC+1\n");
		MDR.put(getEA("MDR"));
		PC.put(MDR);
		mySimulator.ui.display("PC <-- MDR\n");
		PC.data--;
	}
	
	public void RFS()//Register to Register?
	{
		mySimulator.ui.display("\n");
		
		GPR[0].put(immed);
		mySimulator.ui.display("R0 <-- immd\n");
		
		MDR.put(GPR[3]);
		mySimulator.ui.display("MDR <-- R3\n");
		
		PC.put(MDR);
		mySimulator.ui.display("PC <-- MDR\n");
		
		PC.data--;
	}
	
	public void SOB()
	{
		mySimulator.ui.display("\n");
		
		GPR[RS1.get()].data--;
		
		if(GPR[RS1.get()].get()>0)
		{
			MDR.put(getEA("MDR"));
			PC.put(MDR);
			mySimulator.ui.display("PC <-- MDR\n");
			PC.data--;
		}
	}
	
	public void IN()
	{
		if (devid == 0)
		{
			if (cntLength == 0)
			{
				String s;
				s = JOptionPane.showInputDialog("Input the "+ String.valueOf(cntCharacter)+ "th number:");
				cntLength = s.length();
				//System.out.print(cntLength+"cntLength!\n");
				mySimulator.keyboard.putBuffer(s);
				mySimulator.keyboard.status.put(1);
				mySimulator.ui.display("the" + String.valueOf(cntCharacter) + "th character you enter is:"+ s);
			}
			GPR[RS1.data].put(mySimulator.keyboard.getBuffer());
			cntLength--;
			if (cntLength == 0)
			{
				cntCharacter++;
				mySimulator.keyboard.status.put(0);
			}
		}
		else if (devid == 2)
		{
			mySimulator.ui.display("Input the character from cardReader.txt in current director");
	    	
			String fileN;
			fileN = "cardReader.txt";
			try {
				BufferedReader in2;
				File file = new File(fileN);
				file.createNewFile();
				in2 = new BufferedReader(new FileReader(file));
				GPR[RS1.data].put(Integer.parseInt(in2.readLine()));
				in2.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				mySimulator.ui.display("error in loading file\n");
				e1.printStackTrace();
			}
		}
		else
		{
		    mySimulator.ui.display("Device is not available!");
		}
	}	
	
	
	public void OUT()
	{
		if (devid == 1)
		{
			//mySimulator.ui.display1("Output the character to Console");
			//mySimulator.ui.display("The character in R["+RS1.data.toString()+"] is " + GPR[RS1.data].data.toString()+ "\n\n");
			mySimulator.ui.display1(GPR[RS1.data].data.toString()+"\n");
			
		}
		else
		{
		    mySimulator.ui.display("Device is not available!");
		}
	}
	
	public void CHK()
	{
		GPR[RS1.get()].put(mySimulator.keyboard.status);
	}
	
	/**
	 * For this simple demonstration of vector computing, we implement these functions as a method instead of building a new class
	 * These two methods below simulate the vector processor which do vector computing in pipeline 
	 */
	public void VADD()
	{
		mySimulator.ui.display("\n");
		
		MAR.put(getEA("MAR"));
		
		MDR.getMem();
		mySimulator.ui.display("MDR <-- M(MAR)\n");
		
		VP.setAddr1(MDR);
		
		MAR.put(getEA("MAR")+1);
		
		MDR.getMem();
		mySimulator.ui.display("MDR <-- M(MAR)\n");
		
		VP.setAddr2(MDR);
		
		VP.add();
		
	}
	
	public void VSUB()
	{
		mySimulator.ui.display("\n");
		
		MAR.put(getEA("MAR"));
		
		MDR.getMem();
		mySimulator.ui.display("MDR <-- M(MAR)\n");
		
		VP.setAddr1(MDR.get());
		
		MAR.put(getEA("MAR")+1);
		
		MDR.getMem();
		mySimulator.ui.display("MDR <-- M(MAR)\n");
		
		VP.setAddr2(MDR.get());
		
		VP.sub();
	}
	
	public void FADD()
	{
		mySimulator.ui.display("\n");
		
		String s1;
		
		MAR.put(getEA("MAR"));
		
		MDR.getMem();
		mySimulator.ui.display("MDR <-- M(MAR)\n");
		
		s1 = Integer.toString(MDR.get(),2);
		
		MAR.put(getEA("MAR")+1);
		
		MDR.getMem();
		mySimulator.ui.display("MDR <-- M(MAR)\n");
		String s2 = Integer.toString(MDR.get(),2);
		int length = s2.length();
		if(length < 16)
			for(int i=16-length;i>0;i--)
				s2 = "0".concat(s2);
		
		s1 = s1.concat(s2);
		Integer i = Integer.parseInt(s1, 2);
		
		F_op1.put(i);
		mySimulator.ui.display("F_op1 <-- C(MAR)+C(MAR+1）");
		
		F_op2.put(FR[RS1.get()].get());
		mySimulator.ui.display("op1 <-- FR(RS1)\n");
	
		ALU.F_add();
		
		/**
		 * split 32 bits into two 16 bits parts
		 */
		Integer f = F_result.get();
		String s = Integer.toString(f,2);
		length = s.length();
		s1 = s.substring(0, length-16);
		s2 = s.substring(length-16, length);
		Integer part1 = Integer.parseInt(s1, 2);
		Integer part2 = Integer.parseInt(s2, 2);
		mySimulator.ui.display("c(FR) -->First16bits,Last16bits\n");
		
		/**
		 * First 16 bits store in EA
		 */
		MAR.put(getEA("MAR"));
		
		MDR.put(part1);
		mySimulator.ui.display("First16bits --> MDR\n");
		
		MDR.putMem();
		mySimulator.ui.display("c(EA) <-- MDR\n");
		
		/**
		 * Last 16 bits store in EA+1
		 */
		MAR.put(getEA("MAR")+1);
		
		MDR.put(part2);
		mySimulator.ui.display("Last16bits --> MDR\n");
		
		MDR.putMem();
		mySimulator.ui.display("c(EA+1) <-- MDR\n");
	}
	
	public void FSUB()
	{
		mySimulator.ui.display("\n");
		
		String s1;
		
		MAR.put(getEA("MAR"));
		
		MDR.getMem();
		mySimulator.ui.display("MDR <-- M(MAR)\n");
		
		s1 = Integer.toString(MDR.get(),2);
		
		MAR.put(getEA("MAR")+1);
		
		MDR.getMem();
		mySimulator.ui.display("MDR <-- M(MAR)\n");
		String s2 = Integer.toString(MDR.get(),2);
		int length = s2.length();
		if(length < 16)
			for(int i=16-length;i>0;i--)
				s2 = "0".concat(s2);
		
		s1 = s1.concat(s2);
		Integer i = Integer.parseInt(s1, 2);
		
		F_op1.put(i);
		mySimulator.ui.display("F_op1 <-- C(MAR)+C(MAR+1）");
		
		F_op2.put(FR[RS1.get()].get());
		mySimulator.ui.display("op1 <-- FR(RS1)\n");
	
		ALU.F_sub();
		
		/**
		 * split 32 bits into two 16 bits parts
		 */
		Integer f = F_result.get();
		String s = Integer.toString(f,2);
		length = s.length();
		s1 = s.substring(0, length-16);
		s2 = s.substring(length-16, length);
		Integer part1 = Integer.parseInt(s1, 2);
		Integer part2 = Integer.parseInt(s2, 2);
		mySimulator.ui.display("c(FR) -->First16bits,Last16bits\n");
		
		/**
		 * First 16 bits store in EA
		 */
		MAR.put(getEA("MAR"));
		
		MDR.put(part1);
		mySimulator.ui.display("First16bits --> MDR\n");
		
		MDR.putMem();
		mySimulator.ui.display("c(EA) <-- MDR\n");
		
		/**
		 * Last 16 bits store in EA+1
		 */
		MAR.put(getEA("MAR")+1);
		
		MDR.put(part2);
		mySimulator.ui.display("Last16bits --> MDR\n");
		
		MDR.putMem();
		mySimulator.ui.display("c(EA+1) <-- MDR\n");
	}
	
	public void LDFR()
	{
		mySimulator.ui.display("\n");
		
		String s1;
		
		MAR.put(getEA("MAR"));
		
		MDR.getMem();
		mySimulator.ui.display("MDR <-- M(MAR)\n");
		
		s1 = Integer.toString(MDR.get(),2);
		
		MAR.put(getEA("MAR")+1);
		
		MDR.getMem();
		mySimulator.ui.display("MDR <-- M(MAR)\n");
		String s2 = Integer.toString(MDR.get(),2);
		int length = s2.length();
		if(length < 16)
			for(int i=16-length;i>0;i--)
				s2 = "0".concat(s2);
		
		s1 = s1.concat(s2);
		Integer i = Integer.parseInt(s1, 2);
		
		FR[RS1.get()].put(i);
		mySimulator.ui.display("FR(RS1) <-- C(MAR)+C(MAR+1）");
	}
	
	
	public void STFR()
	{
		mySimulator.ui.display("\n");
		
		/**
		 * split 32 bits into two 16 bits parts
		 */
		Integer f = FR[RS1.get()].get();
		String s = Integer.toString(f,2);
		int length = s.length();
		String s1 = s.substring(0, length-16);
		String s2 = s.substring(length-16, length);
		Integer part1 = Integer.parseInt(s1, 2);
		Integer part2 = Integer.parseInt(s2, 2);
		mySimulator.ui.display("c(FR) -->First16bits,Last16bits\n");
		
		/**
		 * First 16 bits store in EA
		 */
		MAR.put(getEA("MAR"));
		
		MDR.put(part1);
		mySimulator.ui.display("First16bits --> MDR\n");
		
		MDR.putMem();
		mySimulator.ui.display("c(EA) <-- MDR\n");
		
		/**
		 * Last 16 bits store in EA+1
		 */
		MAR.put(getEA("MAR")+1);
		
		MDR.put(part2);
		mySimulator.ui.display("Last16bits --> MDR\n");
		
		MDR.putMem();
		mySimulator.ui.display("c(EA+1) <-- MDR\n");
	}
	
	public void CNVRT()
	{
		mySimulator.ui.display("\n");
		
		if(F.equals("00"))
		{
			String s1;
			
			MAR.put(getEA("MAR"));
			
			MDR.getMem();
			mySimulator.ui.display("MDR <-- M(MAR)\n");
			
			s1 = Integer.toString(MDR.get(),2);
			
			MAR.put(getEA("MAR")+1);
			
			MDR.getMem();
			mySimulator.ui.display("MDR <-- M(MAR)\n");
			String s2 = Integer.toString(MDR.get(),2);
			int length = s2.length();
			if(length < 16)
				for(int i=16-length;i>0;i--)
					s2 = "0".concat(s2);
			
			s1 = s1.concat(s2);
			Integer i = Integer.parseInt(s1, 2);

			float f = Float.intBitsToFloat(i);
			mySimulator.ui.display("combine EA,EA+1\n");
			
			int a = (int)f;
			mySimulator.ui.display("convert float to int\n");
			
			GPR[0].put(a);
			mySimulator.ui.display("R0 <-- result");
			
		}
		else
		{
			MAR.put(getEA("MAR"));
			
			MDR.getMem();
			mySimulator.ui.display("MDR <-- M(MAR)\n");
			
			int a = MDR.get();
			float f = (float)a;
			mySimulator.ui.display("float <-- c(EA)");
			
			Integer i = Float.floatToIntBits(f);
			FR[0].put(i);
			mySimulator.ui.display("FR[0] <-- result");
			
			//System.out.println(FR[0].get());
		}
		
	}

	
	/**
	 * Every time we use I,IX,address to generate the effective address, we call this method
	 * In this method, we test if flag I/IX == o, to decide if we need a indirect address or add the content of Register IX to the address
	 * @param name is what we use to display where the data goes to, just for the UI part.
	 * @return Effective Address
	 */
	public Integer getEA(String name)
	{
		if(I.equalsIgnoreCase("0"))
		{
			if(IX.equalsIgnoreCase("0"))
			{
				
				mySimulator.ui.display(name + " <-- address\n");
				return address;
				
			}else
			{
				
				op1.put(address);
				mySimulator.ui.display("op1 <-- address\n");
				
				op2.put(X0);
				mySimulator.ui.display("op2 <-- X0\n");
				
				ALU.add();
				
				mySimulator.ui.display(name + " <-- result\n");
				return result.data;
				
			}
		}else
		{
			if(IX.equalsIgnoreCase("0"))
			{
				MAR.put(address);
				mySimulator.ui.display("MAR <-- address\n");
				
				MDR.getMem();
				mySimulator.ui.display("MDR <-- M(MAR)\n");
				
				mySimulator.ui.display(name + " <-- MDR\n");
				return MDR.data;
			}else
			{
				op1.put(address);
				mySimulator.ui.display("op1 <-- address\n");
				
				op2.put(X0);
				mySimulator.ui.display("op2 <-- X0\n");
				
				ALU.add();
				
				MAR.put(result);
				mySimulator.ui.display("MAR <-- result\n");
				
				MDR.getMem();
				mySimulator.ui.display("MDR <-- M(MAR)\n");
				
				mySimulator.ui.display(name + " <-- MDR\n");
				return MDR.data;
			}
		}
	}
} 
