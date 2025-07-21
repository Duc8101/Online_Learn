package online_learn.services.faq;

import online_learn.services.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FAQService extends BaseService implements IFAQService {

    @Override
    public Map<String, Object> index() {
        Map<String, Object> data = new HashMap<>();
        setValueForHeaderFooter(data, false, true, true, false);
        return data;
    }
}
