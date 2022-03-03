package com.ztx;

import com.sun.mail.imap.IMAPStore;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.search.SearchTerm;

public class EmailUtils {

    private static String dirPath = System.getProperty("user.dir");

    public static void getEmailsByPop3() throws MessagingException, IOException {
        // 定义连接POP3服务器的属性信息
        String popServer = "pop.163.com";
        String popProtocol = "pop3";

        String username = "lwm_fjc@163.com";
        String password = "AUZNEWSMRKJRNPNV"; // 163邮箱的SMTP的授权码，什么是授权码，它又是如何设置？

        //String username = "811011902@qq.com";
        //String password = "asdfhxujstlcbfjg"; // 163邮箱的SMTP的授权码，什么是授权码，它又是如何设置？

        Properties props = new Properties();
        //props.setProperty("mail.transport.protocol", popProtocol); // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.transport.protocol", popProtocol); // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", popServer); // 发件人的邮箱的 SMTP服务器地址

        // 获取连接
        Session session = Session.getDefaultInstance(props);
        session.setDebug(false);

        Store store = session.getStore(popProtocol);
        store.connect(popServer, username, password); // POP3服务器的登陆认证

        // 通过POP3协议获得Store对象调用这个方法时，邮件夹名称只能指定为"INBOX"
        Folder folder = store.getFolder("INBOX");// 获得用户的邮件帐户
        folder.open(Folder.READ_WRITE); // 设置对邮件帐户的访问权限

        /*Message[] messages = folder.search(new SearchTerm() {
            @Override
            public boolean match(Message message) {
                boolean isMatch = true;
                try {
                    isMatch = message.getFlags().contains(Flags.Flag.SEEN);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                return  isMatch;

            }
        });*/
        Message[] messages = folder.getMessages();// 得到邮箱帐户中的所有邮件

        for (Message msg : messages) {
            String subject = msg.getSubject();// 获得邮件主题

            Address from = (Address) msg.getFrom()[0];// 获得发送者地址
            System.out.println("邮件的主题为: " + subject + "\t发件人地址为: " + from
                    + "邮件发送时间:" + msg.getSentDate().getTime());
            if (msg.getReceivedDate() != null) {
                System.out.println("接收时间" + msg.getReceivedDate());
            }


            // 邮件类型不是mixed时，表示邮件中不包含附件，直接输出邮件内容
            if (!msg.isMimeType("multipart/mixed")) {

            } else {

            }
        }

        folder.close(false);// 关闭邮件夹对象
        store.close(); // 关闭连接对象
    }

