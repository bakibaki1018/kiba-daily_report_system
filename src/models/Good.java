package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "goods")
@NamedQueries({
    @NamedQuery(name = "findIsGoodByUser", query="SELECT g from Good g WHERE g.user_id = :user_id AND g.report_id = :report_id"),
    @NamedQuery(name = "getMyGoodCount", query = "SELECT COUNT(g) FROM Good AS g WHERE g.report_id = :report_id "),
})
@Entity
public class Good {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private Report report_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Employee user_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Report getReport_id() {
        return report_id;
    }

    public void setReport_id(Report report_id) {
        this.report_id = report_id;
    }

    public Employee getUser_id() {
        return user_id;
    }

    public void setUser_id(Employee user_id) {
        this.user_id = user_id;
    }




}
