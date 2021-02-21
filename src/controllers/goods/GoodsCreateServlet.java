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
 * Servlet implementation class GoodsCreateServlet
 */
@WebServlet("/goods/create")
public class GoodsCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoodsCreateServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Tokenのチェック
        String _token = (String) request.getParameter("_token");
        if (_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            Report r = em.find(Report.class, Integer.parseInt(request.getParameter("report_id")));

            Good g = new Good();
            g.setUser_id((Employee) request.getSession().getAttribute("login_employee"));

            g.setReport_id(r);

            em.getTransaction().begin();
            em.persist(g);
            em.getTransaction().commit();
            request.getSession().setAttribute("flush", "「いいね！」しました。");
            em.close();





            response.sendRedirect(request.getContextPath() + "/reports/show?id=" + r.getId());
        }

    }
}