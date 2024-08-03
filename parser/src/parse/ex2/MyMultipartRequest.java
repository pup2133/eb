package parse.ex2;

import parse.ex3.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

public class MyMultipartRequest {

    private LinkedHashMap<String, String> headers = new LinkedHashMap<>();

    public String getMethod() {
        String method = "";
        for (String s : headers.keySet()) {
            method = s;
            break;
        }
        return method;
    }

    public String getHeader(String header) {
        return headers.get(header);
    }

    public MyMultipartRequest(LinkedHashMap<String, String> headers) {
        this.headers = headers;
    }

    public MyMultipartRequest() {}

    private String getBoundary(File file) throws IOException {
        String boundary = "";
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains("boundary=")) {
                String[] split = line.split("=");
                boundary = split[1];
                break;
            }
        }
        br.close();

        return boundary;
    }

    public MyMultipartRequest parse(File multipartData) throws IOException {
        String boundary = getBoundary(multipartData);
        String end = ("--" + boundary);
        LinkedHashMap<String, String> headers = getHeader(multipartData, end);

        return new MyMultipartRequest(headers);
    }

    public MultipartFile getMultipartFile(String name) throws IOException {
        File multipartData = new File("request-dummy.txt");
        String text = getContent(multipartData, name);
        return new MultipartFile(text);
    }

    private LinkedHashMap<String, String> getHeader(File file, String end) throws IOException {
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.equals(end)) break;

            String[] object = line.split(" ", 2);
            String key = object[0].replace(":", "");
            String value = object[1];

            header.put(key, value);
        }
        br.close();

        return header;

    }

    private String getBody(File file, String boundary) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));

        String start = ("--" + boundary);
        String end = (start + "--");

        String line;
        boolean flag = false;
        while ((line = br.readLine()) != null) {
            if (end.equals(line)) break;
            if (flag) {
                sb.append(line + "\n");
                if (line.isBlank()) {
                    sb.append(line).append("\n");
                }
                continue;
            }
            if (start.equals(line)) flag = true;
        }
        br.close();

        return sb.toString();
    }

    private String getContent(File file, String name) throws IOException {
        String boundary = getBoundary(file);
        String body = getBody(file, boundary);
        String[] part = body.split(("--" + boundary));

        String text = "";
        for (String p : part) {
            String[] lines = p.split("\\n\\n", 2);
            String[] str = lines[0].split("; ");
            if (str[1].equals(("name=" + name))) {
                text += lines[1];
                break;
            }
        }

        return text.trim();
    }

}