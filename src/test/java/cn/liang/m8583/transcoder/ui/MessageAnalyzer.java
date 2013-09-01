package cn.liang.m8583.transcoder.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;

import cn.liang.m8583.key.DesEncryptor;
import cn.liang.m8583.key.Encryptor;
import cn.liang.m8583.key.KeyFinder;
import cn.liang.m8583.key.KeyFinderDemo;
import cn.liang.m8583.message.Message;
import cn.liang.m8583.message.support.BalanceRequest;
import cn.liang.m8583.message.support.WorkingKeyResponse;
import cn.liang.m8583.transcoder.DefaultTranscoder;
import cn.liang.m8583.transcoder.MessageUtil;

import com.sfpay.framework.common.json.JSONUtils;

public class MessageAnalyzer extends JFrame{

	private static final KeyFinder kf = new KeyFinderDemo();
	private static final DesEncryptor encryptor = new DesEncryptor();
	private static final DefaultTranscoder transcoder = new DefaultTranscoder(kf, encryptor);
	
	private static final String hex="0123456789abcdefABCDEF";
	
	private JTextArea mesArea ;

	private JTextArea resArea ;
	
	
	public MessageAnalyzer(){
		final Rectangle frameBounds = initBounds();
		this.setBounds(frameBounds);  
		this.setTitle("报文分析器");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		
		
		initMessageArea();
		JComponent mc = (mesArea);
		mc.setPreferredSize(new Dimension(500,100));
		this.getContentPane().add(mc);
		
		initResultArea();
		JScrollPane rc = new JScrollPane(resArea);
		rc.setPreferredSize(new Dimension(500,300));
		this.getContentPane().add(rc);
		
		JButton button = initAnalyzeButton();
		this.getContentPane().add(button);
		
		//Spring spring =  Spring.constant(15,15,15);
		int spring = 15;
		layout.putConstraint(SpringLayout.NORTH, mc,spring, SpringLayout.NORTH, this.getContentPane());
		
		layout.putConstraint(SpringLayout.WEST, mc,spring, SpringLayout.WEST, this.getContentPane());
		layout.putConstraint(SpringLayout.WEST, rc,spring, SpringLayout.WEST, this.getContentPane());
		
		layout.putConstraint(SpringLayout.NORTH,button,spring, SpringLayout.SOUTH, mc );
		layout.putConstraint(SpringLayout.NORTH,rc ,spring, SpringLayout.SOUTH, button);
		
		layout.putConstraint(SpringLayout.SOUTH, this.getContentPane(),spring, SpringLayout.SOUTH, rc);
		
		/*layout.putConstraint(SpringLayout.EAST, this.getContentPane(),spring, SpringLayout.EAST, mc);
		layout.putConstraint(SpringLayout.EAST, this.getContentPane(),spring, SpringLayout.EAST, rc);
		layout.putConstraint(SpringLayout.EAST, this.getContentPane() ,spring,  SpringLayout.EAST, button);
		*/
		layout.putConstraint(SpringLayout.EAST, mc,- spring,SpringLayout.EAST, this.getContentPane() );
		layout.putConstraint(SpringLayout.EAST, rc,- spring, SpringLayout.EAST, this.getContentPane());
		layout.putConstraint(SpringLayout.EAST, button ,- spring, SpringLayout.EAST, this.getContentPane() );
		
	}

	protected void initResultArea() {
		resArea = new JTextArea();
		//resArea.setAutoscrolls(true);
	}

	protected void initMessageArea() {
		mesArea = new JTextArea();
		mesArea.setAutoscrolls(true);
		mesArea.setRows(5);
		mesArea.setBorder(new LineBorder(Color.BLACK));
		mesArea.setLineWrap(true);
	}

	private Rectangle initBounds() {
		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) (screenSize.width * 0.2);
		int y = (int) (screenSize.height * 0.2);
		int w = (int) (screenSize.width * 0.6);
		int h = (int) (screenSize.height * 0.6);
		final Rectangle frameBounds = new Rectangle(x, y, w, h);
		return frameBounds;
	}
	
	private JButton initAnalyzeButton(){
		ActionListener listener = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					resArea.setText("");
					redirect();
					String line = mesArea.getText();
					StringBuilder mesBuilder= new StringBuilder();
					for(char c:line.toCharArray()){
						if((c>='0'&&c<='9') || (c>='a' && c<='f') || (c>='A'&& c<='F')){
							mesBuilder.append(c);
						}
					}
					line = mesBuilder.toString();
					byte[] data = MessageUtil.hex_2_byte(line);
					Message mes = transcoder.decode(data);
					System.out.println(mes.getClass());
					System.out.println(JSONUtils.fromObject(mes));

					if (mes instanceof BalanceRequest) {
						BalanceRequest bal = (BalanceRequest) mes;
						Encryptor enc = transcoder.getEncryptor();
						System.out.println(MessageUtil.byte2hex(bal.getPin()));
						String pwd = enc.decryptPassword(
								kf.findKey(bal.getTerminalId()), bal.getPin());
						System.out.println("余额查询，密码：" + pwd);
					}

					if (mes instanceof WorkingKeyResponse) {
						WorkingKeyResponse wkr = (WorkingKeyResponse) mes;
						System.out.println("getNewWorkingKey:"
								+ new String(MessageUtil.byte2hex(wkr
										.getNewWorkingKey())));

						encryptor.decryptWorkingKey(kf.findMasterKey(),
								wkr.getNewWorkingKey());
					}
				} catch (Exception ex) {
					System.out.println(ex);
				}
			}
			
		};
		JButton analyzeButton = new JButton("分析");
		analyzeButton.addActionListener(listener);
		return analyzeButton;
	}
	
	public void redirect() throws IOException{
		final PipedOutputStream out = new PipedOutputStream();
		PrintStream ps = new PrintStream(out);
		System.setOut(ps);
		System.setErr(ps);
		final PipedInputStream in = new PipedInputStream(out);
		Thread th = new Thread(new Runnable(){

			@Override
			public void run() {
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line ;
				try {
					while((line= reader.readLine())!=null){
						resArea.append(line);
						resArea.append("\n");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						reader.close();
					} catch (IOException e1) {}
					try {
						if(in!=null){
							in.close();
						}
					} catch (IOException e) {}
					try {
						if(out!=null){
							out.close();
						}
					} catch (IOException e) {}
				}
			}
			
		});
		th.start();
	}
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MessageAnalyzer frame = new MessageAnalyzer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
