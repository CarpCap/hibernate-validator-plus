import com.carpcap.hvp.annotation.CJson;
import com.carpcap.hvp.utils.CValid;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** @CJson JSON 语法和根节点类型测试。 */
public class CJsonValidatorTest {
    private static class Bean {
        @CJson String defaultStruct;
        @CJson(type = CJson.Type.STRUCT) String struct;
        @CJson(type = CJson.Type.VALUE) String value;
        @CJson(type = CJson.Type.ANY) String any;
        @CJson(type = CJson.Type.OBJECT) String object;
        @CJson(type = CJson.Type.ARRAY) String array;
        @CJson(allowNull = false, type = CJson.Type.ANY) String required;
    }

    @Test
    public void validatesJsonSyntaxAndRootType() {
        Bean bean = new Bean();
        bean.defaultStruct = "{\"name\":\"test\",\"items\":[1,true,null]}";
        bean.struct = "[1, true, null]";
        bean.value = "\"text\"";
        bean.any = "true";
        bean.object = "{\"ok\":true}";
        bean.array = "[1, 2, 3]";
        bean.required = "null";
        assertTrue(CValid.tryValidate(bean).isEmpty());

        bean.any = "{bad}";
        assertFalse(CValid.tryValidateProperty(bean, "any").isEmpty());
        bean.object = "[1]";
        assertFalse(CValid.tryValidateProperty(bean, "object").isEmpty());
        bean.array = "{\"a\":1}";
        assertFalse(CValid.tryValidateProperty(bean, "array").isEmpty());
    }

    @Test
    public void validatesDefaultAndStructType() {
        Bean bean = new Bean();
        bean.defaultStruct = "{}";
        bean.struct = "[]";
        assertTrue(CValid.tryValidateProperty(bean, "defaultStruct").isEmpty());
        assertTrue(CValid.tryValidateProperty(bean, "struct").isEmpty());

        bean.defaultStruct = "\"text\"";
        bean.struct = "123";
        assertFalse(CValid.tryValidateProperty(bean, "defaultStruct").isEmpty());
        assertFalse(CValid.tryValidateProperty(bean, "struct").isEmpty());
    }

    @Test
    public void validatesValueType() {
        Bean bean = new Bean();
        String[] values = {"\"text\"", "123.45e-2", "true", "false", "null"};
        for (String value : values) {
            bean.value = value;
            assertTrue(CValid.tryValidateProperty(bean, "value").isEmpty());
        }

        bean.value = "{}";
        assertFalse(CValid.tryValidateProperty(bean, "value").isEmpty());
        bean.value = "[]";
        assertFalse(CValid.tryValidateProperty(bean, "value").isEmpty());
    }

    @Test
    public void validatesAnyType() {
        Bean bean = new Bean();
        String[] values = {"{}", "[]", "\"text\"", "123", "true", "null"};
        for (String value : values) {
            bean.any = value;
            assertTrue(CValid.tryValidateProperty(bean, "any").isEmpty());
        }
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
