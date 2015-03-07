package simulator;
/**
 * @author Group #1
 * This class is use to load a .asm file which contain the program our simulator should run
 * Firstly, it will read a file from a default direction.
 * Then translate ASM into machine code
 * And finally, load them into memory for future use
 *
 */

import java.io.*;
import java.util.StringTokenizer;

public class ASMLoader implements Parameters{
	
	Simulator mySimulator;
	
	private int instrAddr;
	//private int count = 0; 
	Integer codeLine;
	
	public ASMLoader(Simulator mySimulator)
	{
		instrAddr = initAddr;
		codeLine = 2;
		this.mySimulator = mySimulator;
	}
	
	/**
	 * This is an enumeration of all instructions
	 */
	private enum Opcode 
	{  
		LDR, STR, LDA, LDX, STX,
		JZ, JNE, JCC, JMP, JSR, RFS, SOB,
		AMR, SMR, AIR, SIR,
		MUL, DIV, TER, AND, ORR, NOT, 
		SRC, RRC,
		IN, OUT, CHK,
		FADD, FSUB, VADD, VSUB, CNVRT,
		HLT, TRAP,
		JGE,
		LDFR, STFR;
	 } 
	
	/**
	 * This is the buffer keeps the context reads from asm file
	 */
	private BufferedReader asm;
	private BufferedReader table;
	
	
	/**
	 * read the context from designation.
	 * Type conversation is for the convenience of handle data
	 * @param direct the designation we read asm file from
	 * @return true if read succeed
	 * @throws IOException
	 */
	public boolean getFile(String direct) throws IOException
	{
		File file = new File(direct);
		file.createNewFile();
		FileReader input = new FileReader(file);
		asm = new BufferedReader(input);
		
		//UI-DISPLAY
		//String s;
		/*while((s = asm.readLine())!= null)
		{
			s = s + "\n";
			mySimulator.ui.display(s,6);
		}*/
		
		if(asm == null)
			return false;
		else
			return true;		
	}
	
