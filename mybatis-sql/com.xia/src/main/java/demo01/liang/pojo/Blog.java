package demo01.liang.pojo;


import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;
@Alias("blog")
@Data
public class Blog {
    private String id;
    private String title;
    private String author;
    private Date createTime;
    private int views;
}
