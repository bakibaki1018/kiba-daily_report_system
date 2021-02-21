<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>日報 一覧</h2>

        <h3>
            <a href="<c:url value='/reports/index' />">すべての従業員の日報を表示</a>
        </h3>
        <c:choose>
            <c:when test="${getMyFollowReportsCount == 0}">
                <br />
                <h3>
                    <c:out value="${sessionScope.login_employee.name}" />
                    さんはまだ誰もフォローしていません。
                </h3>
            </c:when>
            <c:otherwise>
                <table id="report_list">
                    <tbody>
                        <tr>
                            <th class="report_name">氏名</th>
                            <th class="report_follow">フォロー</th>
                            <th class="report_date">日付</th>
                            <th class="report_title">タイトル</th>
                            <th class="report_good">いいね！数</th>
                            <th class="report_action">操作</th>
                        </tr>
                        <c:forEach var="report" items="${getMyFollowAllReports}"
                            varStatus="status">
                            <tr class="row${status.count % 2}">
                                <td class="report_name"><c:out
                                        value="${report.employee.name}" /></td>
                                <td class="report_follow"><c:if
                                        test="${sessionScope.login_employee.id != report.employee.id}">
                                        <%--  report.idを変数report_idとして使用可能にします。 --%>
                                        <c:set var="report_id" value="${report.id}" />
                                        <c:choose>
                                            <%--  Mapオブジェクトには"マップ名[キー値]でアクセスできます。 --%>
                                            <c:when test="${followMap[report_id]}">
                                                <form method="POST"
                                                    action="<c:url value="/follows3/destroy" />">
                                                    <input type="hidden" name="_token" value="${_token}" /> <input
                                                        type="hidden" name="unfollow_id" value="${report.id}" />
                                                    <button class="unfollowbutton" type="submit">フォロー中</button>
                                                </form>
                                            </c:when>
                                            <c:otherwise>
                                                <form method="POST"
                                                    action="<c:url value="/follows3/create" />">
                                                    <input type="hidden" name="_token" value="${_token}" /> <input
                                                        type="hidden" name="follow_id" value="${report.id}" />
                                                    <button class="followbutton" type="submit">フォローする</button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if></td>
                                <td class="report_date"><fmt:formatDate
                                        value='${report.report_date}' pattern='yyyy-MM-dd' /></td>
                                <td class="report_title">${report.title}</td>
                                <td class="report_good">
                                    <%--<c:out value="${goodMap[report_id]}" />  --%>
                                </td>
                                <td class="report_action"><a
                                    href="<c:url value='/reports/show?id=${report.id}' />">詳細を見る</a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <div id="pagination">
                    (全 ${getMyFollowReportsCount} 件)<br />
                    <c:forEach var="i" begin="1"
                        end="${((getMyFollowReportsCount - 1) / 15) + 1}" step="1">
                        <c:choose>
                            <c:when test="${i == page}">
                                <c:out value="${i}" />&nbsp;
                    </c:when>
                            <c:otherwise>
                                <a href="<c:url value='/follows/index?page=${i}' />"><c:out
                                        value="${i}" /></a>&nbsp;
                     </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>

                <p>
                    <a href="<c:url value='/reports/new' />">新規日報の登録</a>
                </p>
            </c:otherwise>
        </c:choose>
    </c:param>
</c:import>