	/**
	 * Transfer assembly instruction to machine code and save the machine code into memory
	 * @throws IOException
	 */
	public void translate2MC() throws IOException
	{
		String s, instr = null;
		Boolean rslt = true; // Compilation results flag
		
		mySimulator.ui.display("\nLoading Instructions......\n");
		
		while((s = asm.readLine())!= null)
		{			
			Boolean isInstr = false; // Instruction Validity Flag
			
			//s = s + "\n";
			//mySimulator.ui.display((codeLine++).toString()+":" + s,6);
			
			s = s.trim();
			int abbr = s.indexOf("//");
			
			if(s.equals(""))
			{
				s = s + "\n";
				mySimulator.ui.display(s,6);
				
			}
			else if(abbr == 0)
			{
				s = s + "\n";
				mySimulator.ui.display(s,6);
				
			}
			else
			{
				s = s + "\n";
				mySimulator.ui.display((codeLine++).toString()+": " + s,6);
						
				if(abbr != -1)
				{
					s = s.substring(0, abbr);
				}
				
				// Split Instruction
				StringTokenizer tokens = new StringTokenizer(s);
				if(tokens.countTokens() < 1)
					mySimulator.ui.display("Errors occured when get the instruction.\n");
				
				// Store Instruction Name
				String label = tokens.nextToken().toUpperCase();
				
				// Store Instruction Parameters
				String arg[] = new String[tokens.countTokens()];
				for(int i = 0; i < arg.length; i++)
					arg[i] = tokens.nextToken().toUpperCase(); 
				
				// Check the Validity of an Instruction
				for(Opcode each: Opcode.values())
				{
					String str = each.toString();
					if(label.compareTo(str) == 0)
					{
						// Set Validity Flag
						isInstr = true;
						break;	
					}
				}	
				
				if(isInstr == true)
				{
					// Valid Instruction
					switch(Opcode.valueOf(label)){  
				     
			        case LDR:  // LDR: Load Register From Memory
			        	instr = ldr(arg, codeLine - 1);  
			            break;
			            
			        case STR:  // STR: Store Register To Memory
			        	instr = str(arg, codeLine - 1); 
			            break;
			            
			        case LDA: // LDA: Load Register with Address
			        	instr = lda(arg, codeLine - 1);
						break;
						
			        case LDX: // LDX: Load Index Register from Memory
			        	instr = ldx(arg, codeLine - 1);
						break;
			        	
			        case STX: // STX: Load Index Register from Memory
			        	instr = stx(arg, codeLine - 1);
						break;
			        	
			        case JZ: // JZ: Jump If Zero
			        	instr = jz(arg, codeLine - 1);
						break;
						
			        case JNE: // JNE: Jump If Not Equal
			        	instr = jne(arg, codeLine - 1);
						break;
						
			        case JCC: // JCC: Jump If Condition Code
			        	instr = jcc(arg, codeLine - 1);
						break;
						
			        case JMP: // JMP: Unconditional Jump To Address
			        	instr = jmp(arg, codeLine - 1);
						break;
						
			        case JSR: // JSR: Jump and Save Return Address
			        	instr = jsr(arg, codeLine - 1);
						break;
						
			        case RFS: // RFS: Return From Subroutine
			        	instr = rfs(arg, codeLine - 1);
						break;
						
			        case SOB: // SOB: Subtract One And Branch
			        	instr = sob(arg, codeLine - 1);
						break;
						
			        case AMR: // AMR: Add Memory To Register
			        	instr = amr(arg, codeLine - 1);
						break;
						
			        case SMR: // SMR: Subtract Memory From Register
			        	instr = smr(arg, codeLine - 1);
						break;
						
			        case AIR: // AIR: Add Immediate to Register
			        	instr = air(arg, codeLine - 1);
						break;
						
			        case SIR: // SIR: Subtract Immediate from Register
			        	instr = sir(arg, codeLine - 1);
						break;
						
			        case MUL: // MUL: Multiply Register by Register
			        	instr = mul(arg, codeLine - 1);
						break;
						
			        case DIV: // DIV: Divide Register by Register
			        	instr = div(arg, codeLine - 1);
						break;
						
			        case TER: // TER: Test the Equality of Register and Register
			        	instr = ter(arg, codeLine - 1);
						break;
						
			        case AND: // AND: Logical And of Register and Register
			        	instr = and(arg, codeLine - 1);
						break;
						
			        case ORR: // ORR: Logical Or of Register and Register
			        	instr = orr(arg, codeLine - 1);
						break;
						
			        case NOT: // NOT: Logical Not of Register To Register
			        	instr = not(arg, codeLine - 1);
			        	break;
			        
			        case SRC: // SRC: Shift Register by Count
			        	instr = src(arg, codeLine - 1);
			        	break;
			        	
			        case RRC: // RRC: Rotate Register by Count
			        	instr = rrc(arg, codeLine - 1);
			        	break;
			        	
			        case IN: // IN: Input Character To Register from Device
			        	instr = in(arg, codeLine - 1);
			        	break;
			        	
			        case OUT: // OUT: Output Character to Device from Register
			        	instr = out(arg, codeLine - 1);
			        	break;
			        	
			        case CHK: // CHK: Check Device Status to Register
			        	instr = chk(arg, codeLine - 1);
			        	break;
			        	
			        case FADD: // FADD: Floating Add Memory To Register
			        	instr = fadd(arg, codeLine - 1);
			        	break;
			        	
			        case FSUB: // FSUB: Floating Subtract Memory From Register
			        	instr = fsub(arg, codeLine - 1);
			        	break;
			        	
			        case VADD: // VADD: Vector Add
			        	instr = vadd(arg, codeLine - 1);
			        	break;
			        	
			        case VSUB: // VSUB: Vector Subtract
			        	instr = vsub(arg, codeLine - 1);
			        	break;
			        	
			        case CNVRT: // CNVRT: Convert to Fixed/FloatingPoint
			        	instr = cnvrt(arg, codeLine - 1);
			        	break;
			        	
			        case HLT: // HLT: Stops the machine
			        	instr = hlt();
			        	break;
			        	
			        case TRAP: // TRAP: Traps to memory address 0, which contains the address of a table in memory
			        	instr = trap(arg, codeLine - 1);
			        	break;
			        	
			        case JGE: // JGE: Jump Greater Than or Equal To
			        	instr = jge(arg, codeLine - 1);
			        	break; 
			        	
			        case LDFR: // Load FloatingRegister From Memory
			        	instr = ldfr(arg, codeLine - 1);
			        	break;
			        	
			        case STFR: // Store Floating Register To Memory
			        	instr = stfr(arg, codeLine - 1);
			        	break;
			        }  
					
					if(instr.compareTo("error") != 0)
					{
						// Write to Memory
						Integer intInstr = Integer.parseInt(instr, 2);
						writeMC(intInstr);
					}
					else
					{
						// Set Error Flag
						rslt = false;
					}
				}
				else
				{
					// Unrecognized Instruction
					mySimulator.ui.display((--codeLine).toString()+": " +"Unrecognized instruction.\n");
					codeLine++;
					// Set Error Flag
					rslt = false;
				}
			}
		}
		if(rslt == false)
			mySimulator.ui.display("\nPlease check the instructions and reload.\n"); 
		else
			mySimulator.ui.display("\nAll instructions are correct.\n");
	}
	
	/**
	 * read the table from a txt file.
	 * Type conversation is for the convenience of handle data
	 * @param direct the designation we read table file from
	 * @return true if read succeed
	 * @throws IOException
	 */
	public boolean getTable(String direct) throws IOException
	{
		File file = new File(direct);
		file.createNewFile();
		FileReader input = new FileReader(file);
		table = new BufferedReader(input);
		
		if(table == null)
			return false;
		else
			return true;
	}
	
