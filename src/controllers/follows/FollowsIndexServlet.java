package controllers.follows;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class FollowsIndexServlet
 */
@WebServlet("/follows/index")
public class FollowsIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowsIndexServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        request.setAttribute("_token", request.getSession().getId());

        Employee login_employee = (Employee) request.getSession().getAttribute("login_employee");

        int page;

        Map<Integer, Boolean> followMap = new HashMap<Integer, Boolean>();
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
            page = 1;
        }
        List<Report> getMyFollowAllReports = em.createNamedQuery("getMyFollowAllReports", Report.class)
                .setParameter("user_id", login_employee)
                .setFirstResult(15 * (page - 1))
                .setMaxResults(15)
                .getResultList();

        long getMyFollowReportsCount = (long) em.createNamedQuery("getMyFollowReportsCount", Long.class)
                .setParameter("user_id", login_employee)
                .getSingleResult();


        for (Report report : getMyFollowAllReports) {
            try {
                Follow f = em.createNamedQuery("findIsFollowByUser", Follow.class)
                        .setParameter("user_id", login_employee)
                        .setParameter("follow_id", report.getEmployee())
                        .getSingleResult();
                if(f != null) {
                    followMap.put(report.getId(), true);
                }
            } catch (Exception e) {
                followMap.put(report.getId(), false);
            }
        }
        em.close();
        request.setAttribute("followMap", followMap);
        request.setAttribute("getMyFollowAllReports", getMyFollowAllReports);
        request.setAttribute("getMyFollowReportsCount", getMyFollowReportsCount);
        request.setAttribute("page", page);
        if (request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follows/index.jsp");
        rd.forward(request, response);
    }
}