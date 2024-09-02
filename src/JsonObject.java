

import java.io.*;
import java.util.*;

class TokenFormatter {
    public String Content = null;
    public String TokenType = "string";
    /**
     * Token Type:
     * - string
     * - :
     * - {
     * - ,
     * - }
     * - [
     * - ]
     * - ,
     * - number
     */
    public TokenFormatter(String Content , String TokenType) {
        this.Content = Content;
        this.TokenType = TokenType;
    }

    @Override
    public String toString() {
        return "<"+this.Content+"> ";
    }
}

public class JsonObject {
    public TreeMap<Object , Object> objectObjectTreeMap = new TreeMap<>();

    public static String getFileString(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputStream = new FileInputStream(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            stringBuilder.append(line);
            stringBuilder.append("\n");
        }
        bufferedReader.close();
        inputStream.close();
        return stringBuilder.toString();
    }

    private ArrayList<TokenFormatter> getTokenArrays(String JsonString) throws Exception {
        ArrayList<TokenFormatter> formatterArrayList = new ArrayList<>();

        StringBuilder TmpStrings = new StringBuilder();
        boolean isIn_3 = false; // "

        //int In_1_number = 0;
        int In_4_number = 0;

        for (int i = 0 ; i < JsonString.length() ; i++) {
            char string = JsonString.charAt(i);
            if (string == ' ' && !isIn_3) {
                continue;
            }
            else if (string == '\n') {
                continue;
            }
            else if (string == '{' && !isIn_3) {
                //In_1_number += 1;
                TokenFormatter tokenFormatter = new TokenFormatter("{" , "{" );
                formatterArrayList.add(tokenFormatter);
                TmpStrings = new StringBuilder();
                continue;
            }
            else if (string == '}' && !isIn_3) {
                //In_1_number -= 1;
                formatterArrayList.add(new TokenFormatter(TmpStrings.toString() , getType(TmpStrings.toString())));
                TokenFormatter tokenFormatter = new TokenFormatter("}" , "}");
                formatterArrayList.add(tokenFormatter);
                TmpStrings = new StringBuilder();
                continue;
            }
            else if (string == '[' && !isIn_3) {
                In_4_number += 1;
                TokenFormatter tokenFormatter = new TokenFormatter("[" , "[" );
                formatterArrayList.add(tokenFormatter);
                TmpStrings = new StringBuilder();
                continue;
            }
            else if (string == ']' && !isIn_3) {
                In_4_number -= 1;
                if (In_4_number == 0) {
                    if (!TmpStrings.toString().trim().isEmpty()) {
                        TokenFormatter tokenFormatter = new TokenFormatter(TmpStrings.toString(), getType(
                                TmpStrings.toString()
                        ));
                        formatterArrayList.add(tokenFormatter);
                    }
                    TokenFormatter tokenFormatter = new TokenFormatter("]" , "]");
                    formatterArrayList.add(tokenFormatter);
                    TmpStrings = new StringBuilder();
                    continue;
                }
                formatterArrayList.add(new TokenFormatter(TmpStrings.toString() , getType(TmpStrings.toString())));
                TokenFormatter tokenFormatter = new TokenFormatter("]" , "]");
                formatterArrayList.add(tokenFormatter);
                TmpStrings = new StringBuilder();
                continue;
            }
            else if (string == '\\' && isIn_3) {
                if (i + 1 != JsonString.length()) {
                    char NextChar = JsonString.charAt(i + 1);
                    if (NextChar == 'n') {
                        TmpStrings.append("\n");
                    }
                    else if (NextChar == 't') {
                        TmpStrings.append("\t");
                    }
                    else if (NextChar == 's') {
                        TmpStrings.append(" ");
                    }
                    else if (NextChar == 'r') {
                        TmpStrings.append("\r");
                    }
                    else if (NextChar == '\"') {
                        TmpStrings.append("\"");

                    }
                    else {
                        if (NextChar == 'u') {
                            String unicodeString = "\\u"+JsonString.substring(i+2,i+6);
                            String unicode = unicodeString.substring(2);
                            char character = (char) Integer.parseInt(unicode, 16);
                            TmpStrings.append(character);
                            i += 5;
                            continue;
                        }
                    }
                    i++;
                }
                continue;
            }
            else if (string == '\"' && !isIn_3) {
                isIn_3 = true;
                TmpStrings.append(string);
                continue;
            }
            else if (string == '\"' && isIn_3) {
                isIn_3 = false;
                TmpStrings.append(string);
                TokenFormatter tokenFormatter = new TokenFormatter(TmpStrings.toString(), "string");
                formatterArrayList.add(tokenFormatter);
                TmpStrings = new StringBuilder();
                continue;
            }
            else if (string == '.') {
                TmpStrings.append(string);
                continue;
            }
            else if (string == ',' && !isIn_3) {
                TokenFormatter tokenFormatter = new TokenFormatter(TmpStrings.toString(), getType(TmpStrings.toString()));
                if (!tokenFormatter.Content.isEmpty()) {
                    formatterArrayList.add(tokenFormatter);
                }
                TmpStrings = new StringBuilder();
                formatterArrayList.add(new TokenFormatter(",", ","));
                continue;
            }
            else if ((string == ':') && !isIn_3) {
                TmpStrings = new StringBuilder();
                TmpStrings.append(string);
                TokenFormatter tokenFormatter = new TokenFormatter(TmpStrings.toString(), getType(TmpStrings.toString()));
                formatterArrayList.add(tokenFormatter);
                TmpStrings = new StringBuilder();
                continue;
            }
            else if (this.IsNumber(string)) {
                TmpStrings.append(string);
                continue;
            }
            else {
                TmpStrings.append(string);
                if (i + 1 == JsonString.length() ) {
                    if (!TmpStrings.toString().trim().isEmpty()) {
                        TokenFormatter tokenFormatter = new TokenFormatter(
                                TmpStrings.toString(),
                                getType(TmpStrings.toString()));
                        formatterArrayList.add(tokenFormatter);
                    }
                    break;
                }
            }
        }
        return formatterArrayList;
    }
    private boolean IsNumber(char number) {
        try {
            Double.parseDouble(String.valueOf(number));
            return true;
        }catch (Exception e) {
            return false;
        }
    }
    private boolean IsNumber(String number) {
        try {
            Double.parseDouble(number);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
    private String getType(String string) {
        string = string.trim();
        if (string.equals("{") ||
                string.equals("}") ||
                string.equals("[") ||
                string.equals("]") ||
                string.equals(",") ||
                string.equals(":")
        ) {
            return string;
        }
        else if (this.IsNumber(string)) {
            return "number";
        }
        else {
            return "string";
        }
    }
    private String DealStrings(String str) {
        if (str.startsWith("\"") && str.endsWith("\"")) {
            str = str.substring(1,  str.length()-1);
        }
        return str;
    }

    private ArrayList<Object> EntryToList(ArrayList<TokenFormatter> formatterArrayList) {
        ArrayList<Object> list = new ArrayList<>();
        for (int i = 0 ; i < formatterArrayList.size() ;i++) {
            TokenFormatter token = formatterArrayList.get(i);
            if (
                    token.TokenType.equals("string") ||
                            token.TokenType.equals("number")
            ) {
                list.add(formatterArrayList.get(i).Content);
                continue;
            }
            else if (token.TokenType.equals("{")) {
                int number = 0;
                ArrayList<TokenFormatter> EntryTokens = new ArrayList<>();
                for (int j = i; j < formatterArrayList.size() ;j++) {
                    TokenFormatter new_t = formatterArrayList.get(j);
                    if (new_t.TokenType.equals("{")) {
                        number += 1;
                        EntryTokens.add(new_t);
                        continue;
                    }
                    else if (new_t.TokenType.equals("}")) {
                        number -= 1;
                        EntryTokens.add(new_t);
                        if (number == 0) {
                            break;
                        }
                        continue;
                    }
                    else if (new_t.Content.isEmpty()) {
                        continue;
                    }
                    else {
                        EntryTokens.add(new_t);
                    }
                }
                i += EntryTokens.size();
                EntryTokens.remove(0);
                EntryTokens.remove(EntryTokens.size()-1);
                TreeMap<Object,Object> treeMap = this.DealToMap(EntryTokens);
                list.add(treeMap);
                continue;
            }
            else if (token.TokenType.equals("[")) {
                int number = 0;
                ArrayList<TokenFormatter> EntryTokens = new ArrayList<>();
                for (int j = i; j < formatterArrayList.size() ;j++) {
                    TokenFormatter new_t = formatterArrayList.get(j);
                    if (new_t.TokenType.equals("[")) {
                        number += 1;
                        EntryTokens.add(new_t);
                        continue;
                    }
                    else if (new_t.TokenType.equals("]")) {
                        number -= 1;
                        EntryTokens.add(new_t);
                        if (number == 0) {
                            break;
                        }
                        continue;
                    }
                    else {
                        EntryTokens.add(new_t);
                    }
                }
                i += EntryTokens.size();
                EntryTokens.remove(0);
                EntryTokens.remove(EntryTokens.size()-1);
                list.add(this.EntryToList(EntryTokens));
                continue;
            }
        }

        return list;
    }

    private TreeMap<Object,Object> DealToMap(ArrayList<TokenFormatter> formatterArrayList) {
        TreeMap<Object , Object> objectTreeMap = new TreeMap<>();
        for (int i = 0 ; i < formatterArrayList.size() ;i++) {
            TokenFormatter tokenFormatter = formatterArrayList.get(i);
            if (tokenFormatter.Content.isEmpty()) {
                continue;
            }
            if (
                    (tokenFormatter.TokenType.equals("string") || tokenFormatter.TokenType.equals("number")) &&
                            (i + 2 < formatterArrayList.size() && formatterArrayList.get(i+1).TokenType.equals(":"))
            ) {
                // key
                TokenFormatter t = formatterArrayList.get(i+2);
                if (t.TokenType.equals("string") || t.TokenType.equals("number")) {
                    objectTreeMap.put(this.DealStrings(tokenFormatter.Content) , this.DealStrings(t.Content));
                    i += 2;
                    continue;
                }
                else if (t.TokenType.equals("{")) {
                    int number = 0;
                    ArrayList<TokenFormatter> EntryTokens = new ArrayList<>();
                    for (int j = i+2; j < formatterArrayList.size() ;j++) {
                        TokenFormatter new_t = formatterArrayList.get(j);
                        if (new_t.TokenType.equals("{")) {
                            number += 1;
                            EntryTokens.add(new_t);
                            continue;
                        }
                        else if (new_t.TokenType.equals("}")) {
                            number -= 1;
                            EntryTokens.add(new_t);
                            if (number == 0) {
                                break;
                            }
                            continue;
                        }
                        else {
                            EntryTokens.add(new_t);
                        }
                    }
                    i += EntryTokens.size();
                    EntryTokens.remove(0);
                    EntryTokens.remove(EntryTokens.size()-1);
                    TreeMap<Object,Object> treeMap = this.DealToMap(EntryTokens);
                    objectTreeMap.put(this.DealStrings(tokenFormatter.Content) , treeMap);
                    continue;
                }
                else if (t.TokenType.equals("[")) {
                    int number = 0;
                    ArrayList<TokenFormatter> EntryTokens = new ArrayList<>();
                    for (int j = i+2; j < formatterArrayList.size() ;j++) {
                        TokenFormatter new_t = formatterArrayList.get(j);
                        if (new_t.TokenType.equals("[")) {
                            number += 1;
                            EntryTokens.add(new_t);
                            continue;
                        }
                        else if (new_t.TokenType.equals("]")) {
                            number -= 1;
                            EntryTokens.add(new_t);
                            if (number == 0) {
                                break;
                            }
                            continue;
                        }
                        else {
                            EntryTokens.add(new_t);
                        }
                    }
                    i += EntryTokens.size();
                    EntryTokens.remove(0);
                    EntryTokens.remove(EntryTokens.size()-1);
                    ArrayList<Object> list = this.EntryToList(EntryTokens);
                    objectTreeMap.put(this.DealStrings(tokenFormatter.Content) , list);
                    continue;
                }
            }
        }
        return objectTreeMap;
    }

    public JsonObject(String JsonString) throws Exception{
        // Deal the json string.
        JsonString = JsonString.trim();
        if (!JsonString.startsWith("{") || !JsonString.endsWith("}")) {
            throw new Exception("Format Json Object Error: Can not found '{' or '}' .");
        }
        ArrayList<TokenFormatter> formatterArrayList = this.getTokenArrays(JsonString);
        //System.out.println(formatterArrayList);
        formatterArrayList.remove(0);
        formatterArrayList.remove(formatterArrayList.size()-1);
        this.objectObjectTreeMap = this.DealToMap(formatterArrayList);
        //System.out.println(this.objectObjectTreeMap);
    }

    public Object get(String key) {
        return this.objectObjectTreeMap.get(key);
    }

    public TreeMap<Object , Object> JsonToTreeMap() {
        return this.objectObjectTreeMap;
    }

    public void setTreeMap(TreeMap<Object , Object> treeMap) {
        this.objectObjectTreeMap = treeMap;
    }

    private String replaceString(String str) {
        str = str.replace("\n", "\\n");
        str = str.replace("\t" , "\\t");
        str = str.replace("\r" , "\\r");
        str = str.replace("\"" , "\\\"");
        str = str.replace("\\\\" , "\\");
        return str;
    }

    private String ListToJson(ArrayList<Object> arrayList) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (Object dom : arrayList) {
            if (dom instanceof TreeMap<?,?>) {
                stringBuilder.append(this.TreeMapToJson((TreeMap<Object, Object>) dom));
            }
            else if (dom instanceof ArrayList<?>) {
                stringBuilder.append(this.ListToJson((ArrayList<Object>) dom));
            }
            else if (this.IsNumber(dom.toString())) {
                stringBuilder.append(dom);
            }
            else if (dom.toString().toLowerCase().equalsIgnoreCase("true") || dom.toString().toLowerCase().equalsIgnoreCase("false"))
            {
                stringBuilder.append(dom);
            }
            else {
                stringBuilder.append("\"");
                stringBuilder.append(replaceString(dom.toString()));
                stringBuilder.append("\"");
            }
            stringBuilder.append(",");
        }
        String s = stringBuilder.toString();
        s = s.substring(0 , s.length()-1);
        s += "]";
        return s;
    }

    public String TreeMapToJson(TreeMap<Object , Object> treeMap) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        for (Object key : treeMap.keySet()) {
            String KeyStr = key.toString();
            Object value = treeMap.get(key);

            stringBuilder.append("\"");
            stringBuilder.append(KeyStr);
            stringBuilder.append("\"");
            stringBuilder.append(":");
            if (value instanceof Map<?,?>) {
                stringBuilder.append(this.TreeMapToJson((TreeMap<Object, Object>) value));
            }
            else if (value instanceof ArrayList<?>) {
                stringBuilder.append(this.ListToJson((ArrayList<Object>) value));
            }
            else if (this.IsNumber(value.toString())) {
                stringBuilder.append(value);
            }
            else {
                stringBuilder.append("\"");
                stringBuilder.append(this.replaceString(value.toString()));
                stringBuilder.append("\"");
            }
            stringBuilder.append(",");
        }
        String s = stringBuilder.toString();
        s = s.substring(0 , s.length()-1);
        s += "}";

        return s;
    }
    public void JsonToFile(String path) throws Exception {
        FileWriter fileWriter = new FileWriter(path , false);
        fileWriter.write(this.TreeMapToJson(this.objectObjectTreeMap));
        fileWriter.flush();
        fileWriter.close();
    }
}
