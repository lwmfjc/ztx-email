package com.ztx;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class EmailUtils {

    public static void getEmails() throws MessagingException, IOException {
        // 定义连接POP3服务器的属性信息
        String popServer = "pop.163.com";
        String protocol = "pop3";
        String username = "lwm_fjc@163.com";
        String password = "AUZNEWSMRKJRNPNV"; // QQ邮箱的SMTP的授权码，什么是授权码，它又是如何设置？

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", protocol); // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", popServer); // 发件人的邮箱的 SMTP服务器地址

        // 获取连接
        Session session = Session.getDefaultInstance(props);
        session.setDebug(false);

        // 获取Store对象
        Store store = session.getStore(protocol);
        store.connect(popServer, username, password); // POP3服务器的登陆认证

        // 通过POP3协议获得Store对象调用这个方法时，邮件夹名称只能指定为"INBOX"
        Folder folder = store.getFolder("INBOX");// 获得用户的邮件帐户
        folder.open(Folder.READ_WRITE); // 设置对邮件帐户的访问权限

        Message[] messages = folder.getMessages();// 得到邮箱帐户中的所有邮件

        for (Message message : messages) {
            String subject = message.getSubject();// 获得邮件主题
            Address from = (Address) message.getFrom()[0];// 获得发送者地址
            System.out.println("邮件的主题为: " + subject + "\t发件人地址为: " + from);
            System.out.println("邮件的内容为：");
            message.writeTo(System.out);// 输出邮件内容到控制台
        }

        folder.close(false);// 关闭邮件夹对象
        store.close(); // 关闭连接对象
    }

    /**
     * 邮件发送
     */
    public static void sendEmail() throws MessagingException, IOException {
        String from = "ly_fjc@163.com";
        String to = "lwm_fjc@163.com"; //收件人
        String subject = "my_subject";
        String body = "my_body";

        String smtpHost = "smtp.163.com";

        //设置要发送的文件的属性
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", smtpHost); // 发件人的邮箱的 SMTP服务器地址
        props.setProperty("mail.smtp.auth", "true"); // 请求认证，参数名称与具体实现有关
        //props.setProperty("mail.smtp.starttls.enable", "true"); // ttl

        Session session = Session.getDefaultInstance(
                props);
        //创建MineMessage对象
        MimeMessage msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(from)); //发件人
        msg.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to)); //收件人
        msg.setSentDate(new Date());//发送时间
        msg.setSubject(subject); //设置主题
        msg.setText(body); //正文

        msg.saveChanges();//保存最终的邮件内容

        session.setDebug(true);//可以查看邮件发送的过程
        //写入到本地磁盘
        //msg.writeTo(new FileOutputStream("D:\\Desktop\\test.eml"));


        //获取transport对象，用来发送邮件
        Transport transport = session.getTransport("smtp");

        //使用from的授权码进行发送
        transport.connect(from, "CHFHUBOOMUIGVUPG"); //发件人的密码

        //将邮件发送给所有的接收人
        // 发送，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();
    }

}
