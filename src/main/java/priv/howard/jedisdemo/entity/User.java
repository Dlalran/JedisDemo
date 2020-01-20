package priv.howard.jedisdemo.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements Serializable {
    /**
     * 注意：实体类要实现序列化接口，且属性名前两位必须全为小写或全为大写
     */
    private String id;
    private String name;
    private int age;
    private Address address;
}
