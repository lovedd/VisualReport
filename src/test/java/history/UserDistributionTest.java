package history;

import com.shiker.web.service.impl.EsUserDistributionService;

public class UserDistributionTest {

	public static void main(String[] args) throws Exception {

		EsUserDistributionService service = new EsUserDistributionService();
		service.TimeSearch("20170322", "20170323");
	}
}
