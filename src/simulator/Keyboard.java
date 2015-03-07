package simulator;

/**
 * @author Group #1
 * A class represents I/O device
 * It has 3 Register named kbBufPtr, kbBufLen, status
 * It has a input buffer, which can store no more than 80 characters
 */
public class Keyboard {
	
	Register kbBufPtr, kbBufLen, status;
	Integer[] buffer;
	
	public Keyboard(Simulator ms)
	{
		kbBufPtr = new Register(16, ms);
		kbBufLen = new Register(16, ms);
		status = new Register(16, ms);
		buffer = new Integer[80];
	}
	
	/**
	 * Put user input characters into the buffer
	 * @param input is user input characters
	 * @return true if put succeed
	 */
	public Boolean putBuffer(String input)
	{		
		//status.put(1);
		
		// Check if the length is larger than 80, and set Keyboard Buffer Length
		int length = input.length();
		if(length > 80)
		{
			// If the length is larger than 80
			input = input.substring(0, 81);
			kbBufLen.put(80);
		}
		else
		{
			kbBufLen.put(length);
		}
		
		// Set Keyboard Buffer Pointer to 0
		kbBufPtr.put(0);
		
		// Put each character into the buffer
		int index = 0;
		for(int i = length; i > 0; i--)
		{
			char ch = input.charAt(index);
			buffer[index] = Integer.valueOf(ch);
			index++;
		}
		
		/*for(int j = 0; j<length; j++)
		{
			System.out.print(buffer[j]+"\n");
		}*/
		return true;
	}
	
	/**
	 * Get one character from the buffer
	 * @return the value if the character is a digit, or return the ASCII
	 */
	public Integer getBuffer()
	{
		int length = kbBufLen.get();
		Integer letter = 0;
		
		if(length > 0)
		{	
			int temp = kbBufLen.get();
			temp--;
			kbBufLen.put(temp);
			
			letter = buffer[kbBufPtr.get()];
			if(letter >= 48 && letter <= 57)
				letter = letter - 48;
			//System.out.print(letter+"\n");
			temp = kbBufPtr.get();
			temp++;
			kbBufPtr.put(temp);
		}		
		/*else
		{
			status.put(0);
		}*/
		return letter;
	}	
}