    public static void getEmailsByImap() throws MessagingException, IOException {

        String protocolImap = "imap";

        String imapServer = "imap.163.com";
        String username = "lwm_fjc@163.com";
        String password = "AUZNEWSMRKJRNPNV"; // QQ邮箱的SMTP的授权码，什么是授权码，它又是如何设置？

        //String imapServer = "imap.qq.com";
        //String username = "811011902@qq.com";
        //String password = "asdfhxujstlcbfjg"; // 163邮箱的SMTP的授权码，什么是授权码，它又是如何设置？

        Properties props = new Properties();
        //props.setProperty("mail.transport.protocol", protocolImap); // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.store.protocol", protocolImap); // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.pop.host", imapServer); // 发件人的邮箱的 SMTP服务器地址
        //props.setProperty("mail.imap.host", imapServer); // 发件人的邮箱的 SMTP服务器地址

        HashMap IAM = new HashMap();
        //带上IMAP ID信息，由key和value组成，例如name，version，vendor，support-email等。
        IAM.put("name", "myname");
        IAM.put("version", "1.0.0");
        IAM.put("vendor", "myclient");
        IAM.put("support-email", "testmail@test.com");

        // 获取连接
        Session session = Session.getDefaultInstance(props);
        session.setDebug(false);

        // 获取Store对象
        //Store store = session.getStore(popProtocol);
        //store.connect(popServer, username, password); // POP3服务器的登陆认证

        IMAPStore store = (IMAPStore) session.getStore(protocolImap);
        store.connect(imapServer, username, password); // POP3服务器的登陆认证
        store.id(IAM);

        // 通过POP3协议获得Store对象调用这个方法时，邮件夹名称只能指定为"INBOX"
        Folder folder = store.getFolder("INBOX");// 获得用户的邮件帐户
        folder.open(Folder.READ_WRITE); // 设置对邮件帐户的访问权限

        Message[] messages = folder.search(new SearchTerm() {
            @Override
            public boolean match(Message message) {
                boolean isMatch = true;
                try {
                    isMatch = message.getFlags().contains(Flags.Flag.SEEN);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                return  isMatch;

            }
        });
        //Message[] messages = folder.getMessages();// 得到邮箱帐户中的所有邮件

        for (Message msg : messages) {
            // 如果该邮件是组合型"multipart/*"则可能包含附件等
            if (msg.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) msg.getContent();

                for (int i = 0; i < mp.getCount(); i++) {
                    BodyPart bp = mp.getBodyPart(i);

                    // 如果该BodyPart对象包含附件，则应该解析出来
                    if (bp.getDisposition() != null) {
                        String filename = bp.getFileName();

                        if (filename.startsWith("=?")) {
                            // 把文件名编码成符合RFC822规范
                            filename = MimeUtility.decodeText(filename);
                        }
                        System.out.println("filename：" + filename);
                        //得到附件输入流
                        InputStream inputStream;
                        inputStream = bp.getInputStream();
                        System.out.println(dirPath);
                        FileOutputStream outputStream = new FileOutputStream(dirPath + "\\"+filename+"");
                        int c = 0;
                        byte[] bytes = new byte[1024];
                        while ((c = inputStream.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, c);
                        }
                        outputStream.close();
                        inputStream.close();
                    }
                }
            }
            /*String subject = msg.getSubject();// 获得邮件主题

            Address from = (Address) msg.getFrom()[0];// 获得发送者地址
            System.out.println("邮件的主题为: " + subject + "\t发件人地址为: " + from
                    + "邮件发送时间:" + msg.getReceivedDate().getTime());
            //System.out.println(msg.getFlags());

            //pop3没有判断邮件是否为已读的功能，要使用Imap才可以
            Flags flags = msg.getFlags();
            if (flags.contains(Flags.Flag.SEEN))
                System.out.println("这是一封已读邮件");
            else {
                System.out.println("未读邮件");
                //if (msg.getMessageNumber() % 2 == 0) {
                msg.setFlag(Flags.Flag.SEEN,
                        true);
                //}
            }
            //System.out.println("邮件的内容为：");

            //Message msg = folder.getMessage(msgnum);
            // 邮件类型不是mixed时，表示邮件中不包含附件，直接输出邮件内容
            if (!msg.isMimeType("multipart/mixed")) {
                //response.setContentType("message/rfc822");
                //System.out.println(msg.getContent());
            } else {
                // 查找并输出邮件中的邮件正文
                Multipart mp = (Multipart) msg.getContent();
                int bodynum = mp.getCount();
                for (int i = 0; i < bodynum; i++) {
                    BodyPart bp = mp.getBodyPart(i);
                    System.out.println(bp.getFileName());


                    // MIME消息头中不包含disposition字段， 并且MIME消息类型不为mixed时，
                    // 表示当前获得的MIME消息为邮件正文

                    if (!bp.isMimeType("multipart/mixed") && bp.getDisposition() == null) {
                        //response.setContentType("message/rfc822");
                        //bp.writeTo(sos);
                    } else {
                        //得到附件输入流
                        InputStream inputStream;
                        inputStream = bp.getInputStream();
                        FileOutputStream outputStream = new FileOutputStream(dirPath + "//"+"my_out.xlsx");
                        int c = 0;
                        byte[] bytes = new byte[1024];
                        while ((c = inputStream.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, c);
                        }
                        outputStream.close();
                        inputStream.close();
                    }
                }
                //message.writeTo(System.out);// 输出邮件内容到控制台
            }*/
        }

        folder.close(false);// 关闭邮件夹对象
        store.close(); // 关闭连接对象
    }

    /**
     * 邮件发送
     */
    public static void sendEmail() throws Exception {
        String from = "ly_fjc@163.com";
        String to = "lwm_fjc@163.com"; //收件人
        Date d = new Date(); //获取当前时间对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//创建日期格式化类对象,”yyyy/MM/dd HH:mm:ss”是我们
        String subject = "my_subject" + sdf.format(d);
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

        // 创建用于组合邮件正文和附件的MimeMultipart对象
        MimeBodyPart attachPart = createAttachment(dirPath + "\\数据导出.xlsx");
        MimeMultipart allMultipart = new MimeMultipart("mixed");

        allMultipart.addBodyPart(attachPart);

        msg.setContent(allMultipart);
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

    public static MimeBodyPart createAttachment(String filename)
            throws Exception {
        // 创建保存附件的MimeBodyPart对象，并加入附件内容和相应信息
        MimeBodyPart attachPart = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(filename);
        attachPart.setDataHandler(new DataHandler(fds));
        attachPart.setFileName(fds.getName());
        return attachPart;
    }

}
