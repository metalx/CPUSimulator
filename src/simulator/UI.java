/**
 * UI.java
 * 
 * 
 * 
 */

package simulator;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import simulator.Parameters;
import simulator.UI;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.Dimension;


class UI extends JFrame implements Parameters
{

	//
	public Frame f;
	private Button IPL,IPF,singleStep,Run,Load,Halt,setValue,toBinary, showP2;
	private TextArea MAR,MDR,PC,IR,X0,R0,R1,R2,R3,setMN,setMV, regInfo;
	private Label MARN,MDRN,PCN,IRN,X0N,R0N,R1N,R2N,R3N, sMN, sMV, cW, cW1, instrW;
	private Simulator mainSimulator;
	private int instrCount = 0;
	private JRadioButton selectM;
	private JRadioButton selectR;
	private ButtonGroup bgroup; 
	private String fileN;
	private int tbFlag, cntProgram;
//	private TextArea inParea; 
	private JTextArea Console, Console1,Instructions;
	private JScrollPane jspc,jspi, jspc1, memScroll;
	
	private JPanel p1;
	private JPanel p2;
	private JPanel p3;
	
	private JLabel mB, mE;
	private JTextArea memBegin, memEnd, memTable;
	private JButton showMem;
	
	private JLabel []otherRegL = new JLabel[13];
	private JTextArea []otherRegT = new JTextArea[13];
	

	
	UI(Simulator mainSimulator)
	{
		this.mainSimulator = mainSimulator;
		init();
	}

