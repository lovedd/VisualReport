package history;

import com.shiker.web.service.impl.ESRiskIpService;

public class ESRiskDevServiceTest {

	public static void main(String[] args) throws Exception {

		ESRiskIpService service = new ESRiskIpService();
		service.TimeSearch("20170322", "20170323");
	}
}
