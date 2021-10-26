package demo01.liang.pojo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias("Teacher")
@Data
public class Teacher {
    private int id;
    private String name;
    private List<Student> students;
}
