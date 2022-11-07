package pojo;

import com.pojo.Register;
import com.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class RegisterTest {
    @Test
    public void registerTest() {
        Register.register("张三","233333","zhang3@null.mail");

    }
}