	/**
	 * Write the content of txt file into memory
	 * @throws IOException
	 */
	public void tableInit() throws IOException
	{
		String s = null;
		
		int tableAddrInt = 0;
		int[] entryInt = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		
		if((s = table.readLine())!=null)
		{
			tableAddrInt = Integer.parseInt(s, 2);
			writeTable(tableAddrInt, 0);
		}
		
		s = table.readLine();
		/*
		while(((entry = table.readLine())!=null))
		{
			if(entry.equals(""))
			{
				
			}
			else{break;}
		}
		*/
		
		for(int i = 0; i < 16; i++)
		{
			if((s = table.readLine())!=null)
			{
				entryInt[i] = Integer.parseInt(s, 2);
				writeTable(entryInt[i], tableAddrInt + i);
			}
		}
		
		s = table.readLine();
		/*while(((instr = table.readLine())!=null))
		{
			if(instr.equals("")){}
			else{break;}
		}*/
		
		for(int j = 0; j < 16; j++)
		{
			while((s = table.readLine())!= null)
			{	
				int addr = entryInt[j];
				String instr = null;
				
				if(s.equals("")){
					break;
				}
				
				int abbr = s.indexOf("//");
				if(abbr != -1)
				{
					s = s.substring(0, abbr);
				}
				
				// Split Instruction
				StringTokenizer tokens = new StringTokenizer(s);
				if(tokens.countTokens() < 1)
					mySimulator.ui.display("Errors occured when get the instruction.\n");
				
				// Store Instruction Name
				String label = tokens.nextToken().toUpperCase();
				
				// Store Instruction Parameters
				String arg[] = new String[tokens.countTokens()];
				for(int i = 0; i < arg.length; i++)
					arg[i] = tokens.nextToken().toUpperCase(); 
				
				switch(Opcode.valueOf(label)){  
			     
		        case LDR:  // LDR: Load Register From Memory
		        	instr = ldr(arg, codeLine - 1);  
		            break;
		            
		        case STR:  // STR: Store Register To Memory
		        	instr = str(arg, codeLine - 1); 
		            break;
		            
		        case LDA: // LDA: Load Register with Address
		        	instr = lda(arg, codeLine - 1);
					break;
					
		        case LDX: // LDX: Load Index Register from Memory
		        	instr = ldx(arg, codeLine - 1);
					break;
		        	
		        case STX: // STX: Load Index Register from Memory
		        	instr = stx(arg, codeLine - 1);
					break;
		        	
		        case JZ: // JZ: Jump If Zero
		        	instr = jz(arg, codeLine - 1);
					break;
					
		        case JNE: // JNE: Jump If Not Equal
		        	instr = jne(arg, codeLine - 1);
					break;
					
		        case JCC: // JCC: Jump If Condition Code
		        	instr = jcc(arg, codeLine - 1);
					break;
					
		        case JMP: // JMP: Unconditional Jump To Address
		        	instr = jmp(arg, codeLine - 1);
					break;
					
		        case JSR: // JSR: Jump and Save Return Address
		        	instr = jsr(arg, codeLine - 1);
					break;
					
		        case RFS: // RFS: Return From Subroutine
		        	instr = rfs(arg, codeLine - 1);
					break;
					
		        case SOB: // SOB: Subtract One And Branch
		        	instr = sob(arg, codeLine - 1);
					break;
					
		        case AMR: // AMR: Add Memory To Register
		        	instr = amr(arg, codeLine - 1);
					break;
					
		        case SMR: // SMR: Subtract Memory From Register
		        	instr = smr(arg, codeLine - 1);
					break;
					
		        case AIR: // AIR: Add Immediate to Register
		        	instr = air(arg, codeLine - 1);
					break;
					
		        case SIR: // SIR: Subtract Immediate from Register
		        	instr = sir(arg, codeLine - 1);
					break;
					
		        case MUL: // MUL: Multiply Register by Register
		        	instr = mul(arg, codeLine - 1);
					break;
					
		        case DIV: // DIV: Divide Register by Register
		        	instr = div(arg, codeLine - 1);
					break;
					
		        case TER: // TER: Test the Equality of Register and Register
		        	instr = ter(arg, codeLine - 1);
					break;
					
		        case AND: // AND: Logical And of Register and Register
		        	instr = and(arg, codeLine - 1);
					break;
					
		        case ORR: // ORR: Logical Or of Register and Register
		        	instr = orr(arg, codeLine - 1);
					break;
					
		        case NOT: // NOT: Logical Not of Register To Register
		        	instr = not(arg, codeLine - 1);
		        	break;
		        
		        case SRC: // SRC: Shift Register by Count
		        	instr = src(arg, codeLine - 1);
		        	break;
		        	
		        case RRC: // RRC: Rotate Register by Count
		        	instr = rrc(arg, codeLine - 1);
		        	break;
		        	
		        case IN: // IN: Input Character To Register from Device
		        	instr = in(arg, codeLine - 1);
		        	break;
		        	
		        case OUT: // OUT: Output Character to Device from Register
		        	instr = out(arg, codeLine - 1);
		        	break;
		        	
		        case CHK: // CHK: Check Device Status to Register
		        	instr = chk(arg, codeLine - 1);
		        	break;
		        	
		        case FADD: // FADD: Floating Add Memory To Register
		        	instr = fadd(arg, codeLine - 1);
		        	break;
		        	
		        case FSUB: // FSUB: Floating Subtract Memory From Register
		        	instr = fsub(arg, codeLine - 1);
		        	break;
		        	
		        case VADD: // VADD: Vector Add
		        	instr = vadd(arg, codeLine - 1);
		        	break;
		        	
		        case VSUB: // VSUB: Vector Subtract
		        	instr = vsub(arg, codeLine - 1);
		        	break;
		        	
		        case CNVRT: // CNVRT: Convert to Fixed/FloatingPoint
		        	instr = cnvrt(arg, codeLine - 1);
		        	break;
		        	
		        case HLT: // HLT: Stops the machine
		        	instr = hlt();
		        	break;
		        	
		        case TRAP: // TRAP: Traps to memory address 0, which contains the address of a table in memory
		        	instr = trap(arg, codeLine - 1);
		        	break;    
		        	
		        case JGE: // JGE: Jump Greater Than or Equal To
		        	instr = jge(arg, codeLine - 1);
		        	break;
		        	
		        case LDFR: // Load FloatingRegister From Memory
		        	instr = ldfr(arg, codeLine - 1);
		        	break;
		        	
		        case STFR: // Store Floating Register To Memory
		        	instr = stfr(arg, codeLine - 1);
		        	break;
		        }  
				Integer intInstr = Integer.parseInt(instr, 2);			
				writeTable(intInstr, addr++);			
			}
		}
	}
	
