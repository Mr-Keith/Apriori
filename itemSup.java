package AprioriAlg;
import java.util.ArrayList;
import java.util.List;


public class itemSup {

		  List<String> ItemList ;
		  int supportNum;
		  
		  public itemSup(){
			  this.ItemList = new ArrayList<String>();
			  this.supportNum = 0;
		  };
		  public itemSup(List<String> Str,int a){
			  this.ItemList = Str.subList(0, Str.size());
			  this.supportNum = a;
		  }
		 void itemSet(List<String> Str){
			 this.ItemList = Str.subList(0, Str.size()); 
		 }
		 void supSet(int a){
			 this.supportNum = a;
		 }
	}
