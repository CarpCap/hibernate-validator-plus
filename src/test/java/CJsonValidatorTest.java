import com.carpcap.hvp.annotation.CJson;
import com.carpcap.hvp.utils.CValid;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** @CJson JSON 语法和根节点类型测试。 */
public class CJsonValidatorTest {
    private static class Bean {
        @CJson String any;
        @CJson(type = CJson.Type.OBJECT) String object;
        @CJson(type = CJson.Type.ARRAY) String array;
        @CJson(allowNull = false,type = CJson.Type.ANY) String required;
    }

    @Test
    public void validatesJsonSyntaxAndRootType() {
        Bean bean = new Bean();
        bean.any = "{\"name\":\"test\",\"items\":[1,true,null]}";
        bean.object = "{\"ok\":true}";
        bean.array = "[1, 2, 3]";
        bean.required = "\"text\"";
        assertTrue(CValid.tryValidate(bean).isEmpty());

        bean.any = "{bad}";
        assertFalse(CValid.tryValidateProperty(bean, "any").isEmpty());
        bean.object = "[1]";
        assertFalse(CValid.tryValidateProperty(bean, "object").isEmpty());
        bean.array = "{\"a\":1}";
        assertFalse(CValid.tryValidateProperty(bean, "array").isEmpty());
    }

    @Test
    public void validatesNullPolicy() {
        Bean bean = new Bean();
        bean.required = null;
        assertFalse(CValid.tryValidateProperty(bean, "required").isEmpty());
        bean.required = " ";
        assertFalse(CValid.tryValidateProperty(bean, "required").isEmpty());
    }
}
