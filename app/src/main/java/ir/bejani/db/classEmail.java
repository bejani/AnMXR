package ir.bejani.db;

public class classEmail {
	long id;
	String email;
	String ename;
	public classEmail(){
		
	}
	public long getId(){
		return this.id;
	}
	public void setId(long id){
		this.id=id;
	}
	public void setEmail(String email){
		this.email=email;
	}
	public String getEmail(){
		return this.email;
	}
	public void setEname(String ename){
		this.ename=ename;
	}
	public String getEname(){
		return this.ename;
	}
	public String toString(){
		return this.email+this.ename;
	}

}
