package demo01.liang.utils;

import java.util.UUID;

@SuppressWarnings("all")
public class IdUtils {

    public static String getId(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
//    @Test
//    public void test(){
//        System.out.println(IdUtils.getId());
//    }
}
