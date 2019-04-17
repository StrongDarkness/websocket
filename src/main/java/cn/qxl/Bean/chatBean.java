package cn.qxl.Bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * Created by qiu on 2018/12/27.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class chatBean {
    private String username;
    private String toUser;
    private String msg;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    private boolean self;
}
