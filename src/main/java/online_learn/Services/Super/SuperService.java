package online_learn.Services.Super;

import java.util.Map;

public class SuperService {

    protected void setValueForHeaderFooter(Map<String, Object> data, boolean spinner, boolean navbar, boolean footer, boolean script) {
        data.put("spinner", spinner);
        data.put("navbar", navbar);
        data.put("footer", footer);
        data.put("script", script);
    }
}
