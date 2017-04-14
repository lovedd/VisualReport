package history;

import java.util.ArrayList;
import java.util.List;

import com.shiker.web.entity.EsEnsemble;
import com.shiker.web.service.impl.EsEnsembleService;

public class EsEnsembleServiceTest {

	public static void main(String[] args) throws Exception {

		EsEnsembleService service = new EsEnsembleService();
		List<EsEnsemble> list = service.TimeSearch("20170310", "20170324");
//		List<EsEnsemble> list = esEnsembleService.TimeSearch(startTime, endTime);
        List<EsEnsemble> newList = new ArrayList<EsEnsemble>();
        List<String> xAxis = new ArrayList<String>();
        
        if (list != null) {
        	newList.add(list.get(0));
        	for (int i = 1; i < list.size(); i++) {
        		if (list.get(i).getCdate().equals(list.get(i - 1).getCdate())) {
        			continue;
        		}
        		newList.add(list.get(i));
        	}
        }
        System.out.println("---------newList--------");
        for (int i = 0; i < newList.size(); i++) {
			System.out.println(newList.get(i).getCdate() + ", " + newList.get(i).getTotalCnt() + ", "+ newList.get(i).getHitRuleCnt() + ", " +newList.get(i).getReChargeCnt());
        }
	}
}