	/**
	 * Transfer LDR assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String ldr(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		// Check the Number of Parameters
		if(arg.length != 3 & arg.length != 4)
		{
			mySimulator.ui.display(line.toString() + ": " + "LDR: The number of parameters is incorrect. LDR should have 3 or 4 parameters.\n");
			return "error";
		}
		
		// OPCODE
		instr = replaceIndex(0, instr, "000001");
		
		// I
		if(arg.length == 4)
			if(arg[3].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "LDR: The 4th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "LDR: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[1].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
		
		// R
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "LDR: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}
				
		// ADDR
		int addrInt = Integer.parseInt(arg[2]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "LDR: The 3th parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt); //transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);
		
		//mySimulator.ui.display(line.toString() + ": " + "LDR: Correct.\n");
		return instr;
	}
	
	/**
	 * Transfer STR assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String str(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 3 & arg.length != 4)
		{
			mySimulator.ui.display(line.toString() + ": " + "STR: The number of parameters is incorrect. STR should have 3 or 4 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "000010");
		
		// I
		if(arg.length == 4)
			if(arg[3].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "STR: The 4th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "STR: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[1].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
		
		// R
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "STR: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}
				
		// ADDR
		int addrInt = Integer.parseInt(arg[2]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "STR: The 3th parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt);//transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);

		//mySimulator.ui.display(line.toString() + ": " + "STR: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer LDA assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String lda(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 3 & arg.length != 4)
		{
			mySimulator.ui.display(line.toString() + ": " + "LDA: The number of parameters is incorrect. LDA should have 3 or 4 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "000011");
		
		// I
		if(arg.length == 4)
			if(arg[3].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "LDA: The 4th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "LDA: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[1].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
		
		// R
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "LDA: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}
				
		// ADDR
		int addrInt = Integer.parseInt(arg[2]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "LDA: The 3th parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt);//transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);

		//mySimulator.ui.display(line.toString() + ": " + "LDA: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer LDX assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String ldx(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 2 & arg.length != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "LDX: The number of parameters is incorrect. LDX should have 2 or 3 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "100001");
		
		// I
		if(arg.length == 3)
			if(arg[2].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "LDX: The 3th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "LDX: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[0].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
				
		// ADDR
		int addrInt = Integer.parseInt(arg[1]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "LDX: The 2nd parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt);//transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);

		//mySimulator.ui.display(line.toString() + ": " + "LDX: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer STX assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String stx(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 2 & arg.length != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "STX: The number of parameters is incorrect. STX should have 2 or 3 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "100010");
		
		// I
		if(arg.length == 3)
			if(arg[2].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "STX: The 3th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "STX: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[0].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
				
		// ADDR
		int addrInt = Integer.parseInt(arg[1]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "STX: The 2nd parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt);//transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);

		//mySimulator.ui.display(line.toString() + ": " + "STX: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer JZ assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String jz(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 3 & arg.length != 4)
		{
			mySimulator.ui.display(line.toString() + ": " + "JZ: The number of parameters is incorrect. JZ should have 3 or 4 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "001000");
		
		// I
		if(arg.length == 4)
			if(arg[3].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "JZ: The 4th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "JZ: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[1].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
		
		// R
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "JZ: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}
				
		// ADDR
		int addrInt = Integer.parseInt(arg[2]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "JZ: The 3th parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt);//transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);

		//mySimulator.ui.display(line.toString() + ": " + "JZ: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer JNE assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String jne(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 3 & arg.length != 4)
		{
			mySimulator.ui.display(line.toString() + ": " + "JNE: The number of parameters is incorrect. JNE should have 3 or 4 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "001001");
		
		// I
		if(arg.length == 4)
			if(arg[3].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "JNE: The 4th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "JNE: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[1].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
		
		// R
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "JNE: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}
				
		// ADDR
		int addrInt = Integer.parseInt(arg[2]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "JNE: The 3th parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt);//transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);

		//mySimulator.ui.display(line.toString() + ": " + "JNE: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer JCC assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String jcc(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 3 & arg.length != 4)
		{
			mySimulator.ui.display(line.toString() + ": " + "JCC: The number of parameters is incorrect. JCC should have 3 or 4 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "001010");
		
		// I
		if(arg.length == 4)
			if(arg[3].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "JCC: The 4th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "JCC: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[1].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
		
		// CC
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "JCC: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}
				
		// ADDR
		int addrInt = Integer.parseInt(arg[2]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "JCC: The 3th parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt);//transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);

		//mySimulator.ui.display(line.toString() + ": " + "JCC: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer JMP assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String jmp(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 2 & arg.length != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "JMP: The number of parameters is incorrect. JMP should have 2 or 3 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "001011");
		
		// I
		if(arg.length == 3)
			if(arg[2].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "JMP: The 3th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "JMP: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[0].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
				
		// ADDR
		int addrInt = Integer.parseInt(arg[1]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "JMP: The 2nd parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt);//transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);

		//mySimulator.ui.display(line.toString() + ": " + "JMP: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer JSR assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String jsr(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 2 & arg.length != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "JSR: The number of parameters is incorrect. JSR should have 2 or 3 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "001100");
		
		// I
		if(arg.length == 3)
			if(arg[2].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "JSR: The 3th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "JSR: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[0].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
				
		// ADDR
		int addrInt = Integer.parseInt(arg[1]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "JSR: The 2nd parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt);//transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);

		//mySimulator.ui.display(line.toString() + ": " + "JSR: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer RFS assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String rfs(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "RFS: The number of parameters is incorrect. RFS should have 1 parameter.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "001101");
				
		// IMMED
		int immedInt = Integer.parseInt(arg[0]); //transfer IMMED from String to int
		if(immedInt > 63 | immedInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "RFS: The parameter is incorrect.\n");
			return "error";
		}
		String immed = Integer.toBinaryString(immedInt);//transfer to binary String
		int len = immed.length();			
		instr = replaceIndex(16-len, instr, immed);

		//mySimulator.ui.display(line.toString() + ": " + "RFS: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer SOB assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String sob(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 3 & arg.length != 4)
		{
			mySimulator.ui.display(line.toString() + ": " + "SOB: The number of parameters is incorrect. SUB should have 3 or 4 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "001110");
		
		// I
		if(arg.length == 4)
			if(arg[3].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "SOB: The 4th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "SOB: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[1].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
		
		// R
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "SOB: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}
				
		// ADDR
		int addrInt = Integer.parseInt(arg[2]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "SOB: The 3th parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt);//transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);

		//mySimulator.ui.display(line.toString() + ": " + "SOB: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer AMR assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String amr(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 3 & arg.length != 4)
		{
			mySimulator.ui.display(line.toString() + ": " + "AMR: The number of parameters is incorrect. AMR should have 3 or 4 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "000100");
		
		// I
		if(arg.length == 4)
			if(arg[3].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "AMR: The 4th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "AMR: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[1].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
		
		// R
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "AMR: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}
				
		// ADDR
		int addrInt = Integer.parseInt(arg[2]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "AMR: The 3th parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt);//transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);

		//mySimulator.ui.display(line.toString() + ": " + "AMR: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer SMR assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String smr(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 3 & arg.length != 4)
		{
			mySimulator.ui.display(line.toString() + ": " + "SMR: The number of parameters is incorrect. SMR should have 3 or 4 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "000101");
		
		// I
		if(arg.length == 4)
			if(arg[3].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "SMR: The 4th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "SMR: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[1].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
		
		// R
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "SMR: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}
				
		// ADDR
		int addrInt = Integer.parseInt(arg[2]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "SMR: The 3th parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt);//transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);

		//mySimulator.ui.display(line.toString() + ": " + "SMR: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer AIR assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String air(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 2)
		{
			mySimulator.ui.display(line.toString() + ": " + "AIR: The number of parameters is incorrect. AIR should have 2 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "000110");
		
		// R
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "AIR: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}
				
		// IMMED
		int immedInt = Integer.parseInt(arg[1]); //transfer IMMED from String to int
		if(immedInt > 63 | immedInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "AIR: The 2nd parameter is incorrect.\n");
			return "error";
		}
		String immed = Integer.toBinaryString(immedInt);//transfer to binary String
		int len = immed.length();			
		instr = replaceIndex(16-len, instr, immed);

		//mySimulator.ui.display(line.toString() + ": " + "AIR: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer SIR assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String sir(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 2)
		{
			mySimulator.ui.display(line.toString() + ": " + "SIR: The number of parameters is incorrect. SIR should have 2 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "000111");
		
		// R
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "SIR: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}
				
		// IMMED
		int immedInt = Integer.parseInt(arg[1]); //transfer IMMED from String to int
		if(immedInt > 63 | immedInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "SIR: The 2nd parameter is incorrect.\n");
			return "error";
		}
		String immed = Integer.toBinaryString(immedInt);//transfer to binary String
		int len = immed.length();			
		instr = replaceIndex(16-len, instr, immed);

		//mySimulator.ui.display(line.toString() + ": " + "SIR: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer MUL assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String mul(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 2)
		{
			mySimulator.ui.display(line.toString() + ": " + "MUL: The number of parameters is incorrect. MUL should have 2 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "010000");
		
		// RX
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "MUL: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(6, instr, "01");
				break;
			case "2":
				instr = replaceIndex(6, instr, "10");
				break;
			case "3":
				instr = replaceIndex(6, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(6, instr, "00");				
				break;
		}
		
		// RY
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1 & Integer.parseInt(arg[1]) != 2 & Integer.parseInt(arg[1]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "MUL: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[1]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}

		//mySimulator.ui.display(line.toString() + ": " + "MUL: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer DIV assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String div(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 2)
		{
			mySimulator.ui.display(line.toString() + ": " + "DIV: The number of parameters is incorrect. DIV should have 2 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "010001");
		
		// RX
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "DIV: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(6, instr, "01");
				break;
			case "2":
				instr = replaceIndex(6, instr, "10");
				break;
			case "3":
				instr = replaceIndex(6, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(6, instr, "00");				
				break;
		}
		
		// RY
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1 & Integer.parseInt(arg[1]) != 2 & Integer.parseInt(arg[1]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "DIV: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[1]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}

		//mySimulator.ui.display(line.toString() + ": " + "DIV: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer TER assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String ter(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 2)
		{
			mySimulator.ui.display(line.toString() + ": " + "TER: The number of parameters is incorrect. TER should have 2 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "010010");
		
		// RX
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "TER: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(6, instr, "01");
				break;
			case "2":
				instr = replaceIndex(6, instr, "10");
				break;
			case "3":
				instr = replaceIndex(6, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(6, instr, "00");				
				break;
		}
		
		// RY
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1 & Integer.parseInt(arg[1]) != 2 & Integer.parseInt(arg[1]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "TER: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[1]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}

		//mySimulator.ui.display(line.toString() + ": " + "TER: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer AND assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String and(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 2)
		{
			mySimulator.ui.display(line.toString() + ": " + "AND: The number of parameters is incorrect. AND should have 2 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "010011");
		
		// RX
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "AND: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(6, instr, "01");
				break;
			case "2":
				instr = replaceIndex(6, instr, "10");
				break;
			case "3":
				instr = replaceIndex(6, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(6, instr, "00");				
				break;
		}
		
		// RY
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1 & Integer.parseInt(arg[1]) != 2 & Integer.parseInt(arg[1]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "AND: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[1]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}

		//mySimulator.ui.display(line.toString() + ": " + "AND: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer ORR assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String orr(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 2)
		{
			mySimulator.ui.display(line.toString() + ": " + "ORR: The number of parameters is incorrect. ORR should have 2 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "010100");
		
		// RX
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "ORR: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(6, instr, "01");
				break;
			case "2":
				instr = replaceIndex(6, instr, "10");
				break;
			case "3":
				instr = replaceIndex(6, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(6, instr, "00");				
				break;
		}
		
		// RY
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1 & Integer.parseInt(arg[1]) != 2 & Integer.parseInt(arg[1]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "ORR: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[1]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}

		//mySimulator.ui.display(line.toString() + ": " + "ORR: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer NOT assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String not(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "NOT: The number of parameters is incorrect. NOT should have 1 parameter.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "010101");
		
		// R
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "NOT: The parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(6, instr, "01");
				break;
			case "2":
				instr = replaceIndex(6, instr, "10");
				break;
			case "3":
				instr = replaceIndex(6, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(6, instr, "00");				
				break;
		}

		//mySimulator.ui.display(line.toString() + ": " + "NOT: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer SRC assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String src(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 4)
		{
			mySimulator.ui.display(line.toString() + ": " + "SRC: The number of parameters is incorrect. SRC should have 4 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "011001");
		
		// L/R
		if((arg[2].compareTo("L") != 0) & (arg[2].compareTo("R") != 0))
		{
			mySimulator.ui.display(line.toString() + ": " + "SRC: The 3th parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[2].compareTo("L") == 0)
			instr = replaceIndex(6, instr, "1");
		
		// R
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "SRC: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(7, instr, "01");
				break;
			case "2":
				instr = replaceIndex(7, instr, "10");
				break;
			case "3":
				instr = replaceIndex(7, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(7, instr, "00");				
				break;
		}
			
		// A/L
		if((arg[3].compareTo("L") != 0) & (arg[3].compareTo("A") != 0))
		{
			mySimulator.ui.display(line.toString() + ": " + "SRC: The 4th parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[3].compareTo("L") == 0)
			instr = replaceIndex(9, instr, "1");
				
		// COUNT
		int countInt = Integer.parseInt(arg[1]); //transfer COUNT from String to int
		if(countInt > 15 | countInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "SRC: The 2nd parameter is incorrect.\n");
			return "error";
		}
		String count = Integer.toBinaryString(countInt); //transfer to binary String
		int len = count.length();			
		instr = replaceIndex(16-len, instr, count);

		//mySimulator.ui.display(line.toString() + ": " + "SRC: Correct.\n");
		return instr;
	}
	
	/**
	 * Transfer RRC assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String rrc(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 4)
		{
			mySimulator.ui.display(line.toString() + ": " + "RRC: The number of parameters is incorrect. RRC should have 4 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "011010");
		
		// L/R
		if((arg[2].compareTo("L") != 0) & (arg[2].compareTo("R") != 0))
		{
			mySimulator.ui.display(line.toString() + ": " + "RRC: The 3th parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[2].compareTo("L") == 0)
			instr = replaceIndex(6, instr, "1");
		
		// R
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "RRC: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(7, instr, "01");
				break;
			case "2":
				instr = replaceIndex(7, instr, "10");
				break;
			case "3":
				instr = replaceIndex(7, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(7, instr, "00");				
				break;
		}
			
		// A/L
		if((arg[3].compareTo("L") != 0) & (arg[3].compareTo("A") != 0))
		{
			mySimulator.ui.display(line.toString() + ": " + "RRC: The 4th parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[3].compareTo("L") == 0)
			instr = replaceIndex(9, instr, "1");
				
		// COUNT
		int countInt = Integer.parseInt(arg[1]); //transfer COUNT from String to int
		if(countInt > 15 | countInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "RRC: The 2nd parameter is incorrect.\n");
			return "error";
		}
		String count = Integer.toBinaryString(countInt); //transfer to binary String
		int len = count.length();			
		instr = replaceIndex(16-len, instr, count);

		//mySimulator.ui.display(line.toString() + ": " + "RRC: Correct.\n");
		return instr;
	}
	
	/**
	 * Transfer IN assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String in(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 2)
		{
			mySimulator.ui.display(line.toString() + ": " + "IN: The number of parameters is incorrect. IN should have 2 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "110001");
		
		// R
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "IN: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(7, instr, "01");
				break;
			case "2":
				instr = replaceIndex(7, instr, "10");
				break;
			case "3":
				instr = replaceIndex(7, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(7, instr, "00");				
				break;
		}
				
		// DEVID
		int devIDInt = Integer.parseInt(arg[1]); //transfer devID from String to int
		if(devIDInt > 31 | devIDInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "IN: The 2nd parameter is incorrect.\n");
			return "error";
		}
		String devID = Integer.toBinaryString(devIDInt); //transfer to binary String
		int len = devID.length();			
		instr = replaceIndex(16-len, instr, devID);

		//mySimulator.ui.display(line.toString() + ": " + "IN: Correct.\n");
		return instr;
	}
	
	/**
	 * Transfer OUT assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String out(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 2)
		{
			mySimulator.ui.display(line.toString() + ": " + "OUT: The number of parameters is incorrect. OUT should have 2 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "110010");
		
		// R
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "OUT: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(7, instr, "01");
				break;
			case "2":
				instr = replaceIndex(7, instr, "10");
				break;
			case "3":
				instr = replaceIndex(7, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(7, instr, "00");				
				break;
		}
				
		// DEVID
		int devIDInt = Integer.parseInt(arg[1]); //transfer devID from String to int
		if(devIDInt > 31 | devIDInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "OUT: The 2nd parameter is incorrect.\n");
			return "error";
		}
		String devID = Integer.toBinaryString(devIDInt); //transfer to binary String
		int len = devID.length();			
		instr = replaceIndex(16-len, instr, devID);

		//mySimulator.ui.display(line.toString() + ": " + "OUT: Correct.\n");
		return instr;
	}
	
	/**
	 * Transfer CHK assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String chk(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 2)
		{
			mySimulator.ui.display(line.toString() + ": " + "CHK: The number of parameters is incorrect. CHK should have 2 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "110011");
		
		// R
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "CHK: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(7, instr, "01");
				break;
			case "2":
				instr = replaceIndex(7, instr, "10");
				break;
			case "3":
				instr = replaceIndex(7, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(7, instr, "00");				
				break;
		}
				
		// DEVID
		int devIDInt = Integer.parseInt(arg[1]); //transfer devID from String to int
		if(devIDInt > 31 | devIDInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "CHK: The 2nd parameter is incorrect.\n");
			return "error";
		}
		String devID = Integer.toBinaryString(devIDInt); //transfer to binary String
		int len = devID.length();			
		instr = replaceIndex(16-len, instr, devID);

		//mySimulator.ui.display(line.toString() + ": " + "CHK: Correct.\n");
		return instr;
	}
	
	/**
	 * Transfer FADD assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String fadd(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 3 & arg.length != 4)
		{
			mySimulator.ui.display(line.toString() + ": " + "FADD: The number of parameters is incorrect. FADD should have 3 or 4 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "011011");
		
		// I
		if(arg.length == 4)
			if(arg[3].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "FADD: The 4th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "FADD: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[1].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
		
		// FR
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "FADD: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}
				
		// ADDR
		int addrInt = Integer.parseInt(arg[2]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "FADD: The 3th parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt); //transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);

		//mySimulator.ui.display(line.toString() + ": " + "FADD: Correct.\n");
		return instr;
	}
	
	/**
	 * Transfer FSUB assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String fsub(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 3 & arg.length != 4)
		{
			mySimulator.ui.display(line.toString() + ": " + "FSUB: The number of parameters is incorrect. FSUB should have 3 or 4 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "011100");
		
		// I
		if(arg.length == 4)
			if(arg[3].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "FSUB: The 4th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "FSUB: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[1].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
		
		// FR
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "FSUB: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}
				
		// ADDR
		int addrInt = Integer.parseInt(arg[2]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "FSUB: The 3th parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt); //transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);

		//mySimulator.ui.display(line.toString() + ": " + "FSUB: Correct.\n");
		return instr;
	}
	
	/**
	 * Transfer VADD assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String vadd(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 3 & arg.length != 4)
		{
			mySimulator.ui.display(line.toString() + ": " + "VADD: The number of parameters is incorrect. VADD should have 3 or 4 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "011101");
		
		// I
		if(arg.length == 4)
			if(arg[3].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "VADD: The 4th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "VADD: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[1].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
		
		// FR
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "VADD: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}
				
		// ADDR
		int addrInt = Integer.parseInt(arg[2]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "VADD: The 3th parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt); //transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);

		//mySimulator.ui.display(line.toString() + ": " + "VADD: Correct.\n");
		return instr;
	}
	
	/**
	 * Transfer VSUB assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String vsub(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 3 & arg.length != 4)
		{
			mySimulator.ui.display(line.toString() + ": " + "VSUB: The number of parameters is incorrect. VSUB should have 3 or 4 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "011110");
		
		// I
		if(arg.length == 4)
			if(arg[3].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "VSUB: The 4th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "VSUB: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[1].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
		
		// FR
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "VSUB: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}
				
		// ADDR
		int addrInt = Integer.parseInt(arg[2]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "VSUB: The 3th parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt); //transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);

		//mySimulator.ui.display(line.toString() + ": " + "VSUB: Correct.\n");
		return instr;
	}
	
	/**
	 * Transfer CNVRT assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String cnvrt(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 3 & arg.length != 4)
		{
			mySimulator.ui.display(line.toString() + ": " + "CNVRT: The number of parameters is incorrect. CNVRT should have 3 or 4 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "011111");
		
		// I
		if(arg.length == 4)
			if(arg[3].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "CNVRT: The 4th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "CNVRT: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[1].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
		
		// FR
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "CNVRT: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}
				
		// ADDR
		int addrInt = Integer.parseInt(arg[2]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "CNVRT: The 3th parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt); //transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);

		//mySimulator.ui.display(line.toString() + ": " + "CNVRT: Correct.\n");
		return instr;
	}
	
	/**
	 * Transfer HLT assembly instruction to machine code instruction.
	 * @return a String contains machine code instruction
	 */
	private String hlt()
	{
		String instr = new String("0000000000000000");	
		//mySimulator.ui.display("HLT: Correct.\n");
		return instr;
	}
	
