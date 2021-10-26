package demo01.liang.pojo;


import lombok.Data;
import org.apache.ibatis.type.Alias;

@Alias("Student")
@Data
public class Student {
    private int id;
    private String name;
    private int tid;
}


