package parse.ex1;

import java.util.ArrayList;
import java.util.HashMap;

public class MyRequest {

    private HashMap<String, String> param;
    private HashMap<String, ArrayList<String>> params;

    public String getParam(String id) {
        return param.get(id);
    }

    public ArrayList<String> getParams(String param) {
        return params.get(param);
    }

    public MyRequest(HashMap<String, String> param, HashMap<String, ArrayList<String>> params) {
        this.param = param;
        this.params = params;
    }

    public MyRequest(){}

    public MyRequest parse(String queryString) {
        StringBuilder parameter = new StringBuilder();
        HashMap<String, String> param = new HashMap<>();
        HashMap<String, ArrayList<String>> params = new HashMap<>();

        int index = queryString.indexOf("?");
        for (int i = index + 1; i < queryString.length(); i++) {
            parameter.append(queryString.charAt(i));
        }

        String[] object = parameter.toString().split("&");

        for (int i = 0; i < object.length; i++) {
            int objectIndex = object[i].indexOf("=");
            String key = object[i].substring(0, objectIndex);
            String value = object[i].substring(objectIndex + 1, object[i].length());
            addMap(param,params,key,value);
        }

        return new MyRequest(param, params);
    }

    private void addMap(HashMap<String, String> param,
                        HashMap<String, ArrayList<String>> params,
                        String key,
                        String value){

        if (!param.containsKey(key) && !params.containsKey(key)) { //param과 params에 키가 둘 다 없는 경우
            param.put(key, value);
        } else if (param.containsKey(key) && !params.containsKey(key)) { //param에는 있지만 prams에는 없는 경우
            String paramValue = param.get(key);
            param.remove(key);

            ArrayList<String> list = new ArrayList<>();
            list.add(paramValue);
            list.add(value);

            params.put(key, list);
        } else if (!param.containsKey(key) && params.containsKey(key)) { // param에는 없지만 params에는 있는 경우
            ArrayList<String> list = params.get(key);
            list.add(value);

            params.put(key, list);
        }
    }

}
