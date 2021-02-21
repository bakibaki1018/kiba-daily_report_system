package controllers.reports;
import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import models.Good;
import models.Report;
import utils.DBUtil;
/**
 * Servlet implementation class ReportsShowServlet
 */
@WebServlet("/reports/show")
public class ReportsShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsShowServlet() {
        super();
    }
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        Good g = new Good();
        try{
            g = em.createNamedQuery("findIsGoodByUser", Good.class)
                        .setParameter("user_id", login_employee)
                        .setParameter("report_id", r)
                        .getSingleResult();
        }catch(Exception e){
            g = null;
        }

        Follow f = new Follow();
        try{
            f = em.createNamedQuery("findIsFollowByUser", Follow.class)
                        .setParameter("user_id", login_employee)
                        .setParameter("follow_id", r.getEmployee())
                        .getSingleResult();
        }catch(Exception e){
            f = null;
        }

        long goods_count = (long) em.createNamedQuery("getMyGoodCount", Long.class)
                .setParameter("report_id", r)
                .getSingleResult();

        em.close();
        request.setAttribute("goods_count", goods_count);
        request.setAttribute("follow", f);
        request.setAttribute("good", g);
        request.setAttribute("report", r);
        request.setAttribute("_token", request.getSession().getId());
        if(request.getSession().getAttribute("flush") != null) {
        request.setAttribute("flush", request.getSession().getAttribute("flush"));
        request.getSession().removeAttribute("flush");
        }
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/show.jsp");
        rd.forward(request, response);
    }
}