	/**
	 * Transfer TRAP assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String trap(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "TRAP: The number of parameters is incorrect. TRAP should have 1 parameter.\n");
			return "error";
		}
		
		// R
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "TRAP: The parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}
		
		//mySimulator.ui.display(line.toString() + ": " + "TRAP: Correct.\n");
		return instr;
	}
	
	/**
	 * Transfer JGE assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String jge(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 3 & arg.length != 4)
		{
			mySimulator.ui.display(line.toString() + ": " + "JGE: The number of parameters is incorrect. JGE should have 3 or 4 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "001111");
		
		// I
		if(arg.length == 4)
			if(arg[3].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "JGE: The 4th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "JGE: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[1].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
		
		// R
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "JGE: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}
				
		// ADDR
		int addrInt = Integer.parseInt(arg[2]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "JGE: The 3th parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt);//transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);

		//mySimulator.ui.display(line.toString() + ": " + "JGE: Correct.\n");
		return  instr;
	}
	
	/**
	 * Transfer LDFR assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String ldfr(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 3 & arg.length != 4)
		{
			mySimulator.ui.display(line.toString() + ": " + "LDFR: The number of parameters is incorrect. FADD should have 3 or 4 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "100000");
		
		// I
		if(arg.length == 4)
			if(arg[3].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "LDFR: The 4th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "LDFR: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[1].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
		
		// FR
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "LDFR: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}
				
		// ADDR
		int addrInt = Integer.parseInt(arg[2]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "LDFR: The 3th parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt); //transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);

		//mySimulator.ui.display(line.toString() + ": " + "LDFR: Correct.\n");
		return instr;
	}
	
	/**
	 * Transfer STFR assembly instruction to machine code instruction.
	 * @param arg[] contains the arguments in original assembly instruction 
	 * @param line is the Line number
	 * @return a String contains machine code instruction
	 */
	private String stfr(String arg[], Integer line)
	{
		// Initialize
		String instr = new String("0000000000000000");
		
		// Check the Number of Parameters
		if(arg.length != 3 & arg.length != 4)
		{
			mySimulator.ui.display(line.toString() + ": " + "STFR: The number of parameters is incorrect. FADD should have 3 or 4 parameters.\n");
			return "error";
		}
			
		// OPCODE
		instr = replaceIndex(0, instr, "100011");
		
		// I
		if(arg.length == 4)
			if(arg[3].compareTo("I") == 0)
				instr = replaceIndex(6, instr, "1");
			else 
			{
				mySimulator.ui.display(line.toString() + ": " + "STFR: The 4th parameter is incorrect.\n");
				return "error";
			}
		
		// IX
		if(Integer.parseInt(arg[1]) != 0 & Integer.parseInt(arg[1]) != 1)
		{
			mySimulator.ui.display(line.toString() + ": " + "STFR: The 2nd parameter is incorrect.\n");
			return "error";
		}
		
		if(arg[1].compareTo("1") == 0)
			instr = replaceIndex(7, instr, "1");
		
		// FR
		if(Integer.parseInt(arg[0]) != 0 & Integer.parseInt(arg[0]) != 1 & Integer.parseInt(arg[0]) != 2 & Integer.parseInt(arg[0]) != 3)
		{
			mySimulator.ui.display(line.toString() + ": " + "STFR: The 1st parameter is incorrect.\n");
			return "error";
		}
		
		switch (arg[0]){							
			case "1":
				instr = replaceIndex(8, instr, "01");
				break;
			case "2":
				instr = replaceIndex(8, instr, "10");
				break;
			case "3":
				instr = replaceIndex(8, instr, "11");
				break;
			default: // case "0"
				instr = replaceIndex(8, instr, "00");				
				break;
		}
				
		// ADDR
		int addrInt = Integer.parseInt(arg[2]); //transfer ADDR from String to int
		if(addrInt > 63 | addrInt < 0 )
		{
			mySimulator.ui.display(line.toString() + ": " + "STFR: The 3th parameter is incorrect.\n");
			return "error";
		}
		String addr = Integer.toBinaryString(addrInt); //transfer to binary String
		int len = addr.length();			
		instr = replaceIndex(16-len, instr, addr);

		//mySimulator.ui.display(line.toString() + ": " + "STFR: Correct.\n");
		return instr;
	}
	
	 /**
	 * From the given index, replace this String with a new String
	 * @param index is the position where replacement begin
	 * @param res is the original String
	 * @param str is the replacement String
	 * @return the resulting String
	 */
	public static String replaceIndex(int index, String res, String str)
	{
		int length = str.length();
		return res.substring(0, index) + str + res.substring(index + length);
	}
		
	/**
	 * Write machine code to memory
	 * @param instr is the String to be written into memory
	 */
	private void writeMC(Integer instr)
	{		
		mySimulator.myMemory.memoryBank[(instrAddr)%2][(instrAddr)/2] = instr;
		instrAddr++;
		//count++;
	}
	
	/**
	 * Write table to memory
	 * @param instr is the String to be written into memory
	 * @param addr is the address represents the position in memory
	 */
	private void writeTable(Integer instr, Integer addr)
	{		
		mySimulator.myMemory.memoryBank[addr%2][addr/2] = instr;
	}	
}