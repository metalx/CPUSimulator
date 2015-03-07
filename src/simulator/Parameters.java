/**
 * 
 * @author Group #1
 * This Class contains all the parameters we use through the whole program
 * Every class which uses those parameters should implements this class
 * 
 */

package simulator;

public interface Parameters {
	
	//TextArea
	final static int T_Console = 0;
	final static int T_IR = 1;
	final static int T_MAR = 2;
	final static int T_MDR = 3;
	final static int T_PC = 4;
	final static int T_X0 = 5;
	final static int T_INS = 6;
	final static int T_R0 = 7;
	final static int T_R1 = 8;
	final static int T_R2 = 9;
	final static int T_R3 = 10;
	
	//memory
	final static int memorySize = 32*1024;
	final static int initAddr = 2;
	final static int bankSize = 2;
	final static int memorySizePerBank = (memorySize%bankSize == 0) ? (memorySize/bankSize) : (memorySize/bankSize + 1);
	//Cache
	final static int cacheSize = 4*8;//4 words per block; 8 rows
	final static int rowSize = 8;
	final static int blockSize = 4;
	
	//Argument entrance
		final static int argument_entrance = 10000;
		final static int routines_16 = 9001;
		final static int fault_temp = 16000;
		//16000: IR, 16001:PC, 16002:X0
		//16003: R0, 16004: R1, 16005: R2, 16006: R3
	
	
}
