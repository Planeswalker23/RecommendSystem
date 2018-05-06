package Antispam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20170825.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by hyliu on 16/3/2.
 * 文本检测
 */
public class TextAntispamScanSample extends BaseSample {

    public static void main(String[] args) throws Exception {
        String text = "傻逼";
        AntispamScanResult asr = TextFilter(text);
        System.out.println(asr.getScene());
        System.out.println(asr.getLabel());
        System.out.println(asr.getSuggestion());
    }

    public static AntispamScanResult TextFilter(String content){
        AntispamScanResult asr = new AntispamScanResult();

        //请替换成你自己的accessKeyId、accessKeySecret
        IClientProfile profile = DefaultProfile.getProfile("cn-shanghai", "LTAIiY3klMjraJmZ", "c0A5dWVuOgaNNmiw3Szp69wLY1dVMe");
        try {
            DefaultProfile.addEndpoint(getEndPointName(), "cn-shanghai", "Green", getDomain());
        } catch (ClientException e) {
            e.printStackTrace();
        }

        IAcsClient client = new DefaultAcsClient(profile);

        TextScanRequest textScanRequest = new TextScanRequest();
        textScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        textScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        textScanRequest.setEncoding("UTF-8");
        textScanRequest.setRegionId(regionId);


        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        Map<String, Object> task1 = new LinkedHashMap<String, Object>();
        task1.put("dataId", UUID.randomUUID().toString());
        task1.put("content",content );

        tasks.add(task1);

       /* Map<String, Object> task2 = new LinkedHashMap<String, Object>();
        task2.put("dataId", UUID.randomUUID().toString());
        task2.put("content", "蒙汗药法轮功");

        tasks.add(task2);*/

        /*Map<String, Object> task3 = new LinkedHashMap<String, Object>();
        task3.put("dataId", UUID.randomUUID().toString());
        task3.put("content", "正常人");

        tasks.add(task3);*/
        JSONObject data = new JSONObject();
        data.put("scenes", Arrays.asList("antispam"));
        data.put("tasks", tasks);

        try {
            textScanRequest.setHttpContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 请务必设置超时时间
         */
        textScanRequest.setConnectTimeout(3000);
        textScanRequest.setReadTimeout(6000);
        try {
            HttpResponse httpResponse = client.doAction(textScanRequest);

            if(httpResponse.isSuccess()){
                JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
                System.out.println(JSON.toJSONString(scrResponse, true));
                if (200 == scrResponse.getInteger("code")) {
                    JSONArray taskResults = scrResponse.getJSONArray("data");
                    for (Object taskResult : taskResults) {
                        if(200 == ((JSONObject)taskResult).getInteger("code")){
                            JSONArray sceneResults = ((JSONObject)taskResult).getJSONArray("results");
                            for (Object sceneResult : sceneResults) {
                                String scene = ((JSONObject)sceneResult).getString("scene");
                                String suggestion = ((JSONObject)sceneResult).getString("suggestion");
                                String label = ((JSONObject)sceneResult).getString("label");
                                //根据scene和suggetion做相关的处理
                                //do something
                                System.out.println("args = [" + scene + "]");
                                System.out.println("args = [" + label + "]");
                                System.out.println("args = [" + suggestion + "]");
                                asr.setScene(scene);
                                asr.setLabel(label);
                                asr.setSuggestion(suggestion);
                            }
                        }else{
                            System.out.println("task process fail:" + ((JSONObject)taskResult).getInteger("code"));
                        }
                    }
                } else {
                    System.out.println("detect not success. code:" + scrResponse.getInteger("code"));
                }
            }else{
                System.out.println("response not success. status:" + httpResponse.getStatus());
            }

        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return asr;
    }

}
