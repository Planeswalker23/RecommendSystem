package Classify;

import recommend.DB_io;
import tool.NewsList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class ClassifyByKmp {

    public static int[] table;
    public static String path = "G:\\IdeaProjects\\RecommendSystem\\web\\keyword.txt";

    public static final int sortkeywordline = 8;
    public static final int usekeywordnum = 3;


    public static void generateTab(String key){//查询字串生成偏移对照表，一次迭代就可以
        int len=key.length();
        table=new int[len];
        Arrays.fill(table, 0);
        for(int i=1;i<len;i++){
            if(key.charAt(i)==key.charAt(table[i-1])){
                table[i]=table[i-1]+1;
            }
        }
    }
    public static int KMPSearchs(String doc,String key){
        generateTab(key);
        int result=-1;
        int doc_size=doc.length(),
                key_size=key.length(),
                doc_iter=0,
                key_iter=0;
        while(doc_iter<doc_size){//遍历所查询的文档，同样，单层循环就可以实现→_→
            if(doc.charAt(doc_iter)==key.charAt(key_iter)){
                doc_iter++;
                key_iter++;
            }else{
                if(key_iter==0){
                    doc_iter++;
                    continue;
                }else{
                    key_iter=table[key_iter-1];
                    continue;
                }
            }
            if(key_iter==key_size){
                result=doc_iter-key_size;
                break;
            }
        }
        return result;
    }

    public static String[] getTypeFromFile(String path){
        File file = new File(path);
        String[] types = new String[8];
        int index = 0;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String type = null;
            while((type=reader.readLine())!=null){
                types[index++] = type;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return types;
    }

    public static String[] classify(String word, String fileName){
        String[] type = new String[]{"社会","体育","娱乐","科技","人文","影视","教育","游戏"};
        int[] nums = {0,0,0,0,0,0,0,0};
        String[][] sortkeywords = new String[sortkeywordline][usekeywordnum];
        String[] sortresult = new String[4];
        File file = new File(fileName);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 0;
            while ((tempString = reader.readLine()) != null) {
                String[] keywords = new String[100];
                keywords = tempString.split("、");
                int keynum = 0;
                for (int i = 0; i < keywords.length; i++) {
                    int index = KMPSearchs(word, keywords[i]);
                    //System.out.println("ceshi " + keywords[i]);
                    if (index > 0){
                        nums[line]++;
                        //System.out.println("匹配 " +type[line] + ":" + keywords[i]);
                        if (keynum < 3){
                            sortkeywords[line][keynum] = keywords[i];
                            keynum++;
                        }
                        continue;
                    }
                }
                line++;
            }
            //获取匹配最大值及索引值
            int max = 0;
            int maxindex = 0;
            for (int i = 0; i < nums.length; i++) {
                if (max < nums[i]){
                    maxindex = i;
                    max = nums[i];
                }
            }
            //System.out.println("属于:" + type[maxindex] + "类;" + "匹配关键字：" + nums[maxindex]);
            //设置返回内容,存储在sortkeywords中的关键字传输到sortresult索引1~3中，索引0为类别名，sortresult整体作为返回值
            sortresult[0] = type[maxindex];
            for (int i = 0; i < usekeywordnum; i++) {
                sortresult[i+1] = sortkeywords[maxindex][i];
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return sortresult;
    }

    public static void main(String[] args) {

        String content = "";
        HashMap map = new HashMap();
        map.put("社会",0);
        map.put("体育",1);
        map.put("娱乐",2);
        map.put("科技",3);
        map.put("人文",4);
        map.put("影视",5);
        map.put("教育",6);
        map.put("游戏",7);
        for(int d=0;d<140611;d++){
            NewsList ns = tool.GetJsonText.getNewsFromDB(d);
            content = ns.getContent();
            if(content==null){
                continue;
            }
            String[] result = classify(content,path);
            DB_io.insertArticleTypeIntoDb(ns.getId(),(int)map.get(result[0]));
            System.out.println(ns.getId()+":"+(int)map.get(result[0]));
        }

    }
}
