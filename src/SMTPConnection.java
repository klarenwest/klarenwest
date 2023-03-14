import java.net.*;
import java.io.*;
import java.lang.*;

/* Open an SMTP connection to a mailserver and send one mail. */
public class SMTPConnection {
    /* The socket to the server */
    private Socket connection;

    /* Streams for reading and writing the socket */
    private BufferedReader fromServer;
    private DataOutputStream toServer;

    private static final int SMTP_PORT = 2526;
   private static final String CRLF = "\r\n";

    /* Are we connected? Used in close() to determine what to do. */
    private boolean isConnected = false;

    /* Create an SMTPConnection object. Create the socket and the 
       associated streams. Initialize SMTP connection. */
    public SMTPConnection(Envelope envelope) throws IOException {
    	
	connection = new Socket(envelope.DestAddr, SMTP_PORT);
	fromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	toServer = new DataOutputStream(connection.getOutputStream());
	/* Fill in */
	
	/* Read a line from server and check that the reply code is 220.
	   If not, throw an IOException. */
	/* Fill in */
	String reply = fromServer.readLine();
	String[] reply1 = reply.split(" ");
	if(parseReply(reply1[0]) != 220) {
		throw new IOException();
	}

	/* SMTP handshake. We need the name of the local machine.
	   Send the appropriate SMTP handshake command. */
	String localhost = (InetAddress.getLocalHost()).getCanonicalHostName();
	sendCommand("helo localhost\n", 250); 
	
	isConnected = true;
    }

    /* Send the message. Write the correct SMTP-commands in the
       correct order. No checking for errors, just throw them to the
       caller. */
    public void send(Envelope envelope) throws IOException {
	/* Fill in */
	/* Send all the necessary commands to send a message. Call
	   sendCommand() to do the dirty work. Do _not_ catch the
	   exception thrown from sendCommand(). */
	/* Fill in */
    	sendCommand("MAIL FROM: <" + ">\n", 250);
    	toServer.writeBytes(envelope.Sender);
    	sendCommand("RCPT TO: ", 250);
    	toServer.writeBytes(envelope.Recipient);
    	sendCommand("DATA", 354);
    	toServer.writeBytes(envelope.Message.Body);
    	sendCommand(CRLF + "." + CRLF, 250);
    	
    }

    /* Close the connection. First, terminate on SMTP level, then
       close the socket. */
    public void close() {
	isConnected = false;
	try {
	    sendCommand("QUIT", 221);
	    connection.close();
	} catch (IOException e) {
	    System.out.println("Unable to close connection: " + e);
	    isConnected = true;
	}
    }

    /* Send an SMTP command to the server. Check that the reply code is
       what is is supposed to be according to RFC 821. */
    private void sendCommand(String command, int rc) throws IOException {
	/* Fill in */
	/* Write command to server and read reply from server. */
	/* Fill in */
    	toServer.writeBytes(command + CRLF);
    	String reply = fromServer.readLine();
    	String[] reply1 = reply.split(" ");
	/* Fill in */
	/* Check that the server's reply code is the same as the parameter
	   rc. If not, throw an IOException. */	
	/* Fill in */
    	if(parseReply(reply1[0]) != rc) {
    		throw  new IOException();
    	}
    }

    /* Parse the reply line from the server. Returns the reply code. */
    private int parseReply(String reply) {
    	int ReplyCode = Integer.parseInt(reply);
    	return ReplyCode;
    }

    /* Destructor. Closes the connection if something bad happens. */
    protected void finalize() throws Throwable {
	if(isConnected) {
	    close();
	}
    }
}