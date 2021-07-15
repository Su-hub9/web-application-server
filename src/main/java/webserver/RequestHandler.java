package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            log.debug("request line : {}", line);
            if (line == null) return;

            String[] tokens = line.split(" ");
            int contentLength = 0;
            while (!"".equals(line)) {
                line = br.readLine();
                log.debug("header : {}", line);
                if (line.contains("Content-Length")) {
                    contentLength = getContentLength(line);
                }
            }

            String method = tokens[0];
            String url = tokens[1];
            if (method.equals("GET")) {
                if (url.contains("/user/create")) {
                    int index = url.indexOf("?");
                    String params = url.substring(index + 1);
                    Map<String, String> paramMap = HttpRequestUtils.parseQueryString(params);
                    User user = new User(paramMap.get("userId"), paramMap.get("password"), paramMap.get("name"), paramMap.get("email"));
                    DataBase.addUser(user);
                } else {
                    DataOutputStream dos = new DataOutputStream(out);
                    byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                    response200Header(dos, body.length);
                    responseBody(dos, body);
                }
            } else if (method.equals("POST")) {
                if (url.equals("/user/create")) {
                    String params = IOUtils.readData(br, contentLength);
                    Map<String, String> paramMap = HttpRequestUtils.parseQueryString(params);
                    User user = new User(paramMap.get("userId"), paramMap.get("password"), paramMap.get("name"), paramMap.get("email"));
                    DataBase.addUser(user);
                }
            }
            
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private int getContentLength(String line) {
        String[] headerTokens = line.split(":");
        return Integer.parseInt(headerTokens[1].trim());
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
