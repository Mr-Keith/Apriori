package AprioriAlg;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Apriori {
	static int supNum=600;
	public static void main(String[] args) {
		// TODO Auto-generated method stub 
		JFrame f = new JFrame("Apriori");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(700, 700);
		f.setExtendedState(JFrame.NORMAL);
		f.setLocation(150, 50);

		Container con = f.getContentPane();
		JPanel p = new JPanel();
		p.setLayout(null);
		
		//textArea
		final JTextArea text = new JTextArea(800, 600);
		text.setText("");
		
		JScrollPane sp = new JScrollPane(text);
		sp.setBounds(0, 0, 700, 600);
		p.add(sp);
		
		//button1
		JButton button1 = new JButton("标准数据测试");
		button1.setBounds(20, 610, 200, 40);
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String strCon=AprioriAlg(1);

				text.setText(strCon);
			}
		});
		p.add(button1);
		//button2
		JButton button2 = new JButton("大型数据测试");
		button2.setBounds(460, 610, 200, 40);
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String strCon=AprioriAlg(2);

				text.setText(strCon);
			}
		});
		p.add(button2);

		con.add(p);
		f.setVisible(true);
		
	};
	
	
	
	static String AprioriAlg(int m){
		String JDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String connectDB = "jdbc:sqlserver://localhost:1433;DatabaseName=Apriori";
		String ConStr="";
		try {
			Class.forName(JDriver);
			//System.out.println("success drivered");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("error drivered");
			System.exit(0);
		}
		
		try {
			
			String user = "Keith_Song";
			String password = "s162202015.";
			Connection conn = DriverManager.getConnection(connectDB, user, password);
			//System.out.println("success linked");
			String sql ="";
			if(m==1){
				sql ="SELECT * FROM Test";
			}else if(m==2){
				StringBuffer suffix = new StringBuffer();
				String prefix="insert into Apriori.dbo.CI (TID,I1,I2,I3,I4,I5,I6,I7,I8,I9,I10) VALUES";
				Statement pst =conn.createStatement();
				//生成数据库
//				
//				for(int i=1;i<=100;i++){
//					for(int j=1;j<=100;j++){
//						suffix.append( "('T"+j*i+"',");
//						int t = (int) (11*Math.random());
//						int[] h=new int[11];
//						for(int q=1;q<=t;q++){
//							int l=(int) (11*Math.random());
//							h[l] = l;
//						}
//						for(int a=1;a<10;a++)
//						if(h[a]==a) suffix.append("'T"+a+"',"); else if(h[a]==0) suffix.append("null,");
//						if(h[10]==10) suffix.append("'T10'"); else if(h[10]==0) suffix.append("null");
//						suffix.append("),");
//						sql = prefix + suffix.substring(0, suffix.length() - 1);
//						pst.addBatch(sql); 
//						pst.executeBatch();
//						conn.commit();
//						suffix = new StringBuffer();
//					}
//				
//				}
				//生成结束
				sql ="SELECT * FROM CI";
			}
			
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			long begintime = System.currentTimeMillis();
			
			List<ArrayList<String>> A = new ArrayList<ArrayList<String>>();
			while (rs.next()) {
				int i=1;
			    ArrayList<String> a1 = new ArrayList<String>();
			    i++;
				for(;i<=(m==1?6:11);){
					if(rs.getString(i)!= null) 
					a1.add(rs.getString(i));
					i++;
				}
				A.add(a1);
			}
			System.out.println("原始数据："+A);
			ArrayList<itemSup> L1=supCount(A,m);
			List<ArrayList<String>> B = new ArrayList<ArrayList<String>>();
			List<ArrayList<itemSup>> S = new ArrayList<ArrayList<itemSup>>();
			S.add(L1);
			while(!L1.isEmpty()){
				B=generation(L1);
				L1=supCount(B,A,m);
				S.add(L1);
				//System.out.println(S.get(i).get(0).ItemList.contains(S.get(i-1).get(0).ItemList.get(0)));
				//System.out.println(S.get(i).get(0).ItemList+" "+S.get(i-1).get(0).ItemList.get(0));
			}
			ConAlg str = new ConAlg(S);
			//关联规则生成
		    ConStr=str.conf();
			
			long endtime=System.currentTimeMillis();
			System.out.println((endtime - begintime)+"ms");
			ConStr += "\ncostTime:";
			ConStr += (endtime - begintime);
			ConStr += "ms";
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("error");
			System.exit(0);
		}
		return ConStr;
	}
	//1项频繁项目集
	 static ArrayList<itemSup> supCount(List<ArrayList<String>> Str,int m) {
		ArrayList<itemSup> C1 =	 new ArrayList<itemSup>();
		String connectDB = "jdbc:sqlserver://localhost:1433;DatabaseName=Apriori";
		String user = "Keith_Song";
		String password = "s162202015.";
		String sql="";
		if(m==2){ 
			sql="SELECT count(I1),count(I2),count(I3),count(I4),count(I5),count(I6),count(I7),count(I8),count(I9),count(I10)FROM CI ";
			}
		else	{ 
			sql="SELECT count(I1),count(I2),count(I3),count(I4),count(I5) From Test";
			}
		
		try {
			Connection conn = DriverManager.getConnection(connectDB, user, password);
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			int i=1;
			while(rs.next()){
				for(;i<(m==1?6:11);){
					if ((rs.getInt(i)>=supNum)){
						List<String> a1=new ArrayList<String>();
						a1.add("T"+i);
						itemSup e = new itemSup(a1,rs.getInt(i));
						C1.add(e);
					}
					i++;
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return C1;	
	};
	
	//频繁项目集支持度计算
	 static ArrayList<itemSup> supCount(List<ArrayList<String>> Str,List<ArrayList<String>> A,int m){
		 
		 ArrayList<itemSup> C1 = new ArrayList<itemSup>();
		 //System.out.println(Str);
		 String connectDB = "jdbc:sqlserver://localhost:1433;DatabaseName=Apriori";
			String user = "Keith_Song";
			String password = "s162202015.";
			String sql="";
			for(int i=0;i<Str.size();i++){

				sql = "SELECT count(*) From";
				sql +=(m==1?" TEST WHERE":" CI WHERE");
				for(int j=0;j<Str.get(i).size();j++){
					if(j==0){
						for(int k=1;k<=(m==1?5:10);k++){
						if(Str.get(i).get(j).contains("T"+k))
							{sql += (String)(" I"+k);

							 sql += " is not null";}
						}
					
					}else{
						for(int k=2;k<=(m==1?5:10);k++){
							if(Str.get(i).get(j).contains("T"+k))
								{sql += (String)(" and I"+k);
								 sql += " is not null ";}
						}
					}
				}
					//System.out.println(sql);
			try {
				Connection conn = DriverManager.getConnection(connectDB, user, password);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery(sql);
				while(rs.next()){
					
						if ((rs.getInt(1)>=supNum)){
						itemSup e = new itemSup(Str.get(i),rs.getInt(1));
						C1.add(e);
						}
					
					
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			}
		
		 return C1;
	 }
	//产生候选集
	static List<ArrayList<String>> generation(ArrayList<itemSup> supCount){
		List<ArrayList<String>> Lk = new ArrayList<ArrayList<String>>();
		for(int i=0;i<supCount.size();i++){
			for(int j =i+1;j<supCount.size();j++){
				int k=0;
				boolean flag = true;
				if(supCount.get(i).ItemList.size()>1){
					for(;k<supCount.get(i).ItemList.size()-1;k++){
						flag=flag&&(supCount.get(i).ItemList.get(k)==supCount.get(j).ItemList.get(k));
						if(!flag) break;
					}
				}
				flag=flag&&(supCount.get(i).ItemList.get(k)!=supCount.get(j).ItemList.get(k));
				if(flag){
					ArrayList<String> L1 = new ArrayList<String>();
					for(int q=0;q<k;q++) L1.add(supCount.get(i).ItemList.get(q));
					L1.add(supCount.get(i).ItemList.get(k));
					L1.add(supCount.get(j).ItemList.get(k));
					//Lk.add(L1);
					if(cut(L1,supCount))Lk.add(L1);
				}
				
			}
			
		}

		//System.out.println("候选集:"+Lk);
		return Lk;
	};
	
	static  boolean cut(ArrayList<String> L1,ArrayList<itemSup> supCount){
		int count=0;
		for(int i=0;i<L1.size();i++){
			for(int j=0;j<supCount.size();j++){
				boolean flag=true;
				if(i==0){
					for(int k=1;k<L1.size();k++) flag=flag&&supCount.get(j).ItemList.contains(L1.get(k));
				}
				else if(i==L1.size()-1){
					for(int k=0;k<L1.size()-1;k++) flag=flag&&supCount.get(j).ItemList.contains(L1.get(k));
				}
				else{
					for(int k=0;k<i;k++) flag=flag&&supCount.get(j).ItemList.contains(L1.get(k));
					for(int k=i+1;k<L1.size();k++) flag=flag&&supCount.get(j).ItemList.contains(L1.get(k));
				}
				if(flag)count+=1;
			}
		}	
		if(count==L1.size())return true;
		else return false;
	}

	
	
}