	public void init()
	{
		f = new Frame("Simulator");
		f.setResizable(false);
		tbFlag = 0;
		cntProgram = 1;

		//frame basic settings
		f.setBounds(100,30,1010,705);
		f.setLayout(null);
		f.setBackground(new Color(200,200,200));
		
		cW = new Label(" Console:");
		cW.setBounds(30,60,200,20);
		Console = new JTextArea();
		Console.setEditable(false);
		//Console.setForeground(new Color(255,255,255));
		jspc = new JScrollPane(Console, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jspc.setBounds(30, 80, 300, 290);
		
		cW1 = new Label(" Output:");
		cW1.setBounds(350,60,200,20);
		Console1 = new JTextArea();
		Console1.setEditable(false);
		//Console.setForeground(new Color(255,255,255));
		jspc1 = new JScrollPane(Console1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jspc1.setBounds(350, 80, 200, 290);
		
		instrW = new Label(" Instructions:");
		instrW.setBounds(580,60,200,20);
		Instructions = new JTextArea();
				
		jspi = new JScrollPane(Instructions, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		jspi.setBounds(580, 80, 300, 290);

		
		MARN = new Label("MAR");
		MARN.setBounds(58,460,30,10);
		
		MAR = new TextArea(null,0,0,TextArea.SCROLLBARS_NONE);
		MAR.setBounds(30,480,110,30);
		MAR.setEditable(false);
		MAR.setBackground(new Color(255,255,255));
		
		MDRN = new Label("MDR");
		MDRN.setBounds(58,530,30,10);
		
		MDR = new TextArea(null,0,0,TextArea.SCROLLBARS_NONE);
		MDR.setBounds(30,550,110,30);
		MDR.setEditable(false);
		MDR.setBackground(new Color(255,255,255));
		
		toBinary =new Button("Change format!");
		toBinary.setBounds(70, 600, 120, 40);
		toBinary.setEnabled(false);
		
		PCN = new Label("PC");
		PCN.setBounds(180,460,30,10);
		
		PC = new TextArea(null,0,0,TextArea.SCROLLBARS_NONE);
		PC.setBounds(150,480,110,30);
		PC.setEditable(false);
		PC.setBackground(new Color(255,255,255));
		
		X0N = new Label("X0");
		X0N.setBounds(180,530,30,10);
		
		X0 = new TextArea(null,0,0,TextArea.SCROLLBARS_NONE);
		X0.setBounds(150,550,110,30);
		X0.setEditable(false);
		X0.setBackground(new Color(255,255,255));
		
		IRN = new Label("IR");
		IRN.setBounds(103,390,30,10);
		
		IR = new TextArea(null,0,0,TextArea.SCROLLBARS_NONE);
		IR.setBounds(30,410,180,30);
		IR.setEditable(false);
		IR.setBackground(new Color(255,255,255));
		
		R0N = new Label("R0");
		R0N.setBounds(270,420,30,10);
		
		R0 = new TextArea(null,0,0,TextArea.SCROLLBARS_NONE);
		R0.setBounds(310,410,110,30);
		R0.setEditable(false);
		R0.setBackground(new Color(255,255,255));
		
		R1N = new Label("R1");
		R1N.setBounds(270,470,30,10);
		
		R1 = new TextArea(null,0,0,TextArea.SCROLLBARS_NONE);
		R1.setBounds(310,460,110,30);
		R1.setEditable(false);
		R1.setBackground(new Color(255,255,255));
		
		R2N = new Label("R2");
		R2N.setBounds(270,520,30,10);
		
		R2 = new TextArea(null,0,0,TextArea.SCROLLBARS_NONE);
		R2.setBounds(310,510,110,30);
		R2.setEditable(false);
		R2.setBackground(new Color(255,255,255));
		
		R3N = new Label("R3");
		R3N.setBounds(270,570,30,10);
		
		R3 = new TextArea(null,0,0,TextArea.SCROLLBARS_NONE);
		R3.setBounds(310,560,110,30);
		R3.setEditable(false);
		R3.setBackground(new Color(255,255,255));
		
		setMN = new TextArea(null,0,0,TextArea.SCROLLBARS_NONE);
		setMN.setBounds(530,540,60,30);
		setMN.setEditable(true);
		setMN.setBackground(new Color(255,255,255));
		setMN.setText("0");
		
		sMN = new Label("Index");
		sMN.setBounds(540,510,110,30);
		
		setMV = new TextArea(null,0,0,TextArea.SCROLLBARS_NONE);
		setMV.setBounds(630,540,80,30);
		setMV.setEditable(true);
		setMV.setBackground(new Color(255,255,255));
		setMV.setText("0");
		
		sMV = new Label("Value");
		sMV.setBounds(650,510,150,30);
		
		// Power on button		
		IPL = new Button("Power On");
		IPL.setBounds(900, 640, 80, 50);
		
		// Power off button
		IPF = new Button("Power Off");
		IPF.setBounds(900, 640, 80, 50);
		IPF.setEnabled(true);
		IPF.setVisible(false);

		Load = new Button("Load");
		Load.setBounds(900, 390, 80, 50);
		Load.setEnabled(false);
		
		singleStep = new Button("Single Step");
		singleStep.setBounds(900, 450, 80, 50);
		singleStep.setEnabled(false);
		
		Run = new Button("Run");
		Run.setBounds(900, 510, 80, 50);
		Run.setEnabled(false);
		
		Halt = new Button("Halt");
		Halt.setBounds(900, 570, 80, 50);
		Halt.setEnabled(false);
		
		setValue = new Button("setValue");
		setValue.setBounds(740, 530, 80, 50);
		setValue.setEnabled(false);
		
		selectR = new JRadioButton("Register");
		selectR.setBounds(530, 580, 90, 30);
		selectM = new JRadioButton("Memory",true);
		selectM.setBounds(620, 580, 90, 30);
		bgroup = new ButtonGroup();
		bgroup.add(selectM);
		bgroup.add(selectR);
		regInfo = new TextArea("Index of Register:\nR0--0\nR1--1\nR2--2\nR3--3\nX0--4\n", 0, 0,TextArea.SCROLLBARS_NONE);
		regInfo.setBounds(530, 610, 120, 80);
		regInfo.setEditable(false);
		regInfo.setVisible(false);
				
		p1 = new JPanel();
		p1.setLayout(null);
		p1.setSize(new Dimension(1000,700));
		p1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(40, 5, 5, 0), 
				BorderFactory.createTitledBorder("Simulator:")));
		p1.setVisible(true);
		
		p1.add(cW);
		p1.add(cW1);
		p1.add(instrW);
		p1.add(jspc);
		p1.add(jspc1);
		p1.add(IPL);
		p1.add(IPF);
		p1.add(jspi);
		p1.add(MARN);
		p1.add(MAR);
		p1.add(MDRN);
		p1.add(MDR);
		p1.add(PCN);
		p1.add(PC);
		p1.add(X0N);
		p1.add(X0);
		p1.add(IRN);
		p1.add(IR);
		p1.add(Load);
		p1.add(singleStep);
		p1.add(Run);
		p1.add(Halt);
		p1.add(R0N);
		p1.add(R1N);
		p1.add(R2N);
		p1.add(R3N);
		p1.add(R0);
		p1.add(R1);
		p1.add(R2);
		p1.add(R3);
		p1.add(setValue);
		p1.add(setMN);
		p1.add(setMV);
		p1.add(sMN);
		p1.add(sMV);
		p1.add(selectM);
		p1.add(selectR);
		p1.add(regInfo);
		p1.add(toBinary);
		
		showP2 =new Button("ManagerPane>>");
		showP2.setBounds(885, 200, 105, 80);
		showP2.setEnabled(true);
		p1.add(showP2);
		
		f.add(p1);
		
		p3 = new JPanel();
		p3.setLayout(new GridLayout(0,2));
		p3.setLocation(1000, 400);
		p3.setSize(new Dimension(405,300));
		p3.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5), 
				BorderFactory.createTitledBorder("Other Register:")));
		String []otherRegister = {
				"RS1", "RS2", "CC", "MFR", "FR[0]", "FR[1]",
				"OP1", "OP2", "result", "remainder",
				"F_OP1", "F_OP2", "F_result"
				};
		for (int i = 0; i< 13; i++)
		{
			otherRegL[i] = new JLabel(otherRegister[i]);
			otherRegL[i].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			otherRegT[i] = new JTextArea();
			otherRegT[i].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			otherRegL[i].setLabelFor(otherRegT[i]);
			otherRegT[i].setEditable(false);
			p3.add(otherRegL[i]);
			p3.add(otherRegT[i]);
		}
		
		p2 = new JPanel();
		p2.setLayout(null);
		p2.setLocation(1000, 0);
		p2.setSize(new Dimension(405,405));
		p2.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(40, 5, 0, 5), 
				BorderFactory.createTitledBorder("Debug Panel:")));
		
		memTable = new JTextArea();
		memTable.setEditable(false);
		memTable.setBackground(Color.ORANGE);
		//Console.setForeground(new Color(255,255,255));
		memScroll = new JScrollPane(memTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		memScroll.setBounds(10, 60, 380, 280);
		memTable.append("------ADDRESS---------------Content-------");	
		
		mB = new JLabel("Begin Index");
		mB.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		mB.setBounds(20, 340, 80, 30);
		mB.setLabelFor(memBegin);
		memBegin = new JTextArea();
		memBegin.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		memBegin.setBounds(20, 370, 80, 30);
		memBegin.setEditable(true);
		
		mE = new JLabel("End Index");
		mE.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		mE.setBounds(110, 340, 80, 30);
		mE.setLabelFor(memEnd);
		memEnd = new JTextArea();
		memEnd.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		memEnd.setBounds(110, 370, 80, 30);
		memEnd.setEditable(true);
		
		showMem = new JButton("ShowMemory");
		showMem.setBounds(210, 350, 150, 50);		
		
		p2.add(showMem);
		p2.add(mB);
		p2.add(memBegin);
		p2.add(mE);
		p2.add(memEnd);
		p2.add(memScroll);
		p2.setVisible(true);
		
		f.add(p2);
		f.add(p3);
		myEvent();
		//
		f.setVisible(true);
		f.setExtendedState(NORMAL);

	}

	
	private void myEvent()
	{
		f.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);

			}
		});

		//
		/*
	

		*/
		IPL.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Console.setText("");
				Instructions.setText("");
				Console.append("Simulator is powered on!\n");
				IR.setText("0");
				MAR.setText("0");
				MDR.setText("0");
				PC.setText("0");
				X0.setText("0");
				R0.setText("0");
				R1.setText("0");
				R2.setText("0");
				R3.setText("0");
				Load.setEnabled(true);
				setValue.setEnabled(true);
				toBinary.setEnabled(true);
				IPL.setEnabled(false);
				IPL.setVisible(false);
				IPF.setEnabled(true);
				IPF.setVisible(true);
				refresh();
			}
		});
		
		IPF.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Console.setText("");
				Console1.setText("");
				Instructions.setText("");
				Console.append("Simulator is powered off!\n");
				IPL.setEnabled(true);
				IPL.setVisible(true);
				toBinary.setEnabled(false);
				IPF.setEnabled(false);
				IPF.setVisible(false);
				Load.setEnabled(false);
				singleStep.setEnabled(false);
				Run.setEnabled(false);
				setValue.setEnabled(false);
				mainSimulator.RefressSm();
			}
		});
		
		Load.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Instructions.append("--------Program" + cntProgram++ +"---------\n");
				JFileChooser chooser = new JFileChooser("./");
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "txt", "TXT");
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(getParent());
			    if(returnVal == JFileChooser.APPROVE_OPTION)
			    {
			    	fileN = chooser.getSelectedFile().getName();
			    	try {
						mainSimulator.myASMLoader.getFile(fileN);
						mainSimulator.myASMLoader.translate2MC();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						display("error in loading file\n");
						e1.printStackTrace();
					}
					Run.setEnabled(true);
					singleStep.setEnabled(true);
			    }
			    Load.setEnabled(false);
		
			}
		});
		
		singleStep.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//Run.setEnabled(false);
				mainSimulator.myExecuter.flag = 0;
				mainSimulator.myExecuter.startSingle(instrCount++);
				if(mainSimulator.myExecuter.notEnd == 0)
					singleStep.setEnabled(false);
				showMemory();
			}
		});
		
		Run.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				singleStep.setEnabled(false);
				mainSimulator.myExecuter.flag = 1;
				mainSimulator.myExecuter.start();
			}
		});
		
		Halt.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				

			}
		});
		
		setValue.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (setMN.getText().equals("")||setMV.getText().equals(""))
					return;
				
				if (bgroup.isSelected(selectM.getModel()))
				{
					int index = Integer.parseInt(setMN.getText());
					int context = Integer.parseInt(setMV.getText());
					//mainSimulator.myMemory.memoryCell[index] = context;
					mainSimulator.myMemory.memoryBank[index%2][index/2] = context;

				}
				else
				{
					int index = Integer.parseInt(setMN.getText());
					int context = Integer.parseInt(setMV.getText());
					//mainSimulator.myMemory.memoryCell[index] = context;
					if (index == 4)
						mainSimulator.X0.data = context;
					else if (index>=0&&index<4)
					{
						mainSimulator.GPR[index].data = context;
					}
					
				}

			}
		});
		
		selectR.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						regInfo.setVisible(true);
					}
				});
		
		selectM.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				regInfo.setVisible(false);
			}
		});
		
		toBinary.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				tbFlag = ~tbFlag;
				refresh();
			}
		});
		showP2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(showP2.getLabel().compareTo("ManagerPane>>") == 0)
				{	
					showP2.setLabel("ManagerPane<<");
					f.setBounds(100,30,1410,710);
				}
				else
				{
					showP2.setLabel("ManagerPane>>");
					f.setBounds(100,30,1010,710);
				}
			}
		});
		
		showMem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				showMemory();
			}
		});

	}
