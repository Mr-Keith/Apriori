package AprioriAlg;

import java.util.ArrayList;
import java.util.List;

public class ConAlg {
	double conNum=0.45;
	List<ArrayList<itemSup>> S = new ArrayList<ArrayList<itemSup>>();
	public  ConAlg(List<ArrayList<itemSup>> str){
		S=str;
	}
	String conf(){
		
		String conStr="";
		for(int i=1;i<S.size();i++){
			for(int S1=0;S1<S.get(i).size();S1++){
				for(int j=0;j<i;j++){
					for(int T1=0;T1<S.get(j).size();T1++){
						boolean flag=true;
						for(int T2=0;T2<S.get(j).get(T1).ItemList.size();T2++){
							flag=flag&&(S.get(i).get(S1).ItemList.contains(S.get(j).get(T1).ItemList.get(T2)));
						}
						if(flag){
							double conf=(double)(S.get(i).get(S1).supportNum)/(double)(S.get(j).get(T1).supportNum);
							if(conf>conNum){
								conStr += S.get(j).get(T1).ItemList+"->"+S.get(i).get(S1).ItemList+",conf="+conf+"\n";
								System.out.println(S.get(j).get(T1).ItemList+"->"+S.get(i).get(S1).ItemList+",conf="+conf);
							}
						}
					}
				}
			}
		}
		return conStr;
	}
	
	
	
}
