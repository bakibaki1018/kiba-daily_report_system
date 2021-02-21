package controllers.follows;

import java.io.IOException;

import javax.persistence.EntityManager;
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
 * Servlet implementation class Follows2CreateServlet
 */
@WebServlet("/follows2/create")
public class Follows2CreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Follows2CreateServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String _token = (String) request.getParameter("_token");
        if (_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            Report r = em.find(Report.class, Integer.parseInt(request.getParameter("follow_id")));

            Follow f = new Follow();

            f.setUser_id((Employee) request.getSession().getAttribute("login_employee"));
            f.setFollow_id(r.getEmployee());

            em.getTransaction().begin();
            em.persist(f);
            em.getTransaction().commit();
            request.getSession().setAttribute("flush", "フォローしました。");
            em.close();


            response.sendRedirect(request.getContextPath() + "/reports/index");

        }
    }

}