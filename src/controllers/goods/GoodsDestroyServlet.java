package controllers.goods;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Good;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class GoodsDestroyServlet
 */
@WebServlet("/goods/destroy")
public class GoodsDestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoodsDestroyServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("unReport_id")));
        Employee login_employee = (Employee) request.getSession().getAttribute("login_employee");

        Good g = new Good();
        g = em.createNamedQuery("findIsGoodByUser", Good.class)
                .setParameter("user_id", login_employee)
                .setParameter("report_id", r)
                .getSingleResult();

        em.getTransaction().begin();
        em.remove(g);
        em.getTransaction().commit();
        request.getSession().setAttribute("flush", "「いいね！」を解除しました。");
        em.close();

        response.sendRedirect(request.getContextPath() + "/reports/show?id=" + r.getId());
    }

}
