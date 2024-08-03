package parse.ex1;

public class Ex1 {

    public static void main(String[] args) {

        String input = "https://www.ebrainsoft.com/?id=kmc774&favorite=001&favorite=002";
        MyRequest mr = new MyRequest();
        MyRequest request = mr.parse(input);

        System.out.println("request.getParam(\"id\").equals(\"kmc774\") = " + request.getParam("id").equals("kmc774"));
        System.out.println("request.getParams(\"favorite\").getClass() = " + request.getParams("favorite").getClass());
        System.out.println("request.getParams(\"favorite\").size()==2 = " + (request.getParams("favorite").size() == 2));
        System.out.println("request.getParams(\"favorite\").get(0).equals('001') = " + request.getParams("favorite").get(0).equals("001"));
        System.out.println("request.getParams(\"favorite\").get(1).equals('002') = " + request.getParams("favorite").get(1).equals("002"));
    }

}
