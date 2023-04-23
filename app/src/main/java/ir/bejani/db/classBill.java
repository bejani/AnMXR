package ir.bejani.db;

public class classBill {
	private long billPayable;
	private int billStartDate,billStopDate,payDate;
	private String billID,PayID;
	private String billType;
	private long btid;
	
	public long getbtid(){
		return btid;
	}
	public void setbtid(long btid){
		this.btid=btid;
	}
	public long getBillPayable(){
		return billPayable;
	}
	public void setBillPayable(long bp){
		this.billPayable=bp;
	}
	public int getBillStartDate(){
		return this.billStartDate;
	}
	public void setBillStartDate(int bsd){
		this.billStartDate=bsd;
	}
	public int getBillStopDate(){
		return this.billStopDate;
	}
	public void setBillStopDate(int bsd){
		this.billStopDate=bsd;
	}
	public String getBillID(){
		return this.billID;
	}
	public void setBillID(String bid){
		this.billID=bid;
	}
	public String getPayID(){
		return this.PayID;
	}
	public void setPayID(String pid){
		this.PayID=pid;
	}public String getBillType(){
		return this.billType;
	}
	public void setBillType(String bt){
		this.billType=bt;
	}
	public int getPayDate(){
		return this.payDate;
	}
	public void setPayDate(int pdate){
		this.payDate=pdate;
	}
public String toString(){
	return this.billID+".."+PayID+".."+billType+".."+billPayable+".."+billStartDate+".."+billStopDate+".."+payDate;
}
}
