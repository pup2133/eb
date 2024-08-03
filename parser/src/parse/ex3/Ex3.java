package parse.ex3;

import parse.ex2.MyMultipartRequest;

import java.io.IOException;

public class Ex3 {


    public static void main(String[] args) throws IOException {
        MyMultipartRequest myMultipartRequest = new MyMultipartRequest();

        MultipartFile first = myMultipartRequest.getMultipartFile("text1");
        first.store("/Users/yun/Desktop/eb/parser/src/parse/file/first.txt");

        MultipartFile second = myMultipartRequest.getMultipartFile("text2");
        second.store("/Users/yun/Desktop/eb/parser/src/parse/file/second.txt");
    }


}
