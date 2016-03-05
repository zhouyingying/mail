/**
 * 
 */
package me.gacl.util;

/**
 * @author zhou
 *
 */
public class SendMail {

    public static void main(String[] args) {

        String smtp = "smtp.126.com";// smtp服务器

       String from = "bujie356@126.com";// 邮件显示名称
        String to = "1064013525@qq.com";// 收件人的邮件地址，必须是真实地址

        String copyto = "";// 抄送人邮件地址

        String subject = "测试邮件";// 邮件标题

        String content = "你好！";// 邮件内容

        String username = "bujie356";// 发件人真实的账户名

        String password = "xxxx";// 发件人密码

        Mail.sendAndCc(smtp, from, to, copyto, subject, content, username, password);

    }
}
