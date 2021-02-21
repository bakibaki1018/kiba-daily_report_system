package controllers.reports;

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
 * Servlet implementation class ReportsIndexServlet
 */
@WebServlet("/reports/index")
public class ReportsIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsIndexServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("_token", request.getSession().getId());
        EntityManager em = DBUtil.createEntityManager();
        Employee login_employee = (Employee) request.getSession().getAttribute("login_employee");

        int page;
        Map<Integer, Boolean> followMap = new HashMap<Integer, Boolean>();
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
            page = 1;
        }
        List<Report> reports = em.createNamedQuery("getAllReports", Report.class)
                .setFirstResult(15 * (page - 1))
                .setMaxResults(15)
                .getResultList();
        long reports_count = (long) em.createNamedQuery("getReportsCount", Long.class)
                .getSingleResult();

        for (Report report : reports) {
            try {
                Follow f = em.createNamedQuery("findIsFollowByUser", Follow.class)
                        .setParameter("user_id", login_employee)
                        .setParameter("follow_id", report.getEmployee())
                        .getSingleResult();
                if (f != null) {
                    followMap.put(report.getId(), true);
                }
            } catch (Exception e) {
                followMap.put(report.getId(), false);
            }
        }

            /*good　とりあえずコメントアウト
        Map<Integer, Long> goodMap = new HashMap<Integer, Long>();


        for (Report report :reports) {

            Long goods_count = (Long)em.createNamedQuery("getMyGoodCount", Long.class)
                    .setParameter("report_id", report.getEmployee())
                    .getSingleResult();

            goodMap.put(report.getId(),goods_count);
        }
             */

            em.close();

            //request.setAttribute("goodMap", goodMap);
            request.setAttribute("followMap", followMap);
            request.setAttribute("reports", reports);
            request.setAttribute("reports_count", reports_count);
            request.setAttribute("page", page);
            if (request.getSession().getAttribute("flush") != null) {
                request.setAttribute("flush", request.getSession().getAttribute("flush"));
                request.getSession().removeAttribute("flush");
            }
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/index.jsp");
            rd.forward(request, response);
        }
    }
