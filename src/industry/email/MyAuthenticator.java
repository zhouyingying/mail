package industry.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
/**
 * 密码验证器
 * @author
 * @date 2014年7月1日 下午2:09:31
 */
public class MyAuthenticator  extends Authenticator{
	
	String username=null;
	String password=null;
	
	public MyAuthenticator() {
		super();
	}
	
	public MyAuthenticator(String username,String password){
		this.username=username;
		this.password=password;
	}
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}
}