//	iBtn.addActionListener(new ActionListener()
//	{
//		public void actionPerformed(ActionEvent e)
//		{
//			iBtn.setEnabled(false);
//			s = Tkb.getText();
//			flag = false;
//		}
//	});	
//	public String input()
//	{
//		iBtn.setEnabled(true);
//		flag = true;
//		s = "10";
//		return s;
//	}
	
	/*The default display textArea is Console;
	If you want to display in other textArea, use two parameters. 
	The value second parameter determine the textArea.
	The value can be found in Parameters.*/
	public void display(String s) 
	{
		Console.append(s);	
	}
	
	public void display1(String s) 
	{
		Console1.append(s);	
	}
	
	public void display(String s, int textAreaNumber) 
	{
		switch(textAreaNumber)
		{
			case 0:Console.append(s);break;
			case 1:IR.setText(s);break;
			case 2:MAR.setText(s);break;
			case 3:MDR.setText(s);break;
			case 4:PC.setText(s);break;
			case 5:X0.setText(s);break;
			case 6:Instructions.append(s);break;
			case 7:R0.setText(s);break;
			case 8:R1.setText(s);break;
			case 9:R2.setText(s);break;
			case 10:R3.setText(s);break;
			default:Console.append("**Invalid textAreaNumber**\n");
		}
	}
	
	public void refresh()
	{
		String instr = Integer.toBinaryString(mainSimulator.IR.data);
		int length = instr.length();
		instr = ASMLoader.replaceIndex(16 - length, "0000000000000000", instr);
		
		IR.setText(instr);

		otherRegT[0].setText(mainSimulator.RS1.data.toString());
		otherRegT[1].setText(mainSimulator.RS2.data.toString());
		otherRegT[2].setText(mainSimulator.CC.data.toString());
		otherRegT[3].setText(mainSimulator.MFR.data.toString());
		otherRegT[4].setText(mainSimulator.FR[0].data.toString());
		otherRegT[5].setText(mainSimulator.FR[1].data.toString());
		otherRegT[6].setText(mainSimulator.myALU.op1.data.toString());
		otherRegT[7].setText(mainSimulator.myALU.op2.data.toString());
		otherRegT[8].setText(mainSimulator.myALU.result.data.toString());
		otherRegT[9].setText(mainSimulator.myALU.remainder.data.toString());
		otherRegT[10].setText(mainSimulator.myALU.F_op1.data.toString());
		otherRegT[11].setText(mainSimulator.myALU.F_op2.data.toString());
		otherRegT[12].setText(mainSimulator.myALU.F_result.data.toString());
		showMemory();
		
		if (tbFlag == 0)
		{
			IR.setText(mainSimulator.IR.data.toString());
			MAR.setText(mainSimulator.MAR.data.toString());
			MDR.setText(mainSimulator.MDR.data.toString());
			PC.setText(mainSimulator.PC.data.toString());
			X0.setText(mainSimulator.X0.data.toString());
			R0.setText(mainSimulator.GPR[0].data.toString());
			R1.setText(mainSimulator.GPR[1].data.toString());
			R2.setText(mainSimulator.GPR[2].data.toString());
			R3.setText(mainSimulator.GPR[3].data.toString());
		}
		else
		{
			IR.setText(to16(mainSimulator.MAR.data)); 
			MAR.setText(to16(mainSimulator.MAR.data));
			MDR.setText(to16(mainSimulator.MDR.data));
			PC.setText(to16(mainSimulator.PC.data));
			X0.setText(to16(mainSimulator.X0.data));
			R0.setText(to16(mainSimulator.GPR[0].data));
			R1.setText(to16(mainSimulator.GPR[1].data));
			R2.setText(to16(mainSimulator.GPR[2].data));
			R3.setText(to16(mainSimulator.GPR[3].data));
		}
	}


	static String to16(Integer i) // use to change the integer to binary format~ 
	{
		String instr = Integer.toBinaryString(i);
		int length = instr.length();
		if(length > 16)
			instr = instr.substring(length-16,length);
		else
			instr = ASMLoader.replaceIndex(16 - length, "0000000000000000", instr);
		return instr;
	}
	
	static String resizeFormat(Integer i) // use to control the format ~ 
	{
		String num = Integer.toString(i);
		int length = num.length();
		for (int j =0; j< 8 - length; j++)
		{
			num = num.concat("#");
		}
		return num;
	}
	

 public void showMemory()
 {
		memTable.setText("------ADDRESS---------------Content-------");
		if(memBegin.getText().equals("")||memEnd.getText().equals(""))
			return;
		
		int memB = 0;
		int memE = Integer.parseInt(memEnd.getText());
		
		
		for (memB = Integer.parseInt(memBegin.getText()); memB<=memE; memB++)
		{
//			mainSimulator.MAR.put(memB);
			memTable.append("\n");
			memTable.append("------" + resizeFormat(memB)+
					"----------------" + mainSimulator.myMemory.memoryBank[memB%2][memB/2]+ "-------");
		}
		
	}
 }
	/*public static void main(String[] args) 
	{
		new UI();
		
	}*/

