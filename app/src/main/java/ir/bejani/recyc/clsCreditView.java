package ir.bejani.recyc;

/**
 * Created by ulusen on 10/25/2018.
 */

public class clsCreditView {
    private String cid,credit,crdtName,crdtDate,crdtYear,crdtMonth,crdtDay,crdtGrp,crdtFlag;

    public void setCrdtYear(String crdtYear) {
        this.crdtYear = crdtYear;
    }

    public void setCrdtMonth(String crdtMonth) {
        this.crdtMonth = crdtMonth;
    }

    public void setCrdtDay(String crdtDay) {
        this.crdtDay = crdtDay;
    }

    public String getCrdtYear() {
        return crdtYear;
    }

    public String getCrdtMonth() {
        return crdtMonth;
    }

    public String getCrdtDay() {
        return crdtDay;
    }

    public clsCreditView(String cid, String credit, String crdtName, String crdtDate,String crdtYear,String crdtMonth,String crdtDay, String crdtGrp, String flag) {
        this.cid = cid;
        this.credit = credit;
        this.crdtName = crdtName;
        this.crdtDate = crdtDate;
        this.crdtYear = crdtYear;
        this.crdtMonth = crdtMonth;
        this.crdtDay = crdtDay;

        this.crdtGrp = crdtGrp;
        this.crdtFlag=flag;
    }

    public clsCreditView() {
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCid() {
        return cid;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public void setCrdtName(String crdtName) {
        this.crdtName = crdtName;
    }

    public void setCrdtDate(String crdtDate) {
        this.crdtDate = crdtDate;
    }

    public void setCrdtGrp(String crdtGrp) {
        this.crdtGrp = crdtGrp;
    }

    public String getCredit() {
        return credit;
    }

    public String getCrdtName() {
        return crdtName;
    }

    public String getCrdtDate() {
        return crdtDate;
    }

    public String getCrdtGrp() {
        return crdtGrp;
    }
    public String getCrdtFlag(){return crdtFlag;}
    public void setCrdtFlag(String flag){this.crdtFlag=flag;}
}
