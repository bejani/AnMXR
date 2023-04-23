package ir.bejani.db;

/**
 * Created by ulusen on 9/9/2018.
 */

public class clsCredit {
    String credit,creditName,cdate,cgroup;
    clsCredit(String cr_amount,String cr_name,String cr_date,String cr_group)
    {
        this.credit=cr_amount;
        this.creditName=cr_name;
        this.cdate=cr_date;
        this.cgroup=cr_group;

    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public void setCreditName(String creditName) {
        this.creditName = creditName;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public void setCgroup(String cgroup) {
        this.cgroup = cgroup;
    }

    public String getCredit() {
        return credit;
    }

    public String getCreditName() {
        return creditName;
    }

    public String getCdate() {
        return cdate;
    }

    public String getCgroup() {
        return cgroup;
    }
}
