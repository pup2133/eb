package parse.ex2;

import java.io.File;
import java.io.IOException;

public class Ex2 {

    public static void main(String[] args) throws IOException {
        File multipartData = new File("request-dummy.txt");
        MyMultipartRequest mr = new MyMultipartRequest();
        MyMultipartRequest myMultipartRequest = mr.parse(multipartData);

        System.out.println("Method: " + myMultipartRequest.getMethod());
        System.out.println("Content-Length: " + myMultipartRequest.getHeader("Content-Length"));
        System.out.println("Content-Type: " + myMultipartRequest.getHeader("Content-Type"));
        System.out.println("Host: " + myMultipartRequest.getHeader("Host"));
        System.out.println("Connection: " + myMultipartRequest.getHeader("Connection"));
        System.out.println("User-Agent: " + myMultipartRequest.getHeader("User-Agent"));
        System.out.println("Accept-Encoding: " + myMultipartRequest.getHeader("Accept-Encoding"));
    }

}
