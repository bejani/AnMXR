package ir.bejani.db;

public class classCredits {
	private static final String LOGTAG = null;
	private long id;
	private int credit;
	private String creditName;
	private int ccat;
	private int year;
	private int month;
	private int day;
	private int insDate;
	private long flag;
	public classCredits(){
		//Log.i(LOGTAG,"in the classCredits less args constructor...");

	}

	public long getId(){
		 return id;
	}
	public void setId(long id){
		this.id=id;
	}
	
	public int getCredit(){
		return credit;
	}
	public void setCredit(int credit){
		this.credit=credit;
	}
	public int getYear(){
		return year;
	}
	public void setYear(int year){
		this.year=year;
	}
	public int getMonth(){
		return month;
	}
	public void setMonth(int month){
		this.month=month;
	}
	public int getDay(){
		return day;
	}
	public void setDay(int day){
		this.day=day;
	}
	
	public String getCreditName(){
		return creditName;
	}
	public void setCreditName(String creditName){
		this.creditName=creditName;
	}
	

	public int getCreditCat(){
		return ccat;
	}
	public void setCreditCat(int ccat){
		this.ccat=ccat;
	}
	public int getInsDate(){
		return insDate;
	}
	public void setInsDate(int insDate){
		this.insDate=insDate;
	}
	public long getFlag(){
		return flag;
	}
	public void setFlag(long flg){
		this.flag=flg;
	}
	public String toString(){
		return  "���� "+this.credit+" ����  "+this.creditName+" [�����  " +this.year+"/"+this.month+"/"+this.day+"]"+this.ccat;
	}


}
