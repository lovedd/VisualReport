package history;

import com.shiker.web.service.impl.ESRiskRuleService;

public class ESRiskRuleServiceTest {

	public static void main(String[] args) throws Exception {

		ESRiskRuleService service = new ESRiskRuleService();
		service.TimeSearch("20170322", "20170323");
	}
}
