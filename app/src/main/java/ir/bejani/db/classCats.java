package ir.bejani.db;


public class classCats {
	private long id;
	private String name;
	public void classcats(){
		
	}
//	public void classCats(Long id,String cname){
//		this.id=id;
//		this.name=cname;
//	}
	public long getId(){
		 return id;
	}
	public void setId(long id){
		this.id=id;
	}
	public String getName(){
		return name.toString();
	}
	public void setName(String catName){
		this.name=catName;
	}
	public String toString(){
		return this.name;
	}

}
